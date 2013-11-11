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
import java.math.BigInteger;
import org.apache.commons.lang.ObjectUtils;

/**
 * Chart item entity.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ChartItem implements Comparable {
    
    /**
     * @see #getPrimaryVariationId()
     */
    @JsonProperty("primary_variation_id")
    private String primaryVariationId;
    
    /**
     * @see #getTitle()
     */
    @JsonProperty
    private String title;
    
    /**
     * @see #getValue()
     */
    @JsonProperty
    private BigInteger value;

    /**
     * Default construction.
     */
    public ChartItem() {
        // Nothing to do.
    }

    /**
     * Populates new instance with supplied values.
     * 
     * @param primaryVariationId chart item entity primary variation identifier
     * @param title chart item entity title
     * @param value chart item entity calculated value (amount or duration)
     */
    public ChartItem(String primaryVariationId, String title, BigInteger value) {
        this.primaryVariationId = primaryVariationId;
        this.title = title;
        this.value = value;
    }

    /**
     * Returns chart item entity primary variation identifier.
     * 
     * @return chart item entity primary variation identifier
     */
    public String getPrimaryVariationId() {
        return primaryVariationId;
    }

    /**
     * @param primaryVariationId
     * @see #getPrimaryVariationId()
     */
    public void setPrimaryVariationId(String primaryVariationId) {
        this.primaryVariationId = primaryVariationId;
    }

    /**
     * Returns chart item entity title.
     * 
     * @return chart item entity title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     * @see #getTitle()
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns chart item entity calculated value (amount or duration).
     * 
     * @return chart item entity calculated value (amount or duration)
     */
    public BigInteger getValue() {
        return value;
    }

    /**
     * @param value
     * @see #getValue()
     */
    public void setValue(BigInteger value) {
        this.value = value;
    }

    @Override
    // Descending order
    public int compareTo(Object o) {
        if (o != null && o instanceof ChartItem) {
            return -ObjectUtils.compare(value, ((ChartItem) o).getValue());
        }
        
        return -1;
    }
    
}
