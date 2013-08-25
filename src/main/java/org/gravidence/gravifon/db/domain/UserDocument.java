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
     * @see #getFullname()
     */
    @JsonProperty
    private String fullname;
    
    /**
     * @see #getPassword()
     */
    @JsonProperty
    private String password;

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
     * Returns user account first and last name.
     * 
     * @return user account first and last name
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
     * Returns user account clear text password.
     * 
     * @return user account clear text password
     * @deprecated no clear text password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     * @see #getPassword()
     * @deprecated no clear text password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, username=%s}", getId(), username);
    }
    
}
