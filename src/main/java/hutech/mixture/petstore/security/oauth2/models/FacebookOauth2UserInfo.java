package hutech.mixture.petstore.security.oauth2.models;

import lombok.Getter;

import java.util.Map;

@Getter
public class FacebookOauth2UserInfo extends Oauth2UserInfo{
    public FacebookOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId(){
        return (String) attributes.get("id");
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
