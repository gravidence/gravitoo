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
package org.gravidence.gravifon.email;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Email sender implementation that uses Apache Commons Email under the hood.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ApacheCommonsEmailSender implements EmailSender {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ApacheCommonsEmailSender.class);
    
    /**
     * @see #getHost()
     */
    private String host;

    /**
     * @see #getPort()
     */
    private String port;

    /**
     * @see #getUsername()
     */
    private String username;

    /**
     * @see #getPassword()
     */
    private String password;

    /**
     * @see #getFromAddress()
     */
    private String fromAddress;

    /**
     * @see #getFromName()
     */
    private String fromName;

    /**
     * Returns SMTP server hostname.
     * 
     * @return SMTP server hostname
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     * @see #getHost()
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Returns SMTP server port.
     * 
     * @return SMTP server port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port
     * @see #getPort()
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Returns SMTP server username.
     * 
     * @return SMTP server username
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
     * Returns SMTP server password.
     * 
     * @return SMTP server password
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
     * Returns email address for FROM field.
     * 
     * @return email address for FROM field
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * @param fromAddress
     * @see #getFromAddress()
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * Returns name for FROM field.
     * 
     * @return name for FROM field
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * @param fromName
     * @see #getFromName()
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
    
    @Override
    public boolean send(String toAddress, String toName, String subject, String htmlMessage, String textMessage) {
        HtmlEmail email = new HtmlEmail();
        
        email.setHostName(host);
        email.setSslSmtpPort(port);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true);
        
        try {
            if (StringUtils.isBlank(toName)) {
                email.addTo(toAddress);
            }
            else {
                email.addTo(toAddress, toName);
            }
            email.setFrom(fromAddress, fromName);

            email.setSubject(subject);

            if (htmlMessage != null) {
                email.setHtmlMsg(htmlMessage);
            }
            if (textMessage != null) {
                email.setTextMsg(textMessage);
            }

            email.send();
        }
        catch (EmailException ex) {
            // TODO think about throwing GravifonException
            LOGGER.warn(String.format("Failed to send an email to %s", toAddress), ex);
            
            return false;
        }
        
        return true;
    }
    
}
