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
 * Track document.<p>
 * Represents Track database model.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class TrackDocument extends VariableDocument {
    
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
     * @see #getLength()
     */
    @JsonProperty
    private Duration length;
    
    /**
     * @see #getAlbumId()
     */
    @JsonProperty("album_id")
    private String albumId;
    
    /**
     * @see #getPosition()
     */
    @JsonProperty
    private String position;

    /**
     * Returns track title.
     * 
     * @return track title
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
     * Returns list of track artist identifiers.
     * 
     * @return list of track artist identifiers
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
     * Returns track length.
     * 
     * @return track length
     */
    public Duration getLength() {
        return length;
    }

    /**
     * @param length
     * @see #getLength()
     */
    public void setLength(Duration length) {
        this.length = length;
    }

    /**
     * Returns track album identifier.
     * 
     * @return track album identifier
     */
    public String getAlbumId() {
        return albumId;
    }

    /**
     * @param albumId
     * @see #getAlbumId()
     */
    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    /**
     * Returns track position in the album tracklist.
     * 
     * @return track position in the album tracklist
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position
     * @see #getPosition()
     */
    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, title=%s}", getId(), title);
    }
    
}
