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
package org.gravidence.gravifon.validation;

import javax.ws.rs.core.MultivaluedMap;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.ValidationException;
import org.gravidence.gravifon.resource.bean.ValidateableBean;

/**
 * Validates <code>delete</code> method of <code>Scrobbles</code> resource.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ScrobbleDeleteValidator extends AbstractValidator<ValidateableBean> {

    @Override
    protected void validateHeaders(MultivaluedMap<String, String> headers)
            throws GravifonException, ValidationException {
        checkRequiredAuthorizationHeader(headers);
    }

    @Override
    protected void validateQueryParams(MultivaluedMap<String, String> queryParams) throws ValidationException {
        // No query params are expected.
    }

    @Override
    protected void validateEntity(ValidateableBean entity) throws ValidationException {
        // No entity is expected.
    }
    
}
