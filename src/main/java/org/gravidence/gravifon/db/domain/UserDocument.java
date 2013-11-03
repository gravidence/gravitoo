/*
 * The MIT License
 *
 * Copyright 2013 Gravidence.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.gravidence.gravifon.db.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User account document.<p>
 * Represents User database model.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class UserDocument extends CouchDBDocument {

    /**
     * @see #getUsername()
     */
    @JsonProperty
    private String username;
    
    /**
     * @see #getStatus()
     */
    @JsonProperty
    private UserStatus status;
    
    /**
     * @see #getFullname()
     */
    @JsonProperty
    private String fullname;
    
    /**
     * @see #getPasswordHash()
     */
    @JsonProperty("password_hash")
    private SaltedHash passwordHash;
    
    /**
     * @see #getEmail()
     */
    @JsonProperty
    private String email;
    
    /**
     * @see #getRegistrationKey()
     */
    @JsonProperty("registration_key")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String registrationKey;
    
    /**
     * @see #getRegistrationDatetime()
     */
    @JsonProperty("registration_datetime")
    private int[] registrationDatetime;

    /**
     * Returns user account nickname.
     * 
     * @return user account nickname
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     * @see #getUsername()
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns user account status.
     * 
     * @return user account status
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * @param status
     * @see #getStatus()
     */
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * Returns user account full name.
     * 
     * @return user account full name
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname
     * @see #getFullname()
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Returns user account password hash.
     * 
     * @return user account password hash
     */
    public SaltedHash getPasswordHash() {
        return passwordHash;
    }

    /**
     * @param passwordHash
     * @see #getPasswordHash()
     */
    public void setPasswordHash(SaltedHash passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns user account email.
     * 
     * @return user account email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     * @see #getEmail()
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns key that is required for user account registration completion.
     * 
     * @return key that is required for user account registration completion
     */
    public String getRegistrationKey() {
        return registrationKey;
    }

    /**
     * @param registrationKey
     * @see #getRegistrationKey()
     */
    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    /**
     * Returns user account registration datetime (UTC).<p>
     * Array content is as follows: <code>[yyyy,MM,dd,HH,mm,ss,SSS]</code>.
     * 
     * @return user account registration datetime (UTC)
     */
    public int[] getRegistrationDatetime() {
        return registrationDatetime;
    }

    /**
     * @param registrationDatetime
     * @see #getRegistrationDatetime()
     */
    public void setRegistrationDatetime(int[] registrationDatetime) {
        this.registrationDatetime = registrationDatetime;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, username=%s}", getId(), username);
    }
    
}
