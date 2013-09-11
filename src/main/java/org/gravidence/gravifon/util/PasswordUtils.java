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
package org.gravidence.gravifon.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.gravidence.gravifon.db.domain.SaltedHash;
import org.gravidence.gravifon.exception.GravifonException;
import org.gravidence.gravifon.exception.error.GravifonError;

/**
 * Password management utility methods.
 * 
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class PasswordUtils {
    
    /**
     * Number of iterations for PBKDF2.
     */
    private static final int ITERATIONS_NUMBER = Short.MAX_VALUE;
    
    /**
     * Length of the derived key for PBKDF2.
     */
    private static final int HASH_LENGTH = 512;
    
    /**
     * Length of cryptographic salt for PBKDF2.
     */
    private static final int SALT_LENGTH = 32;

    /**
     * Preventing class instantiation.
     */
    private PasswordUtils() {
        // Nothing to do
    }
    
    /**
     * Generates salted PBKDF2 hash for given password.
     * 
     * @param password plaintext password
     * @return salted PBKDF2 hash
     */
    public static SaltedHash getSaltedHash(String password) {
        byte[] salt;
        try {
            salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(SALT_LENGTH);
        }
        catch (NoSuchAlgorithmException ex) {
            throw new GravifonException(GravifonError.INTERNAL, "Failed to generate secure random seed.", ex);
        }
        
        String hash = hash(password, salt);
        
        SaltedHash result = new SaltedHash();
        result.setHash(hash);
        result.setSalt(Base64.encodeBase64String(salt));
        
        return result;
    }
    
    /**
     * Verifies that given password matches salted hash.
     * 
     * @param password plaintext password
     * @param saltedHash salted hash
     * @return <code>true</code> in case given password matches salted hash, <code>false</code> otherwise
     */
    public static boolean verify(String password, SaltedHash saltedHash) {
        String hash = hash(password, Base64.decodeBase64(saltedHash.getSalt()));
        
        return StringUtils.equals(saltedHash.getHash(), hash);
    }
    
    /**
     * Generates salted PBKDF2 hash for given password and salt.
     * 
     * @param password plaintext password
     * @param salt cryptographic salt
     * @return salted PBKDF2 hash
     */
    private static String hash(String password, byte[] salt) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("password");
        }
        if (ArrayUtils.isEmpty(salt)) {
            throw new IllegalArgumentException("salt");
        }
        
        byte[] hash;
        try {
            PBEKeySpec pbeks = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS_NUMBER, HASH_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = skf.generateSecret(pbeks).getEncoded();
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new GravifonException(GravifonError.INTERNAL, "Failed to generate hash.", ex);
        }
        
        return Base64.encodeBase64String(hash);
    }
    
}
