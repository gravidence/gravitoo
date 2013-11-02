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

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.JsonException;
import org.gravidence.gravifon.exception.error.GravifonError;

/**
 * Basic utility methods.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class BasicUtils {

    /**
     * {@link SecureRandom} instance initiated with default random number algorithm.
     */
    public static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    /**
     * Preventing class instantiation.
     */
    private BasicUtils() {
        // Nothing to do
    }

    /**
     * Generates unique identifier.
     * 
     * @return unique identifier
     */
    public static String generateUniqueIdentifier() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates secure random token of specified length.<p>
     * Generated token contains following characters: <code>[0-9a-z]</code>.
     * 
     * @param numberOfBits token length in bits
     * @return secure random token
     */
    public static String generateToken(int numberOfBits) {
        return new BigInteger(numberOfBits, SECURE_RANDOM).toString(Character.MAX_RADIX);
    }
    
    /**
     * Converts a string to lower case using {@link Locale.ENGLISH ENGLISH} locale.
     * 
     * @param value string value
     * @return string value in lower case
     */
    public static String lowerCase(String value) {
        return StringUtils.lowerCase(value, Locale.ENGLISH);
    }
    
    /**
     * Converts object to JSON string.
     * 
     * @param value object
     * @return JSON string that represents the object
     * @throws JsonException in case JSON processing exception took place
     */
    public static String objectToJsonString(Object value) {
        String result;
        
        try {
            result = SharedInstanceHolder.OBJECT_MAPPER.writeValueAsString(value);
        }
        catch (JsonProcessingException ex) {
            throw new JsonException(ex);
        }
        
        return result;
    }
    
    /**
     * Encodes a string according to URL safe base64 rules. UTF-8 character encoding is used.
     * 
     * @param input clear text string
     * @return encoded string
     */
    public static String encodeToBase64(String input) {
        String result;
        
        if (input == null) {
            result = null;
        }
        else {
            try {
                result = Base64.encodeBase64URLSafeString(input.getBytes(CharEncoding.UTF_8));
            }
            catch (UnsupportedEncodingException ex) {
                throw new GravifonException(
                        GravifonError.UNEXPECTED, "UTF-8 encoding is not supported by underlying system.", ex);
            }
        }
        
        return result;
    }
    
    /**
     * Decodes a base64 string according to base64 rules. UTF-8 character encoding is used.
     * 
     * @param input encoded string
     * @return decoded string
     */
    public static String decodeFromBase64(String input) {
        String result;
        
        if (input == null) {
            result = null;
        }
        else {
            try {
                result = new String(Base64.decodeBase64(input), CharEncoding.UTF_8);
            }
            catch (UnsupportedEncodingException ex) {
                throw new GravifonException(
                        GravifonError.UNEXPECTED, "UTF-8 encoding is not supported by underlying system.", ex);
            }
        }
        
        return result;
    }
    
}
