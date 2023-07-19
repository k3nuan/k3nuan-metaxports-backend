package com.meta.sports.email.domain;

import javax.validation.constraints.NotBlank;

public class ChangePassword {

    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank
    private String tokenPassWord;

    public ChangePassword() {

    }

    public ChangePassword(String password, String confirmPassword, String tokenPassWord) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.tokenPassWord = tokenPassWord;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getTokenPassWord() {
        return tokenPassWord;
    }

    public void setTokenPassWord(String tokenPassWord) {
        this.tokenPassWord = tokenPassWord;
    }
}
