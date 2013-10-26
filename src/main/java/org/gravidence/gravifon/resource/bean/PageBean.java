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
import java.util.List;

/**
 * Page result bean.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class PageBean<T> extends IOBean {

    /**
     * @see #getNext()
     */
    @JsonProperty
    private String next;

    /**
     * @see #getItems()
     */
    @JsonProperty
    private List<T> items;

    /**
     * Returns first after the page item identifier.
     * 
     * @return first after the page item identifier
     */
    public String getNext() {
        return next;
    }

    /**
     * @param next
     * @see #getNext()
     */
    public void setNext(String next) {
        this.next = next;
    }

    /**
     * Returns page items.
     * 
     * @return page items
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * @param items
     * @see #getItems()
     */
    public void setItems(List<T> items) {
        this.items = items;
    }
    
}
