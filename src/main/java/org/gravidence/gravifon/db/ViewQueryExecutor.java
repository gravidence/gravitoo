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

import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Design document view query executor.<p>
 * Extracts entities from view query results via {@link ViewQueryResultsExtractor}.
 * 
 * @see ViewQueryResultsExtractor
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ViewQueryExecutor {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewQueryExecutor.class);
    
    /**
     * Executes a view query and extracts all documents from results.
     * 
     * @param target JAX-RS target to CouchDB database design document view
     * @param args view query arguments
     * @param documentType document object type
     * @return list of <code>documentType</code> objects if any, or <code>null</code> otherwise
     * 
     * @see ViewQueryResultsExtractor#extractDocuments(java.lang.Class, java.io.InputStream)
     */
    public static <T> List<T> queryDocuments(WebTarget target, ViewQueryArguments args, Class<T> documentType) {
        for (Entry<String, String> entry : args.getArguments().entrySet()) {
            target = target.queryParam(entry.getKey(), entry.getValue());
        }
        
        Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        
        List<T> documents = null;
        
        if (CouchDBClient.isSuccessful(response)) {
            InputStream json = response.readEntity(InputStream.class);
            documents = ViewQueryResultsExtractor.extractDocuments(documentType, json);
        }
        else {
            LOGGER.error("Failed to execute '{}' view query: [{}] {}", target.getUri().getPath(),
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());
            throw new GravifonException(GravifonError.DATABASE_OPERATION, "Failed to execute view query.");
        }
        
        return documents;
    }
    
}
