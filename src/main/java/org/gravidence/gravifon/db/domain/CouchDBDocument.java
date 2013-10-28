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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic document could be stored in CouchDB.<p>
 * Specifies the document identifier and revision.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
// Ignore all unknown elements to read only needed properties
// (e.g. to remove dependency on particular CouchDB release version)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CouchDBDocument {
    
    /**
     * @see #getId()
     */
    @JsonProperty("_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    
    /**
     * @see #getRevision()
     */
    @JsonProperty("_rev")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String revision;

    /**
     * Returns CouchDB document identifier.
     * 
     * @return document identifier
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
     * Returns CouchDB document revision.
     * 
     * @return document revision
     */
    public String getRevision() {
        return revision;
    }
    
}
