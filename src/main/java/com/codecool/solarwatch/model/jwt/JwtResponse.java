package com.codecool.solarwatch.model.jwt;

import java.util.List;

public class JwtResponse {
    private String jwt;
    private String userName;
    private List<String> roles;

    public JwtResponse() {
    }

    public JwtResponse(String jwt, String userName, List<String> roles) {
        this.jwt = jwt;
        this.userName = userName;
        this.roles = roles;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
