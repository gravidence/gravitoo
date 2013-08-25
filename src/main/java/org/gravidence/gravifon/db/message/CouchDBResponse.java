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
package org.gravidence.gravifon.db.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic response details could be returned by CouchDB.<p>
 * Specifies response status, error type and error reason. Error fields are optional.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class CouchDBResponse {
    
    /**
     * @see #getStatus()
     */
    @JsonProperty("ok")
    private boolean status;
    
    /**
     * @see #getErrorType()
     */
    @JsonProperty("error")
    private String errorType;
    
    /**
     * @see #getErrorReason()
     */
    @JsonProperty("reason")
    private String errorReason;

    /**
     * Returns response status.
     * 
     * @return <code>true</code> if response is successful
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Returns error type.<p>
     * Populated when {@link #getStatus() response status}=<code>false</code>.
     * 
     * @return error type
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * Returns error reason.<p>
     * Populated when {@link #getStatus() response status}=<code>false</code>.
     * 
     * @return error reason
     */
    public String getErrorReason() {
        return errorReason;
    }
    
}
