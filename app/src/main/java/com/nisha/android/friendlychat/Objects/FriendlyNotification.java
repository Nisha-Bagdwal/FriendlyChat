package com.nisha.android.friendlychat.Objects;

public class FriendlyNotification {
    private String text;
    private String contact;
    private String toToken;
    private String photoUrl;
    private String fromToken;

    public FriendlyNotification() {
    }

    public FriendlyNotification(String text, String contact, String toToken,String photoUrl, String fromToken) {
        this.text = text;
        this.photoUrl = photoUrl;
        this.contact=contact;
        this.toToken=toToken;
        this.fromToken=fromToken;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String name) {
        this.contact = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFromToken() {
        return fromToken;
    }

    public void setFromToken(String fromToken) {
        this.fromToken = fromToken;
    }

    public String getToToken() {
        return toToken;
    }

    public void setToToken(String toToken) {
        this.toToken = toToken;
    }
}
