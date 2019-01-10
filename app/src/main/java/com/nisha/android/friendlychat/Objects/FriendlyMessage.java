/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nisha.android.friendlychat.Objects;

public class FriendlyMessage {

    private String text;
    private String contact;
    private String toContact;
    private String photoUrl;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String contact, String toContact,String photoUrl) {
        this.text = text;
        this.photoUrl = photoUrl;
        this.contact=contact;
        this.toContact=toContact;
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

    public String getToContact() {
        return toContact;
    }

    public void setToContact(String toContact) {
        this.toContact = toContact;
    }
}
