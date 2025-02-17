package access.security;

import access.model.User;
import access.repository.UserRepository;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private static final Log LOG = LogFactory.getLog(CustomOidcUserService.class);

    @Getter
    private final UserRepository userRepository;
    private final OidcUserService delegate;

    public CustomOidcUserService(UserRepository userRepository
    ) {
        this.userRepository = userRepository;
        delegate = new OidcUserService();
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // Delegate to the default implementation for loading a user
        OidcUser oidcUser = delegate.loadUser(userRequest);
        Map<String, Object> claims = oidcUser.getUserInfo().getClaims();
        // We need a mutable Map instead of the returned immutable Map
        Map<String, Object> newClaims = new HashMap<>(claims);

        String sub = (String) newClaims.get("sub");
        Optional<User> optionalUser = userRepository.findBySubIgnoreCase(sub);
        optionalUser.ifPresent(user -> {
            boolean changed = user.updateAttributes(newClaims);
            if (changed) {
                userRepository.save(user);
            }

        });
        OidcUserInfo oidcUserInfo = new OidcUserInfo(newClaims);
        oidcUser = new DefaultOidcUser(oidcUser.getAuthorities(), oidcUser.getIdToken(), oidcUserInfo);
        return oidcUser;

    }
}
