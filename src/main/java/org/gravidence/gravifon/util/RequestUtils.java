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
package org.gravidence.gravifon.util;

import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.internal.util.Base64;

/**
 * Request utility methods.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class RequestUtils {

    /**
     * Preventing class instantiation.
     */
    private RequestUtils() {
        // Nothing to do
    }
    
    /**
     * Extracts Basic HTTP Authorization credentials from request headers.
     * 
     * @param headers request headers
     * @return credentials if presented, <code>null</code> otherwise
     */
    public static String[] extractCredentials(MultivaluedMap<String, String> headers) {
        String credentialsRaw = headers.getFirst("Authorization");
        String credentialsBase64 = StringUtils.trim(StringUtils.substringAfter(credentialsRaw, "Basic "));

        String[] credentials;
        if (credentialsBase64 == null) {
            credentials = null;
        }
        else {
            String credentialsPlaintext = Base64.decodeAsString(credentialsBase64);
            credentials = StringUtils.split(credentialsPlaintext, ":", 2);
        }
        
        return credentials;
    }
    
}
