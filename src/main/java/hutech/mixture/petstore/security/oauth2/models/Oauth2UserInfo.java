package hutech.mixture.petstore.security.oauth2.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class Oauth2UserInfo {
    protected Map<String, Object> attributes;
    public abstract String getId();
    public abstract String getUsername();
    public abstract String getEmail();
}
