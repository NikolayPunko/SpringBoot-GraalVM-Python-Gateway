create table User_org(
                         id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
                         username varchar(100) NOT NULL ,
                         email varchar(100) UNIQUE NOT NULL ,
                         password varchar NOT NULL,
                         role varchar NOT NULL,
                         GLN numeric(13,0) NOT NULL,
                         lastName varchar(30) NOT NULL default ('unknown'),
                         firstName varchar(30) NOT NULL default ('unknown'),
                         middleName varchar(30),
                         phone varchar(20),
                         profileUpdate timestamp,
                         lastLogin timestamp
);

INSERT INTO User_org(username, email, password, role, GLN) VALUES ('user', 'user@mail.ru', '$2a$10$1zViA2sSYKVimgCJmCEg3uccihxpLc..TRyGjTwctQ0UPXgX6Ayz2', 'ROLE_USER', 1111111111111);
INSERT INTO User_org(username, email, password, role, GLN) VALUES ('savushkin', 'savushkin@mail.ru', '$2a$10$n0Dm4kF3FGilgtfq//POveoRc7fR6hmogW482ifDNKEZ4uDLYMW4W', 'ROLE_USER', 4810268900006);
INSERT INTO User_org(username, email, password, role, GLN) VALUES ('santa', 'santa@mail.ru', '$2a$10$P1Hpux0FR8gYoqyh7ICS6uhxMjrdfJ041MKAe3.zqayJpk.FQDT9y', 'ROLE_USER', 4819003720008);
INSERT INTO User_org(username, email, password, role, GLN) VALUES ('usertest2', 'usertest2@mail.ru', '$2a$10$sw48LF7jTSaxyJb0PwKZc.01IqVWSd74l/.UZiDmzrpAFiweIRvWW', 'ROLE_USER', 2222222222222);



--delete from User_org where username = 'user'

SELECT * FROM User_org;

-- DROP TABLE Person;
-- TRUNCATE TABLE Person;
-- DELETE FROM User_org;

-- ALTER TABLE User_org ADD GLN numeric(13,0) NOT NULL;
ALTER TABLE User_org ADD lastName varchar(30) NOT NULL;
ALTER TABLE User_org ADD firstName varchar(30) NOT NULL;
ALTER TABLE User_org ADD middleName varchar(30);
ALTER TABLE User_org ADD phone varchar(20);
ALTER TABLE User_org ADD profileUpdate timestamp;
ALTER TABLE User_org ADD lastLogin timestamp;


-- Alter table Users rename to User_org;
-- Alter table Person rename to Users;

-- SELECT * FROM pg_tables;


create table BD_EDOC(
                        F_GUID bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
                        User_id int NULL ,
                        F_ID bigint GENERATED BY DEFAULT AS IDENTITY ,
                        F_TM timestamp NOT NULL ,
                        F_DEL smallint NOT NULL DEFAULT (0) ,
                        EDI varchar(3) NOT NULL DEFAULT ('') ,
                        TP varchar(10) NOT NULL DEFAULT ('') ,
                        PST varchar(30) NULL ,
                        NDE varchar(70) NULL ,
                        DT timestamp NOT NULL DEFAULT ('1899-12-30T00:00:00') ,
                        DTDOC timestamp NOT NULL ,
                        RECEIVER numeric(13,0) NULL ,
                        SENDER numeric(13,0) NULL ,
                        DOC text NULL ,
                        ID numeric(12,0) NOT NULL DEFAULT (0),
                        DTINS timestamp NULL ,
                        DTUPD timestamp NULL
);

--drop table BD_EDOC;

SELECT * FROM BD_EDOC;

SELECT (F_GUID, User_id, F_ID, PST, DT, RECEIVER, SENDER) FROM BD_EDOC;

INSERT INTO BD_EDOC(User_id, F_TM, DTDOC, RECEIVER, SENDER ) VALUES (5, '1899-12-30T00:00:00', '1899-12-30T00:00:00', 1111111111111, 1111111111111);
INSERT INTO BD_EDOC(User_id, F_TM, EDI, TP, PST, NDE, DT, DTDOC, RECEIVER, SENDER) VALUES (12, '1999-12-30T00:00:00', '001', 'PRICAT', 'IMPORTED', '1', '2023-12-29T00:00:00', '2023-12-29T00:00:00', 2222222222222, 1111111111111);

