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
 * all copies or substantial confirmationUrlions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.gravidence.gravifon.template.email;

/**
 * Data bean for user registration confirmation email template.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class UserRegistrationConfirmationEmailData {
    
    /**
     * @see #getRecipientName()
     */
    private final String recipientName;

    /**
     * @see #getConfirmationUrl()
     */
    private final String confirmationUrl;

    /**
     * Constructs fully populated data bean instance.
     * 
     * @param recipientName email recipient name (username or fullname)
     * @param confirmationUrl registration confirmation url
     */
    public UserRegistrationConfirmationEmailData(String recipientName, String confirmationUrl) {
        this.recipientName = recipientName;
        this.confirmationUrl = confirmationUrl;
    }
    
    /**
     * Returns email recipient name (username or fullname).
     * 
     * @return email recipient name (username or fullname)
     */
    public String getRecipientName() {
        return recipientName;
    }

    /**
     * Returns registration confirmation url.
     * 
     * @return registration confirmation url
     */
    public String getConfirmationUrl() {
        return confirmationUrl;
    }
    
}
