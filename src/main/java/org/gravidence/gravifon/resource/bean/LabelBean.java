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
import org.apache.commons.lang.StringUtils;

/**
 * Label bean.<p>
 * Represents Label interface between service and clients.
 * Contains helpful methods to work with {@link LabelDocument}.
 * 
 * @see LabelDocument
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class LabelBean extends ValidateableBean {
    
    /**
     * @see #getId()
     */
    @JsonProperty
    private String id;
    
    /**
     * @see #getName()
     */
    @JsonProperty
    private String name;
    
    /**
     * @see #getCatalogId()
     */
    @JsonProperty("catalog_id")
    private String catalogId;
    
    /**
     * @see #getVariationInfo()
     */
    @JsonProperty("variation_info")
    private VariationInfoBean<LabelBean> variationInfo;

    /**
     * Returns label identifier.
     * 
     * @return label identifier
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     * @see #getId()
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns label name.
     * 
     * @return label name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     * @see #getName()
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns an {@link AlbumBean album} identifier in label catalog.
     * 
     * @return an album identifier in label catalog
     */
    public String getCatalogId() {
        return catalogId;
    }

    /**
     * @param catalogId
     * @see #getCatalogId()
     */
    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    /**
     * Returns label variation info.
     * 
     * @return label variation info
     */
    public VariationInfoBean<LabelBean> getVariationInfo() {
        return variationInfo;
    }

    /**
     * @param variationInfo
     * @see #getVariationInfo()
     */
    public void setVariationInfo(VariationInfoBean<LabelBean> variationInfo) {
        this.variationInfo = variationInfo;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, name=%s}", id, name);
    }

    @Override
    public void validate() {
        checkRequired(name, "name");
        checkLength(name, "name", 1, 400);
        
        if (StringUtils.isNotEmpty(catalogId)) {
            checkLength(catalogId, "catalog_id", 1, 400);
        }
        
        if (variationInfo != null) {
            variationInfo.validate();
        }
    }
    
}
