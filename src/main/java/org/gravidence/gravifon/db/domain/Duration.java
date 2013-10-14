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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Duration information.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class Duration {
    
    /**
     * @see #getAmount()
     */
    @JsonProperty
    private Long amount;
    
    /**
     * @see #getUnit()
     */
    @JsonProperty
    private String unit;

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
     * Returns duration unit.<p>
     * Allowed values are: "s" for seconds and "ms" for milliseconds.
     * 
     * @return duration unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit
     * @see #getUnit()
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
}
