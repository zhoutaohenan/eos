if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[SMS_MO]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[SMS_MO]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[SMS_MT]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[SMS_MT]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[SMS_MT_TASK]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[SMS_MT_TASK]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[SMS_SENT]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[SMS_SENT]
GO
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[SMS_SENT_TEMP]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[SMS_SENT_TEMP]

GO

CREATE TABLE [dbo].[SMS_MO] (
	[MO_ID] [numeric](18, 0) IDENTITY (1, 1) PRIMARY KEY  ,
	[SOURCEADDR] [varchar] (50)  NOT NULL ,
	[RECEIVETIME] [datetime] NOT NULL default getdate(),
	[CONTENT] [varchar] (200)  NULL ,
	[EXTCODE] [varchar] (20)  NULL ,
	[CHANNEL] [varchar] (50)  NOT NULL ,
	[DESTADDR] [varchar] (50)  NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[SMS_MT] (
	[MT_ID] [numeric](18, 0) IDENTITY (1, 1) PRIMARY KEY  ,
	[SMSID] [varchar] (50) NOT NULL ,
	[DESTADDR] [varchar] (1000)  NOT NULL ,
	[MESSAGE] [varchar] (2000)  NOT NULL ,
	[ISNEEDREPORT] [int] NOT NULL ,
	[CREATE_TIME] [datetime] NOT NULL default getdate(),
	[CHANNEL] [varchar] (50)  NULL ,
	[PRIORTY] [int] NOT NULL ,
	[PRESEND_TIME] [datetime] NOT NULL default getdate(),
	[VALID_TIME] [numeric](18, 0) NULL ,
	[EXTCODE] [varchar] (20)  NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[SMS_MT_TASK] (
	[MT_ID] [numeric](18, 0) NOT NULL PRIMARY KEY, 
	[SMSID] [varchar] (50)  NOT NULL ,
	[DESTADDR] [varchar] (1000)  NOT NULL ,
	[MESSAGE] [varchar] (2000)  NOT NULL ,
	[ISNEEDREPORT] [int] NOT NULL ,
	[CREATE_TIME] [datetime] NOT NULL default getdate(),
	[CHANNEL] [varchar] (50)  NULL ,
	[PRIORTY] [int] NOT NULL ,
	[PRESEND_TIME] [datetime] NOT NULL default getdate(),
	[VALID_TIME] [numeric](18, 0) NULL ,
	[EXTCODE] [varchar] (20)  NULL ,
	[IS_RESEND] [int] NOT NULL default 0
) ON [PRIMARY]
CREATE  index channel_index on SMS_MT_TASK(CHANNEL) 

CREATE  index priority_index on SMS_MT_TASK(PRIORTY) 

CREATE  index presend_index on SMS_MT_TASK(PRESEND_TIME) 

GO

CREATE TABLE [dbo].[SMS_SENT] (
	[SENT_ID] [numeric](18, 0) IDENTITY (1, 1) PRIMARY KEY  not null,
	[MT_ID] [numeric](18, 0) NOT NULL ,
	[SMSID] [varchar] (50)  NOT NULL ,
	[DESTADDR] [varchar] (1000)  NOT NULL ,
	[CREATE_TIME] [datetime] NOT NULL default getdate(),
	[ISNEEDREPORT] [int] NOT NULL ,
	[SEND_FLAG] [int] NOT NULL ,
	[SEND_TIME] [datetime] NOT NULL default getdate(),
	[CHANNEL] [varchar] (50)  NULL ,
	[RESEND_CHANNEL] [varchar] (50)  NULL ,
	[MESSAGE] [varchar] (2000) NOT NULL ,
	[SPLIT_MESSAGE] [varchar] (2000)  NULL ,
	[PK_TOTAL] [int] NOT NULL ,
	[PK_NUMBER] [int] NOT NULL ,
	[ISNEED_RESEND] [int] NULL ,
	[RESEND_STATUS] [int] NULL ,
	[MESSAGE_ID] [varchar] (100)  NULL ,
	[SEND_RET] [int] NULL ,
	[STATUS] [varchar] (50) NULL ,
	[REPORT_TIME] [datetime] NULL ,
	[EXTCODE] [varchar] (20)  NULL ,
	[DESCRIPTION] [varchar] (100)  NULL 
) ON [PRIMARY]
GO
CREATE TABLE [dbo].[SMS_SENT_TEMP] (
  [SENT_TEMP_ID] [numeric](18, 0) IDENTITY (1, 1) PRIMARY KEY  not null,
	[MT_ID] [numeric](18, 0) NOT NULL ,
	[SMSID] [varchar] (50)  NOT NULL ,
	[DESTADDR] [varchar] (1000)  NOT NULL ,
	[CREATE_TIME] [datetime] NOT NULL ,
	[ISNEEDREPORT] [int] NOT NULL ,
	[SEND_FLAG] [int] NOT NULL ,
	[SEND_TIME] [datetime] NOT NULL ,
	[CHANNEL] [varchar] (50)  NULL ,
	[RESEND_CHANNEL] [varchar] (50)  NULL ,
	[MESSAGE] [varchar] (2000) NOT NULL ,
	[SPLIT_MESSAGE] [varchar] (2000)  NULL ,
	[PK_TOTAL] [int] NOT NULL ,
	[PK_NUMBER] [int] NOT NULL ,
	[ISNEED_RESEND] [int] NULL ,
	[RESEND_STATUS] [int] NULL ,
	[MESSAGE_ID] [varchar] (100)  NULL ,
	[SEND_RET] [int] NULL ,
	[STATUS] [varchar] (50) NULL ,
	[REPORT_TIME] [datetime] NULL ,
	[EXTCODE] [varchar] (20)  NULL ,
	[DESCRIPTION] [varchar] (100)  NULL 
) ON [PRIMARY]
GO

CREATE  index Messageid_index on SMS_SENT_TEMP(MESSAGE_ID) 

CREATE  index ISNEED_RESEND_INDEX on SMS_SENT_TEMP(ISNEED_RESEND) 

CREATE  index RESEND_STATUS_INDEX on SMS_SENT_TEMP(RESEND_STATUS) 

CREATE  index RESEND_CHANNEL_INDEX on SMS_SENT_TEMP(RESEND_CHANNEL) 











