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
package org.gravidence.gravifon.resource.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gravidence.gravifon.db.message.CreateDocumentResponse;
import org.gravidence.gravifon.db.domain.UserDocument;

/**
 * User account bean.<p>
 * Represents User interface between service and clients. Contains helpful methods to work with {@link UserDocument}.
 * 
 * @see UserDocument
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class UserBean {

    /**
     * @see #getId()
     */
    @JsonProperty
    private String id;

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
     * Returns user account identifier.
     * 
     * @return user account identifier
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     * @see #getId()
     */
    public void setId(String id) {
        this.id = id;
    }

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
     * @deprecated Yes, this will be replaced with password hash.
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
        return String.format("{id=%s, username=%s}", id, username);
    }

    /**
     * Updates bean with created document identifier.
     * DB returns document identifier and revision only, so there's no need to update bean values.
     * 
     * @param document a created document
     * @return updated bean
     */
    public UserBean updateBean(CreateDocumentResponse document) {
        if (document != null) {
            id = document.getId();
        }
        
        return this;
    }
    
    /**
     * Updates bean with document revision values.
     * 
     * @param document a document revision
     * @return updated bean
     */
    public UserBean updateBean(UserDocument document) {
        if (document != null) {
            id = document.getId();
            username = document.getUsername();
            fullname = document.getFullname();
            password = document.getPassword();
        }
        
        return this;
    }
    
    /**
     * Creates document with bean values except identifier.
     * 
     * @return created document
     */
    public UserDocument createDocument() {
        return updateDocument(new UserDocument());
    }
    
    /**
     * Updates document with bean values except identifier.
     * 
     * @param document a document revision
     * @return updated document
     */
    public UserDocument updateDocument(UserDocument document) {
        if (document != null) {
            document.setUsername(username);
            document.setFullname(fullname);
            document.setPassword(password);
        }
        
        return document;
    }
    
}
