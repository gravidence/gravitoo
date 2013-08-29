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
package org.gravidence.gravifon.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.gravidence.gravifon.db.CouchDBClient;
import org.gravidence.gravifon.db.ViewQueryResultsExtractor;
import org.gravidence.gravifon.db.message.CreateDocumentResponse;
import org.gravidence.gravifon.db.domain.UserDocument;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.UserNotFoundException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.gravidence.gravifon.resource.bean.StatusBean;
import org.gravidence.gravifon.resource.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User resource.<p>
 * Provides basic CRUD management API. Consumes/produces <code>application/json</code> media type only.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Users {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Users.class);
    
    /**
     * {@link CouchDBClient} instance.
     * 
     * @see CouchDBClient
     */
    @Autowired
    private CouchDBClient dbClient;
    
    /**
     * JSON data binding instance.
     */
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Creates a new user if such does not exist yet.
     * 
     * @param user new user details bean
     * @return original user details bean updated with created {@link UserDocument document} identifier
     */
    @POST
    public UserBean create(UserBean user) {
        UserDocument doc = retrieveUserDocumentByUsername(user.getUsername());
        if (doc != null) {
            throw new GravifonException(GravifonError.USER_EXISTS, "User already exists.");
        }
        
        Response response = getResourceTarget()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(user.createDocument()));
        
        if (CouchDBClient.isSuccessful(response)) {
            CreateDocumentResponse document = response.readEntity(CreateDocumentResponse.class);
            user.updateBean(document);
            LOGGER.trace("'{}' user created", user);
        }
        else {
            LOGGER.error("Failed to create '{}' user: [{}] {}", user.getUsername(),
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to create user.");
        }
        
        // user is already updated with document values
        return user;
    }
    
    /**
     * Retrieves existing user details.
     * 
     * @param id user identifier
     * @return user details bean
     */
    @GET
    @Path("{user_id}")
    public UserBean retrieve(@PathParam("user_id") String id) {
        UserDocument document = retrieveUserDocumentByID(id);
        
        if (document == null) {
            LOGGER.trace("No user found for '{}' id", id);
            throw new UserNotFoundException();
        }
        
        return new UserBean().updateBean(document);
    }
    
    /**
     * Searches for existing user details by username.
     * 
     * @param username username
     * @return user details bean
     */
    @GET
    @Path("search")
    public UserBean search(@QueryParam("username") String username) {
        UserDocument document = retrieveUserDocumentByUsername(username);
        
        if (document == null) {
            LOGGER.trace("No user found for '{}' username", username);
            throw new UserNotFoundException();
        }
        
        return new UserBean().updateBean(document);
    }
    
    /**
     * Updates existing user details.
     * 
     * @param id user identifier
     * @param user details bean
     * @return updated user details bean
     */
    @PUT
    @Path("{user_id}")
    public UserBean update(@PathParam("user_id") String id, UserBean user) {
        UserDocument original = retrieveUserDocumentByID(id);
        
        if (original == null) {
            LOGGER.trace("No user found for '{}' id", id);
            throw new UserNotFoundException();
        }
        
        user.updateDocument(original);
        
        Response response = getResourceTarget()
                .path(id)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(original));
        
        if (CouchDBClient.isSuccessful(response)) {
            CreateDocumentResponse document = response.readEntity(CreateDocumentResponse.class);
            user.updateBean(document);
            LOGGER.trace("'{}' user updated", user);
        }
        else {
            LOGGER.error("Failed to update '{}' user: [{}] {}", user.getUsername(),
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to update user.");
        }
        
        return user;
    }
    
    /**
     * Deletes existing user.
     * 
     * @param id user identifier
     * @return status bean
     */
    @DELETE
    @Path("{user_id}")
    public StatusBean delete(@PathParam("user_id") String id) {
        UserDocument original = retrieveUserDocumentByID(id);
        
        if (original == null) {
            LOGGER.trace("No user found for '{}' id", id);
            throw new UserNotFoundException();
        }
        
        Response response = getResourceTarget()
                .path(id)
                .queryParam("rev", original.getRevision())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        
        UserBean user = new UserBean();
        if (CouchDBClient.isSuccessful(response)) {
            UserDocument document = response.readEntity(UserDocument.class);
            user.updateBean(document);
            // CouchDB does not return deleted document properties so taking username from original document
            user.setUsername(original.getUsername());
            LOGGER.trace("'{}' user deleted", user);
        }
        else {
            LOGGER.error("Failed to delete '{}' user: [{}] {}", original.getUsername(),
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to delete user.");
        }
        
        return new StatusBean();
    }
    
    /**
     * Retrieves existing user {@link UserDocument document}.
     * 
     * @param id user identifier
     * @return user details document
     */
    private UserDocument retrieveUserDocumentByID(String id) {
        Response response = getResourceTarget()
                .path(id)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        
        UserDocument document;
        if (CouchDBClient.isSuccessful(response)) {
            document = response.readEntity(UserDocument.class);
            LOGGER.trace("'{}' user retrieved", document);
        }
        else if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
            document = null;
        }
        else {
            LOGGER.error("Failed to retrieve user for '{}' id: [{}] {}", id,
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to retrieve user.");
        }
        
        return document;
    }
    
    /**
     * Retrieves existing user {@link UserDocument document}.
     * 
     * @param username username
     * @return user details document
     */
    private UserDocument retrieveUserDocumentByUsername(String username) {
        Response response = getViewTarget(CouchDBClient.USERS_DATABASE_MAIN_DESIGN_DOC_NAME,
                CouchDBClient.USERS_DATABASE_MAIN_DESIGN_DOC_ALL_USERNAMES_VIEW_NAME)
                .queryParam("key", String.format("\"%s\"", username))
                .queryParam("include_docs", Boolean.TRUE)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        
        List<UserDocument> documents = null;
        if (CouchDBClient.isSuccessful(response)) {
            InputStream json = response.readEntity(InputStream.class);
            documents = ViewQueryResultsExtractor.extractDocuments(UserDocument.class, json, objectMapper);
        }
        else {
            LOGGER.error("Failed to retrieve user for '{}' username: [{}] {}", username,
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to retrieve user.");
        }
        
        return documents == null ? null : documents.get(0);
    }
    
    /**
     * Creates JAX-RS client target associated with Users database.
     * 
     * @return resource specific target
     * @see CouchDBClient#USERS_DATABASE_NAME
     */
    // TODO think how to cache the target instance
    private WebTarget getResourceTarget() {
        return dbClient.getTarget()
                .path(CouchDBClient.USERS_DATABASE_NAME);
    }
    
    /**
     * Creates JAX-RS client target associated with design document view of Users database.
     * 
     * @param designDocName design document name
     * @param viewName view name
     * @return design document view specific target
     */
    private WebTarget getViewTarget(String designDocName, String viewName) {
        return getResourceTarget()
                .path("_design")
                .path(designDocName)
                .path("_view")
                .path(viewName);
    }

}
