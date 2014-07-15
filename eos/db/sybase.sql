
if exists(select 1 from sysobjects where name = 'SMS_MO' and type = 'U')
 drop table SMS_MO
go 
if exists(select 1 from sysobjects where name = 'SMS_MT_TASK' and type = 'U')
 drop table SMS_MT_TASK
go 
if exists(select 1 from sysobjects where name = 'SMS_SENT' and type = 'U')
 drop table SMS_SENT
go 
if exists(select 1 from sysobjects where name = 'SMS_SENT_TEMP' and type = 'U')
 drop table SMS_SENT_TEMP
go 
if exists(select 1 from sysobjects where name = 'SMS_MT' and type = 'U')
 drop table SMS_MT
go 


create table SMS_MT (
	MT_ID                           numeric(15,0)                    identity ,
	SMSID                           varchar(100)                     not null  ,
	DESTADDR                        varchar(1000)                    not null  ,
	MESSAGE                         varchar(2000)                    not null  ,
	ISNEEDREPORT                    int                             DEFAULT  0
  not null  ,
	CREATE_TIME                     datetime                        DEFAULT  getdate()
  not null  ,
	CHANNEL                         varchar(50)                          null  ,
	PRIORTY                         int                             DEFAULT  5
  not null  ,
	PRESEND_TIME                    datetime                        DEFAULT  getdate()
  not null  ,
	VALID_TIME                      numeric(10,0)                         null  ,
	EXTCODE                       varchar(20)                        null   ,
PRIMARY KEY (MT_ID)
)with identity_gap = 1
go

CREATE TABLE SMS_MT_TASK
(
  MT_ID         numeric(15,0)                        not null,
  SMSID          varchar(100)                not null,
  DESTADDR       varchar(1000)             not null,
  MESSAGE        varchar(2000)             not null,
  ISNEEDREPORT   int                          DEFAULT 0                    not null,
  CREATE_TIME   datetime                             DEFAULT getdate()               not null,
  CHANNEL       varchar(50)      null,
  PRIORTY        int                           DEFAULT 5                    not null,
  PRESEND_TIME   datetime                        DEFAULT  getdate()              not null,
  VALID_TIME                      numeric(10,0)                         null  ,
  EXTCODE                         varchar(10)                         null   ,
  IS_RESEND    int  default 0   not null,
  PRIMARY KEY (MT_ID)
) 

go

CREATE TABLE SMS_SENT
(
  SENT_ID        numeric(15,0)                    identity ,
  MT_ID          numeric(15,0)                      not null,
  SMSID           varchar(100)             not null,
  DESTADDR        varchar(1000)             not null,
  CREATE_TIME     datetime                        DEFAULT  getdate()   not null,
  ISNEEDREPORT    int                        not null,
  SEND_FLAG       int                     not null,
  SEND_TIME       datetime                        DEFAULT  getdate()   not null,
  CHANNEL          varchar(50) null,
  RESEND_CHANNEL   varchar(50) null,
  MESSAGE         varchar(2000)              not null,
  SPLIT_MESSAGE   varchar(2000) null,            
  PK_TOTAL        numeric(3,0)                       not null,
  PK_NUMBER      numeric(3,0)                       not null,
  ISNEED_RESEND   int null,
  RESEND_STATUS   int null,
  MESSAGE_ID      varchar(100) null,
  SEND_RET        int null,
  STATUS          varchar(20) null,
  REPORT_TIME    datetime null,
  DESCRIPTION     varchar(150) null, 
  EXTCODE         varchar(20)  null,
  PRIMARY KEY ( SENT_ID ) 
) with identity_gap = 1
go

CREATE TABLE SMS_SENT_TEMP
(
  SENT_TEMP_ID        numeric(15,0)                    identity ,
  MT_ID          numeric(15,0)                      not null,
  SMSID           varchar(100)             not null,
  DESTADDR        varchar(1000)             not null,
  CREATE_TIME     datetime                        DEFAULT  getdate()   not null,
  ISNEEDREPORT    int                        not null,
  SEND_FLAG       int                     not null,
  SEND_TIME       datetime                        DEFAULT  getdate()   not null,
  CHANNEL          varchar(50) null,
  RESEND_CHANNEL   varchar(50) null,
  MESSAGE         varchar(2000)              not null,
  SPLIT_MESSAGE   varchar(2000) null,            
  PK_TOTAL        numeric(3,0)                       not null,
  PK_NUMBER      numeric(3,0)                       not null,
  ISNEED_RESEND   int null,
  RESEND_STATUS   int null,
  MESSAGE_ID      varchar(100) null,
  SEND_RET        int null,
  STATUS          varchar(20) null,
  REPORT_TIME    datetime null,
  DESCRIPTION     varchar(150) null,
  EXTCODE         varchar(20)  null,
   PRIMARY KEY ( SENT_TEMP_ID ) 
)lock datarows with identity_gap = 1
go
CREATE TABLE SMS_MO
(
  MO_ID        numeric(15,0)                    identity ,
  SOURCEADDR   varchar(50)                not null,
  RECEIVETIME  datetime                        DEFAULT  getdate()   not null,
  CONTENT      varchar(400)  null,
  EXTCODE      varchar(30)  null,
  CHANNEL      varchar(50)               not null,
  DESTADDR     varchar(50)  null,
  PRIMARY KEY ( MO_ID ) 
) with identity_gap = 1

go
CREATE  index mid_index on SMS_SENT_TEMP(MESSAGE_ID) 
go
CREATE  index channel_index on SMS_MT_TASK(CHANNEL) 
go
CREATE  index priority_index on SMS_MT_TASK(PRIORTY) 
