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

import java.util.Locale;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

/**
 * Basic utility methods.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class BasicUtils {

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
     * Converts a string to lower case using {@link Locale.ENGLISH ENGLISH} locale.
     * 
     * @param value string value
     * @return string value in lower case
     */
    public static String lowerCase(String value) {
        return StringUtils.lowerCase(value, Locale.ENGLISH);
    }
    
}
