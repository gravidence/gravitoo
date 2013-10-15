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
import org.gravidence.gravifon.db.domain.UserDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * CouchDB JAX-RS client to <code>/users</code> database.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class UsersDBClient extends BasicDBClient<UserDocument> implements InitializingBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersDBClient.class);
    
    /**
     * JAX-RS client target associated with <code>main/all_usernames</code> view.
     */
    private WebTarget viewMainAllUsernamesTarget;

    @Override
    public void afterPropertiesSet() throws Exception {
        setDbTarget(getDbClient().getTarget()
                .path("users"));
        
        viewMainAllUsernamesTarget = ViewUtils.getViewTarget(
                getDbTarget(), "main", "all_usernames");
    }
    
    /**
     * Retrieves user amount.<p>
     * User amount is equal to <code>main/all_usernames</code> view size.
     * 
     * @return user amount
     * 
     * @see #viewMainAllUsernamesTarget
     * @see ViewQueryExecutor#querySize(javax.ws.rs.client.WebTarget)
     */
    public long retrieveUserAmount() {
        return ViewQueryExecutor.querySize(viewMainAllUsernamesTarget);
    }
    
    /**
     * Retrieves existing user {@link UserDocument document}.
     * 
     * @param id user identifier
     * @return user details document if found, <code>null</code> otherwise
     */
    public UserDocument retrieveUserByID(String id) {
        return retrieve(id, UserDocument.class);
    }
    
    /**
     * Retrieves existing user {@link UserDocument document}.<p>
     * Makes sure that <code>username</code> written in lower case
     * since <code>main/all_usernames</code> view is case sensitive.
     * 
     * @param username username
     * @return user details document if found, <code>null</code> otherwise
     */
    public UserDocument retrieveUserByUsername(String username) {
        ViewQueryArguments args = new ViewQueryArguments()
                .addKey(StringUtils.lowerCase(username, Locale.ENGLISH))
                .addIncludeDocs(true);
        
        List<UserDocument> documents = ViewQueryExecutor.queryDocuments(viewMainAllUsernamesTarget, args,
                UserDocument.class);
        
        // usernames are unique, so don't care about multiple results
        return documents == null ? null : documents.get(0);
    }
    
}
