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
package org.gravidence.gravifon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Status response bean.<p>
 * Represents technical response details: status, error code and error description.
 * Error related fields are not populated if response {@link #isOk() status} is successful.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class StatusResponse {

    /**
     * @see #isOk()
     */
    @JsonProperty
    private boolean ok;
    
    /**
     * @see #getErrorCode()
     */
    @JsonProperty("error_code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer errorCode;
    
    /**
     * @see #getErrorDescription()
     */
    @JsonProperty("error_description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorDescription;

    /**
     * Constructs successful status response.
     */
    public StatusResponse() {
        ok = true;
    }

    /**
     * Constructs error status response.
     *
     * @param errorCode error code
     * @param errorDescription human readable error description
     */
    public StatusResponse(Integer errorCode, String errorDescription) {
        // TODO uncomment when some error codes appear
//        if (errorCode == null) {
//            throw new NullPointerException("errorCode");
//        }
//        if (errorDescription == null) {
//            throw new NullPointerException("errorDescription");
//        }

        this.ok = false;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    /**
     * Returns response status.
     * 
     * @return <code>true</code> if response is successful
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * Returns error code.<p>
     * Populated if {@link #isOk() response status} is not successful.
     * 
     * @return error code
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * Returns human readable error description.<p>
     * Populated if {@link #isOk() response status} is not successful.
     * 
     * @return error description
     */
    public String getErrorDescription() {
        return errorDescription;
    }
    
}
