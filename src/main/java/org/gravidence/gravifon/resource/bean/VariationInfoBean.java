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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Variation information bean.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class VariationInfoBean<T> extends ValidateableBean {
    
    /**
     * @see #getUpvotes()
     */
    @JsonProperty
    private List<UpvoteBean> upvotes;
    
    /**
     * @see #getPrimaryVariationId()
     */
    @JsonProperty("primary_variation_id")
    private String primaryVariationId;
    
    /**
     * @see #getVariations()
     */
    @JsonProperty
    private List<T> variations;

    /**
     * Returns list of entity variation upvotes.
     * 
     * @return list of entity variation upvotes
     */
    public List<UpvoteBean> getUpvotes() {
        return upvotes;
    }

    /**
     * @param upvotes
     * @see #getUpvotes()
     */
    public void setUpvotes(List<UpvoteBean> upvotes) {
        this.upvotes = upvotes;
    }

    /**
     * Returns primary entity variation identifier.
     * 
     * @return primary entity variation identifier
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
     * Returns list of entity variations.<p>
     * Returned in case the entity variation is primary.
     * 
     * @return list of entity variations
     */
    public List<T> getVariations() {
        return variations;
    }

    /**
     * @param variations
     * @see #getVariations()
     */
    public void setVariations(List<T> variations) {
        this.variations = variations;
    }

    @Override
    public void validate() {
        // Nothing to validate.
    }
    
}
