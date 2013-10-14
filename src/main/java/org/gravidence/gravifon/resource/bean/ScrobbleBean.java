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
import org.gravidence.gravifon.db.domain.ScrobbleDocument;
import org.gravidence.gravifon.db.message.CreateDocumentResponse;
import org.gravidence.gravifon.util.BasicUtils;
import org.gravidence.gravifon.util.DateTimeUtils;
import org.joda.time.DateTime;

/**
 * Scrobble bean.<p>
 * Represents Scrobble interface between service and clients.
 * Contains helpful methods to work with {@link ScrobbleDocument}.
 * 
 * @see ScrobbleDocument
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ScrobbleBean extends ValidateableBean {
    
    /**
     * @see #getId()
     */
    @JsonProperty
    private String id;
    
    /**
     * @see #getScrobbleStartDatetime()
     */
    @JsonProperty("scrobble_start_datetime")
    private DateTime scrobbleStartDatetime;
    
    /**
     * @see #getScrobbleEndDatetime()
     */
    @JsonProperty("scrobble_end_datetime")
    private DateTime scrobbleEndDatetime;
    
    /**
     * @see #getScrobbleDuration()
     */
    @JsonProperty("scrobble_duration")
    private DurationBean scrobbleDuration;
    
    /**
     * @see #getTrack()
     */
    @JsonProperty
    private TrackBean track;

    /**
     * Returns scrobble identifier.
     * 
     * @return scrobble identifier
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
     * Returns date and time when scrobble event was initiated.
     * 
     * @return date and time when scrobble event was initiated
     */
    public DateTime getScrobbleStartDatetime() {
        return scrobbleStartDatetime;
    }

    /**
     * @param scrobbleStartDatetime
     * @see #getScrobbleStartDatetime()
     */
    public void setScrobbleStartDatetime(DateTime scrobbleStartDatetime) {
        this.scrobbleStartDatetime = scrobbleStartDatetime;
    }

    /**
     * Returns date and time when scrobble event was finished.
     * 
     * @return date and time when scrobble event was finished
     */
    public DateTime getScrobbleEndDatetime() {
        return scrobbleEndDatetime;
    }

    /**
     * @param scrobbleEndDatetime
     * @see #getScrobbleEndDatetime()
     */
    public void setScrobbleEndDatetime(DateTime scrobbleEndDatetime) {
        this.scrobbleEndDatetime = scrobbleEndDatetime;
    }

    /**
     * Returns scrobble length.
     * 
     * @return scrobble length
     */
    public DurationBean getScrobbleDuration() {
        return scrobbleDuration;
    }

    /**
     * @param scrobbleDuration
     * @see #getScrobbleDuration()
     */
    public void setScrobbleDuration(DurationBean scrobbleDuration) {
        this.scrobbleDuration = scrobbleDuration;
    }

    /**
     * Returns track associated with scrobble event.
     * 
     * @return track associated with scrobble event
     */
    public TrackBean getTrack() {
        return track;
    }

    /**
     * @param track
     * @see #getTrack()
     */
    public void setTrack(TrackBean track) {
        this.track = track;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, start=%s, track=%s}", id, scrobbleStartDatetime, track);
    }

    @Override
    public void validate() {
        checkRequired(scrobbleStartDatetime, "scrobble_start_datetime");
        
        checkRequired(scrobbleEndDatetime, "scrobble_end_datetime");
        
        checkRequired(scrobbleDuration, "scrobble_duration");
        scrobbleDuration.validate();
        
        checkRequired(track, "track");
        track.validate();
        
        // TODO add datetime/duration consistency check
    }

    /**
     * Updates bean with created document identifier.
     * DB returns document identifier and revision only, so there's no need to update bean values.
     * 
     * @param document a created document
     * @return updated bean
     */
    public ScrobbleBean updateBean(CreateDocumentResponse document) {
        if (document != null) {
            id = document.getId();
        }
        
        return this;
    }
    
    /**
     * Updates bean with document values.
     * 
     * @param document scrobble details document
     * @return updated bean
     */
    public ScrobbleBean updateBean(ScrobbleDocument document) {
        if (document != null) {
            id = document.getId();
            scrobbleStartDatetime = DateTimeUtils.arrayToDateTime(document.getScrobbleStartDatetime());
            scrobbleEndDatetime = DateTimeUtils.arrayToDateTime(document.getScrobbleEndDatetime());
            scrobbleDuration = new DurationBean().updateBean(document.getScrobbleDuration());
            track = BeanUtils.idToTrackBean(document.getTrackId());
        }
        
        return this;
    }
    
    /**
     * Creates document with bean values.
     * 
     * @return created document
     */
    public ScrobbleDocument createDocument() {
        ScrobbleDocument document = new ScrobbleDocument();
        document.setId(BasicUtils.generateUniqueIdentifier());
        
        updateDocument(document);
        
        return document;
    }
    
    /**
     * Updates document with bean values.
     * 
     * @param document scrobble details document
     * @return updated document
     */
    public ScrobbleDocument updateDocument(ScrobbleDocument document) {
        if (document != null) {
            document.setScrobbleStartDatetime(DateTimeUtils.dateTimeToArray(scrobbleStartDatetime));
            document.setScrobbleEndDatetime(DateTimeUtils.dateTimeToArray(scrobbleEndDatetime));
            document.setScrobbleDuration(BeanUtils.durationBeanToDuration(scrobbleDuration));
            document.setTrackId(track == null ? null : track.getId());
        }
        
        return document;
    }
    
}
