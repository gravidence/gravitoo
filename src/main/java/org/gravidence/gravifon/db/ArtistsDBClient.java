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

import javax.ws.rs.client.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * CouchDB JAX-RS client to <code>/artists</code> database.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ArtistsDBClient implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistsDBClient.class);
    
    /**
     * @see #setDbClient(org.gravidence.gravifon.db.CouchDBClient)
     */
    private CouchDBClient dbClient;
    
    /**
     * JAX-RS client target associated with <code>/artists</code> database.
     */
    private WebTarget dbTarget;
    
    /**
     * JAX-RS client target associated with <code>main/all_primary_artist_variations</code> view.
     */
    private WebTarget viewMainAllPrimaryArtistVariationsTarget;

    /**
     * Sets {@link CouchDBClient} instance.
     * 
     * @param dbClient CouchDB client instance
     */
    public void setDbClient(CouchDBClient dbClient) {
        this.dbClient = dbClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dbTarget = dbClient.getTarget()
                .path("artists");
        
        viewMainAllPrimaryArtistVariationsTarget = ViewUtils.getViewTarget(
                dbTarget, "main", "all_primary_artist_variations");
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
    
}