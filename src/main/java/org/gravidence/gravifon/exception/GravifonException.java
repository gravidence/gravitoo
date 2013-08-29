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
package org.gravidence.gravifon.exception;

import org.gravidence.gravifon.exception.error.GravifonError;

/**
 * Gravifon service exception.<p>
 * Must be initialized with {@link GravifonError error} instance.
 *
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class GravifonException extends RuntimeException {

    /**
     * @see #getError()
     */
    private GravifonError error;
    
    /**
     * @see #isLogMe()
     */
    private boolean logMe;

    /**
     * Constructs a new Gravifon service exception.<p>
     * <code>Message</code> and <code>cause</code> are initialized with <code>null</code> value.<br>
     * <code>LogMe</code> is initialized with <code>true</code> value.
     * 
     * @param error internal error
     * @throws NullPointerException in case the supplied <code>error</code> argument is <code>null</code>
     */
    public GravifonException(GravifonError error) {
        this(error, null, null);
    }

    /**
     * Constructs a new Gravifon service exception.<p>
     * <code>Cause</code> is initialized with <code>null</code> value.<br>
     * <code>LogMe</code> is initialized with <code>true</code> value.
     * 
     * @param error internal error
     * @param message detail message
     * @throws NullPointerException in case the supplied <code>error</code> argument is <code>null</code>
     */
    public GravifonException(GravifonError error, String message) {
        this(error, message, null);
    }

    /**
     * Constructs a new Gravifon service exception.<p>
     * <code>Message</code> is initialized with <code>null</code> value.<br>
     * <code>LogMe</code> is initialized with <code>true</code> value.
     * 
     * @param error internal error
     * @param cause cause
     * @throws NullPointerException in case the supplied <code>error</code> argument is <code>null</code>
     */
    public GravifonException(GravifonError error, Throwable cause) {
        this(error, null, cause);
    }

    /**
     * Constructs a new Gravifon service exception.<p>
     * <code>LogMe</code> is initialized with <code>true</code> value.
     * 
     * @param error internal error
     * @param message detail message
     * @param cause cause
     * @throws NullPointerException in case the supplied <code>error</code> argument is <code>null</code>
     */
    public GravifonException(GravifonError error, String message, Throwable cause) {
        this(error, message, cause, true);
    }

    /**
     * Constructs a new Gravifon service exception.
     * 
     * @param error internal error
     * @param message detail message
     * @param cause cause
     * @param logMe exception logging required indicator
     * @throws NullPointerException in case the supplied <code>error</code> argument is <code>null</code>
     */
    public GravifonException(GravifonError error, String message, Throwable cause, boolean logMe) {
        super(message, cause);

        if (error == null) {
            throw new NullPointerException("gravifonError");
        }

        this.error = error;
        this.logMe = logMe;
    }

    /**
     * Returns error associated with the exception.
     *
     * @return error
     */
    public GravifonError getError() {
        return error;
    }
    
    /**
     * Says if exception logging is required.
     * 
     * @return <code>true</code> in case exception logging is required
     */
    public boolean isLogMe() {
        return logMe;
    }
    
}
