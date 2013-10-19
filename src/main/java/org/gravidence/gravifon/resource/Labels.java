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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.gravidence.gravifon.db.LabelsDBClient;
import org.gravidence.gravifon.resource.bean.LabelsInfoBean;
import org.gravidence.gravifon.resource.message.StatusResponse;
import org.gravidence.gravifon.validation.LabelsInfoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Labels resource.<p>
 * Provides <code>label</code> entity management API.
 * 
 * @see LabelsDBClient
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@Path("/v1/labels")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Labels {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Labels.class);
    
    /**
     * {@link LabelsDBClient} instance.
     */
    @Autowired
    private LabelsDBClient labelsDBClient;
    
    // Validators
    private LabelsInfoValidator labelsInfoValidator = new LabelsInfoValidator();
    
    /**
     * Retrieves <code>/labels</code> database info.
     * 
     * @return status response with <code>/labels</code> database info bean
     */
    @GET
    public StatusResponse info() {
        labelsInfoValidator.validate(null, null, null);
        
        LabelsInfoBean entity = new LabelsInfoBean();
        
        entity.setLabelAmount(labelsDBClient.retrievePrimaryLabelVariationAmount());
        
        return new StatusResponse<>(entity);
    }

}
