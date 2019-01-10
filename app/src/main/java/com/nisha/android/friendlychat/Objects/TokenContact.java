package com.nisha.android.friendlychat.Objects;

public class TokenContact {

    String contact, token;

    public TokenContact(String contact, String token){
        this.contact=contact;
        this.token=token;
    }

    TokenContact(){

    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
