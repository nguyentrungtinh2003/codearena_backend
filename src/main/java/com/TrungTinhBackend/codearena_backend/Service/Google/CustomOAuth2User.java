package com.TrungTinhBackend.codearena_backend.Service.Google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    @Autowired
    private OAuth2User oAuth2User;

    public CustomOAuth2User(OAuth2User user) {
        this.oAuth2User = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

    public String getPicture() {
        return oAuth2User.getAttribute("picture");
    }
}
