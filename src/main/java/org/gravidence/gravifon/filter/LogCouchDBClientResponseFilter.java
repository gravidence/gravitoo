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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CouchDB JAX-RS client log response filter.<p>
 * Logs HTTP status code, media type, reason phrase and entity if presented.<p>
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@Provider
public class LogCouchDBClientResponseFilter implements ClientResponseFilter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LogCouchDBClientResponseFilter.class);

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            String entity = FilterUtils.NO_ENTITY;
            
            if (responseContext.hasEntity()) {
                try (InputStream in = responseContext.getEntityStream()) {
                    byte[] buf = IOUtils.toByteArray(in);

                    responseContext.setEntityStream(new ByteArrayInputStream(buf));

                    entity = FilterUtils.entityToString(buf);
                }
            }

            LOGGER.debug("Database response:\n{} {}\n{}", responseContext.getStatus(),
                    responseContext.getStatusInfo().getReasonPhrase(), entity);
        }
    }
    
}
