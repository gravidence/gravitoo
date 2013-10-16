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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.collections.CollectionUtils;
import org.gravidence.gravifon.db.AlbumsDBClient;
import org.gravidence.gravifon.db.ArtistsDBClient;
import org.gravidence.gravifon.db.LabelsDBClient;
import org.gravidence.gravifon.db.ScrobblesDBClient;
import org.gravidence.gravifon.db.TracksDBClient;
import org.gravidence.gravifon.db.UsersDBClient;
import org.gravidence.gravifon.db.domain.AlbumDocument;
import org.gravidence.gravifon.db.domain.ScrobbleDocument;
import org.gravidence.gravifon.db.domain.TrackDocument;
import org.gravidence.gravifon.db.domain.UserDocument;
import org.gravidence.gravifon.resource.bean.AlbumBean;
import org.gravidence.gravifon.resource.bean.ScrobbleBean;
import org.gravidence.gravifon.resource.bean.ScrobblesInfoBean;
import org.gravidence.gravifon.resource.bean.TrackBean;
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
    
    /**
     * {@link TracksDBClient} instance.
     */
    @Autowired
    private TracksDBClient tracksDBClient;
    
    /**
     * {@link ArtistsDBClient} instance.
     */
    @Autowired
    private ArtistsDBClient artistsDBClient;
    
    /**
     * {@link AlbumsDBClient} instance.
     */
    @Autowired
    private AlbumsDBClient albumsDBClient;
    
    /**
     * {@link LabelsDBClient} instance.
     */
    @Autowired
    private LabelsDBClient labelsDBClient;
    
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
     * Submits a list of new scrobbles.
     * 
     * @param httpHeaders request http headers and cookies
     * @param uriInfo request URI details
     * @param scrobbles list of new scrobble details beans
     * @return 201 Created response with list of created scrobble details bean
     */
    @POST
    public List<ScrobbleBean> submit(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo, List<ScrobbleBean> scrobbles) {
        scrobbleSubmitValidator.validate(httpHeaders.getRequestHeaders(), null, scrobbles);
        
        UserDocument user = ResourceUtils.authenticateUser(httpHeaders.getRequestHeaders(), usersDBClient, LOGGER);
        
        for (ScrobbleBean scrobble : scrobbles) {
            resolveAlbumId(scrobble.getTrack().getAlbum());
            resolveTrackId(scrobble.getTrack());
            
            ScrobbleDocument scrobbleDoc = scrobble.createDocument();
            scrobbleDoc.setUserId(user.getId());
            
            scrobbleDoc = scrobblesDBClient.create(scrobbleDoc);
            scrobble.setId(scrobbleDoc.getId());
        }
        
        return scrobbles;
    }
    
    private void resolveAlbumId(AlbumBean album) {
        List<AlbumDocument> albums = albumsDBClient.retrieveAlbumsByKey(album.getKey());
        if (CollectionUtils.isEmpty(albums)) {
            AlbumDocument albumDoc = albumsDBClient.create(album.createDocument());
            album.setId(albumDoc.getId());
        }
        else {
            // TODO think about this hardcode
            album.setId(albums.get(0).getId());
        }
    }
    
    private void resolveTrackId(TrackBean track) {
        List<TrackDocument> tracks = tracksDBClient.retrieveTracksByKey(track.getKey());
        if (CollectionUtils.isEmpty(tracks)) {
            TrackDocument trackDoc = tracksDBClient.create(track.createDocument());
            track.setId(trackDoc.getId());
        }
        else {
            // TODO think about this hardcode
            track.setId(tracks.get(0).getId());
        }
    }

}
