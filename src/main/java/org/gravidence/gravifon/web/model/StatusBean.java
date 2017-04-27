/*
 * The MIT License
 *
 * Copyright 2016 Gravidence.
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

package org.gravidence.gravifon.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Status response entity.<p>
 * Represents technical response details (status, error code and error description)
 * as well as business details (entity identifier or entity itself).
 * Error related fields are not populated if response status is successful.
 *
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class StatusBean<T> {

    @JsonProperty
    private boolean ok;

    @JsonProperty("error_code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer errorCode;

    @JsonProperty("error_description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorDescription;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T entity;

    /**
     * Constructs successful status response.
     */
    public StatusBean() {
        ok = true;
    }

    /**
     * Constructs error status response.
     *
     * @param errorCode error code
     * @param errorDescription human readable error description
     */
    public StatusBean(Integer errorCode, String errorDescription) {
        if (errorCode == null) {
            throw new IllegalArgumentException("errorCode");
        }
        if (errorDescription == null) {
            throw new IllegalArgumentException("errorDescription");
        }

        this.ok = false;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    /**
     * Constructs successful status response with entity identifier.
     *
     * @param id entity identifier
     */
    public StatusBean(String id) {
        this();
        this.id = id;
    }

    /**
     * Constructs successful status response with entity.
     *
     * @param entity entity
     */
    public StatusBean(T entity) {
        this();
        this.entity = entity;
    }

}
