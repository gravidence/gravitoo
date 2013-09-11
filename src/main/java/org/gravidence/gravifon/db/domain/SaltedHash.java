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
package org.gravidence.gravifon.db.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Salted hash bean.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class SaltedHash {

    /**
     * @see #getHash()
     */
    @JsonProperty
    private String hash;
    
    /**
     * @see #getSalt()
     */
    @JsonProperty
    private String salt;

    /**
     * Returns salted hash.
     * 
     * @return salted hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @param hash
     * @see #getHash()
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Returns salt.
     * 
     * @return salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt
     * @see #getSalt()
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
}
