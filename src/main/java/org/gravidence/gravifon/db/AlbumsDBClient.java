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
 * CouchDB JAX-RS client to <code>/albums</code> database.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class AlbumsDBClient implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumsDBClient.class);
    
    /**
     * @see #setDbClient(org.gravidence.gravifon.db.CouchDBClient)
     */
    private CouchDBClient dbClient;
    
    /**
     * JAX-RS client target associated with <code>/albums</code> database.
     */
    private WebTarget dbTarget;
    
    /**
     * JAX-RS client target associated with <code>main/all_primary_album_variations</code> view.
     */
    private WebTarget viewMainAllPrimaryAlbumVariationsTarget;

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
                .path("albums");
        
        viewMainAllPrimaryAlbumVariationsTarget = ViewUtils.getViewTarget(
                dbTarget, "main", "all_primary_album_variations");
    }
    
    /**
     * Retrieves amount of primary album variations.<p>
     * Amount of primary album variations is equal to <code>main/all_primary_album_variations</code> view size.
     * 
     * @return amount of primary album variations
     * 
     * @see #viewMainAllPrimaryAlbumVariationsTarget
     * @see ViewQueryExecutor#querySize(javax.ws.rs.client.WebTarget)
     */
    public long retrievePrimaryAlbumVariationAmount() {
        return ViewQueryExecutor.querySize(viewMainAllPrimaryAlbumVariationsTarget);
    }
    
}
