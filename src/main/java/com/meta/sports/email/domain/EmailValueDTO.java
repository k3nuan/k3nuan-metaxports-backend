package com.meta.sports.email.domain;

public class EmailValueDTO {
    private String emailFrom;
    private String emailTo;
    private String subject;
    private String username;
    private String tokenPassword;

    public EmailValueDTO() {

    }

    public EmailValueDTO(String emailFrom, String emailTo, String subject, String username, String tokenPassword) {
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.subject = subject;
        this.username = username;
        this.tokenPassword = tokenPassword;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokenPassword() {
        return tokenPassword;
    }

    public void setTokenPassword(String tokenPassword) {
        this.tokenPassword = tokenPassword;
    }
}
