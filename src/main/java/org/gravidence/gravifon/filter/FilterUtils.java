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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.gravidence.gravifon.util.SharedInstanceHolder;
import org.slf4j.Logger;

/**
 * Service and client filter utility methods.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class FilterUtils {
    
    /**
     * Corrupted entity placeholder. Used when deserialization error occurs.
     */
    private static final String CORRUPTED_ENTITY = "<corrupted entity>";

    /**
     * No entity placeholder.
     */
    public static final String NO_ENTITY = "<no entity>";

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
     *         </ul>
     */
    public static String entityToString(Object entity, Logger logger) {
        String result;

        if (entity == null) {
            result = NO_ENTITY;
        }
        else {
            JsonNode root = SharedInstanceHolder.OBJECT_MAPPER.valueToTree(entity);

            hideSensitiveInformation(root, "password");

            result = root.toString();
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
     *           <li>{@link #CORRUPTED_ENTITY} in case of deserialization error</li>
     *         </ul>
     */
    public static String entityToString(byte[] entity, Logger logger) {
        String result;
        
        if (entity == null) {
            result = NO_ENTITY;
        }
        else {
            try {
                JsonNode root = SharedInstanceHolder.OBJECT_MAPPER.readTree(entity);

                hideSensitiveInformation(root, "password");

                result = root.toString();
            }
            catch (IOException ex) {
                logger.error("Failed to deserialize entity", ex);

                result = CORRUPTED_ENTITY;
            }
        }
        
        return result;
    }
    
    /**
     * Replaces sensitive information with placeholder.
     * 
     * @param root JSON object
     * @param propertyName name of property that contains sensitive information
     */
    private static void hideSensitiveInformation(JsonNode root, String propertyName) {
        List<JsonNode> nodes = root.findParents(propertyName);
        if (CollectionUtils.isNotEmpty(nodes)) {
            for (JsonNode node : nodes) {
                ((ObjectNode) node).put(propertyName, "***");
            }
        }
    }
    
}
