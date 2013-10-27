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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.gravidence.gravifon.db.domain.CouchDBDocument;
import org.gravidence.gravifon.db.message.CreateDocumentResponse;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CouchDB JAX-RS client to particular Gravifon database.<p>
 * Implements CRUD functionality.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class BasicDBClient<T extends CouchDBDocument> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicDBClient.class);
    
    public static final long MAX_PAGE_SIZE = 50;
    
    /**
     * @see #getDbClient()
     */
    private CouchDBClient dbClient;
    
    /**
     * @see #getDbTarget()
     */
    private WebTarget dbTarget;
    
    /**
     * Returns {@link CouchDBClient} instance.
     * 
     * @return {@link CouchDBClient} instance
     */
    public CouchDBClient getDbClient() {
        return dbClient;
    }

    /**
     * @param dbClient
     * @see #getDbClient()
     */
    public void setDbClient(CouchDBClient dbClient) {
        this.dbClient = dbClient;
    }
    
    /**
     * Returns JAX-RS client target associated with particular database.
     * 
     * @return JAX-RS client target associated with particular database
     */
    public WebTarget getDbTarget() {
        return dbTarget;
    }

    /**
     * @param dbTarget
     * @see #getDbTarget()
     */
    public void setDbTarget(WebTarget dbTarget) {
        this.dbTarget = dbTarget;
    }
    
    /**
     * Creates a new document.
     * 
     * @param document new document
     * @return created document
     * 
     * @see CouchDBDocument
     */
    public T create(T document) {
        String documentName = document.getClass().getSimpleName();
        
        Response response = dbTarget
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(document));
        
        if (CouchDBClient.isSuccessful(response)) {
            CreateDocumentResponse cdr = response.readEntity(CreateDocumentResponse.class);
            document.setId(cdr.getId());
            LOGGER.trace("{} created ('{}')", documentName, document);
        }
        else {
            LOGGER.error("Failed to create {} ('{}'): [{}] {}", documentName, document,
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            
            response.close();
            
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to create document.");
        }
        
        return document;
    }
    
    /**
     * Retrieves existing document.
     * 
     * @param id document identifier
     * @param documentType document object type
     * @return document if found, <code>null</code> otherwise
     * 
     * @see CouchDBDocument
     */
    public T retrieve(String id, Class<T> documentType){
        T document;
        
        String documentName = documentType.getSimpleName();
        
        Response response = dbTarget
                .path(id)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        
        if (CouchDBClient.isSuccessful(response)) {
            document = response.readEntity(documentType);
            LOGGER.trace("{} retrieved ('{}')", documentName, document);
        }
        else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            document = null;
        }
        else {
            LOGGER.error("Failed to retrieve {} (id={}): [{}] {}", documentName, id,
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            
            response.close();
            
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to retrieve document.");
        }
        
        return document;
    }
    
    /**
     * Updates existing document.
     * 
     * @param document updated existing document
     * @return updated document
     * 
     * @see CouchDBDocument
     */
    public T update(T document) {
        String documentName = document.getClass().getSimpleName();
        
        Response response = dbTarget
                .path(document.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(document));
        
        if (CouchDBClient.isSuccessful(response)) {
            CreateDocumentResponse cdr = response.readEntity(CreateDocumentResponse.class);
            document.setId(cdr.getId());
            LOGGER.trace("{} updated ('{}')", documentName, document);
        }
        else {
            LOGGER.error("Failed to update {} ('{}'): [{}] {}", documentName, document,
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            
            response.close();
            
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to update document.");
        }
        
        return document;
    }
    
    /**
     * Deletes existing document.
     * 
     * @param document existing document
     * 
     * @see CouchDBDocument
     */
    public void delete(T document) {
        String documentName = document.getClass().getSimpleName();
        
        Response response = dbTarget
                .path(document.getId())
                .queryParam("rev", document.getRevision())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        
        if (CouchDBClient.isSuccessful(response)) {
            LOGGER.trace("{} deleted ('{}')", documentName, document);
        }
        else {
            LOGGER.error("Failed to delete {} ('{}'): [{}] {}", documentName, document,
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            
            response.close();
            
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to delete document.");
        }
    }
    
    /**
     * Retrieves documents by complete key.
     * 
     * @param viewTarget JAX-RS client target associated with particular view
     * @param key complete key
     * @param documentType document object type
     * @return documents of <code>documentType</code> type if found, <code>null</code> otherwise
     */
    public List<T> retrieveByKey(WebTarget viewTarget, JsonNode key, Class<T> documentType) {
        ViewQueryArguments args = new ViewQueryArguments()
                .addKey(key)
                .addIncludeDocs(true);
        
        List<T> documents = ViewQueryExecutor.queryDocuments(viewTarget, args, documentType);
        
        return documents;
    }
    
    /**
     * Retrieves limited amount of documents (a page).<p>
     * {@link #MAX_PAGE_SIZE} is used once it is less than <code>limit</code>.<p>
     * No <code>subKeyStart</code> means that retrieval is started from the very first document found by <code>key</code>.
     * No <code>subKeyEnd</code> means that retrieval is finished at the very last document found by <code>key</code>
     * if page limit is not reached ealier.
     * 
     * @param viewTarget JAX-RS client target associated with particular view
     * @param key main key that characterizes retrieve query
     * @param subKeyStart sub key to start from
     * @param subKeyEnd sub key to finish at
     * @param ascending retrieve direction
     * @param limit max number of documents to retrieve
     * @param documentType document object type
     * @return documents of <code>documentType</code> type if found, <code>null</code> otherwise
     */
    public List<T> retrievePage(WebTarget viewTarget, ArrayNode key, JsonNode subKeyStart, JsonNode subKeyEnd,
            boolean ascending, Long limit, Class<T> documentType) {
        ArrayNode startkey;
        ArrayNode endkey;
        
        startkey = key;
        endkey = key.deepCopy();
        
        if (ascending) {
            if (subKeyStart != null) {
                startkey.add(subKeyStart);
            }
            if (subKeyEnd != null) {
                endkey.add(subKeyEnd);
            }
            else {
                endkey.addObject();
            }
        }
        else {
            if (subKeyEnd != null) {
                startkey.add(subKeyEnd);
            }
            else {
                startkey.addObject();
            }
            if (subKeyStart != null) {
                endkey.add(subKeyStart);
            }
        }
        
        ViewQueryArguments args = new ViewQueryArguments()
                .addStartKey(startkey)
                .addEndKey(endkey)
                .addIncludeDocs(true);
        
        if (!ascending) {
            args.addDescending();
        }
        
        if (limit == null || limit < 1 || limit > MAX_PAGE_SIZE) {
            limit = MAX_PAGE_SIZE;
        }
        args.addLimit(limit + 1);
        
        List<T> documents = ViewQueryExecutor.queryDocuments(viewTarget, args, documentType);
        
        return documents;
    }
    
}
