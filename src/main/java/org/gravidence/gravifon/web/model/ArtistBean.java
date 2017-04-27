/*
 * The MIT License
 *
 * Copyright 2017 Gravidence.
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Artist web model entity.
 *
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
public class ArtistBean extends JsonBean {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String title;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ArtistBean master;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ArtistBean> aliases;

    /**
     * Default constructor for DI.
     */
    public ArtistBean() {
        // do nothing
    }

    /**
     * Constructs an artist bean using supplied properties.
     *
     * @param id artist ID
     */
    public ArtistBean(Long id) {
        this(id, null, null, null);
    }

    /**
     * Constructs an artist bean using supplied properties.
     *
     * @param title artist title
     */
    public ArtistBean(String title) {
        this(null, title, null, null);
    }

    /**
     * Constructs an artist bean using supplied properties.
     *
     * @param id artist ID
     * @param title artist title
     * @param description artist description
     */
    public ArtistBean(Long id, String title, String description) {
        this(id, title, description, null);
    }

    /**
     * Constructs an artist bean using supplied properties.
     *
     * @param id artist ID
     * @param title artist title
     * @param description artist description
     * @param masterId master artist ID
     */
    public ArtistBean(Long id, String title, String description, Long masterId) {
        this.id = id;
        this.title = title;
        this.description = description;
        if (masterId != null) {
            this.master = new ArtistBean(masterId);
        }
    }

    /**
     * @see #setId(Long)
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets artist identifier.
     *
     * @param id artist identifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @see #setTitle(String)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets artist title.
     *
     * @param title artist title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @see #setDescription(String)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets artist description.
     *
     * @param description artist description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @see #setMaster(ArtistBean)
     */
    public ArtistBean getMaster() {
        return master;
    }

    /**
     * Sets master artist to variation (<code>this</code>) artist.
     *
     * @param master master artist bean
     */
    public void setMaster(ArtistBean master) {
        this.master = master;
    }

    /**
     * @see #setAliases(List)
     */
    public List<ArtistBean> getAliases() {
        return aliases;
    }

    /**
     * Sets aliases of master (<code>this</code>) artist.
     *
     * @param aliases list of alias artist beans
     */
    public void setAliases(List<ArtistBean> aliases) {
        this.aliases = aliases;
    }

    /**
     * Sets aliases of master (<code>this</code>) artist initialized by supplied artist bean.
     *
     * @param alias alias artist bean
     */
    public void setAlias(ArtistBean alias) {
        ArrayList<ArtistBean> aliases = new ArrayList<>();
        aliases.add(alias);

        setAliases(aliases);
    }

}
