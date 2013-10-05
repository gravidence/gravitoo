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
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.joda.time.LocalDate;

/**
 * Album bean.<p>
 * Represents Album interface between service and clients.
 * Contains helpful methods to work with {@link AlbumDocument}.
 * 
 * @see AlbumDocument
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class AlbumBean extends ValidateableBean {
    
    /**
     * @see #getId()
     */
    @JsonProperty
    private String id;
    
    /**
     * @see #getTitle()
     */
    @JsonProperty
    private String title;
    
    /**
     * @see #getArtists()
     */
    @JsonProperty
    private List<ArtistBean> artists;
    
    /**
     * @see #getReleaseDate()
     */
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    
    /**
     * @see #getLabels()
     */
    @JsonProperty
    private List<LabelBean> labels;
    
    /**
     * @see #getTracks()
     */
    @JsonProperty
    private List<TrackBean> tracks;
    
    /**
     * @see #getType()
     */
    @JsonProperty
    private String type;
    
    /**
     * @see #getVariationInfo()
     */
    @JsonProperty("variation_info")
    private VariationInfoBean<AlbumBean> variationInfo;

    /**
     * Returns album identifier.
     * 
     * @return album identifier
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
     * Returns album title.
     * 
     * @return album title
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
     * Returns list of album artists.<p>
     * Usually used when album artist(s) differs from track artists (DJ mix, compilation, etc.).
     * 
     * @return list of album artists
     */
    public List<ArtistBean> getArtists() {
        return artists;
    }

    /**
     * @param artists
     * @see #getArtists()
     */
    public void setArtists(List<ArtistBean> artists) {
        this.artists = artists;
    }

    /**
     * Returns album release date.
     * 
     * @return album release date
     */
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate
     * @see #getReleaseDate()
     */
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Returns list of labels that produced the album.
     * 
     * @return list of labels that produced the album
     */
    public List<LabelBean> getLabels() {
        return labels;
    }

    /**
     * @param labels
     * @see #getLabels()
     */
    public void setLabels(List<LabelBean> labels) {
        this.labels = labels;
    }

    /**
     * Returns list of album tracks.
     * 
     * @return list of artist tracks
     */
    public List<TrackBean> getTracks() {
        return tracks;
    }

    /**
     * @param tracks
     * @see #getTracks()
     */
    public void setTracks(List<TrackBean> tracks) {
        this.tracks = tracks;
    }

    /**
     * Returns album type.<p>
     * Allowed values are: "Album", "Compilation".
     * 
     * @return artist identifier
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     * @see #getType()
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns album variation info.
     * 
     * @return album variation info
     */
    public VariationInfoBean<AlbumBean> getVariationInfo() {
        return variationInfo;
    }

    /**
     * @param variationInfo
     * @see #getVariationInfo()
     */
    public void setVariationInfo(VariationInfoBean<AlbumBean> variationInfo) {
        this.variationInfo = variationInfo;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, title=%s}", id, title);
    }

    @Override
    public void validate() {
        checkRequired(title, "title");
        checkLength(title, "title", 1, 400);
        
        validateCollection(artists);
        
        validateCollection(labels);
        
        if (StringUtils.isNotEmpty(type)) {
            if (!("Album".equals(type) || "Compilation".equals(type))) {
                throw new GravifonException(GravifonError.UNKNOWN, "Unsupported 'type' property value.");
            }
        }
        
        if (variationInfo != null) {
            variationInfo.validate();
        }
    }
    
}
