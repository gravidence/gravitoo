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

import java.util.List;
import java.util.Locale;
import javax.ws.rs.client.WebTarget;
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.db.domain.LabelDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * CouchDB JAX-RS client to <code>/labels</code> database.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class LabelsDBClient extends BasicDBClient<LabelDocument> implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LabelsDBClient.class);
    
    /**
     * JAX-RS client target associated with <code>main/all_primary_label_variations</code> view.
     */
    private WebTarget viewMainAllPrimaryLabelVariationsTarget;
    
    /**
     * JAX-RS client target associated with <code>main/all_label_variations</code> view.
     */
    private WebTarget viewMainAllLabelVariationsTarget;

    @Override
    public void afterPropertiesSet() throws Exception {
        setDbTarget(getDbClient().getTarget()
                .path("labels"));
        
        viewMainAllPrimaryLabelVariationsTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_primary_label_variations");
        viewMainAllLabelVariationsTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_label_variations");
    }
    
    /**
     * Retrieves amount of primary label variations.<p>
     * Amount of primary label variations is equal to <code>main/all_primary_label_variations</code> view size.
     * 
     * @return amount of primary label variations
     * 
     * @see #viewMainAllPrimaryLabelVariationsTarget
     * @see ViewQueryExecutor#querySize(javax.ws.rs.client.WebTarget)
     */
    public long retrievePrimaryLabelVariationAmount() {
        return ViewQueryExecutor.querySize(viewMainAllPrimaryLabelVariationsTarget);
    }
    
    /**
     * Retrieves existing label {@link LabelDocument document}.
     * 
     * @param id label identifier
     * @return label details document if found, <code>null</code> otherwise
     */
    public LabelDocument retrieveLabelByID(String id) {
        return retrieve(id, LabelDocument.class);
    }
    
    /**
     * Retrieves existing label {@link LabelDocument documents}.<p>
     * Makes sure that <code>name</code> written in lower case
     * since <code>main/all_label_variations</code> view is case sensitive.
     * 
     * @param name label name
     * @return list of label details documents if found, <code>null</code> otherwise
     */
    public List<LabelDocument> retrieveLabelsByName(String name) {
        ViewQueryArguments args = new ViewQueryArguments()
                .addKey(StringUtils.lowerCase(name, Locale.ENGLISH))
                .addIncludeDocs(true);
        
        List<LabelDocument> documents = ViewQueryExecutor.queryDocuments(viewMainAllLabelVariationsTarget, args,
                LabelDocument.class);
        
        return documents;
    }
    
}
