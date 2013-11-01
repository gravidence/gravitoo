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
package org.gravidence.gravifon.template;

import org.gravidence.gravifon.template.email.UserRegistrationConfirmationEmailData;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.error.GravifonError;

/**
 * Template processor implementation that uses Freemarker under the hood.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class FreemarkerTemplateProcessor implements TemplateProcessor {
    
    /**
     * Configuration settings of FreeMarker.
     */
    private static final Configuration FREEMARKER_CONFIG;
    
    static {
        ClassTemplateLoader emailTemplateLoader = new ClassTemplateLoader(TemplateProcessor.class, "email");
        
        TemplateLoader[] allLoaders = new TemplateLoader[] {
            emailTemplateLoader
        };
        
        MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(allLoaders);
        
        FREEMARKER_CONFIG = new Configuration();
        FREEMARKER_CONFIG.setTemplateLoader(multiTemplateLoader);
        FREEMARKER_CONFIG.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
    }
    
    @Override
    public String processUserRegistrationConfirmationEmail(UserRegistrationConfirmationEmailData data)
            throws GravifonException {
        StringWriter result = new StringWriter();
        
        try {
            Template template = FREEMARKER_CONFIG.getTemplate("user-registration-confirmation.ftl");
            template.process(data, result);
        }
        catch (IOException | TemplateException ex) {
            throw new GravifonException(GravifonError.UNEXPECTED,
                    "An unexpected error occurred during processing of confirmation email.", ex);
        }
        
        return result.toString();
    }
    
}
