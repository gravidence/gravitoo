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

package org.gravidence.gravifon.db;

import junit.framework.TestCase;
import org.gravidence.gravifon.web.model.ArtistBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Integration tests for {@link ArtistDao}.<p>
 * H2 in-memory is used underneath.
 *
 * @author Maksim Liauchuk <maksim_liauchuk@fastmail.fm>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ArtistDaoTest extends TestCase {

    @Autowired
    private ArtistDao artistDao;

    /**
     * Tests {@link ArtistDao#addArtist(ArtistBean)}.<p>
     * Happy path.
     */
    @Test
    public void addArtist() {
        final ArtistBean expected = new ArtistBean("Red Snapper");
        ArtistBean actual = artistDao.addArtist(expected);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNotNull(actual.getTitle());

        assertEquals(expected.getTitle(), actual.getTitle()); // returning bean
        actual = artistDao.getArtist(actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle()); // actual bean from database
        assertNull(actual.getDescription());
        assertNull(actual.getMaster());
    }

    /**
     * Tests {@link ArtistDao#addArtist(ArtistBean)}.<p>
     * Adding an artist variation specifying the link to master artist.
     */
    @Test
    public void addArtistFullDetails() {
        final ArtistBean funckarma = artistDao.getArtist("Funckarma");
        final ArtistBean funkarma = new ArtistBean(null, "Funkarma", null, funckarma.getId());
        ArtistBean actual = artistDao.addArtist(funkarma);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNotNull(actual.getTitle());

        assertEquals(funkarma.getTitle(), actual.getTitle()); // returning bean
        actual = artistDao.getArtist(actual.getId());
        assertEquals(funkarma.getTitle(), actual.getTitle()); // actual bean from database
        assertEquals(funkarma.getDescription(), actual.getDescription());
        assertNotNull(actual.getMaster());
        assertEquals(funkarma.getMaster().getId(), actual.getMaster().getId());
    }

    /**
     * Tests {@link ArtistDao#addArtist(ArtistBean)}.<p>
     * Several artist could share same title, adding two of such.
     */
    @Test
    public void addArtistSameTitle() {
        artistDao.addArtist(new ArtistBean(null, "Human Error", "Tom Box", null));
        artistDao.addArtist(new ArtistBean(null, "Human Error", "Sinecore & T-Psy", null));

        final List<ArtistBean> actual = artistDao.getArtists("Human Error");

        assertNotNull(actual);

        assertEquals(2, actual.size());
    }

    /**
     * Tests {@link ArtistDao#addArtist(ArtistBean)}.
     * Artist ID specified (actually, should be treated as wrong usage of DAO).
     */
    @Test(expected = IllegalArgumentException.class)
    public void addArtistWithId() {
        artistDao.addArtist(new ArtistBean(42L, "Makyo", null));
    }

    /**
     * Tests {@link ArtistDao#getArtist(Long)}.<p>
     * Happy path.
     */
    @Test
    public void getArtistById() {
        final ArtistBean expected = new ArtistBean(Long.valueOf(4));
        ArtistBean actual = artistDao.getArtist(expected.getId());

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNotNull(actual.getTitle());

        assertEquals(expected.getId(), actual.getId());
        assertEquals("Vidna Obmana", actual.getTitle());
    }

    /**
     * Tests {@link ArtistDao#getArtist(Long)}.<p>
     * Not found.
     */
    @Test
    public void getArtistByIdNotExists() {
        ArtistBean actual = artistDao.getArtist(Long.MAX_VALUE);

        assertNull(actual);
    }

    /**
     * Tests {@link ArtistDao#getArtist(String)}.<p>
     * Happy path.
     */
    @Test
    public void getArtistByTitle() {
        final ArtistBean expected = new ArtistBean("Vidna Obmana");
        ArtistBean actual = artistDao.getArtist(expected.getTitle());

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNotNull(actual.getTitle());

        assertEquals(expected.getTitle(), actual.getTitle());
    }

    /**
     * Tests {@link ArtistDao#getArtist(String)}.<p>
     * Retrieving an artist by title should be case insensitive.
     */
    @Test
    public void getArtistByTitleCaseDoesntMatter() {
        final ArtistBean expected = new ArtistBean("vidna obmana");
        ArtistBean actual = artistDao.getArtist(expected.getTitle());

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNotNull(actual.getTitle());

        assertEquals(expected.getTitle().toLowerCase(), actual.getTitle().toLowerCase());
    }

    /**
     * Tests {@link ArtistDao#getArtist(String)}.<p>
     * Not found.
     */
    @Test
    public void getArtistByTitleNotExists() {
        ArtistBean actual = artistDao.getArtist("Rammstein");

        // check test case
        assertNull(actual);
    }

    /**
     * Tests {@link ArtistDao#getArtists(String)}.<p>
     * Happy path.
     */
    @Test
    public void getArtistsByTitle() {
        final ArtistBean expected = new ArtistBean("Quench");
        List<ArtistBean> actual = artistDao.getArtists(expected.getTitle());

        assertNotNull(actual);

        assertEquals(2, actual.size());
        final Set<Long> artistIds = new HashSet<>(); // to store unique artist record identifiers
        for (ArtistBean artist : actual) {
            assertNotNull(artist.getId());
            assertNotNull(artist.getTitle());

            assertEquals(expected.getTitle(), artist.getTitle());
            // not an artist record should appear twice in result set
            assertFalse(artistIds.contains(artist.getId()));
            artistIds.add(artist.getId());
        }
    }

    /**
     * Tests {@link ArtistDao#getArtists(String)}.<p>
     * Not found.
     */
    @Test
    public void getArtistsByTitleNotExists() {
        List<ArtistBean> actual = artistDao.getArtists("Duran Duran Duran");

        assertNull(actual);
    }

    /**
     * Tests {@link ArtistDao#getArtistVariations(Long)}.<p>
     * Happy path.
     */
    @Test
    public void getArtistVariations() {
        final ArtistBean vidnaObmana = artistDao.getArtist("Vidna Obmana");
        List<ArtistBean> actual = artistDao.getArtistVariations(vidnaObmana.getId());

        assertNotNull(actual);

        assertEquals(2, actual.size());
        for (ArtistBean artist : actual) {
            assertNotNull(artist.getId());
            assertNotNull(artist.getTitle());

            assertTrue(artist.getTitle().equals("vidnaObmana") || artist.getTitle().equals("V.O."));
            assertNotNull(artist.getMaster());
            assertEquals(vidnaObmana.getId(), artist.getMaster().getId());
        }
    }

    /**
     * Tests {@link ArtistDao#getArtistVariations(Long)}.<p>
     * Not found.
     */
    @Test
    public void getArtistVariationsNotExist() {
        final ArtistBean funckarma = artistDao.getArtist("Funckarma");
        List<ArtistBean> actual = artistDao.getArtistVariations(funckarma.getId());

        assertNull(actual);
    }

    /**
     * Tests {@link ArtistDao#addArtistAlias(Long, Long)}.<p>
     * Happy path.
     */
    @Test
    public void addArtistAlias() {
        final ArtistBean autechre = artistDao.getArtist("Autechre");
        final ArtistBean legoFeet = artistDao.addArtist(new ArtistBean("Lego Feet"));

        assertNull(artistDao.getArtistAliases(autechre.getId())); // no Autechre aliases should be registered

        artistDao.addArtistAlias(autechre.getId(), legoFeet.getId());
        final List<ArtistBean> actual = artistDao.getArtistAliases(autechre.getId());

        assertNotNull(actual);

        assertEquals(1, actual.size());
        assertEquals(legoFeet.getId(), actual.get(0).getId());
    }

    /**
     * Tests {@link ArtistDao#addArtistAlias(Long, Long)}.<p>
     * Adding a duplicate alias (not allowed).
     */
    @Test
    public void addArtistAliasDuplicate() {
        final ArtistBean funckarma = artistDao.getArtist("Funckarma");
        final List<ArtistBean> expected = artistDao.getArtistAliases(funckarma.getId());

        try {
            artistDao.addArtistAlias(funckarma.getId(), expected.get(0).getId());
        }
        catch (DataIntegrityViolationException e) {
            fail("No data integrity violation exception expected here.");
        }
        final List<ArtistBean> actual = artistDao.getArtistAliases(funckarma.getId());
        assertEquals(expected.size(), actual.size());
    }

    /**
     * Tests {@link ArtistDao#addArtistAlias(Long, Long)}.<p>
     * Adding an alias using non-existing master ID.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void addArtistAliasNoMasterArtist() {
        final ArtistBean bancoDeGaia = artistDao.addArtist(new ArtistBean("Banco De Gaia"));
        final Long nonExistingArtistId = System.currentTimeMillis();

        assertNull(artistDao.getArtistAliases(bancoDeGaia.getId()));
        assertNull(artistDao.getArtist(nonExistingArtistId));

        artistDao.addArtistAlias(nonExistingArtistId, bancoDeGaia.getId());

        fail("Data integrity violation exception is expected.");
    }

    /**
     * Tests {@link ArtistDao#addArtistAlias(Long, Long)}.<p>
     * Adding an alias using non-existing alias ID.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void addArtistAliasNoAliasArtist() {
        final ArtistBean bancoDeGaia = artistDao.addArtist(new ArtistBean("Banco De Gaia"));
        final Long nonExistingArtistId = System.currentTimeMillis();

        assertNull(artistDao.getArtistAliases(bancoDeGaia.getId()));
        assertNull(artistDao.getArtist(nonExistingArtistId));

        artistDao.addArtistAlias(bancoDeGaia.getId(), nonExistingArtistId);

        fail("Data integrity violation exception is expected.");
    }

    /**
     * Tests {@link ArtistDao#getArtistAliases(Long)}.<p>
     * Happy path.
     */
    @Test
    public void getArtistAliases() {
        final ArtistBean funckarma = artistDao.getArtist("Funckarma");
        List<ArtistBean> actual = artistDao.getArtistAliases(funckarma.getId());

        assertNotNull(actual);

        assertEquals(2, actual.size());
        for (ArtistBean artist : actual) {
            assertNotNull(artist.getId());
            assertNotNull(artist.getTitle());

            assertTrue(artist.getTitle().equals("Quench") || artist.getTitle().equals("Cane"));
        }
    }

    /**
     * Tests {@link ArtistDao#getArtistAliases(Long)}.<p>
     * Not found.
     */
    @Test
    public void getArtistAliasesNotExist() {
        final ArtistBean beefcake = artistDao.getArtist("Autechre");
        List<ArtistBean> actual = artistDao.getArtistAliases(beefcake.getId());

        assertNull(actual);
    }

    /**
     * Tests {@link ArtistDao#updateArtist(ArtistBean)}.
     * Happy path.
     */
    @Test
    public void updateArtist() {
        final ArtistBean vidnaObmana = artistDao.getArtist("VidnaObmana");

        assertNull(vidnaObmana.getDescription());

        vidnaObmana.setDescription("Dirk Serries");
        artistDao.updateArtist(vidnaObmana);

        final ArtistBean actual = artistDao.getArtist(vidnaObmana.getId());
        assertEquals(vidnaObmana.getTitle(), actual.getTitle());
        assertEquals(vidnaObmana.getDescription(), actual.getDescription());
        assertEquals(vidnaObmana.getMaster().getId(), actual.getMaster().getId());
    }

    /**
     * Tests {@link ArtistDao#updateArtist(ArtistBean)}.
     * Not found, should fallback to insert.
     */
    @Test
    public void updateArtistNotExists() {
        final ArtistBean expected = new ArtistBean(9000L, "AE", "Sean Booth & Rob Brown", 1L);

        assertNull(artistDao.getArtist(expected.getTitle()));

        artistDao.updateArtist(expected);

        final ArtistBean actual = artistDao.getArtist("AE");

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getMaster().getId(), actual.getMaster().getId());
    }

    /**
     * Tests {@link ArtistDao#updateArtist(ArtistBean)}.
     * No artist ID specified (actually, should be treated as wrong usage of DAO).
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateArtistNoId() {
        artistDao.updateArtist(new ArtistBean("Another Electronic Musician"));
    }

}
