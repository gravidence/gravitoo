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

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

/**
 * Joda datetime utility methods.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class DateTimeUtils {

    /**
     * Preventing class instantiation.
     */
    private DateTimeUtils() {
        // Nothing to do
    }
    
    /**
     * Converts local date object to array of date fields.<p>
     * Resulting array content is as follows: <code>[yyyy,MM,dd]</code>.
     * 
     * @param value date object
     * @return array of date fields
     */
    public static int[] localDateToArray(LocalDate value) {
        int[] result;
        
        if (value == null) {
            result = null;
        }
        else {
            result = new int[3];
            
            result[0] = value.getYear();
            result[1] = value.getMonthOfYear();
            result[2] = value.getDayOfMonth();
        }
        
        return result;
    }
    
    /**
     * Converts array of date fields to local date object.<p>
     * Array content is as follows: <code>[yyyy,MM,dd]</code>.
     * 
     * @param value array of date fields
     * @return date object
     */
    public static LocalDate arrayToLocalDate(int[] value) {
        LocalDate result;
        
        if (ArrayUtils.isEmpty(value)) {
            result = null;
        }
        else {
            result = new LocalDate(value[0], value[1], value[2]);
        }
        
        return result;
    }
    
    /**
     * Converts datetime object to array of UTC datetime fields.<p>
     * Given datetime object is casted to UTC.<p>
     * Resulting array content is as follows: <code>[yyyy,MM,dd,HH,mm,ss,SSS]</code>.
     * 
     * @param value datetime object
     * @return array of UTC datetime fields
     */
    public static int[] dateTimeToArray(DateTime value) {
        int[] result;
        
        if (value == null) {
            result = null;
        }
        else {
            result = new int[7];
            
            DateTime valueUTC = value.toDateTime(DateTimeZone.UTC);

            result[0] = valueUTC.getYear();
            result[1] = valueUTC.getMonthOfYear();
            result[2] = valueUTC.getDayOfMonth();
            result[3] = valueUTC.getHourOfDay();
            result[4] = valueUTC.getMinuteOfHour();
            result[5] = valueUTC.getSecondOfMinute();
            result[6] = valueUTC.getMillisOfSecond();
        }
        
        return result;
    }
    
    /**
     * Converts array of UTC datetime fields to datetime object.<p>
     * Array content is as follows: <code>[yyyy,MM,dd,HH,mm,ss,SSS]</code>.
     * 
     * @param value array of UTC datetime fields
     * @return datetime object
     */
    public static DateTime arrayToDateTime(int[] value) {
        DateTime result;
        
        if (ArrayUtils.isEmpty(value)) {
            result = null;
        }
        else {
            result = new DateTime(DateTimeZone.UTC)
                    .withYear(value[0])
                    .withMonthOfYear(value[1])
                    .withDayOfMonth(value[2])
                    .withHourOfDay(value[3])
                    .withMinuteOfHour(value[4])
                    .withSecondOfMinute(value[5])
                    .withMillisOfSecond(value[6]);
        }
        
        return result;
    }
    
    /**
     * Converts datetime object to JSON array node.<p>
     * Given datetime object is casted to UTC.
     * 
     * @param value datetime object
     * @return JSON array node (UTC)
     */
    public static ArrayNode dateTimeToArrayNode(DateTime value) {
        ArrayNode result;
        
        if (value == null) {
            result = null;
        }
        else {
            DateTime valueUTC = value.toDateTime(DateTimeZone.UTC);
            
            result = SharedInstanceHolder.OBJECT_MAPPER.valueToTree(DateTimeUtils.dateTimeToArray(valueUTC));
        }
        
        return result;
    }
    
}
