DROP ALL OBJECTS ;

CREATE SCHEMA IF NOT EXISTS GRAVIDAT ;
USE GRAVIDAT ;

CREATE TABLE G_ARTIST (
    ID IDENTITY,
    MASTER_ID BIGINT,
    TITLE VARCHAR(500) NOT NULL,
    DESCRIPTION VARCHAR(500),
    FOREIGN KEY (MASTER_ID) REFERENCES (ID)
) ;

CREATE TABLE G_ARTIST_ALIAS (
    MASTER_ID BIGINT,
    ALIAS_ID BIGINT,
    FOREIGN KEY (MASTER_ID) REFERENCES G_ARTIST(ID),
    FOREIGN KEY (ALIAS_ID) REFERENCES G_ARTIST(ID)
) ;
CREATE UNIQUE INDEX G_ARTIST_ALIAS_IDX ON G_ARTIST_ALIAS(MASTER_ID, ALIAS_ID) ;

CREATE TABLE G_RELEASE_TYPE (
    ID IDENTITY,
    TITLE VARCHAR(20) NOT NULL
) ;

CREATE TABLE G_RELEASE_GROUP (
    ID IDENTITY,
    TITLE VARCHAR(500) NOT NULL,
    DATE DATE
) ;

CREATE TABLE G_RELEASE (
    ID IDENTITY,
    MASTER_ID BIGINT,
    RELEASE_GROUP_ID BIGINT,
    TITLE VARCHAR(500) NOT NULL,
    DATE DATE,
    TYPE_ID TINYINT,
    FOREIGN KEY (MASTER_ID) REFERENCES (ID),
    FOREIGN KEY (RELEASE_GROUP_ID) REFERENCES G_RELEASE_GROUP(ID),
    FOREIGN KEY (TYPE_ID) REFERENCES G_RELEASE_TYPE(ID)
) ;

CREATE TABLE G_RELEASE_ARTIST (
    RELEASE_ID BIGINT,
    ARTIST_ID BIGINT,
    FOREIGN KEY (RELEASE_ID) REFERENCES G_RELEASE(ID),
    FOREIGN KEY (ARTIST_ID) REFERENCES G_ARTIST(ID)
) ;
CREATE UNIQUE INDEX G_RELEASE_ARTIST_IDX ON G_RELEASE_ARTIST(RELEASE_ID, ARTIST_ID) ;

CREATE TABLE G_TRACK (
    ID IDENTITY,
    MASTER_ID BIGINT,
    TITLE VARCHAR(500) NOT NULL,
    LENGTH INTEGER NOT NULL,
    POSITION VARCHAR(10),
    RELEASE_ID BIGINT,
    RELEASE_GROUP_ID BIGINT,
    FOREIGN KEY (MASTER_ID) REFERENCES (ID),
    FOREIGN KEY (RELEASE_ID) REFERENCES G_RELEASE(ID),
    FOREIGN KEY (RELEASE_GROUP_ID) REFERENCES G_RELEASE_GROUP(ID)
) ;

CREATE TABLE G_TRACK_ARTIST (
    TRACK_ID BIGINT,
    ARTIST_ID BIGINT,
    FOREIGN KEY (TRACK_ID) REFERENCES G_TRACK(ID),
    FOREIGN KEY (ARTIST_ID) REFERENCES G_ARTIST(ID)
) ;
CREATE UNIQUE INDEX G_TRACK_ARTIST_IDX ON G_TRACK_ARTIST(TRACK_ID, ARTIST_ID) ;

CREATE TABLE G_USER (
    ID IDENTITY,
    USERNAME VARCHAR(100) NOT NULL,
    FULLNAME VARCHAR(255),
    EMAIL VARCHAR(100) NOT NULL,
    CREATED TIMESTAMP NOT NULL,
    LAST_ACTIVITY TIMESTAMP NOT NULL
) ;
CREATE UNIQUE INDEX G_USER_USERNAME_IDX ON G_USER(USERNAME) ;
CREATE UNIQUE INDEX G_USER_EMAIL_IDX ON G_USER(EMAIL) ;

CREATE TABLE G_GRAVION (
    ID IDENTITY,
    USER_ID BIGINT NOT NULL,
    TRACK_ID BIGINT NOT NULL,
    BEGIN TIMESTAMP NOT NULL,
    END TIMESTAMP NOT NULL,
    DURATION INTEGER,
    FOREIGN KEY (USER_ID) REFERENCES G_USER(ID),
    FOREIGN KEY (TRACK_ID) REFERENCES G_TRACK(ID)
) ;
