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
import org.springframework.dao.DataIntegrityViolationException;
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
     *
     * @throws IllegalArgumentException if artist ID is specified
     */
    public ArtistBean addArtist(ArtistBean artist) {
        if (artist.getId() != null) {
            throw new IllegalArgumentException("Artist must have no ID at that stage.");
        }

        GArtistRecord rs = dslContext.insertInto(G_ARTIST)
                .set(G_ARTIST.TITLE, artist.getTitle())
                .set(G_ARTIST.DESCRIPTION, artist.getDescription())
                .set(G_ARTIST.MASTER_ID, getMasterId(artist))
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

    /**
     * Links an alias artist to master one.<p>
     * Master and alias IDs are FKs, their combinations are unique:
     * <ul>
     *     <li>existing combination won't be added (silently);</li>
     *     <li>combination with broken IDs won't be added.</li>
     * </ul>
     *
     * @param masterId master artist ID
     * @param aliasId alias artist ID
     *
     * @throws DataIntegrityViolationException if at least one of supplied IDs doesn't exist
     */
    public void addArtistAlias(Long masterId, Long aliasId) {
        try {
            dslContext.insertInto(G_ARTIST_ALIAS)
                    .set(G_ARTIST_ALIAS.MASTER_ID, masterId)
                    .set(G_ARTIST_ALIAS.ALIAS_ID, aliasId)
                    .execute();
        }
        catch (DataIntegrityViolationException e) {
            // Ignore duplicate case
            if (!dslContext.fetchExists(
                    dslContext.selectFrom(G_ARTIST_ALIAS)
                        .where(G_ARTIST_ALIAS.MASTER_ID.eq(masterId))
                        .and(G_ARTIST_ALIAS.ALIAS_ID.eq(aliasId)))) {
                throw e;
            }
        }
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
     * Updates artist record with supplied field values.<p>
     * New artist is added if there's no matching ID the database.
     *
     * @param artist artist bean
     *
     * @throws IllegalArgumentException if artist ID is not specified
     */
    public void updateArtist(ArtistBean artist) {
        if (artist.getId() == null) {
            throw new IllegalArgumentException("Artist must have an ID.");
        }

        dslContext.mergeInto(G_ARTIST)
                .columns(G_ARTIST.ID, G_ARTIST.TITLE, G_ARTIST.DESCRIPTION, G_ARTIST.MASTER_ID)
                .key(G_ARTIST.ID)
                .values(artist.getId(), artist.getTitle(), artist.getDescription(), getMasterId(artist))
                .execute();
    }

    /**
     * Null-safe getter of master artist ID.
     *
     * @param artist artist bean
     * @return Master artist ID of supplied artist.
     */
    private static Long getMasterId(ArtistBean artist) {
        return artist.getMaster() == null ? null : artist.getMaster().getId();
    }

    /**
     * Database to web model converter (single entity).
     *
     * @param rs database model entity
     * @return Corresponding web model entity or <code>null</code> if no value supplied.
     */
    private static ArtistBean toWebModel(GArtistRecord rs) {
        return rs == null ? null : new ArtistBean(rs.getId(), rs.getTitle(), rs.getDescription(), rs.getMasterId());
    }

    /**
     * Database to web model converter (multiple entities).
     *
     * @param rs list of database model entities
     * @return List of corresponding web model entities or <code>null</code> if no value supplied.
     */
    private static List<ArtistBean> toWebModel(Result<GArtistRecord> rs) {
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
