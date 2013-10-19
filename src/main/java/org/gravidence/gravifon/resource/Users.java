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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.gravidence.gravifon.db.UsersDBClient;
import org.gravidence.gravifon.db.domain.UserDocument;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.UserNotFoundException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.gravidence.gravifon.resource.bean.UserBean;
import org.gravidence.gravifon.resource.bean.UsersInfoBean;
import org.gravidence.gravifon.resource.message.StatusResponse;
import org.gravidence.gravifon.validation.UserCreateValidator;
import org.gravidence.gravifon.validation.UserDeleteValidator;
import org.gravidence.gravifon.validation.UserRetrieveValidator;
import org.gravidence.gravifon.validation.UserSearchValidator;
import org.gravidence.gravifon.validation.UserUpdateValidator;
import org.gravidence.gravifon.validation.UsersInfoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Users resource.<p>
 * Provides <code>user</code> entity management API.
 * 
 * @see UsersDBClient
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@Path("/v1/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Users {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Users.class);
    
    /**
     * {@link UsersDBClient} instance.
     */
    @Autowired
    private UsersDBClient usersDBClient;
    
    // Validators
    private UsersInfoValidator usersInfoValidator = new UsersInfoValidator();
    private UserCreateValidator userCreateValidator = new UserCreateValidator();
    private UserRetrieveValidator userRetrieveValidator = new UserRetrieveValidator();
    private UserSearchValidator userSearchValidator = new UserSearchValidator();
    private UserUpdateValidator userUpdateValidator = new UserUpdateValidator();
    private UserDeleteValidator userDeleteValidator = new UserDeleteValidator();
    
    /**
     * Retrieves <code>/users</code> database info.
     * 
     * @return status response with <code>/users</code> database info bean
     */
    @GET
    public StatusResponse info() {
        usersInfoValidator.validate(null, null, null);
        
        UsersInfoBean entity = new UsersInfoBean();
        
        entity.setUserAmount(usersDBClient.retrieveUserAmount());
        
        return new StatusResponse<>(entity);
    }
    
    /**
     * Creates a new user if such does not exist yet.
     * 
     * @param uriInfo request URI details
     * @param user new user details bean
     * @return 201 Created with status response with user identifier
     */
    @POST
    public Response create(@Context UriInfo uriInfo, UserBean user) {
        userCreateValidator.validate(null, null, user);
        
        UserDocument original = usersDBClient.retrieveUserByUsername(user.getUsername());
        
        if (original != null) {
            throw new GravifonException(GravifonError.USER_EXISTS, "User already exists.");
        }
        
        UserDocument document = usersDBClient.create(user.createDocument());
        
        return Response
                .created(UriBuilder.fromUri(uriInfo.getAbsolutePath()).path(document.getId()).build())
                .entity(new StatusResponse<UserBean>(document.getId()))
                .build();
    }
    
    /**
     * Retrieves existing user details.<p>
     * Basic HTTP Authorization details are required. {@link GravifonError#NOT_ALLOWED NOT_ALLOWED} error is thrown
     * in case requested user identifier doesn't match authorization details.
     * 
     * @param httpHeaders request http headers and cookies
     * @param id user identifier
     * @return status response with user details bean
     */
    @GET
    @Path("{user_id}")
    public StatusResponse retrieve(@Context HttpHeaders httpHeaders, @PathParam("user_id") String id) {
        userRetrieveValidator.validate(httpHeaders.getRequestHeaders(), null, null);
        
        UserDocument document = ResourceUtils.authorizeUser(httpHeaders.getRequestHeaders(), id, usersDBClient, LOGGER);
        
        return new StatusResponse<>(new UserBean().updateBean(document));
    }
    
    /**
     * Searches for existing user details by username.<p>
     * No private user details returned with response as this method is public and does not require any authorization.
     * 
     * @param uriInfo request URI details
     * @param username username
     * @return status response with user details bean (<code>id</code> and <code>username</code> only)
     */
    @GET
    @Path("search")
    public StatusResponse search(@Context UriInfo uriInfo, @QueryParam("username") String username) {
        userSearchValidator.validate(null, uriInfo.getQueryParameters(), null);
        
        UserDocument document = usersDBClient.retrieveUserByUsername(username);
        
        if (document == null) {
            LOGGER.trace("No user found for '{}' username", username);
            throw new UserNotFoundException();
        }
        
        UserBean entity = new UserBean();
        entity.setId(document.getId());
        entity.setUsername(document.getUsername());
        
        return new StatusResponse<>(entity);
    }
    
    /**
     * Updates existing user details.<p>
     * Basic HTTP Authorization details are required. {@link GravifonError#NOT_ALLOWED NOT_ALLOWED} error is thrown
     * in case requested user identifier doesn't match authorization details.
     * 
     * @param httpHeaders request http headers and cookies
     * @param id user identifier
     * @param user details bean
     * @return status response
     */
    @PUT
    @Path("{user_id}")
    public StatusResponse update(@Context HttpHeaders httpHeaders, @PathParam("user_id") String id, UserBean user) {
        userUpdateValidator.validate(httpHeaders.getRequestHeaders(), null, user);
        
        UserDocument original = ResourceUtils.authorizeUser(httpHeaders.getRequestHeaders(), id, usersDBClient, LOGGER);
        
        usersDBClient.update(user.updateDocument(original));
        
        return new StatusResponse();
    }
    
    /**
     * Deletes existing user.<p>
     * Basic HTTP Authorization details are required. {@link GravifonError#NOT_ALLOWED NOT_ALLOWED} error is thrown
     * in case requested user identifier doesn't match authorization details.
     * 
     * @param httpHeaders request http headers and cookies
     * @param id user identifier
     * @return status response
     */
    @DELETE
    @Path("{user_id}")
    public StatusResponse delete(@Context HttpHeaders httpHeaders, @PathParam("user_id") String id) {
        userDeleteValidator.validate(httpHeaders.getRequestHeaders(), null, null);
        
        UserDocument original = ResourceUtils.authorizeUser(httpHeaders.getRequestHeaders(), id, usersDBClient, LOGGER);
        
        usersDBClient.delete(original);
        
        return new StatusResponse();
    }

}
