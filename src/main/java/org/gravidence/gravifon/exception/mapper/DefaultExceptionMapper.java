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
package org.gravidence.gravifon.exception.mapper;

import org.gravidence.gravifon.resource.message.StatusResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.gravidence.gravifon.exception.error.GravifonError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mapper that handles all "unexpected" exceptions.<p>
 * Logs an exception and produces response with {@link GravifonError#INTERNAL INTERNAL} error.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.error("Unexpected exception captured", exception);
        
        GravifonError error = GravifonError.INTERNAL;
        
        StatusResponse entity = new StatusResponse(
                error.getErrorCode(), "An unexpected internal error.");
        
        return Response
                .status(error.getHttpStatusCode())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(entity)
                .build();
    }
    
}
