package access.api;

import access.config.Config;
import access.exception.NotAllowedException;
import access.exception.NotFoundException;
import access.model.User;
import access.repository.UserRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static access.SwaggerOpenIdConfig.API_TOKENS_SCHEME_NAME;
import static access.SwaggerOpenIdConfig.OPEN_ID_SCHEME_NAME;

@RestController
@RequestMapping(value = {"/api/v1/users"}, produces = MediaType.APPLICATION_JSON_VALUE)
@Transactional
@SecurityRequirement(name = OPEN_ID_SCHEME_NAME, scopes = {"openid"})
@EnableConfigurationProperties(Config.class)
@SecurityRequirement(name = API_TOKENS_SCHEME_NAME)
public class UserController {

    private static final Log LOG = LogFactory.getLog(UserController.class);

    private final Config config;
    private final UserRepository userRepository;

    @Autowired
    public UserController(Config config,
                          UserRepository userRepository
    ) {
        this.config = config;
        this.userRepository = userRepository;
    }

    @GetMapping("config")
    public ResponseEntity<Config> config(User user,
                                         @RequestParam(value = "guest", required = false, defaultValue = "false") boolean guest) {
        LOG.debug("/config");
        Config result = new Config(this.config);
        result
                .withAuthenticated(user != null && user.getId() != null)
                .withName(user != null ? user.getName() : null);
        if (user != null && user.getId() == null) {
            verifyMissingAttributes(user, result, guest);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("me")
    public ResponseEntity<User> me(@Parameter(hidden = true) User user) {
        LOG.debug(String.format("/me for user %s", user.getEduPersonPrincipalName()));

        return ResponseEntity.ok(user);
    }

    @GetMapping("other/{id}")
    public ResponseEntity<User> details(@PathVariable("id") Long id, @Parameter(hidden = true) User user) {
        LOG.debug(String.format("/other/%s for user $s", id, user.getEduPersonPrincipalName()));

        if (!user.isSuperUser()) {
            throw new NotAllowedException("Not allowed endpoint by" + user);
        }
        User other = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return ResponseEntity.ok(other);
    }

    @GetMapping("logout")
    public ResponseEntity<Map<String, Integer>> logout(HttpServletRequest request) {
        LOG.debug("/logout");
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Results.okResult();
    }

    private void verifyMissingAttributes(User user, Config result, boolean guest) {
        List<String> missingAttributes = new ArrayList<>();
        if (!StringUtils.hasText(user.getSub())) {
            missingAttributes.add("sub");
        }
        if (!StringUtils.hasText(user.getEmail())) {
            missingAttributes.add("email");
        }
        if (!StringUtils.hasText(user.getSchacHomeOrganization())) {
            missingAttributes.add("schacHomeOrganization");
        }
        if (guest && !StringUtils.hasText(user.getFamilyName())) {
            missingAttributes.add("familyName");
        }
        if (guest && !StringUtils.hasText(user.getGivenName())) {
            missingAttributes.add("givenName");
        }
        if (!missingAttributes.isEmpty()) {
            result.withMissingAttributes(missingAttributes);
        }
    }


}
