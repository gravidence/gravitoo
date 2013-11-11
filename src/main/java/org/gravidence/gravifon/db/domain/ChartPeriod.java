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
package org.gravidence.gravifon.db.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.StringUtils;

/**
 * Chart period enumeration.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public enum ChartPeriod {
    
    DAY_3("day_3"),
    DAY_7("day_7"),
    DAY_30("day_30"),
    DAY_100("day_100"),
    DAY_180("day_180"),
    DAY_365("day_365"),
    ALL_TIME("all_time");
    
    /**
     * @see #toString()
     */
    private final String value;
    
    /**
     * Constructor that initiates particular chart period with human-friendly string representation.
     * 
     * @param value string representation of particular chart period
     */
    private ChartPeriod(String value) {
        this.value = value;
    }
    
    /**
     * Returns string representation of particular chart period.
     * 
     * @return string representation of particular chart period
     */
    @Override
    @JsonValue
    public String toString() {
        return value;
    }
    
    /**
     * Returns chart period that corresponds to supplied <code>value</code>.
     * 
     * @param value string representation of chart period
     * @return chart period
     * @throws IllegalArgumentException in case supplied <code>value</code> is not supported
     */
    @JsonCreator
    public static ChartPeriod fromString(String value) throws IllegalArgumentException {
        for (ChartPeriod period : ChartPeriod.values()) {
            if (StringUtils.equalsIgnoreCase(period.toString(), value)) {
                return period;
            }
        }
        
        throw new IllegalArgumentException();
    }
    
}
