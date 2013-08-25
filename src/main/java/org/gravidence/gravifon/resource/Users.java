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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.gravidence.gravifon.db.CouchDBClient;
import org.gravidence.gravifon.db.message.CreateDocumentResponse;
import org.gravidence.gravifon.db.domain.UserDocument;
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
     * Creates a new user.
     * 
     * @param user new user details bean
     * @return original user details bean updated with created {@link UserDocument document} identifier
     */
    @POST
    public UserBean create(UserBean user) {
        Response response = getResourceTarget().request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(user.createDocument()));
        
        if (CouchDBClient.isSuccessful(response)) {
            CreateDocumentResponse document = response.readEntity(CreateDocumentResponse.class);
            user.updateBean(document);
            LOGGER.trace("'{}' user created", user);
        }
        else {
            LOGGER.error("Failed to create '{}' user: [{}] {}", user.getUsername(),
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new ServiceUnavailableException("Database error: failed to create user.");
        }
        
        // user is already updated with document values
        return user;
    }
    
    /**
     * Retrieves existing user.
     * 
     * @param id user identifier
     * @return user details bean
     */
    @GET
    @Path("{user_id}")
    public UserBean retrieve(@PathParam("user_id") String id) {
        Response response = getResourceTarget().path(id).request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        
        UserDocument document = null;
        if (CouchDBClient.isSuccessful(response)) {
            document = response.readEntity(UserDocument.class);
            LOGGER.trace("'{}' user retrieved", document);
        }
        else {
            LOGGER.error("Failed to retrieve '{}' user: [{}] {}", document,
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new ServiceUnavailableException("Database error: failed to create user.");
        }
        
        return new UserBean().updateBean(document);
    }
    
    /**
     * Creates JAX-RS client target associated with Users database.
     * 
     * @return resource specific target
     * @see CouchDBClient#USERS_DATABASE_NAME
     */
    // TODO think how to cache the target instance
    private WebTarget getResourceTarget() {
        return dbClient.getTarget().path(CouchDBClient.USERS_DATABASE_NAME);
    }
    
}
