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

import org.gravidence.gravifon.filter.LogCouchDBClientResponseFilter;
import org.gravidence.gravifon.filter.LogCouchDBClientRequestFilter;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * CouchDB JAX-RS client.<p>
 * Connection to DB instance is verified during initialization.<p>
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class CouchDBClient implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CouchDBClient.class);
    
    // JAX-RS client instance
    private WebTarget instance;
    
    // Associated filters
    private LogCouchDBClientRequestFilter logRequestFilter;
    private LogCouchDBClientResponseFilter logResponseFilter;

    // DB settings
    private String url;

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
     * Returns JAX-RS client instance associated with CouchDB instance.
     */
    public WebTarget getTarget() {
        return instance;
    }

    /**
     * Injects JAX-RS client log request filter instance.
     * 
     * @param logRequestFilter log request filter instance
     */
    public void setLogRequestFilter(LogCouchDBClientRequestFilter logRequestFilter) {
        this.logRequestFilter = logRequestFilter;
    }

    /**
     * Injects JAX-RS client log response filter instance.
     * 
     * @param logResponseFilter log response filter instance
     */
    public void setLogResponseFilter(LogCouchDBClientResponseFilter logResponseFilter) {
        this.logResponseFilter = logResponseFilter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initDBConnection();
    }
    
    private void initDBConnection() throws IOException {
        LOGGER.info("Initializing DB layer");
        ClientConfig clientConfig = new ClientConfig()
                .register(logRequestFilter)
                .register(logResponseFilter);
        instance = ClientBuilder.newClient(clientConfig).target(url);
        
        LOGGER.info("Connecting to CouchDB instance at {}", url);
        Response response = instance
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        
        try {
            if (isSuccessful(response)) {
                if (response.hasEntity()) {
                    JsonNode node = SharedInstanceHolder.OBJECT_MAPPER.readTree(response.readEntity(InputStream.class));
                    LOGGER.info("CouchDB version: {}", node.get("version").asText());
                }
                else {
                    LOGGER.warn("CouchDB version: unknown");
                }
            }
            else {
                LOGGER.error("DB connection failure: [{}] {}", response.getStatus(),
                        response.getStatusInfo().getReasonPhrase());
                throw new RuntimeException("DB layer initialization failed");
            }
        }
        finally {
            response.close();
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
