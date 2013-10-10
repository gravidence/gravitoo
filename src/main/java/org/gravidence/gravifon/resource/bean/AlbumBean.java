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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.db.domain.AlbumDocument;
import org.gravidence.gravifon.db.domain.Label;
import org.gravidence.gravifon.db.domain.Upvote;
import org.gravidence.gravifon.db.domain.VariationInfo;
import org.gravidence.gravifon.db.message.CreateDocumentResponse;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.gravidence.gravifon.util.BasicUtils;
import org.gravidence.gravifon.util.DateTimeUtils;
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

    /**
     * Updates bean with created document identifier.
     * DB returns document identifier and revision only, so there's no need to update bean values.
     * 
     * @param document a created document
     * @return updated bean
     */
    public AlbumBean updateBean(CreateDocumentResponse document) {
        if (document != null) {
            id = document.getId();
        }
        
        return this;
    }
    
    /**
     * Updates bean with document values.
     * 
     * @param document label details document
     * @return updated bean
     */
    public AlbumBean updateBean(AlbumDocument document) {
        if (document != null) {
            id = document.getId();
            title = document.getTitle();
            type = document.getType();
            releaseDate = DateTimeUtils.arrayToLocalDate(document.getReleaseDate());
            artists = extractArtists(document.getArtistIds());
            tracks = extractTracks(document.getTrackIds());
            labels = extractLabels(document.getLabels());
            
            if (document.getVariationInfo() == null) {
                variationInfo = null;
            }
            else {
                if (variationInfo == null) {
                    variationInfo = new VariationInfoBean<>();
                }
                // Update with upvotes and primary identifier
                variationInfo.updateBean(document.getVariationInfo());
                // Update with variation identifiers
                variationInfo.setVariations(extractVariations(document.getVariationInfo().getVariationIds()));
            }
        }
        
        return this;
    }
    
    /**
     * Creates document with bean values.
     * 
     * @return created document
     */
    public AlbumDocument createDocument() {
        AlbumDocument document = new AlbumDocument();
        document.setId(BasicUtils.generateUniqueIdentifier());
        
        if (variationInfo == null) {
            variationInfo = new VariationInfoBean<>();
            variationInfo.setPrimaryVariationId(document.getId());
        }
        
        updateDocument(document);
        
        return document;
    }
    
    /**
     * Updates document with bean values.
     * 
     * @param document album details document
     * @return updated document
     */
    public AlbumDocument updateDocument(AlbumDocument document) {
        if (document != null) {
            document.setTitle(title);
            document.setType(type);
            document.setReleaseDate(DateTimeUtils.localDateToArray(releaseDate));
            document.setArtistIds(extractArtistIds());
            document.setTrackIds(extractTrackIds());
            document.setLabels(extractLabels());
            
            if (variationInfo == null) {
                document.setVariationInfo(null);
            }
            else {
                VariationInfo vi = new VariationInfo();
                vi.setUpvotes(extractUpvotes());
                vi.setPrimaryVariationId(variationInfo.getPrimaryVariationId());
                vi.setVariationIds(extractVariationIds());
                
                document.setVariationInfo(vi);
            }
        }
        
        return document;
    }
    
    private List<ArtistBean> extractArtists(List<String> ids) {
        List<ArtistBean> result;
        
        if (CollectionUtils.isNotEmpty(ids)) {
            result = new ArrayList<>(ids.size() + 1);

            for (String id : ids) {
                ArtistBean resultItem = new ArtistBean();
                resultItem.setId(id);

                result.add(resultItem);
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    private List<TrackBean> extractTracks(List<String> ids) {
        List<TrackBean> result;
        
        if (CollectionUtils.isNotEmpty(ids)) {
            result = new ArrayList<>(ids.size() + 1);

            for (String id : ids) {
                TrackBean resultItem = new TrackBean();
                resultItem.setId(id);

                result.add(resultItem);
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    private List<LabelBean> extractLabels(List<Label> labels) {
        List<LabelBean> result;
        
        if (CollectionUtils.isNotEmpty(labels)) {
            result = new ArrayList<>(labels.size() + 1);

            for (Label label : labels) {
                LabelBean resultItem = new LabelBean();
                resultItem.setId(label.getId());
                resultItem.setCatalogId(label.getCatalogId());

                result.add(resultItem);
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    private List<AlbumBean> extractVariations(List<String> ids) {
        List<AlbumBean> result;
        
        if (CollectionUtils.isNotEmpty(ids)) {
            result = new ArrayList<>(ids.size() + 1);

            for (String id : ids) {
                AlbumBean resultItem = new AlbumBean();
                resultItem.setId(id);

                result.add(resultItem);
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    private List<String> extractArtistIds() {
        List<String> result;
        
        if (CollectionUtils.isNotEmpty(artists)) {
            result = new ArrayList<>(artists.size() + 1);

            for (ArtistBean artist : artists) {
                result.add(artist.getId());
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    private List<String> extractTrackIds() {
        List<String> result;
        
        if (CollectionUtils.isNotEmpty(tracks)) {
            result = new ArrayList<>(tracks.size() + 1);

            for (TrackBean track : tracks) {
                result.add(track.getId());
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    private List<Label> extractLabels() {
        List<Label> result;
        
        if (CollectionUtils.isNotEmpty(labels)) {
            result = new ArrayList<>(labels.size() + 1);

            for (LabelBean label : labels) {
                Label resultItem = new Label();
                resultItem.setId(label.getId());
                resultItem.setCatalogId(label.getCatalogId());

                result.add(resultItem);
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    private List<Upvote> extractUpvotes() {
        List<Upvote> result;
        
        if (variationInfo != null && CollectionUtils.isNotEmpty(variationInfo.getUpvotes())) {
            result = new ArrayList<>(variationInfo.getUpvotes().size() + 1);

            for (UpvoteBean upvote : variationInfo.getUpvotes()) {
                Upvote resultItem = new Upvote();
                resultItem.setUserId(upvote.getUserId());

                result.add(resultItem);
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    private List<String> extractVariationIds() {
        List<String> result;
        
        if (variationInfo != null && CollectionUtils.isNotEmpty(variationInfo.getVariations())) {
            result = new ArrayList<>(variationInfo.getVariations().size() + 1);

            for (AlbumBean album : variationInfo.getVariations()) {
                result.add(album.getId());
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
}
