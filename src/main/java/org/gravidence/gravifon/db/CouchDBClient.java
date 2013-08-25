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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * CouchDB JAX-RS client instance.<p>
 * Connection to DB instance is verified during initialization.<p>
 * DB instance could be initialized (see {@link #setSetup(java.lang.Boolean) setup} and {@link #setCleanup(java.lang.Boolean) cleanup} settings).
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class CouchDBClient implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CouchDBClient.class);
    
    /**
     * Users database name.
     */
    public static final String USERS_DATABASE_NAME = "users";
    
    // JAX-RS client instance
    private WebTarget instance;
    
    // JSON data binding instance
    private ObjectMapper objectMapper;
    
    // Associated filters
    private CouchDBClientRequestFilter requestFilter;
    private CouchDBClientResponseFilter responseFilter;

    // DB settings
    private String url;
    private Boolean setup;
    private Boolean cleanup;

    /**
     * @see #setUrl(java.lang.String)
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets URL to CouchDB instance.
     * 
     * @param url URL to CouchDB instance
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @see #setSetup(java.lang.Boolean)
     */
    public Boolean getSetup() {
        return setup;
    }

    /**
     * Sets 'create all databases' indicator.<p>
     * Once set to <code>true</code>, DB setup is performed at last initialization step.
     * 
     * @param setup indicator value
     */
    public void setSetup(Boolean setup) {
        this.setup = setup;
    }

    /**
     * @see #setCleanup(java.lang.Boolean)
     */
    public Boolean getCleanup() {
        return cleanup;
    }

    /**
     * Sets 'drop all databases' indicator.<p>
     * Once set to <code>true</code>, DB cleanup is performed before {@link #setSetup(java.lang.Boolean) setup} step.
     * 
     * @param cleanup indicator value
     */
    public void setCleanup(Boolean cleanup) {
        this.cleanup = cleanup;
    }
    
    /**
     * Returns JAX-RS client instance associated with CouchDB instance.
     */
    public WebTarget getTarget() {
        return instance;
    }

    /**
     * Injects JSON data binding instance.
     * 
     * @param objectMapper Jackson ObjectMapper instance
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Injects JAX-RS client request filter instance.
     * 
     * @param requestFilter {@link CouchDBClientRequestFilter} instance which consumes JSON
     */
    public void setRequestFilter(CouchDBClientRequestFilter requestFilter) {
        this.requestFilter = requestFilter;
    }

    /**
     * Injects JAX-RS client response filter instance.
     * 
     * @param responseFilter {@link CouchDBClientResponseFilter} instance which produces JSON
     */
    public void setResponseFilter(CouchDBClientResponseFilter responseFilter) {
        this.responseFilter = responseFilter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initDBConnection();
        initDBContent();
    }
    
    private void initDBConnection() throws IOException {
        LOGGER.info("Initializing DB layer");
        ClientConfig clientConfig = new ClientConfig()
                .register(requestFilter)
                .register(responseFilter);
        instance = ClientBuilder.newClient(clientConfig).target(url);
        
        LOGGER.info("Connecting to CouchDB instance at {}", url);
        Response response = instance.request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        
        if (isSuccessful(response)) {
            if (response.hasEntity()) {
                JsonNode node = objectMapper.readTree(response.readEntity(InputStream.class));
                LOGGER.info("CouchDB version: {}", node.get("version").asText());
            }
            else {
                LOGGER.warn("No reponse entity returned from CouchDB instance");
            }
        }
        else {
            LOGGER.error("DB connection failure: [{}] {}", response.getStatus(),
                    response.getStatusInfo().getReasonPhrase());
            throw new RuntimeException("DB layer initialization failed");
        }
    }
    
    private void initDBContent() {
        if (Boolean.TRUE.equals(cleanup)) {
            LOGGER.info("Initiating DB cleanup");
            
            dropDatabase("users");
        }

        if (Boolean.TRUE.equals(setup)) {
            LOGGER.info("Initiating DB setup");

            createDatabase("users");
        }
    }
    
    private void dropDatabase(String name) {
        WebTarget database = instance.path(name);
        Response response = database.request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        if (isSuccessful(response)) {
            LOGGER.info("'{}' database dropped", database.getUri().getPath());
        }
        else {
            LOGGER.error("Failed to drop {} database: [{}] {}", database.getUri().getPath(),
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new RuntimeException("DB layer initialization failed");
        }
    }
    
    private void createDatabase(String name) {
        WebTarget database = instance.path(name);
        Response response = database.request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(""));
        if (isSuccessful(response)) {
            LOGGER.info("'{}' database created", database.getUri().getPath());
        }
        else if (response.getStatus() == Status.PRECONDITION_FAILED.getStatusCode()) {
            LOGGER.info("'{}' database already exists", database.getUri().getPath());
        }
        else {
            LOGGER.error("Failed to create {} database: [{}] {}", database.getUri().getPath(),
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new RuntimeException("DB layer initialization failed");
        }
    }
    
    /**
     * Checks if response is successful (i.e. belongs to 2xx range).
     * 
     * @param response JAX-RS response
     * @return <code>true</code> if response status code belongs to 2xx range
     * @throws NullPointerException in case the supplied argument is <code>null</code>
     */
    public static boolean isSuccessful(Response response) {
        if (response == null) {
            throw new NullPointerException("response");
        }
        
        return Family.SUCCESSFUL == response.getStatusInfo().getFamily();
    }

}
