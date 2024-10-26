package hutech.mixture.petstore.security.oauth2.services;

import hutech.mixture.petstore.enums.AuthenticationType;
import hutech.mixture.petstore.models.Role;
import hutech.mixture.petstore.models.User;
import hutech.mixture.petstore.repositories.IRoleRepository;
import hutech.mixture.petstore.repositories.IUserRepository;
import hutech.mixture.petstore.security.oauth2.models.CustomOAuth2User;
import hutech.mixture.petstore.security.oauth2.models.FacebookOauth2UserInfo;
import hutech.mixture.petstore.security.oauth2.models.GoogleOauth2UserInfo;
import hutech.mixture.petstore.security.oauth2.models.Oauth2UserInfo;
import hutech.mixture.petstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Lazy
    @Autowired
    private UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String clientName = userRequest.getClientRegistration().getClientName().toLowerCase();

        Oauth2UserInfo oauth2UserInfo;
        AuthenticationType authType;

        if(clientName.equals("google")){
            oauth2UserInfo = new GoogleOauth2UserInfo(oAuth2User.getAttributes());
            authType = AuthenticationType.GOOGLE;
        } else if(clientName.equals("facebook")){
            oauth2UserInfo = new FacebookOauth2UserInfo(oAuth2User.getAttributes());
            authType = AuthenticationType.FACEBOOK;
        } else {
            throw new OAuth2AuthenticationException("Sorry! Login with " + clientName + " is not supported yet.");
        }

        String email = oauth2UserInfo.getEmail();
        User user = userRepository.findByEmail(email).orElse(null);

        if(user != null){
            if(user.getAuthenticationType() == AuthenticationType.LOCAL){
                throw new OAuth2AuthenticationException(
                        "Email " + email + " đã được đăng ký. " +
                    "Vui lòng sử dụng đăng nhập bằng tên người dùng và mật khẩu"
                );
        } else if (user.getAuthenticationType() != AuthenticationType.valueOf(clientName.toUpperCase())){
            throw new OAuth2AuthenticationException(
                    "Email " + email + " đã được đăng ký bằng tài khoản " + user.getAuthenticationType().name() + ". " +
                            "Vui lòng sử dụng đăng nhập bằng" + user.getAuthenticationType().name()
            );
        }
        updateExistingUser(user, oauth2UserInfo, authType);
        }
        else{
            user = createNewUser(oauth2UserInfo, authType);
        }
        
        return new CustomOAuth2User(oAuth2User, user);
    }

    private User createNewUser(Oauth2UserInfo oauth2UserInfo, AuthenticationType authType) {
        String password = "MIXTURE" + oauth2UserInfo.getId() + "PETSTORE";
        String passwordEncode = passwordEncoder.encode(password);

        User newUser = User.builder()
                .email(oauth2UserInfo.getEmail())
                .username(oauth2UserInfo.getUsername())
                .password(passwordEncode)
                .authenticationType(authType)
                .build();

        Role role = roleRepository.findRoleByName("USER");
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    private void updateExistingUser(User user, Oauth2UserInfo oauth2UserInfo, AuthenticationType authType) {
        boolean needUpdate = false;

        if(user.getUsername().equals(oauth2UserInfo.getUsername())){
            user.setUsername(oauth2UserInfo.getUsername());
            needUpdate = true;
        }

        if(user.getAuthenticationType() != authType){
            user.setAuthenticationType(authType);
            needUpdate = true;
        }

        if (needUpdate){
            userRepository.save(user);
        }
    }
}
