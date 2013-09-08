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
package org.gravidence.gravifon.resource.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Gravifon application info bean.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ApplicationInfoBean extends IOBean {

    /**
     * @see #getAppName()
     */
    @JsonProperty("app_name")
    private String appName;

    /**
     * @see #getAppDescription()
     */
    @JsonProperty("app_description")
    private String appDescription;

    /**
     * @see #getAppVersion()
     */
    @JsonProperty("app_version")
    private String appVersion;

    /**
     * @see #getApiVersion()
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * Returns Gravifon application name.
     * 
     * @return Gravifon application name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName
     * @see #getAppName()
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * Returns Gravifon application description.
     * 
     * @return Gravifon application description
     */
    public String getAppDescription() {
        return appDescription;
    }

    /**
     * @param appDescription
     * @see #getAppDescription()
     */
    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    /**
     * Returns Gravifon application version.
     * 
     * @return Gravifon application version
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * @param appVersion
     * @see #getAppVersion()
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * Returns Gravifon API version.
     * 
     * @return Gravifon API version
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * @param apiVersion
     * @see #getApiVersion()
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
    
}
