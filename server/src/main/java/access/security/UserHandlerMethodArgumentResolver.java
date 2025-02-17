package access.security;

import access.exception.UserRestrictionException;
import access.model.User;
import access.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
    private final SuperAdmin superAdmin;

    public UserHandlerMethodArgumentResolver(UserRepository userRepository,
                                             SuperAdmin superAdmin) {
        this.userRepository = userRepository;
        this.superAdmin = superAdmin;
    }

    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(User.class);
    }

    @SuppressWarnings("unchecked")
    public User resolveArgument(MethodParameter methodParameter,
                                ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) {
        Principal userPrincipal = webRequest.getUserPrincipal();
        Map<String, Object> attributes;

        if (userPrincipal instanceof BearerTokenAuthentication bearerTokenAuthentication) {
            //The user has logged in and obtained an access_token. Invite is acting as an API resource server
            attributes = bearerTokenAuthentication.getTokenAttributes();
        } else if (userPrincipal instanceof OAuth2AuthenticationToken authenticationToken) {
            //The user has logged in with OpenIDConnect. Invite is acting as a backend server
            attributes = authenticationToken.getPrincipal().getAttributes();
        } else {
            throw new UserRestrictionException();
        }

        String sub = attributes.get("sub").toString();
        AtomicBoolean validImpersonation = new AtomicBoolean(false);
        Optional<User> optionalUser = userRepository.findBySubIgnoreCase(sub)
                .or(() ->
                        //Provision super-admin users on the fly
                        superAdmin.getUsers().stream().filter(adminSub -> adminSub.equals(sub))
                                .findFirst()
                                .map(adminSub -> userRepository.save(new User(true, attributes)))
                )
                .or(() -> {
                    User user = new User(attributes);
                    userRepository.save(user);
                    return Optional.of(user);
                })
                .map(user -> {
                    String impersonateId = webRequest.getHeader("X-IMPERSONATE-ID");
                    if (StringUtils.hasText(impersonateId) && user.isSuperUser()) {
                        validImpersonation.set(true);
                        return userRepository.findById(Long.valueOf(impersonateId))
                                .orElseThrow(UserRestrictionException::new);
                    }
                    return user;
                });
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String requestURI = request.getRequestURI();
        if (optionalUser.isEmpty() && requestURI.equals("/api/v1/users/config")) {
            return new User(attributes);
        }
        return optionalUser.map(user -> {
            user.updateRemoteAttributes(attributes);
            return user;
        }).orElseThrow(UserRestrictionException::new);

    }

}