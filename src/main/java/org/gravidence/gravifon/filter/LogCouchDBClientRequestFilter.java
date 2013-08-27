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
package org.gravidence.gravifon.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CouchDB JAX-RS client log request filter.<p>
 * Logs HTTP method, media type, resource URI and entity if presented.<p>
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@Provider
public class LogCouchDBClientRequestFilter implements ClientRequestFilter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LogCouchDBClientRequestFilter.class);
    
    /**
     * @see #setObjectMapper(com.fasterxml.jackson.databind.ObjectMapper)
     */
    private ObjectMapper objectMapper;

    /**
     * Injects JSON data binding instance.
     * 
     * @param objectMapper JSON data binding instance
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            String entity = FilterUtils.NO_ENTITY;
            
            if (requestContext.hasEntity()) {
                entity = FilterUtils.entityToString(objectMapper, requestContext.getEntity(), LOGGER);
            }
            
            LOGGER.debug("Database request:\n{} {}\n{}", requestContext.getMethod(),
                    requestContext.getUri(), entity);
        }
    }
    
}
