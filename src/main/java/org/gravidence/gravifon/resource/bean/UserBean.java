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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.regex.Pattern;
import org.apache.commons.validator.routines.EmailValidator;
import org.gravidence.gravifon.db.message.CreateDocumentResponse;
import org.gravidence.gravifon.db.domain.UserDocument;
import org.gravidence.gravifon.exception.ValidationException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.gravidence.gravifon.util.DateTimeUtils;
import org.gravidence.gravifon.util.PasswordUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * User account bean.<p>
 * Represents User interface between service and clients. Contains helpful methods to work with {@link UserDocument}.
 * 
 * @see UserDocument
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class UserBean extends ValidateableBean {
    
    /**
     * Latin-1 characters set excluding some specific ones (i.e. ":", "?", "\", etc.).
     */
    private static final String LATIN_LIGHT = "\\w!#$%'()*+,\\-.;=@\\[\\]\\^`{}";
    
    /**
     * Latin-1 characters set.
     */
    private static final String LATIN_FULL = LATIN_LIGHT + " \\\"\\&/:<>?\\\\\\|";

    /**
     * {@link #getUsername() Username} attribute valid format pattern.
     */
    private static final Pattern USERNAME_PATTERN = Pattern.compile(String.format("[%s]+", LATIN_LIGHT));
    
    /**
     * {@link #getPassword() Password} attribute valid format pattern.
     */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(String.format("[%s]+", LATIN_FULL));
    
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    
    /**
     * @see #getEmail()
     */
    @JsonProperty
    private String email;
    
    /**
     * @see #getRegistrationDatetime()
     */
    @JsonProperty("registration_datetime")
    private DateTime registrationDatetime;

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
     * Returns user account plaintext password.
     * 
     * @return user account plaintext password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     * @see #getPassword()
     */
    public void setPassword(String password) {
        this.password = password;
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
     * Returns user account registration datetime (UTC).
     * 
     * @return user account registration datetime (UTC)
     */
    public DateTime getRegistrationDatetime() {
        return registrationDatetime;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, username=%s}", id, username);
    }

    @Override
    public void validate() {
        checkRequired(username, "username");
        checkLength(username, "username", 1, 40);
        checkPattern(username, "username", USERNAME_PATTERN);
        
        if (fullname != null) {
            checkLength(fullname, "fullname", 1, 120);
        }
        
        checkRequired(email, "email");
        checkLength(email, "email", 6, 100);
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new ValidationException(GravifonError.INVALID, "Property 'email' is invalid.");
        }
    }
    
    /**
     * Validates user password.<p>
     * {@link GravifonError#REQUIRED REQUIRED} error is thrown only if <code>password</code> value is <code>null</code>
     * and <code>required</code> value is <code>true</code>.
     * 
     * @param required password required indicator
     */
    public void checkPassword(boolean required) {
        if (required) {
            checkRequired(password, "password");
        }
        
        if (password != null) {
            checkLength(password, "password", 6, 40);
            checkPattern(password, "password", PASSWORD_PATTERN);
        }
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
     * Updates bean with document values.
     * 
     * @param document user details document
     * @return updated bean
     */
    public UserBean updateBean(UserDocument document) {
        if (document != null) {
            id = document.getId();
            username = document.getUsername();
            fullname = document.getFullname();
            email = document.getEmail();
            registrationDatetime = DateTimeUtils.fromArray(document.getRegistrationDatetime());
        }
        
        return this;
    }
    
    /**
     * Creates document with bean values except identifier.
     * 
     * @return created document
     */
    public UserDocument createDocument() {
        UserDocument document = updateDocument(new UserDocument());
        
        // Initialize created UserDocument with username as it is not updateable after registration
        document.setUsername(username);
        // Read-only value generated by server side
        document.setRegistrationDatetime(DateTimeUtils.toArray(new DateTime(DateTimeZone.UTC)));
        
        return document;
    }
    
    /**
     * Updates document with bean values except identifier and username.
     * 
     * @param document user details document
     * @return updated document
     */
    public UserDocument updateDocument(UserDocument document) {
        if (document != null) {
            document.setFullname(fullname);
            // Missing password value means it is not changed
            if (password != null) {
                document.setPasswordHash(PasswordUtils.getSaltedHash(password));
            }
            document.setEmail(email);
        }
        
        return document;
    }
    
}
