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

package org.gravidence.gravifon.web.resource;

import org.gravidence.gravifon.web.model.ApplicationInfoBean;
import org.gravidence.gravifon.web.model.StatusBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Application resource.<p>
 * Provides basic information about application.
 *
 * @see ApplicationInfoBean
 *
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@RestController
@RequestMapping("/v1")
public class Application {

    /**
     * Application description entity.
     */
    @Autowired
    private ApplicationInfoBean applicationInfo;

    /**
     * Response instance to hold application description.<p>
     * Virtually immutable.
     */
    private StatusBean<ApplicationInfoBean> infoRS;

    /**
     * Instantiates response with application description entity.
     */
    @PostConstruct
    private void setUp() {
        infoRS = new StatusBean<>(applicationInfo);
    }

    /**
     * Returns application description.
     *
     * @return application description
     */
    @RequestMapping(method = RequestMethod.GET)
    public StatusBean info() {
        return infoRS;
    }

}
