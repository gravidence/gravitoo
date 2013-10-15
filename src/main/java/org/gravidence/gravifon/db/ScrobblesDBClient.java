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
import org.gravidence.gravifon.db.domain.ScrobbleDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * CouchDB JAX-RS client to <code>/scrobbles</code> database.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ScrobblesDBClient extends BasicDBClient<ScrobbleDocument> implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrobblesDBClient.class);
    
    /**
     * JAX-RS client target associated with <code>main/all_scrobbles</code> view.
     */
    private WebTarget viewMainAllScrobblesTarget;

    @Override
    public void afterPropertiesSet() throws Exception {
        setDbTarget(getDbClient().getTarget()
                .path("scrobbles"));
        
        viewMainAllScrobblesTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_scrobbles");
    }
    
    /**
     * Retrieves scrobble amount.<p>
     * Scrobble amount is equal to <code>main/all_scrobbles</code> view size.
     * 
     * @return scrobble amount
     * 
     * @see #viewMainAllScrobblesTarget
     * @see ViewQueryExecutor#querySize(javax.ws.rs.client.WebTarget)
     */
    public long retrieveScrobbleAmount() {
        return ViewQueryExecutor.querySize(viewMainAllScrobblesTarget);
    }
    
    /**
     * Retrieves existing scrobble {@link ScrobbleDocument document}.
     * 
     * @param id scrobble identifier
     * @return scrobble details document if found, <code>null</code> otherwise
     */
    public ScrobbleDocument retrieveScrobbleByID(String id) {
        return retrieve(id, ScrobbleDocument.class);
    }
    
}
