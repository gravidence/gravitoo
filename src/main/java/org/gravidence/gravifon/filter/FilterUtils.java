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
package org.gravidence.gravifon.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;
import org.gravidence.gravifon.db.SharedInstanceHolder;
import org.slf4j.Logger;

/**
 * Service and client filter utility methods.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class FilterUtils {
    
    /**
     * Not serialized entity placeholder. Used when serialization error occurs.
     */
    private static final String NOT_SERIALIZED_ENTITY = "<not serialized entity>";

    /**
     * No entity placeholder.
     */
    public static final String NO_ENTITY = "<no entity>";
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "\"password\"\\s*:\\s*\".*\"");

    /**
     * Preventing class instantiation.
     */
    private FilterUtils() {
        // Nothing to do
    }
    
    /**
     * Serializes entity to JSON {@link String}.<p>
     * Sensitive information is replaced by <code>***</code>. Logs serialization error if such one happens.
     * 
     * @param entity entity
     * @param logger logger instance
     * @return <ul>
     *           <li>serialized entity in successful case</li>
     *           <li>{@link #NO_ENTITY} in case the supplied <code>entity</code> argument is <code>null</code></li>
     *           <li>{@link #NOT_SERIALIZED_ENTITY} in case of serialization error</li>
     *         </ul>
     */
    public static String entityToString(Object entity, Logger logger) {
        String result;

        if (entity == null) {
            result = NO_ENTITY;
        } else {
            try {
                result = SharedInstanceHolder.OBJECT_MAPPER.writeValueAsString(entity);
                
                // hide sensitive information
                result = PASSWORD_PATTERN.matcher(result).replaceAll("\"password\":\"***\"");
            }
            catch (JsonProcessingException ex) {
                logger.error("Failed to serialize entity", ex);

                result = NOT_SERIALIZED_ENTITY;
            }
        }

        return result;
    }

    /**
     * Serializes entity to {@link String}.<p>
     * Sensitive information is replaced by <code>***</code>.
     * 
     * @param entity entity
     * @param logger logger instance
     * @return <ul>
     *           <li>serialized entity in successful case</li>
     *           <li>{@link #NO_ENTITY} in case the supplied <code>entity</code> argument is <code>null</code></li>
     *           <li>{@link #NOT_SERIALIZED_ENTITY} in case of serialization error</li>
     *         </ul>
     */
    public static String entityToString(byte[] entity, Logger logger) {
        String result;
        
        if (entity == null) {
            result = NO_ENTITY;
        }
        else {
            try {
                result = new String(entity, "UTF-8").trim();

                // hide sensitive information
                result = PASSWORD_PATTERN.matcher(result).replaceAll("\"password\":\"***\"");
            }
            catch (UnsupportedEncodingException ex) {
                logger.error("Failed to serialize entity", ex);

                result = NOT_SERIALIZED_ENTITY;
            }
        }
        
        return result;
    }
    
}
