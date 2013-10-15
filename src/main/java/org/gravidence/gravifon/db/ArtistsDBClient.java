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
import org.gravidence.gravifon.db.domain.ArtistDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * CouchDB JAX-RS client to <code>/artists</code> database.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ArtistsDBClient extends BasicDBClient<ArtistDocument> implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistsDBClient.class);
    
    /**
     * JAX-RS client target associated with <code>main/all_primary_artist_variations</code> view.
     */
    private WebTarget viewMainAllPrimaryArtistVariationsTarget;
    
    /**
     * JAX-RS client target associated with <code>main/all_artist_variations</code> view.
     */
    private WebTarget viewMainAllArtistVariationsTarget;

    @Override
    public void afterPropertiesSet() throws Exception {
        setDbTarget(getDbClient().getTarget()
                .path("artists"));
        
        viewMainAllPrimaryArtistVariationsTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_primary_artist_variations");
        viewMainAllArtistVariationsTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_artist_variations");
    }
    
    /**
     * Retrieves amount of primary artist variations.<p>
     * Primary artist variation amount is equal to <code>main/all_primary_artist_variations</code> view size.
     * 
     * @return amount of primary artist variations
     * 
     * @see #viewMainAllPrimaryArtistVariationsTarget
     * @see ViewQueryExecutor#querySize(javax.ws.rs.client.WebTarget)
     */
    public long retrievePrimaryArtistVariationAmount() {
        return ViewQueryExecutor.querySize(viewMainAllPrimaryArtistVariationsTarget);
    }
    
    /**
     * Retrieves existing artist {@link ArtistDocument document}.
     * 
     * @param id artist identifier
     * @return artist details document if found, <code>null</code> otherwise
     */
    public ArtistDocument retrieveArtistByID(String id) {
        return retrieve(id, ArtistDocument.class);
    }
    
    /**
     * Retrieves existing artist {@link ArtistDocument documents}.<p>
     * Makes sure that <code>name</code> written in lower case
     * since <code>main/all_artist_variations</code> view is case sensitive.
     * 
     * @param name artist name
     * @return list of artist details documents if found, <code>null</code> otherwise
     */
    public List<ArtistDocument> retrieveArtistsByName(String name) {
        ViewQueryArguments args = new ViewQueryArguments()
                .addKey(StringUtils.lowerCase(name, Locale.ENGLISH))
                .addIncludeDocs(true);
        
        List<ArtistDocument> documents = ViewQueryExecutor.queryDocuments(viewMainAllArtistVariationsTarget, args,
                ArtistDocument.class);
        
        return documents;
    }
    
}
