package access.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.function.Consumer;

public class AuthorizationRequestCustomizer implements Consumer<OAuth2AuthorizationRequest.Builder> {

    @Override
    public void accept(OAuth2AuthorizationRequest.Builder builder) {
        builder.additionalParameters(params -> {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpSession session = ((ServletRequestAttributes) requestAttributes)
                    .getRequest().getSession(false);
            if (session == null) {
                return;
            }
            DefaultSavedRequest savedRequest = (DefaultSavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            String[] force = savedRequest.getParameterValues("force");
            if (force != null && force.length == 1) {
                params.put("prompt", "login");
            }
        });
    }
}
