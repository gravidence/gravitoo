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
import javax.ws.rs.core.Response.Status.Family;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CouchDB client instance.<p>
 * Verifies connection during initialization and initializes DB according to {@link #setup}/{@link #cleanup} settings.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class CouchDBClient implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CouchDBClient.class);
    
    /**
     * Jersey REST client instance associated with CouchDB instance.
     */
    private WebTarget instance;
    
    /**
     * Jackson JSON data binder instance.
     */
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * URL to CouchDB instance.
     */
    private String url;
    
    /**
     * Indicates if DB setup is required during client initialization.
     * @see #cleanup
     */
    private Boolean setup;
    
    /**
     * Indicates if DB cleanup is required before DB setup.
     * @see #setup
     */
    private Boolean cleanup;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getSetup() {
        return setup;
    }

    public void setSetup(Boolean setup) {
        this.setup = setup;
    }

    public Boolean getCleanup() {
        return cleanup;
    }

    public void setCleanup(Boolean cleanup) {
        this.cleanup = cleanup;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initDBConnection();
        initDBContent();
    }
    
    private void initDBConnection() throws IOException {
        LOGGER.info("Initializing DB layer");
        instance = ClientBuilder.newClient().target(url);
        
        LOGGER.info("Connecting to CouchDB instance at {}", url);
        Response response = instance.request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        
        LOGGER.info("Response status code: {}", response.getStatus());
        
        if (isSuccessful(response)) {
            if (response.hasEntity()) {
                JsonNode node = objectMapper.readTree(response.readEntity(InputStream.class));
                LOGGER.info("CouchDB version: {}", node.get("version").asText());
            }
            else {
                LOGGER.warn("No reponse entity returned from CouchDB instance.");
            }
        }
        else {
            LOGGER.error("Failure reason: {}", response.getStatusInfo().getReasonPhrase());
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
    
    private void dropDatabase(final String name) {
        WebTarget table = instance.path(name);
        Response response = table.request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        if (isSuccessful(response)) {
            LOGGER.info("'{}' database dropped", table.getUri().getPath());
        }
    }
    
    private void createDatabase(final String name) {
        WebTarget table = instance.path(name);
        Response response = table.request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(""));
        if (isSuccessful(response)) {
            LOGGER.info("'{}' database created", table.getUri().getPath());
        }
        else if (response.getStatus() == 412) {
            LOGGER.info("'{}' database already exists", table.getUri().getPath());
        }
        else {
            LOGGER.error("Failed to create {} database: [{}] {}", table.getUri().getPath(),
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
    public static boolean isSuccessful(final Response response) {
        if (response == null) {
            throw new NullPointerException("response");
        }
        
        return Family.SUCCESSFUL == response.getStatusInfo().getFamily();
    }

}
