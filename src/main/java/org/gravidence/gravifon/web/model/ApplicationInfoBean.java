/*
 * The MIT License
 *
 * Copyright 2016 Gravidence.
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

package org.gravidence.gravifon.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Holder for brief application description (name, service version, api version).<p>
 * Actual values are taken from properties.
 *
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@Component
public class ApplicationInfoBean extends JsonBean {

    @Value("${org.gravidence.gravifon.application.name}")
    @JsonProperty("app_name")
    private String appName;

    @Value("${org.gravidence.gravifon.application.description}")
    @JsonProperty("app_description")
    private String appDescription;

    @Value("${org.gravidence.gravifon.application.version}")
    @JsonProperty("app_version")
    private String appVersion;

    @Value("${org.gravidence.gravifon.api.version}")
    @JsonProperty("api_version")
    private String apiVersion;

}
