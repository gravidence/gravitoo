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
 * Album document.<p>
 * Represents Album database model.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class AlbumDocument extends VariableDocument {
    
    /**
     * @see #getTitle()
     */
    @JsonProperty
    private String title;
    
    /**
     * @see #getArtistIds()
     */
    @JsonProperty("artist_ids")
    private List<String> artistIds;
    
    /**
     * @see #getReleaseDate()
     */
    @JsonProperty("release_date")
    private int[] releaseDate;
    
    /**
     * @see #getLabels()
     */
    @JsonProperty
    private List<Label> labels;
    
    /**
     * @see #getTrackIds()
     */
    @JsonProperty("track_ids")
    private List<String> trackIds;
    
    /**
     * @see #getType()
     */
    @JsonProperty
    private String type;

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
     * Returns list of album artist identifiers.
     * 
     * @return list of album artist identifiers
     */
    public List<String> getArtistIds() {
        return artistIds;
    }

    /**
     * @param artistIds
     * @see #getArtistIds()
     */
    public void setArtistIds(List<String> artistIds) {
        this.artistIds = artistIds;
    }

    /**
     * Returns album release date (UTC).<p>
     * Array content is as follows: <code>[yyyy,MM,dd]</code>.
     * 
     * @return album release date (UTC)
     */
    public int[] getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate
     * @see #getReleaseDate()
     */
    public void setReleaseDate(int[] releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Returns list of labels that produced the album.
     * 
     * @return list of labels that produced the album
     */
    public List<Label> getLabels() {
        return labels;
    }

    /**
     * @param labels
     * @see #getLabels()
     */
    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    /**
     * Returns list of album track identifiers.
     * 
     * @return list of artistId track identifiers
     */
    public List<String> getTrackIds() {
        return trackIds;
    }

    /**
     * @param trackIds
     * @see #getTrackIds()
     */
    public void setTrackIds(List<String> trackIds) {
        this.trackIds = trackIds;
    }

    /**
     * Returns album type.<p>
     * Allowed values are: "Album", "Compilation".
     * 
     * @return album type
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

    @Override
    public String toString() {
        return String.format("{id=%s, title=%s}", getId(), title);
    }
    
}
