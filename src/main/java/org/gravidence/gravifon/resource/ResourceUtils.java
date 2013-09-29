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
package org.gravidence.gravifon.resource;

import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.db.UsersDBClient;
import org.gravidence.gravifon.db.domain.UserDocument;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.gravidence.gravifon.util.PasswordUtils;
import org.gravidence.gravifon.util.RequestUtils;
import org.slf4j.Logger;

/**
 * Resource utility methods.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ResourceUtils {

    /**
     * Preventing class instantiation.
     */
    private ResourceUtils() {
        // Nothing to do
    }
    
    /**
     * Verifies that Basic HTTP Authorization details are presented and correct.
     * 
     * @param headers request headers
     * @param usersDBClient <code>/users</code> database client instance
     * @param logger logger instance
     * @return authenticated user details document
     * @throws GravifonException in case user not found or credentials are invalid
     */
    public static UserDocument authenticateUser(MultivaluedMap<String, String> headers, UsersDBClient usersDBClient,
            Logger logger) throws GravifonException {
        String[] credentials = RequestUtils.extractCredentials(headers);

        UserDocument user = usersDBClient.retrieveUserByUsername(credentials[0]);

        if (user == null) {
            throw new GravifonException(GravifonError.NOT_AUTHORIZED, "User not found.", null, false);
        }
        else if (PasswordUtils.verify(credentials[1], user.getPasswordHash())) {
            logger.trace("'{}' user successfully authenticated.", user);
        }
        else {
            logger.trace("'{}' user failed to authenticate.", user);
            throw new GravifonException(GravifonError.NOT_AUTHORIZED,
                    "Username or password are invalid.", null, false);
        }
        
        return user;
    }
    
    /**
     * Verifies that Basic HTTP Authorization details are presented,
     * correct and matches requested resource owner identifier.
     * 
     * @param headers request headers
     * @param id requested resource owner identifier
     * @param usersDBClient <code>/users</code> database client instance
     * @param logger logger instance
     * @return authorized user details document
     * @throws GravifonException in case user not found, credentials are invalid
     * or access to requested resource is denied
     */
    public static UserDocument authorizeUser(MultivaluedMap<String, String> headers, String id,
            UsersDBClient usersDBClient, Logger logger) throws GravifonException {
        UserDocument user = authenticateUser(headers, usersDBClient, logger);
        
        if (StringUtils.equals(user.getId(), id)) {
            logger.trace("'{}' user successfully authorized.", user);
        }
        else {
            logger.trace("'{}' user is not allowed to access requested resource.", user);
            throw new GravifonException(GravifonError.NOT_ALLOWED,
                    "Access to requested resource is denied.", null, false);
        }
        
        return user;
    }
    
}
