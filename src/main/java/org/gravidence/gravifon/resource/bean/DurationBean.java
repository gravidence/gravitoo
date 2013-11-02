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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gravidence.gravifon.db.domain.Duration;
import org.gravidence.gravifon.db.domain.DurationUnit;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.error.GravifonError;

/**
 * Duration bean.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class DurationBean extends ValidateableBean {
    
    /**
     * @see #getAmount()
     */
    @JsonProperty
    private Long amount;
    
    /**
     * @see #getUnit()
     */
    @JsonProperty
    private DurationUnit unit;

    /**
     * Returns duration amount.
     * 
     * @return duration amount
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * @param amount
     * @see #getAmount()
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * Returns duration unit.
     * 
     * @return duration unit
     */
    public DurationUnit getUnit() {
        return unit;
    }

    /**
     * @param unit
     * @see #getUnit()
     */
    public void setUnit(DurationUnit unit) {
        this.unit = unit;
    }

    @Override
    public void validate() {
        checkRequired(amount, "amount");
        if (amount < 1) {
            throw new GravifonException(GravifonError.INVALID,
                    "Invalid 'amount' property value. It must be a positive number.");
        }
        
        checkRequired(unit, "unit");
    }
    
    /**
     * Updates bean with duration information.
     * 
     * @param duration duration information
     * @return duration bean
     */
    public DurationBean updateBean(Duration duration) {
        if (duration != null) {
            amount = duration.getAmount();
            unit = duration.getUnit();
        }
        
        return this;
    }

    /**
     * Returns amount in millis. Amount is converted to millis if another unit type is used.
     * 
     * @param duration a duration
     * @return amount in millis
     */
    @JsonIgnore
    public static Long getMillisAmount(DurationBean duration) {
        Long result;
        
        if (duration == null || duration.getAmount() == null) {
            result = null;
        }
        else {
            switch (duration.getUnit()) {
                case SECOND:
                    result = duration.getAmount() * 1000;
                    break;
                case MILLISECOND:
                default:
                    result = duration.getAmount();
            }
        }
        
        return result;
    }

    /**
     * Returns amount in millis. Amount is converted to millis if another unit type is used.
     * 
     * @param duration a duration
     * @return amount in millis
     */
    @JsonIgnore
    public static Long getMillisAmount(Duration duration) {
        Long result;
        
        if (duration == null || duration.getAmount() == null) {
            result = null;
        }
        else {
            switch (duration.getUnit()) {
                case SECOND:
                    result = duration.getAmount() * 1000;
                    break;
                case MILLISECOND:
                default:
                    result = duration.getAmount();
            }
        }
        
        return result;
    }
    
}
