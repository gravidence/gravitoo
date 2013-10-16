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
import javax.ws.rs.client.WebTarget;
import org.gravidence.gravifon.db.domain.AlbumDocument;
import org.gravidence.gravifon.util.BasicUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * CouchDB JAX-RS client to <code>/albums</code> database.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class AlbumsDBClient extends BasicDBClient<AlbumDocument> implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumsDBClient.class);
    
    /**
     * JAX-RS client target associated with <code>main/all_primary_album_variations</code> view.
     */
    private WebTarget viewMainAllPrimaryAlbumVariationsTarget;
    
    /**
     * JAX-RS client target associated with <code>main/all_album_variations</code> view.
     */
    private WebTarget viewMainAllAlbumVariationsTarget;
    
    /**
     * JAX-RS client target associated with <code>main/all_album_keys</code> view.
     */
    private WebTarget viewMainAllAlbumKeysTarget;

    @Override
    public void afterPropertiesSet() throws Exception {
        setDbTarget(getDbClient().getTarget()
                .path("albums"));
        
        viewMainAllPrimaryAlbumVariationsTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_primary_album_variations");
        viewMainAllAlbumVariationsTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_album_variations");
        viewMainAllAlbumKeysTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_album_keys");
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
    
    /**
     * Retrieves existing album {@link AlbumDocument document}.
     * 
     * @param id album identifier
     * @return album details document if found, <code>null</code> otherwise
     */
    public AlbumDocument retrieveAlbumByID(String id) {
        return retrieve(id, AlbumDocument.class);
    }
    
    /**
     * Retrieves existing album {@link AlbumDocument documents}.<p>
     * Makes sure that <code>title</code> written in lower case
     * since <code>main/all_album_variations</code> view is case sensitive.
     * 
     * @param title album title
     * @return list of album details documents if found, <code>null</code> otherwise
     */
    public List<AlbumDocument> retrieveAlbumsByTitle(String title) {
        ViewQueryArguments args = new ViewQueryArguments()
                .addKey(BasicUtils.lowerCase(title))
                .addIncludeDocs(true);
        
        List<AlbumDocument> documents = ViewQueryExecutor.queryDocuments(viewMainAllAlbumVariationsTarget, args,
                AlbumDocument.class);
        
        return documents;
    }
    
    /**
     * Retrieves existing album {@link AlbumDocument documents}.
     * 
     * @param name album key
     * @return list of album details documents if found, <code>null</code> otherwise
     */
    public List<AlbumDocument> retrieveAlbumsByKey(List<String> key) {
        ViewQueryArguments args = new ViewQueryArguments()
                .addKey(key)
                .addIncludeDocs(true);
        
        List<AlbumDocument> documents = ViewQueryExecutor.queryDocuments(viewMainAllAlbumKeysTarget, args,
                AlbumDocument.class);
        
        return documents;
    }
    
}
