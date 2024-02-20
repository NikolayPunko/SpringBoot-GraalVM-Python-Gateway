create table User_org(
                         id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
                         username varchar(100) NOT NULL ,
                         email varchar(100) UNIQUE NOT NULL ,
                         password varchar NOT NULL,
                         role varchar NOT NULL,
                         GLN numeric(13,0) NOT NULL,
                         last_name varchar(30) NOT NULL default ('unknown'),
                         first_name varchar(30) NOT NULL default ('unknown'),
                         middle_name varchar(30),
                         phone varchar(20),
                         profile_update timestamp,
                         last_login timestamp
);



INSERT INTO User_org(username, email, password, role, GLN) VALUES ('user', 'user@mail.ru', '$2a$10$1zViA2sSYKVimgCJmCEg3uccihxpLc..TRyGjTwctQ0UPXgX6Ayz2', 'ROLE_USER', 1111111111111);
INSERT INTO User_org(username, email, password, role, GLN) VALUES ('savushkin', 'savushkin@mail.ru', '$2a$10$n0Dm4kF3FGilgtfq//POveoRc7fR6hmogW482ifDNKEZ4uDLYMW4W', 'ROLE_USER', 4810268900006);
INSERT INTO User_org(username, email, password, role, GLN) VALUES ('santa', 'santa@mail.ru', '$2a$10$P1Hpux0FR8gYoqyh7ICS6uhxMjrdfJ041MKAe3.zqayJpk.FQDT9y', 'ROLE_USER', 4819003720008);
INSERT INTO User_org(username, email, password, role, GLN) VALUES ('usertest2', 'usertest2@mail.ru', '$2a$10$sw48LF7jTSaxyJb0PwKZc.01IqVWSd74l/.UZiDmzrpAFiweIRvWW', 'ROLE_USER', 2222222222222);

--delete from User_org where username = 'user';

SELECT * FROM User_org;

-- DROP TABLE Person;
-- TRUNCATE TABLE Person;
-- DELETE FROM User_org;

-- ALTER TABLE User_org ADD GLN numeric(13,0) NOT NULL;
ALTER TABLE User_org ADD last_name varchar(30) NOT NULL default ('unknown');
ALTER TABLE User_org ADD first_name varchar(30) NOT NULL default ('unknown');
ALTER TABLE User_org ADD middle_name varchar(30);
ALTER TABLE User_org ADD phone varchar(20);
ALTER TABLE User_org ADD profile_update timestamp;
ALTER TABLE User_org ADD last_login timestamp;

-- Alter table Users rename to User_org;
-- Alter table Person rename to Users;

-- SELECT * FROM pg_tables;

-- ALTER TABLE User_org
--     DROP COLUMN middle_name;

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


SELECT * FROM BD_EDOC;

SELECT (F_GUID, User_id, F_ID, PST, NDE, DT, RECEIVER, SENDER, DTINS, DTUPD) FROM BD_EDOC order by F_GUID;

INSERT INTO BD_EDOC(User_id, F_TM, EDI, TP, PST, NDE, DT, DTDOC, RECEIVER, SENDER) VALUES (12, '1999-12-30T00:00:00', '001', 'PRICAT', 'IMPORTED', '1', '2023-12-29T00:00:00', '2023-12-29T00:00:00', 2222222222222, 1111111111111);

-- ALTER TABLE BD_EDOC
--     ALTER COLUMN F_ID TYPE bigint;
--
-- DELETE FROM BD_EDOC
-- WHERE F_TM BETWEEN '2024-01-01 00:00:00.000' AND '2024-01-23 08:14:00.000';



-- create table test (
--     name varchar,
--     date1 date NULL DEFAULT '1899-12-30T00:00:00',
--     date2 timestamp NULL DEFAULT '1899-12-30T00:00:00',
--     date3 timestamp(3) NULL DEFAULT '1899-12-30T00:00:00'
-- );
--
-- SELECT * FROM test;
-- DROP TABLE test;
-- INSERT INTO test(name) VALUES ('name1');
















-- create table Test(
--                        id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
--                        username varchar(100) NOT NULL ,
--                        email varchar(100) UNIQUE NOT NULL ,
--                        password varchar NOT NULL,
--                        role varchar NOT NULL
-- ) WITH (comression='zlib');
--
-- SELECT version();
--
-- DROP TABLE Test;







-- USE [disp]
-- GO
--
-- /****** Object:  Table [dbo].[BD_EDOC]    Script Date: 22.12.2023 15:43:07 ******/
-- SET ANSI_NULLS ON
-- GO
--
-- SET QUOTED_IDENTIFIER ON
-- GO
--
-- CREATE TABLE [dbo].[BD_EDOC](
--     [F_GUID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
--     [F_ID] [bigint] IDENTITY(1,1) NOT NULL,
--     [F_TM] [timestamp] NOT NULL,
--     [F_DEL] [tinyint] NOT NULL,
--     [EDI] [char](3) NOT NULL,
--     [TP] [char](10) NOT NULL,
--     [PST] [char](30) NULL,
--     [NDE] [char](70) NULL,
--     [DT] [datetime] NOT NULL,
--     [DTDOC] [datetime] NULL,
--     [RECEIVER] [char](70) NULL,
--     [SENDER] [char](70) NULL,
--     [ADDR] [char](250) NULL,
--     [DTDLR] [datetime] NULL,
--     [DOC] [varchar](max) NULL,
--     [ID] [numeric](12, 0) NOT NULL,
--     [PSTN] [char](30) NULL,
--     [DTINS] [datetime] NULL,
--     [DTUPD] [datetime] NULL,
--     CONSTRAINT [F_GUID_BD_EDOC] PRIMARY KEY NONCLUSTERED
-- (
-- [F_GUID] ASC
-- )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
--     ) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
--     GO
--
-- ALTER TABLE [dbo].[BD_EDOC] ADD  DEFAULT (newsequentialid()) FOR [F_GUID]
-- GO
--
-- ALTER TABLE [dbo].[BD_EDOC] ADD  DEFAULT ((0)) FOR [F_DEL]
-- GO
--
-- ALTER TABLE [dbo].[BD_EDOC] ADD  DEFAULT ('') FOR [EDI]
-- GO
--
-- ALTER TABLE [dbo].[BD_EDOC] ADD  DEFAULT ('') FOR [TP]
-- GO
--
-- ALTER TABLE [dbo].[BD_EDOC] ADD  DEFAULT ('1899-12-30T00:00:00') FOR [DT]
-- GO
--
-- ALTER TABLE [dbo].[BD_EDOC] ADD  DEFAULT ('0') FOR [ID]
-- GO


