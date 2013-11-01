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
package org.gravidence.gravifon.db;

import org.gravidence.gravifon.util.SharedInstanceHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.gravidence.gravifon.exception.JsonException;

/**
 * CouchDB design document view query results extractor.<p>
 * Works with JSON object that represents view query results.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ViewQueryResultsExtractor {

    /**
     * Document identifier property name.
     */
    public static final String ID_PROPERTY = "id";
    
    /**
     * Key property name.
     */
    public static final String KEY_PROPERTY = "key";
    
    /**
     * Value property name.
     */
    public static final String VALUE_PROPERTY = "value";
    
    /**
     * Document property name.
     */
    public static final String DOCUMENT_PROPERTY = "doc";
    
    /**
     * Preventing class instantiation.
     */
    private ViewQueryResultsExtractor() {
        // Nothing to do
    }
    
    /**
     * Extracts view size from view query results.<p>
     * View size is total rows amount the view contains. It is returned along with query results.
     * 
     * @param json view query results
     * @return view size
     */
    public static long extractSize(InputStream json) {
        try {
            return SharedInstanceHolder.OBJECT_MAPPER.readTree(json).get("total_rows").asLong();
        }
        catch (IOException ex) {
            throw new JsonException(ex);
        }
    }
    
    /**
     * Extracts all document identifiers from view query results.
     * 
     * @param json view query results
     * @return list of document identifiers if any, or <code>null</code> otherwise
     * 
     * @see #ID_PROPERTY
     */
    public static List<String> extractIds(InputStream json) {
        return extractProperies(String.class, ID_PROPERTY, json);
    }
    
    /**
     * Extracts all keys from view query results.
     * 
     * @param keyType key object type
     * @param json view query results
     * @return list of <code>keyType</code> objects if any, or <code>null</code> otherwise
     * 
     * @see #KEY_PROPERTY
     */
    public static <T> List<T> extractKeys(Class<T> keyType, InputStream json) {
        return extractProperies(keyType, KEY_PROPERTY, json);
    }
    
    /**
     * Extracts all values from view query results.
     * 
     * @param valueType value object type
     * @param json view query results
     * @return list of <code>valueType</code> objects if any, or <code>null</code> otherwise
     * 
     * @see #VALUE_PROPERTY
     */
    public static <T> List<T> extractValues(Class<T> valueType, InputStream json) {
        return extractProperies(valueType, VALUE_PROPERTY, json);
    }

    /**
     * Extracts all documents from view query results.
     * 
     * @param documentType document object type
     * @param json view query results
     * @return list of <code>documentType</code> objects if any, or <code>null</code> otherwise
     * 
     * @see #DOCUMENT_PROPERTY
     */
    public static <T> List<T> extractDocuments(Class<T> documentType, InputStream json) {
        return extractProperies(documentType, DOCUMENT_PROPERTY, json);
    }

    /**
     * Extracts all properties from view query results.
     * 
     * @param propertyType property object type
     * @param propertyName property name
     * @param json view query results
     * @return list of <code>propertyType</code> objects if any, or <code>null</code> otherwise
     */
    private static <T> List<T> extractProperies(Class<T> propertyType, String propertyName, InputStream json) {
        List<T> values = null;
        
        JsonNode rows = extractRows(json);
        if (rows.isArray() && rows.size() > 0) {
            values = new ArrayList<>(rows.size());
            
            Iterator<JsonNode> it = rows.iterator();
            while (it.hasNext()) {
                JsonNode row = it.next();
                values.add(extractProperty(propertyType, propertyName, row));
            }
        }
        
        return values;
    }
    
    /**
     * Extracts all rows from view query results.
     * 
     * @param json view query results
     * @return node that represents rows
     */
    private static JsonNode extractRows(InputStream json) {
        try {
            return SharedInstanceHolder.OBJECT_MAPPER.readTree(json).get("rows");
        }
        catch (IOException ex) {
            throw new JsonException(ex);
        }
    }
    
    /**
     * Extracts a property from a view query results row.
     * 
     * @param propertyType property object type
     * @param propertyName property name
     * @param row a row
     * @return <code>propertyType</code> object
     */
    private static <T> T extractProperty(Class<T> propertyType, String propertyName, JsonNode row) {
        try {
            return SharedInstanceHolder.OBJECT_MAPPER.treeToValue(row.get(propertyName), propertyType);
        }
        catch (JsonProcessingException ex) {
            throw new JsonException(ex);
        }
    }
    
}
