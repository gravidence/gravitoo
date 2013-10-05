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
 * Artist bean.<p>
 * Represents Artist interface between service and clients.
 * Contains helpful methods to work with {@link ArtistDocument}.
 * 
 * @see ArtistDocument
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ArtistBean extends ValidateableBean {
    
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
     * @see #getSubname()
     */
    @JsonProperty
    private String subname;
    
    /**
     * @see #getSubartists()
     */
    @JsonProperty
    private List<ArtistBean> subartists;
    
    /**
     * @see #getAlbums()
     */
    @JsonProperty
    private List<AlbumBean> albums;
    
    /**
     * @see #getVariationInfo()
     */
    @JsonProperty("variation_info")
    private VariationInfoBean<ArtistBean> variationInfo;
    
    /**
     * @see #getAliases()
     */
    @JsonProperty
    private List<ArtistBean> aliases;

    /**
     * Returns artist identifier.
     * 
     * @return artist identifier
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
     * Returns list of artists that form this artist variation.
     * 
     * @return list of artists that form this artist variation
     */
    public List<ArtistBean> getSubartists() {
        return subartists;
    }

    /**
     * @param subartists
     * @see #getSubartists()
     */
    public void setSubartists(List<ArtistBean> subartists) {
        this.subartists = subartists;
    }

    /**
     * Returns list of artist albums.
     * 
     * @return list of artist albums
     */
    public List<AlbumBean> getAlbums() {
        return albums;
    }

    /**
     * @param albums
     * @see #getAlbums()
     */
    public void setAlbums(List<AlbumBean> albums) {
        this.albums = albums;
    }

    /**
     * Returns artist variation info.
     * 
     * @return artist variation info
     */
    public VariationInfoBean<ArtistBean> getVariationInfo() {
        return variationInfo;
    }

    /**
     * @param variationInfo
     * @see #getVariationInfo()
     */
    public void setVariationInfo(VariationInfoBean<ArtistBean> variationInfo) {
        this.variationInfo = variationInfo;
    }

    /**
     * Returns list of artist aliases.
     * 
     * @return list of artist aliases
     */
    public List<ArtistBean> getAliases() {
        return aliases;
    }

    /**
     * @param aliases
     * @see #getAliases()
     */
    public void setAliases(List<ArtistBean> aliases) {
        this.aliases = aliases;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, name=%s}", id, name);
    }

    @Override
    public void validate() {
        checkRequired(name, "name");
        
        if (variationInfo != null) {
            variationInfo.validate();
        }
    }
    
}
