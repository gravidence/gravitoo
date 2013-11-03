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

package org.gravidence.gravifon.schedule;

import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.gravidence.gravifon.db.UsersDBClient;
import org.gravidence.gravifon.db.domain.UserDocument;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Scheduled task that removes users who didn't manage to complete registration in time.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class RemoveUsersFailedToCompleteRegistrationTask {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveUsersFailedToCompleteRegistrationTask.class);
    
    /**
     * @see #setUsersDbClient()
     */
    private UsersDBClient usersDbClient;
    
    /**
     * @see #setThreshold()
     */
    private Long threshold;

    /**
     * Sets {@link UsersDBClient} instance.
     * 
     * @param usersDbClient {@link UsersDBClient} instance
     */
    public void setUsersDbClient(UsersDBClient usersDbClient) {
        this.usersDbClient = usersDbClient;
    }

    /**
     * Sets max allowed amount of time to complete the registration.
     * 
     * @param threshold max allowed amount of time to complete the registration
     */
    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }

    /**
     * Performs task job.
     */
    @Scheduled(cron = "${org.gravidence.gravifon.schedule.removeUsersFailedToCompleteRegistration.cron}")
    public void run() {
        Date start = new Date();

        int i = 0;
        try {
            List<UserDocument> users = usersDbClient.retrieveUsersFailedToCompleteRegistration(
                    Duration.standardHours(threshold));

            if (CollectionUtils.isNotEmpty(users)) {
                for (; i < users.size(); i++) {
                    UserDocument user = users.get(i);
                    
                    usersDbClient.delete(user);
                    
                    LOGGER.trace("{} user removed", user);
                }
            }
        }
        catch (Exception e) {
            LOGGER.warn("Exception occurred during task execution", e);
        }
        
        Date end = new Date();
        
        LOGGER.info("Task executed (duration: {} ms, users removed: {})",
                end.getTime() - start.getTime(), i);
    }
    
}
