create table User_org(
                         id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
                         username varchar(100) NOT NULL ,
                         email varchar(100) UNIQUE NOT NULL ,
                         password varchar NOT NULL,
                         role varchar NOT NULL,
                         GLN numeric(13,0) NOT NULL
);



INSERT INTO User_org(username, email, password, role, GLN) VALUES ('user', 'user@mail.ru', '$2a$10$DpWWTJyZFQ7XuAVBlb5YXOPtJEc8QNqWI1/SF3Vhe87Z4Mhiy4uCy', 'ROLE_USER', 1111111111111);

SELECT * FROM User_org;

-- DROP TABLE Person;
-- TRUNCATE TABLE Person;
-- DELETE FROM User_org;

-- ALTER TABLE User_org ADD GLN numeric(13,0) NOT NULL;

-- Alter table Users rename to User_org;
-- Alter table Person rename to Users;

-- SELECT * FROM pg_tables;


create table BD_EDOC(
                        F_GUID bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
                        User_id int NULL REFERENCES User_org(id),
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

INSERT INTO BD_EDOC(User_id, F_TM, DTDOC, RECEIVER, SENDER ) VALUES (5, '1899-12-30T00:00:00', '1899-12-30T00:00:00', 1111111111111, 1111111111111);

