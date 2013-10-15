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
package org.gravidence.gravifon.db;

import java.util.List;
import java.util.Locale;
import javax.ws.rs.client.WebTarget;
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.db.domain.TrackDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * CouchDB JAX-RS client to <code>/tracks</code> database.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class TracksDBClient extends BasicDBClient<TrackDocument> implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TracksDBClient.class);
    
    /**
     * JAX-RS client target associated with <code>main/all_primary_track_variations</code> view.
     */
    private WebTarget viewMainAllPrimaryTrackVariationsTarget;
    
    /**
     * JAX-RS client target associated with <code>main/all_track_variations</code> view.
     */
    private WebTarget viewMainAllTrackVariationsTarget;

    @Override
    public void afterPropertiesSet() throws Exception {
        setDbTarget(getDbClient().getTarget()
                .path("tracks"));
        
        viewMainAllPrimaryTrackVariationsTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_primary_track_variations");
        viewMainAllTrackVariationsTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_track_variations");
    }
    
    /**
     * Retrieves amount of primary track variations.<p>
     * Amount of primary track variations is equal to <code>main/all_primary_track_variations</code> view size.
     * 
     * @return amount of primary track variations
     * 
     * @see #viewMainAllPrimaryTrackVariationsTarget
     * @see ViewQueryExecutor#querySize(javax.ws.rs.client.WebTarget)
     */
    public long retrievePrimaryTrackVariationAmount() {
        return ViewQueryExecutor.querySize(viewMainAllPrimaryTrackVariationsTarget);
    }
    
    /**
     * Retrieves existing track {@link TrackDocument document}.
     * 
     * @param id track identifier
     * @return track details document if found, <code>null</code> otherwise
     */
    public TrackDocument retrieveTrackByID(String id) {
        return retrieve(id, TrackDocument.class);
    }
    
    /**
     * Retrieves existing track {@link TrackDocument documents}.<p>
     * Makes sure that <code>name</code> written in lower case
     * since <code>main/all_track_variations</code> view is case sensitive.
     * 
     * @param name track name
     * @return list of track details documents if found, <code>null</code> otherwise
     */
    public List<TrackDocument> retrieveTracksByName(String name) {
        ViewQueryArguments args = new ViewQueryArguments()
                .addKey(StringUtils.lowerCase(name, Locale.ENGLISH))
                .addIncludeDocs(true);
        
        List<TrackDocument> documents = ViewQueryExecutor.queryDocuments(viewMainAllTrackVariationsTarget, args,
                TrackDocument.class);
        
        return documents;
    }
    
}
