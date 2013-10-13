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

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.gravidence.gravifon.db.ScrobblesDBClient;
import org.gravidence.gravifon.db.UsersDBClient;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.gravidence.gravifon.resource.bean.ScrobbleBean;
import org.gravidence.gravifon.resource.bean.ScrobblesInfoBean;
import org.gravidence.gravifon.resource.message.StatusResponse;
import org.gravidence.gravifon.validation.ScrobbleSubmitValidator;
import org.gravidence.gravifon.validation.ScrobblesInfoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Scrobbles resource.<p>
 * Provides <code>scrobble</code> entity management API.
 * 
 * @see ScrobblesDBClient
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@Path("/v1/scrobbles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Scrobbles {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Scrobbles.class);
    
    /**
     * {@link UsersDBClient} instance.
     */
    @Autowired
    private UsersDBClient usersDBClient;
    
    /**
     * {@link ScrobblesDBClient} instance.
     */
    @Autowired
    private ScrobblesDBClient scrobblesDBClient;
    
    // Validators
    private ScrobblesInfoValidator scrobblesInfoValidator = new ScrobblesInfoValidator();
    private ScrobbleSubmitValidator scrobbleSubmitValidator = new ScrobbleSubmitValidator();
    
    /**
     * Retrieves <code>/scrobbles</code> database info.
     * 
     * @return <code>/scrobbles</code> database info bean
     */
    @GET
    public ScrobblesInfoBean info() {
        scrobblesInfoValidator.validate(null, null, null);
        
        ScrobblesInfoBean result = new ScrobblesInfoBean();
        
        result.setScrobbleAmount(scrobblesDBClient.retrieveScrobbleAmount());
        
        return result;
    }
    
    /**
     * Creates a new scrobble.
     * 
     * @param uriInfo request URI details
     * @param scrobbles list of new scrobble details beans
     * @return 201 Created response with list of created scrobble details bean
     */
    @POST
    public Response submit(@Context UriInfo uriInfo, List<ScrobbleBean> scrobbles) {
        scrobbleSubmitValidator.validate(null, null, scrobbles);
        
        return Response
                .status(Status.SERVICE_UNAVAILABLE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new StatusResponse(GravifonError.UNEXPECTED.getErrorCode(), "Not implemented."))
                .build();
    }

}
