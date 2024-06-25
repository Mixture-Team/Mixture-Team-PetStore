package hutech.mixture.petstore.security.oauth2;

import hutech.mixture.petstore.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private ApplicationContext applicationContext;
    public OAuthLoginSuccessHandler() {
        super();
        setDefaultTargetUrl("/trang-chu");
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String oauth2ClientName = oAuth2User.getOauth2ClientName();
        String username = oAuth2User.getEmail();
        String email = oAuth2User.getName();

        UserService userService = applicationContext.getBean(UserService.class);
        userService.updateAuthenticationType(username, oauth2ClientName);
        System.out.println("TAO LÀ HOÀNG " + username + " " + email);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
