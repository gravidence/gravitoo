USE GRAVIDAT ;

INSERT INTO G_ARTIST(ID, MASTER_ID, TITLE, DESCRIPTION) VALUES
    (1, NULL, 'Autechre', NULL),
    (2, NULL, 'Quench', 'Christopher J. Dolan'),
    (3, NULL, 'Quench', 'Don & Roel Funcken'),
    (4, NULL, 'Vidna Obmana', 'Dirk Serries'),
    (5, 4, 'vidnaObmana', NULL),
    (6, NULL, 'Funckarma', 'Don & Roel Funcken'),
    (7, NULL, 'Cane', 'Don & Roel Funcken'),
    (8, 4, 'V.O.', NULL) ;

INSERT INTO G_ARTIST_ALIAS(MASTER_ID, ALIAS_ID) VALUES
    (6, 3),
    (6, 7) ;

INSERT INTO G_RELEASE_TYPE(TITLE) VALUES
    ('Album'),
    ('EP'),
    ('Single'),
    ('Compilation') ;

INSERT INTO G_RELEASE(TITLE, "DATE") VALUES
    ('The River Of Appearance', '1996-01-01'),
    ('The River Of Appearance', '2006-01-01') ;

INSERT INTO G_TRACK(MASTER_ID, TITLE, "LENGTH", RELEASE_ID) VALUES
    (NULL, 'The Angelic Appearance', 548, 2),
    (NULL, 'Ephemeral Vision', 847, 2),
    (NULL, 'The Angelic Appearance', 548, 1) ;

INSERT INTO G_USER(USERNAME, EMAIL, CREATED, LAST_ACTIVITY) VALUES
    ('m2', 'maksim_liauchuk@fastmail.fm', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
    ('i3', 'ina_liauchuk@fastmail.fm', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()) ;

INSERT INTO G_GRAVION(USER_ID, TRACK_ID, "BEGIN", "END", DURATION) VALUES
    (1, 1, '2017-04-15 12:44:00', '2017-04-15 12:50:00', 548),
    (1, 2, '2017-04-15 12:50:00', '2017-04-15 12:59:00', 847),
    (1, 1, '2017-04-15 12:59:00', '2017-04-15 13:05:00', 548) ;
