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

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.db.AlbumsDBClient;
import org.gravidence.gravifon.db.ArtistsDBClient;
import org.gravidence.gravifon.db.LabelsDBClient;
import org.gravidence.gravifon.db.ScrobblesDBClient;
import org.gravidence.gravifon.db.TracksDBClient;
import org.gravidence.gravifon.db.UsersDBClient;
import org.gravidence.gravifon.db.domain.AlbumDocument;
import org.gravidence.gravifon.db.domain.ArtistDocument;
import org.gravidence.gravifon.db.domain.ScrobbleDocument;
import org.gravidence.gravifon.db.domain.TrackDocument;
import org.gravidence.gravifon.db.domain.UserDocument;
import org.gravidence.gravifon.exception.EntityNotFoundException;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.gravidence.gravifon.resource.bean.AlbumBean;
import org.gravidence.gravifon.resource.bean.ArtistBean;
import org.gravidence.gravifon.resource.bean.ScrobbleBean;
import org.gravidence.gravifon.resource.bean.ScrobblesInfoBean;
import org.gravidence.gravifon.resource.bean.TrackBean;
import org.gravidence.gravifon.resource.message.StatusResponse;
import org.gravidence.gravifon.validation.ScrobbleDeleteValidator;
import org.gravidence.gravifon.validation.ScrobbleRetrieveValidator;
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
    private ScrobbleRetrieveValidator scrobbleRetrieveValidator = new ScrobbleRetrieveValidator();
    private ScrobbleDeleteValidator scrobbleDeleteValidator = new ScrobbleDeleteValidator();
    
    /**
     * Retrieves <code>/scrobbles</code> database info.
     * 
     * @return status response with <code>/scrobbles</code> database info bean
     */
    @GET
    public StatusResponse<ScrobblesInfoBean> info() {
        scrobblesInfoValidator.validate(null, null, null);
        
        ScrobblesInfoBean entity = new ScrobblesInfoBean();
        
        entity.setScrobbleAmount(scrobblesDBClient.retrieveScrobbleAmount());
        
        return new StatusResponse<>(entity);
    }
    
    /**
     * Submits a list of new scrobbles.
     * 
     * @param httpHeaders request http headers and cookies
     * @param uriInfo request URI details
     * @param scrobbles list of new scrobble details beans
     * @return list of status responses with scrobble identifiers
     */
    @POST
    public List<StatusResponse> submit(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo,
            List<ScrobbleBean> scrobbles) {
        scrobbleSubmitValidator.validate(httpHeaders.getRequestHeaders(), null, scrobbles);
        
        UserDocument user = ResourceUtils.authenticateUser(httpHeaders.getRequestHeaders(), usersDBClient, LOGGER);
        
        List<StatusResponse> result = new ArrayList<>(scrobbles.size() + 1);
        
        for (ScrobbleBean scrobble : scrobbles) {
            try {
                scrobble.validate();

                resolveTrackIds(scrobble.getTrack());

                ScrobbleDocument scrobbleDoc = scrobble.createDocument();
                scrobbleDoc.setUserId(user.getId());

                scrobbleDoc = scrobblesDBClient.create(scrobbleDoc);
                
                result.add(new StatusResponse(scrobbleDoc.getId()));
            }
            catch (GravifonException ex) {
                result.add(new StatusResponse(ex.getError().getErrorCode(), ex.getMessage()));
            }
        }
        
        return result;
    }
    
    /**
     * Retrieves existing scrobble details.<p>
     * Basic HTTP Authorization details are required. {@link GravifonError#NOT_ALLOWED NOT_ALLOWED} error is thrown
     * in case requested scrobble doesn't belong to user specified in authorization details.
     * 
     * @param httpHeaders request http headers and cookies
     * @param uriInfo request URI details
     * @param deep deep retrieval indicator (<code>true</code> means all child entities are to be retrieved as well)
     * @param id scrobble identifier
     * @return status response with scrobble details bean
     */
    @GET
    @Path("{scrobble_id}")
    public StatusResponse retrieve(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo,
            @QueryParam("deep") Boolean deep, @PathParam("scrobble_id") String id) {
        scrobbleRetrieveValidator.validate(httpHeaders.getRequestHeaders(), uriInfo.getQueryParameters(), null);
        
        ScrobbleDocument scrobble = scrobblesDBClient.retrieveScrobbleByID(id);
        
        if (scrobble == null) {
            throw new EntityNotFoundException("Scrobble not found.");
        }
        
        ResourceUtils.authorizeUser(httpHeaders.getRequestHeaders(), scrobble.getUserId(), usersDBClient, LOGGER);
        
        ScrobbleBean result = new ScrobbleBean().updateBean(scrobble);
            
        if (Boolean.TRUE.equals(deep)) {
            TrackDocument track = tracksDBClient.retrieveTrackByID(scrobble.getTrackId());
            
            TrackBean trackBean = new TrackBean().updateBean(track);
            result.setTrack(trackBean);
            
            List<ArtistBean> trackArtists = trackBean.getArtists();
            
            trackArtists.clear();
            for (String artistId : track.getArtistIds()) {
                ArtistDocument artist = artistsDBClient.retrieveArtistByID(artistId);
                trackArtists.add(new ArtistBean().updateBean(artist));
            }
            
            AlbumDocument album = albumsDBClient.retrieveAlbumByID(track.getAlbumId());
            
            AlbumBean albumBean = new AlbumBean().updateBean(album);
            trackBean.setAlbum(albumBean);
            
            if (CollectionUtils.isNotEmpty(albumBean.getArtists())) {
                List<ArtistBean> albumArtists = albumBean.getArtists();
                
                albumArtists.clear();
                for (String artistId : album.getArtistIds()) {
                    ArtistDocument artist = artistsDBClient.retrieveArtistByID(artistId);
                    albumArtists.add(new ArtistBean().updateBean(artist));
                }
            }
        }
        
        return new StatusResponse<>(result);
    }
    
    /**
     * Deletes existing scrobble.<p>
     * Basic HTTP Authorization details are required. {@link GravifonError#NOT_ALLOWED NOT_ALLOWED} error is thrown
     * in case requested scrobble doesn't belong to user specified in authorization details.
     * 
     * @param httpHeaders request http headers and cookies
     * @param id scrobble identifier
     * @return status response
     */
    @DELETE
    @Path("{scrobble_id}")
    public StatusResponse delete(@Context HttpHeaders httpHeaders, @PathParam("scrobble_id") String id) {
        scrobbleDeleteValidator.validate(httpHeaders.getRequestHeaders(), null, null);
        
        ScrobbleDocument scrobble = scrobblesDBClient.retrieveScrobbleByID(id);
        
        if (scrobble == null) {
            throw new EntityNotFoundException("Scrobble not found.");
        }
        
        ResourceUtils.authorizeUser(httpHeaders.getRequestHeaders(), scrobble.getUserId(), usersDBClient, LOGGER);
        
        scrobblesDBClient.delete(scrobble);
        
        return new StatusResponse();
    }
    
    private ArtistDocument retrieveOrCreateArtistDocument(ArtistBean artist) {
        ArtistDocument result;
        
        if (artist == null) {
            result = null;
        }
        else {
            List<ArtistDocument> artistDocs = artistsDBClient.retrieveArtistsByName(artist.getName());
            if (CollectionUtils.isEmpty(artistDocs)) {
                result = artistsDBClient.create(artist.createDocument());
            }
            else {
                // TODO think about this hardcode
                result = artistDocs.get(0);
            }
        }
        
        return result;
    }
    
    private AlbumDocument retrieveOrCreateAlbumDocument(AlbumBean album) {
        AlbumDocument result;
        
        if (album == null) {
            result = null;
        }
        else {
            List<AlbumDocument> albumDocs = albumsDBClient.retrieveAlbumsByKey(album.getKey());
            if (CollectionUtils.isEmpty(albumDocs)) {
                resolveArtistIds(album.getArtists());
                
                result = albumsDBClient.create(album.createDocument());
            }
            else {
                // TODO think about this hardcode
                result = albumDocs.get(0);
            }
        }
        
        return result;
    }
    
    private TrackDocument retrieveOrCreateTrackDocument(TrackBean track) {
        TrackDocument result;
        
        if (track == null) {
            result = null;
        }
        else {
            List<String> key = track.getKey();
            // find exact track record
            List<TrackDocument> trackDocs = tracksDBClient.retrieveTracksByKey(key);
            if (CollectionUtils.isEmpty(trackDocs)) {
                resolveAlbumIds(track.getAlbum());
                
                // find all tracks that belong to artists+album
                key.remove(key.size() - 1);
                trackDocs = tracksDBClient.retrieveTracksByIncompleteKey(key);
                // no tracks found for artists+album, so just need to use first found artists or create new ones
                if (CollectionUtils.isEmpty(trackDocs)) {
                    resolveArtistIds(track.getArtists());
                }
                // use existing artist identifiers from found tracks
                else {
                    for (String artistId : trackDocs.get(0).getArtistIds()) {
                        ArtistDocument artistDoc = artistsDBClient.retrieveArtistByID(artistId);
                        for (ArtistBean artist : track.getArtists()) {
                            if (StringUtils.equalsIgnoreCase(artist.getName(), artistDoc.getName())) {
                                artist.setId(artistDoc.getId());
                                break;
                            }
                        }
                    }
                }
                
                result = tracksDBClient.create(track.createDocument());
            }
            else {
                // TODO think about this hardcode
                result = trackDocs.get(0);
            }
        }
        
        return result;
    }
    
    private void resolveArtistId(ArtistBean artist) {
        ArtistDocument artistDoc = retrieveOrCreateArtistDocument(artist);
        
        artist.setId(artistDoc.getId());
    }
    
    private void resolveArtistIds(List<ArtistBean> artists) {
        if (CollectionUtils.isNotEmpty(artists)) {
            for (ArtistBean artist : artists) {
                resolveArtistId(artist);
            }
        }
    }
    
    private void resolveAlbumIds(AlbumBean album) {
        AlbumDocument albumDoc = retrieveOrCreateAlbumDocument(album);
        
        album.setId(albumDoc.getId());
    }
    
    private void resolveTrackIds(TrackBean track) {
        TrackDocument trackDoc = retrieveOrCreateTrackDocument(track);
        
        track.setId(trackDoc.getId());
    }

}
