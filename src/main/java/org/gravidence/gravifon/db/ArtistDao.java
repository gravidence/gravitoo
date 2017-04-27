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

package org.gravidence.gravifon.db;

import org.gravidence.gravidat.model.tables.records.GArtistRecord;
import org.gravidence.gravifon.web.model.ArtistBean;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.gravidence.gravidat.model.Tables.G_ARTIST;
import static org.gravidence.gravidat.model.Tables.G_ARTIST_ALIAS;

/**
 * DAO for Artist domain (<code>G_ARTIST</code> and <code>G_ARTIST_ALIAS</code> tables).
 *
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@Component
public class ArtistDao {

    /**
     * jOOQ instance bean.
     */
    @Autowired
    private DSLContext dslContext;

    /**
     * Adds a new artist record.
     *
     * @param artist artist bean
     * @return Supplied artist bean updated with ID.
     */
    public ArtistBean addArtist(ArtistBean artist) {
        GArtistRecord rs = dslContext.insertInto(G_ARTIST)
                .set(G_ARTIST.TITLE, artist.getTitle())
                .set(G_ARTIST.DESCRIPTION, artist.getDescription())
                .set(G_ARTIST.MASTER_ID, artist.getMaster() == null ? null : artist.getMaster().getId())
                .returning(G_ARTIST.ID)
                .fetchOne();

        artist.setId(rs.getId());

        return artist;
    }

    /**
     * Fetches an artist by ID.
     *
     * @param id artist ID
     * @return The artist bean or <code>null</code> if not found.
     */
    public ArtistBean getArtist(Long id) {
        GArtistRecord rs = dslContext.selectFrom(G_ARTIST)
                .where(G_ARTIST.ID.eq(id))
                .fetchOne();

        return toWebModel(rs);
    }

    /**
     * Fetches an artist by title.<p>
     * Only first matching record is fetched. For complete result set use {@link #getArtists(String)}.
     *
     * @param title artist title
     * @return The artist bean or <code>null</code> if not found.
     *
     * @see #getArtists(String)
     */
    public ArtistBean getArtist(String title) {
        GArtistRecord rs = dslContext.selectFrom(G_ARTIST)
                .where(G_ARTIST.TITLE.equalIgnoreCase(title))
                .fetchOne();

        return toWebModel(rs);
    }

    /**
     * Fetches all artists with particular title.
     *
     * @param title artist title
     * @return List of artist beans or <code>null</code> if none found.
     */
    public List<ArtistBean> getArtists(String title) {
        Result<GArtistRecord> rs = dslContext.selectFrom(G_ARTIST)
                .where(G_ARTIST.TITLE.eq(title))
                .fetch();

        return toWebModel(rs);
    }

    /**
     * Fetches artist variations for a master artist.<p>
     * Master artist is NOT part of resulting list.
     *
     * @param masterId master artist ID
     * @return List of artist beans or <code>null</code> if none found.
     */
    public List<ArtistBean> getArtistVariations(Long masterId) {
        Result<GArtistRecord> rs = dslContext.selectFrom(G_ARTIST)
                .where(G_ARTIST.MASTER_ID.eq(masterId))
                .fetch();

        return toWebModel(rs);
    }

    // TODO JavaDoc needs re-wording
    /**
     * Links an alias artist to master one.<p>
     * Master and alias IDs combination must be unique.
     * Should such violation take place, it's gracefully handled by the method (by returning <code>false</code>) and never propagated outside.
     *
     * @param masterId master artist ID
     * @param aliasId alias artist ID
     * @return An indicator whether the operation was successful or not.
     */
    public boolean addArtistAlias(Long masterId, Long aliasId) {
        boolean result = true;

        try {
            dslContext.insertInto(G_ARTIST_ALIAS)
                    .set(G_ARTIST_ALIAS.MASTER_ID, masterId)
                    .set(G_ARTIST_ALIAS.ALIAS_ID, aliasId)
                    .execute();
        }
        catch (Exception e) {
            // TODO more specific error handling should be used here (dig exceptions and error codes)
            // "java.lang.IllegalArgumentException: Database product name must not be null" is currently thrown for some reason
            System.out.println(e);
            result = false;
        }

        return result;
    }

    /**
     * Fetches alias artists of master one.<p>
     * Master artist is NOT part of resulting list.
     *
     * @param masterId master artist ID
     * @return List of artist bean or <code>null</code> if none found.
     */
    // TODO add TX test
    @Transactional
    public List<ArtistBean> getArtistAliases(Long masterId) {
        Result<Record1<Long>> aliases = dslContext.select(G_ARTIST_ALIAS.ALIAS_ID).from(G_ARTIST_ALIAS)
                .where(G_ARTIST_ALIAS.MASTER_ID.eq(masterId))
                .fetch();

        Result<GArtistRecord> rs = dslContext.selectFrom(G_ARTIST)
                .where(G_ARTIST.ID.in(aliases))
                .fetch();

        return toWebModel(rs);
    }

    /**
     * Database to web model converter (single entity).
     *
     * @param rs database model entity
     * @return Corresponding web model entity or <code>null</code> if no value supplied.
     */
    private ArtistBean toWebModel(GArtistRecord rs) {
        return rs == null ? null : new ArtistBean(rs.getId(), rs.getTitle(), rs.getDescription(), rs.getMasterId());
    }

    /**
     * Database to web model converter (multiple entities).
     *
     * @param rs list of database model entities
     * @return List of corresponding web model entities or <code>null</code> if no value supplied.
     */
    private List<ArtistBean> toWebModel(Result<GArtistRecord> rs) {
        List<ArtistBean> artists;
        if (CollectionUtils.isEmpty(rs)) {
            artists = null;
        }
        else {
            artists = new ArrayList<>();
            for (GArtistRecord r : rs) {
                artists.add(toWebModel(r));
            }
        }

        return artists;
    }

}
