package com.nisha.android.friendlychat.Objects;

import android.support.annotation.NonNull;

public class FriendlyContact implements Comparable<FriendlyContact>{

    private String name,contact,friendToken;

    public FriendlyContact(String name, String contact,String friendToken){
        this.name=name;
        this.contact=contact;
        this.friendToken=friendToken;
    }
    public FriendlyContact(String name, String contact){
        this.contact=contact;
        this.name=name;
    }

    public FriendlyContact(String contact){
        this.contact=contact;
    }

    public FriendlyContact(){

    }

    public String getFriendToken() {
        return friendToken;
    }

    public void setFriendToken(String friendToken) {
        this.friendToken = friendToken;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(@NonNull FriendlyContact friendlyContact) {
        return name.compareTo(friendlyContact.name);
    }

    public boolean equals(Object obj){
        System.out.println("In equals");
        if (obj instanceof FriendlyContact) {
            FriendlyContact pp = (FriendlyContact) obj;
            return (pp.contact == this.contact);
        } else {
            return false;
        }
    }

    public int hashCode(){
        System.out.println("In hashcode");
        int hashcode = 0;
        hashcode = contact.hashCode();
        return hashcode;
    }
}
