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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.gravidence.gravifon.db.domain.Duration;
import org.gravidence.gravifon.db.domain.Label;
import org.gravidence.gravifon.db.domain.Upvote;

/**
 * Bean utility methods.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class BeanUtils {

    /**
     * Preventing class instantiation.
     */
    private BeanUtils() {
        // Nothing to do
    }
    
    /**
     * Creates album bean populated with given identifier.
     * 
     * @param id album identifier
     * @return album bean populated with given identifier
     */
    public static AlbumBean idToAlbumBean(String id) {
        AlbumBean result;
        
        if (id == null) {
            result = null;
        }
        else {
            result = new AlbumBean();
            result.setId(id);
        }
        
        return result;
    }
    
    /**
     * Creates list of artist beans populated with given identifiers.
     * 
     * @param ids artist identifiers
     * @return list of artist beans populated with given identifiers
     */
    public static List<ArtistBean> idsToArtistBeans(List<String> ids) {
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
    
    /**
     * Creates list of track beans populated with given identifiers.
     * 
     * @param ids track identifiers
     * @return list of track beans populated with given identifiers
     */
    public static List<TrackBean> idsToTrackBeans(List<String> ids) {
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
    
    /**
     * Creates list of album beans populated with given identifiers.
     * 
     * @param ids album identifiers
     * @return list of album beans populated with given identifiers
     */
    public static List<AlbumBean> idsToAlbumBeans(List<String> ids) {
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
    
    /**
     * Creates list of label beans populated with given identifiers.
     * 
     * @param ids label identifiers
     * @return list of label beans populated with given identifiers
     */
    public static List<LabelBean> idsToLabelBeans(List<String> ids) {
        List<LabelBean> result;
        
        if (CollectionUtils.isNotEmpty(ids)) {
            result = new ArrayList<>(ids.size() + 1);

            for (String id : ids) {
                LabelBean resultItem = new LabelBean();
                resultItem.setId(id);

                result.add(resultItem);
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    /**
     * Creates list of label beans populated with given label details.
     * 
     * @param labels label details
     * @return list of label beans populated with given label details
     */
    public static List<LabelBean> labelsToLabelBeans(List<Label> labels) {
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
    
    /**
     * Creates list of artist identifiers according to given artist beans.
     * 
     * @param artists artist beans
     * @return list of artist identifiers according to given artist beans
     */
    public static List<String> artistBeansToIds(List<ArtistBean> artists) {
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
    
    /**
     * Creates list of track identifiers according to given track beans.
     * 
     * @param tracks track beans
     * @return list of track identifiers according to given track beans
     */
    public static List<String> trackBeansToIds(List<TrackBean> tracks) {
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
    
    /**
     * Creates list of album identifiers according to given album beans.
     * 
     * @param albums album beans
     * @return list of album identifiers according to given album beans
     */
    public static List<String> albumBeansToIds(List<AlbumBean> albums) {
        List<String> result;
        
        if (CollectionUtils.isNotEmpty(albums)) {
            result = new ArrayList<>(albums.size() + 1);

            for (AlbumBean album : albums) {
                result.add(album.getId());
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    /**
     * Creates list of label identifiers according to given label beans.
     * 
     * @param labels label beans
     * @return list of label identifiers according to given label beans
     */
    public static List<String> labelBeansToIds(List<LabelBean> labels) {
        List<String> result;
        
        if (CollectionUtils.isNotEmpty(labels)) {
            result = new ArrayList<>(labels.size() + 1);

            for (LabelBean label : labels) {
                result.add(label.getId());
            }
        }
        else {
            result = null;
        }
        
        return result;
    }
    
    /**
     * Creates duration details according to given duration bean.
     * 
     * @param duration duration bean
     * @return duration details according to given duration bean
     */
    public static Duration durationBeanToDuration(DurationBean duration) {
        Duration result;
        
        if (duration == null) {
            result = null;
        }
        else {
            result = new Duration();
            result.setAmount(duration.getAmount());
            result.setUnit(duration.getUnit());
        }
        
        return result;
    }
    
    /**
     * Creates list of label details according to given label beans.
     * 
     * @param labels label beans
     * @return list of label details according to given label beans
     */
    public static List<Label> labelBeansToLabels(List<LabelBean> labels) {
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
    
    /**
     * Creates list of upvote details according to given upvote beans.
     * 
     * @param upvotes upvote beans
     * @return list of upvote details according to given upvote beans
     */
    public static List<Upvote> upvoteBeansToUpvotes(List<UpvoteBean> upvotes) {
        List<Upvote> result;
        
        if (CollectionUtils.isNotEmpty(upvotes)) {
            result = new ArrayList<>(upvotes.size() + 1);

            for (UpvoteBean upvote : upvotes) {
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
    
}
