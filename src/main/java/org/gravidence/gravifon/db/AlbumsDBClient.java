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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.db.domain.AlbumDocument;
import org.gravidence.gravifon.db.message.CreateDocumentResponse;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.gravidence.gravifon.resource.bean.AlbumBean;
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
     * JAX-RS client target associated with <code>main/all_album_names</code> view.
     */
    private WebTarget viewMainAllAlbumNamesTarget;

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
        viewMainAllAlbumNamesTarget = ViewUtils.getViewTarget(
                dbTarget, "main", "all_album_names");
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
     * Creates a new album {@link AlbumDocument document}.
     * 
     * @param album new album details document
     * @return created album document identifier and revision
     */
    public CreateDocumentResponse createAlbum(AlbumDocument album) {
        Response response = dbTarget
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(album));
        
        CreateDocumentResponse document;
        if (CouchDBClient.isSuccessful(response)) {
            document = response.readEntity(CreateDocumentResponse.class);
            LOGGER.trace("'{}' album created", new AlbumBean().updateBean(album).updateBean(document));
        }
        else {
            LOGGER.error("Failed to create '{}' album: [{}] {}", album.getTitle(),
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            
            response.close();
            
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to create album.");
        }
        
        return document;
    }
    
    /**
     * Retrieves existing album {@link AlbumDocument document}.
     * 
     * @param id album identifier
     * @return album details document if found, <code>null</code> otherwise
     */
    public AlbumDocument retrieveAlbumByID(String id) {
        Response response = dbTarget
                .path(id)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        
        AlbumDocument document;
        if (CouchDBClient.isSuccessful(response)) {
            document = response.readEntity(AlbumDocument.class);
            LOGGER.trace("'{}' album retrieved", document);
        }
        else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            document = null;
        }
        else {
            LOGGER.error("Failed to retrieve album for '{}' id: [{}] {}", id,
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            
            response.close();
            
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to retrieve album.");
        }
        
        return document;
    }
    
    /**
     * Retrieves existing album {@link AlbumDocument documents}.<p>
     * Makes sure that <code>name</code> written in lower case
     * since <code>main/all_album_names</code> view is case sensitive.
     * 
     * @param name album name
     * @return list of album details documents if found, <code>null</code> otherwise
     */
    public List<AlbumDocument> retrieveAlbumsByName(String name) {
        ViewQueryArguments args = new ViewQueryArguments()
                .addKey(StringUtils.lowerCase(name, Locale.ENGLISH))
                .addIncludeDocs(true);
        
        List<AlbumDocument> documents = ViewQueryExecutor.queryDocuments(viewMainAllAlbumNamesTarget, args,
                AlbumDocument.class);
        
        return documents;
    }
    
    /**
     * Updates existing album details.
     * 
     * @param album album details document
     * @return updated album document identifier and revision
     */
    public CreateDocumentResponse updateAlbum(AlbumDocument album) {
        Response response = dbTarget
                .path(album.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(album));
        
        CreateDocumentResponse document;
        if (CouchDBClient.isSuccessful(response)) {
            document = response.readEntity(CreateDocumentResponse.class);
            LOGGER.trace("'{}' album updated", new AlbumBean().updateBean(album).updateBean(document));
        }
        else {
            LOGGER.error("Failed to update '{}' album: [{}] {}", album,
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            
            response.close();
            
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to update album.");
        }
        
        return document;
    }
    
    /**
     * Deletes existing album.
     * 
     * @param album existing album details document
     */
    public void deleteAlbum(AlbumDocument album) {
        Response response = dbTarget
                .path(album.getId())
                .queryParam("rev", album.getRevision())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        
        if (CouchDBClient.isSuccessful(response)) {
//            AlbumDocument document = response.readEntity(AlbumDocument.class);
            LOGGER.trace("'{}' album deleted", album);
        }
        else {
            LOGGER.error("Failed to delete '{}' album: [{}] {}", album,
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            
            response.close();
            
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to delete album.");
        }
    }
    
}
