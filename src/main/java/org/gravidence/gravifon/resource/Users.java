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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.db.UsersDBClient;
import org.gravidence.gravifon.db.domain.UserDocument;
import org.gravidence.gravifon.db.domain.UserStatus;
import org.gravidence.gravifon.email.EmailSender;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.UserNotFoundException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.gravidence.gravifon.resource.bean.UserBean;
import org.gravidence.gravifon.resource.bean.UsersInfoBean;
import org.gravidence.gravifon.resource.message.StatusResponse;
import org.gravidence.gravifon.template.TemplateProcessor;
import org.gravidence.gravifon.template.email.UserRegistrationConfirmationEmailData;
import org.gravidence.gravifon.util.BasicUtils;
import org.gravidence.gravifon.validation.UserCompleteValidator;
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
@Consumes("application/json;charset=UTF-8")
@Produces("application/json;charset=UTF-8")
public class Users {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Users.class);
    
    /**
     * {@link UsersDBClient} instance.
     */
    @Autowired
    private UsersDBClient usersDBClient;
    
    /**
     * A {@link TemplateProcessor} implementation instance.
     */
    @Autowired
    private TemplateProcessor templateProcessor;
    
    /**
     * An {@link EmailSender} implementation instance.
     */
    @Autowired
    private EmailSender emailSender;
    
    // Validators
    private final UsersInfoValidator usersInfoValidator = new UsersInfoValidator();
    private final UserCreateValidator userCreateValidator = new UserCreateValidator();
    private final UserCompleteValidator userCompleteValidator = new UserCompleteValidator();
    private final UserRetrieveValidator userRetrieveValidator = new UserRetrieveValidator();
    private final UserSearchValidator userSearchValidator = new UserSearchValidator();
    private final UserUpdateValidator userUpdateValidator = new UserUpdateValidator();
    private final UserDeleteValidator userDeleteValidator = new UserDeleteValidator();
    
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
     * This is 1st phase of user account registration flow.
     * 
     * @param uriInfo request URI details
     * @param user new user details bean
     * @return 201 Created with status response with user identifier
     */
    @POST
    public Response create(@Context UriInfo uriInfo, UserBean user) {
        userCreateValidator.validate(null, null, user);
        
        UserDocument document = usersDBClient.retrieveUserByUsername(user.getUsername());

        if (document != null) {
            throw new GravifonException(GravifonError.USER_EXISTS, "User already exists.");
        }
        
        if (usersDBClient.retrieveUserByEmail(user.getEmail()) != null) {
            throw new GravifonException(GravifonError.EMAIL_IN_USE, "Email already in use.");
        }

        String registrationKey = BasicUtils.generateToken(256);

        document = usersDBClient.create(user.createDocument(registrationKey));
        
        UriBuilder userResourceUri = UriBuilder.fromUri(uriInfo.getAbsolutePath()).path(document.getId());

        sendRegistrationConfirmationEmail(document, userResourceUri, registrationKey);

        return Response
                .created(userResourceUri.build())
                .entity(new StatusResponse<UserBean>(document.getId()))
                .build();
    }
    
    /**
     * Tries to complete user account registration by verifying supplied registration key.
     * This is 2nd phase of user account registration flow.
     * 
     * @param uriInfo request URI details
     * @param id user identifier
     * @param registrationKey key that is required for user account registration completion
     * @return simple status response
     */
    @GET
    @Path("{user_id}/complete")
    public StatusResponse complete(@Context UriInfo uriInfo,
            @PathParam("user_id") String id, @QueryParam("registration_key") String registrationKey) {
        userCompleteValidator.validate(null, uriInfo.getQueryParameters(), null);
        
        UserDocument document = usersDBClient.retrieveUserByID(id);

        // User was removed automatically by scheduled task because registration was not completed in time
        if (document == null) {
            throw new UserNotFoundException();
        }
        // User is allowed to complete the registration
        else if (document.getStatus() == UserStatus.CREATED) {
            if (StringUtils.equals(registrationKey, document.getRegistrationKey())) {
                // Registration completed - change user status
                document.setStatus(UserStatus.ACTIVE);
                // There is no sense to keep the registration key for fully registered used
                document.setRegistrationKey(null);

                usersDBClient.update(document);
            }
            else {
                throw new GravifonException(GravifonError.USER_REGISTRATION_NOT_COMPLETED,
                        "Invalid registration key.");
            }
        }
        else {
            throw new GravifonException(GravifonError.USER_REGISTRATION_NOT_COMPLETED,
                    "User registration was already completed.");
        }
        
        return new StatusResponse();
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
        
        ResourceUtils.checkUserStatus(document);
        
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
        
        UserDocument document = ResourceUtils.authorizeUser(httpHeaders.getRequestHeaders(), id, usersDBClient, LOGGER);
        
        ResourceUtils.checkUserStatus(document);
        
        if (!StringUtils.equalsIgnoreCase(user.getEmail(), document.getEmail())
                && usersDBClient.retrieveUserByEmail(user.getEmail()) != null) {
            throw new GravifonException(GravifonError.EMAIL_IN_USE, "Email already in use.");
        }
        
        usersDBClient.update(user.updateDocument(document));
        
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
        
        UserDocument document = ResourceUtils.authorizeUser(httpHeaders.getRequestHeaders(), id, usersDBClient, LOGGER);
        
        ResourceUtils.checkUserStatus(document);
        
        usersDBClient.delete(document);
        
        return new StatusResponse();
    }

    /**
     * Sends registration confirmation email to user.
     * 
     * @param user user document details
     * @param userResourceUri absolute uri to registered user resource
     * @param registrationKey user's registration confirmation key
     */
    private void sendRegistrationConfirmationEmail(UserDocument user, UriBuilder userResourceUri,
            String registrationKey) {
        String recipientName = user.getFullname() == null ? user.getUsername() : user.getFullname();
        
        UserRegistrationConfirmationEmailData data = new UserRegistrationConfirmationEmailData(recipientName,
                userResourceUri.path("complete").queryParam("registration_key", registrationKey).build().toString());
        
        String textMessage = templateProcessor.processUserRegistrationConfirmationEmail(data);
        
        // TODO move subject text to locale specific configuration
        emailSender.send(user.getEmail(), recipientName, "Confirmation of new Gravifon user account created",
                null, textMessage);
    }

}
