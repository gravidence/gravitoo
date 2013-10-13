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
import java.util.List;

/**
 * Artist document.<p>
 * Represents Artist database model.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ArtistDocument extends VariableDocument {
    
    /**
     * @see #getName()
     */
    @JsonProperty
    private String name;
    
    /**
     * @see #getSubname()
     */
    @JsonProperty
    private String subname;
    
    /**
     * @see #getSubartistIds()
     */
    @JsonProperty("subartist_ids")
    private List<String> subartistIds;
    
    /**
     * @see #getAliasIds()
     */
    @JsonProperty("alias_ids")
    private List<String> aliasIds;

    /**
     * Returns artist name.
     * 
     * @return artist name
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
     * Returns artist disambiguation info (real name, alias, etc.).
     * 
     * @return artist disambiguation info (real name, alias, etc.)
     */
    public String getSubname() {
        return subname;
    }

    /**
     * @param subname
     * @see #getSubname()
     */
    public void setSubname(String subname) {
        this.subname = subname;
    }

    /**
     * Returns list of artist subartist identifiers.
     * 
     * @return list of artist subartist identifiers
     */
    public List<String> getSubartistIds() {
        return subartistIds;
    }

    /**
     * @param subartistIds
     * @see #getSubrtistIds()
     */
    public void setSubartistIds(List<String> subartistIds) {
        this.subartistIds = subartistIds;
    }

    /**
     * Returns list of artist alias identifiers.
     * 
     * @return list of artist alias identifiers
     */
    public List<String> getAliasIds() {
        return aliasIds;
    }

    /**
     * @param aliasIds
     * @see #getAliasIds()
     */
    public void setAliasIds(List<String> aliasIds) {
        this.aliasIds = aliasIds;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, name=%s}", getId(), name);
    }
    
}
