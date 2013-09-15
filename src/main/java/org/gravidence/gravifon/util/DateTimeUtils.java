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

import org.apache.commons.lang.ArrayUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

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
     * Converts local datetime object to array of local datetime fields.
     * 
     * @param value local datetime object
     * @return array of local datetime fields
     */
    public static int[] toArray(LocalDateTime value) {
        int[] result;
        
        if (value == null) {
            result = null;
        }
        else {
            result = new int[7];

            result[0] = value.getYear();
            result[1] = value.getMonthOfYear();
            result[2] = value.getDayOfMonth();
            result[3] = value.getHourOfDay();
            result[4] = value.getMinuteOfHour();
            result[5] = value.getSecondOfMinute();
            result[6] = value.getMillisOfSecond();
        }
        
        return result;
    }
    
    /**
     * Converts array of local datetime fields to local datetime object.
     * 
     * @param value array of local datetime fields
     * @return local datetime object
     */
    public static LocalDateTime fromArray(int[] value) {
        LocalDateTime result;
        
        if (ArrayUtils.isEmpty(value)) {
            result = null;
        }
        else {
            result = new LocalDateTime(DateTimeZone.UTC)
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
    
}
