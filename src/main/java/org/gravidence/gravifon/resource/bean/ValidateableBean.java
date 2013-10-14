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

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.exception.ValidationException;
import org.gravidence.gravifon.exception.error.GravifonError;

/**
 * Self-validateable bean.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public abstract class ValidateableBean extends IOBean implements Validateable {
    
    /**
     * Checks that required field is actually presented (e.g. its value is not <code>null</code>).
     * 
     * @param fieldValue field value
     * @param fieldName field name to use in error message
     * @throws ValidationException in case field is missed
     */
    public static void checkRequired(Object fieldValue, String fieldName) {
        if (fieldValue == null) {
            throw new ValidationException(GravifonError.REQUIRED,
                    String.format("Property '%s' is required.", fieldName));
        }
    }
    
    /**
     * Checks that required field is actually presented (e.g. its value is not <code>null</code> or <code>""</code>).
     * 
     * @param fieldValue field value
     * @param fieldName field name to use in error message
     * @throws ValidationException in case field is missed
     */
    public static void checkRequired(String fieldValue, String fieldName) {
        if (StringUtils.isEmpty(fieldValue)) {
            throw new ValidationException(GravifonError.REQUIRED,
                    String.format("Property '%s' is required.", fieldName));
        }
    }
    
    /**
     * Checks that field length satisfies specified range (inclusive).
     * 
     * @param fieldValue field value
     * @param fieldName field name to use in error message
     * @param min minimum number of characters expected
     * @param max maximum number of characters expected
     * @throws ValidationException in case field length is invalid
     */
    public static void checkLength(String fieldValue, String fieldName, int min, int max) {
        int fieldLength = StringUtils.length(fieldValue);
        if (fieldLength < min || fieldLength > max) {
            throw new ValidationException(GravifonError.INVALID,
                    String.format("Property '%s' value length is out of expected range.", fieldName));
        }
    }
    
    /**
     * Checks that field length satisfies specified range (inclusive).
     * 
     * @param fieldValue field value
     * @param fieldName field name to use in error message
     * @param min minimum number of elements expected
     * @param max maximum number of elements expected
     * @throws ValidationException in case field length is invalid
     */
    public static void checkLength(List fieldValue, String fieldName, int min, int max) {
        int fieldLength = CollectionUtils.size(fieldValue);
        if (fieldLength < min || fieldLength > max) {
            throw new ValidationException(GravifonError.INVALID,
                    String.format("Property '%s' value length is out of expected range.", fieldName));
        }
    }
    
    /**
     * Checks that field matches specified pattern.
     * 
     * @param fieldValue field value
     * @param fieldName field name to use in error message
     * @param pattern regular expression pattern
     * @throws ValidationException in case field is invalid
     */
    public static void checkPattern(String fieldValue, String fieldName, Pattern pattern) {
        if (!pattern.matcher(fieldValue).matches()) {
            throw new ValidationException(GravifonError.INVALID,
                    String.format("Property '%s' value does not match expected format.", fieldName));
        }
    }
    
    /**
     * Validates each {@link ValidateableBean} entity in collection
     * by calling its {@link ValidateableBean#validate() validate()} method.
     * 
     * @param entities ValidateableBean entity collection
     */
    public static void validateCollection(Collection<? extends ValidateableBean> entities) {
        if (CollectionUtils.isNotEmpty(entities)) {
            for (ValidateableBean artist : entities) {
                artist.validate();
            }
        }
    }
    
}
