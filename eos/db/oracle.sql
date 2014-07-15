DROP TABLE SMS_MO CASCADE CONSTRAINTS;
CREATE TABLE SMS_MO
(
  MO_ID        NUMBER                           NOT NULL,
  SOURCEADDR   VARCHAR2(50 BYTE)                NOT NULL,
  RECEIVETIME  DATE     DEFAULT sysdate               NOT NULL ,
  CONTENT      VARCHAR2(400 BYTE),
  EXTCODE      VARCHAR2(30 BYTE),
  CHANNEL      VARCHAR2(50 BYTE)                NOT NULL,
  DESTADDR     VARCHAR2(50 BYTE),
 PRIMARY KEY ( MO_ID ) 
);

drop sequence MO_ID;
create sequence MO_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;


DROP TABLE SMS_MT CASCADE CONSTRAINTS;
CREATE TABLE SMS_MT
(
  MT_ID         NUMBER                          NOT NULL,
  SMSID         VARCHAR2(100 BYTE)               NOT NULL,
  DESTADDR      VARCHAR2(2000 BYTE)             NOT NULL,
  MESSAGE       VARCHAR2(4000 BYTE)             NOT NULL,
  ISNEEDREPORT  NUMBER(1)                          DEFAULT 0                     NOT NULL,
  CREATE_TIME   DATE                            DEFAULT sysdate               NOT NULL,
  CHANNEL       VARCHAR2(50 BYTE),
  PRIORTY       NUMBER(1)                          DEFAULT 5                     NOT NULL,
  PRESEND_TIME  DATE                            DEFAULT sysdate               NOT NULL,
  VALID_TIME    NUMBER,
  EXTCODE       VARCHAR2(50 BYTE),
  PRIMARY KEY ( MT_ID ) 
);

drop sequence MT_ID;
create sequence MT_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;


DROP TABLE SMS_MT_TASK CASCADE CONSTRAINTS;
CREATE TABLE SMS_MT_TASK
(
  MT_ID         NUMBER                          NOT NULL,
  SMSID         VARCHAR2(100 BYTE)               NOT NULL,
  DESTADDR      VARCHAR2(2000 BYTE)             NOT NULL,
  MESSAGE       VARCHAR2(4000 BYTE)             NOT NULL,
  ISNEEDREPORT  NUMBER                          DEFAULT 0                     NOT NULL,
  CREATE_TIME   DATE                            DEFAULT sysdate               NOT NULL,
  CHANNEL       VARCHAR2(50 BYTE),
  PRIORTY       NUMBER(1)                          DEFAULT 5                     NOT NULL,
  PRESEND_TIME  DATE                            DEFAULT sysdate               NOT NULL,
  VALID_TIME    NUMBER,
  EXTCODE       VARCHAR2(50 BYTE),
 IS_RESEND    NUMBER default 0 not null
);



DROP TABLE SMS_SENT CASCADE CONSTRAINTS;
CREATE TABLE SMS_SENT
(
  SENT_ID         NUMBER                        NOT NULL,
  MT_ID           NUMBER                        NOT NULL,
  SMSID           VARCHAR2(100 BYTE)             NOT NULL,
  DESTADDR        VARCHAR2(2000 BYTE)             NOT NULL,
  CREATE_TIME     DATE                         DEFAULT sysdate    NOT NULL,
  ISNEEDREPORT    NUMBER(1)                        NOT NULL,
  SEND_FLAG       NUMBER (1)                       NOT NULL,
  SEND_TIME       DATE                          DEFAULT sysdate   NOT NULL,
  CHANNEL         VARCHAR2(50 BYTE),
  RESEND_CHANNEL  VARCHAR2(50 BYTE),
  MESSAGE         VARCHAR2(4000 BYTE)           NOT NULL,
  SPLIT_MESSAGE   VARCHAR2(4000 BYTE),            
  PK_TOTAL        NUMBER(4)                        NOT NULL,
  PK_NUMBER       NUMBER(4)                        NOT NULL,
  ISNEED_RESEND   NUMBER(1),
  RESEND_STATUS   NUMBER(1),
  MESSAGE_ID      VARCHAR2(100 BYTE),
  SEND_RET        NUMBER,
  STATUS          VARCHAR2(40 BYTE),
  REPORT_TIME     DATE,
  DESCRIPTION     VARCHAR2(150 BYTE),
  EXTCODE       VARCHAR2(50 BYTE),
  PRIMARY KEY ( SENT_ID ) 
);


drop sequence SENT_ID;
create sequence SENT_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;


comment on column SMS_MT.MT_ID
  is '主键，自增长';
comment on column SMS_MT.SMSID
  is '客户写入的短信ID标识';
comment on column SMS_MT.DESTADDR
  is '接收手机号码，暂时支持一个号码';
comment on column SMS_MT.MESSAGE
  is '短信内容';
comment on column SMS_MT.ISNEEDREPORT
  is '是否需要状态报告';
comment on column SMS_MT.CREATE_TIME
  is '入库时间，自动生成';
comment on column SMS_MT.CHANNEL
  is '指定发送通道';
comment on column SMS_MT.PRIORTY
  is '优先级，数值范围0~9，数值越小，优先级越高';
comment on column SMS_MT.PRESEND_TIME
  is '定时发送时间，默认自动生成当前时间';
comment on column SMS_MT.VALID_TIME
  is '有效期从入库算起短信存活期，多长时间发布出去，则不再发送，(单位：秒)';
comment on column SMS_MT.EXTCODE
  is '扩展字段';


  -- Add comments to the columns 
comment on column SMS_SENT.SENT_ID
  is '自动增长ID';
comment on column SMS_SENT.MT_ID
  is '对应 SMS_MT 表中的 ID';
comment on column SMS_SENT.SMSID
  is '客户提交短信时生成的ID';
comment on column SMS_SENT.DESTADDR
  is '手机号码';
comment on column SMS_SENT.CREATE_TIME
  is '入库时间，从SMS_MT 带来';
comment on column SMS_SENT.ISNEEDREPORT
  is '是否需要状态报告
0:不需要
1:需要
';
comment on column SMS_SENT.SEND_FLAG
  is '提交到网关响应
0:成功
1:失败
';
comment on column SMS_SENT.SEND_TIME
  is '提交到网关的时间';
comment on column SMS_SENT.CHANNEL
  is '发送所走的通道';
comment on column SMS_SENT.RESEND_CHANNEL
  is '重发所走的通道';
comment on column SMS_SENT.MESSAGE
  is '短信内容';
comment on column SMS_SENT.SPLIT_MESSAGE
  is '拆分后的内容';
comment on column SMS_SENT.PK_TOTAL
  is '同一条长短信拆分后的条数';
comment on column SMS_SENT.PK_NUMBER
  is '处于拆分后的第几条';
comment on column SMS_SENT.ISNEED_RESEND
  is '0：不需要重发
1：需要重发
';
comment on column SMS_SENT.RESEND_STATUS
  is 'Isneedresend 为1时此字段有效
1：失败已重发
2：失败未重发
';
comment on column SMS_SENT.MESSAGE_ID
  is '提交到网关返回的ID';
comment on column SMS_SENT.SEND_RET
  is '提交到网关的返回结果';
comment on column SMS_SENT.STATUS
  is '如果用户需要状态报告，发送成功后状态报告保存在此字段';
comment on column SMS_SENT.REPORT_TIME
  is '收到状态报告的时间';
comment on column SMS_SENT.DESCRIPTION
  is '描述';


  -- Add comments to the columns 
comment on column SMS_MT_TASK.MT_ID
  is '对应 SMS_MT表 中的 MT_ID';
comment on column SMS_MT_TASK.SMSID
  is '客户写入的短信ID标标识';
comment on column SMS_MT_TASK.DESTADDR
  is '接受手机号码，暂时支持一个手机号码。';
comment on column SMS_MT_TASK.MESSAGE
  is '短信内容';
comment on column SMS_MT_TASK.ISNEEDREPORT
  is '是否需要状态报告
0:不需要
1:需要
';
comment on column SMS_MT_TASK.CREATE_TIME
  is '入库时间(短信发送请求时间，数据库默认当前时间)';
comment on column SMS_MT_TASK.CHANNEL
  is '路由后发送的通道,可以多个,以”,”隔开';
comment on column SMS_MT_TASK.PRIORTY
  is '优先级，数值范围0~9，数值越小，优先级越高';
comment on column SMS_MT_TASK.PRESEND_TIME
  is '定时发送时间';
comment on column SMS_MT_TASK.VALID_TIME
  is '有效期从入库算起短信存活期，多长时间发布出去，则不再发送，(单位：秒)';
comment on column SMS_MT_TASK.IS_RESEND
  is 'IS_RESEND';


COMMENT ON COLUMN SMS_MO.MO_ID IS '自动增长ID';

COMMENT ON COLUMN SMS_MO.SOURCEADDR IS '源号码';

COMMENT ON COLUMN SMS_MO.RECEIVETIME IS '收到MO信息时间';

COMMENT ON COLUMN SMS_MO.CONTENT IS 'MO信息内容';

COMMENT ON COLUMN SMS_MO.EXTCODE IS '扩展码';

COMMENT ON COLUMN SMS_MO.CHANNEL IS '通道';

COMMENT ON COLUMN SMS_MO.DESTADDR IS '目的号码';





DROP TABLE SMS_SENT_TEMP CASCADE CONSTRAINTS;
CREATE TABLE SMS_SENT_TEMP
(
  SENT_TEMP_ID         NUMBER                        NOT NULL,
  MT_ID           NUMBER                        NOT NULL,
  SMSID           VARCHAR2(100 BYTE)             NOT NULL,
  DESTADDR        VARCHAR2(2000 BYTE)             NOT NULL,
  CREATE_TIME     DATE                         DEFAULT sysdate    NOT NULL,
  ISNEEDREPORT    NUMBER(1)                        NOT NULL,
  SEND_FLAG       NUMBER (1)                       NOT NULL,
  SEND_TIME       DATE                          DEFAULT sysdate   NOT NULL,
  CHANNEL         VARCHAR2(50 BYTE),
  RESEND_CHANNEL  VARCHAR2(50 BYTE),
  MESSAGE         VARCHAR2(4000 BYTE)           NOT NULL,
  SPLIT_MESSAGE   VARCHAR2(4000 BYTE),            
  PK_TOTAL        NUMBER(4)                        NOT NULL,
  PK_NUMBER       NUMBER(4)                        NOT NULL,
  ISNEED_RESEND   NUMBER(1),
  RESEND_STATUS   NUMBER(1),
  MESSAGE_ID      VARCHAR2(100 BYTE),
  SEND_RET        NUMBER,
  STATUS          VARCHAR2(40 BYTE),
  REPORT_TIME     DATE,
  DESCRIPTION     VARCHAR2(150 BYTE),
  EXTCODE       VARCHAR2(50 BYTE),
  PRIMARY KEY ( SENT_TEMP_ID ) 
);
drop sequence SENT_TEMP_ID;
create sequence SENT_TEMP_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

create index mid_index on SMS_SENT_TEMP(MESSAGE_ID);

create index channel_index on SMS_MT_TASK(CHANNEL,MT_ID);

CREATE  index priority_index on SMS_MT_TASK(PRIORTY);

DROP TABLE SMS_STAT CASCADE CONSTRAINTS;
CREATE TABLE SMS_STAT
(
  STAT_ID       INTEGER NOT NULL,
  ORGID         INTEGER NOT NULL, 
  USERID        INTEGER NOT NULL,
  CHANNEL_NAME  VARCHAR2(20 BYTE),
  SUCCESS_NUM   INTEGER                         DEFAULT 0,
  FAIL_NUM      INTEGER                         DEFAULT 0,
  NO_REPORTNUM  INTEGER                         DEFAULT 0,
  STAT_TIME     VARCHAR2(50 BYTE) NOT NULL,
  CREATE_TIME   DATE                            DEFAULT sysdate,
  MO_NUM        INTEGER                         DEFAULT 0,
  SMS_TOTAL     INTEGER                         DEFAULT 0
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;

COMMENT ON COLUMN SMS_STAT.STAT_ID IS '统计ID';

COMMENT ON COLUMN SMS_STAT.ORGID IS '机构ID';

COMMENT ON COLUMN SMS_STAT.USERID IS '用户ID';

COMMENT ON COLUMN SMS_STAT.CHANNEL_NAME IS '通道名';

COMMENT ON COLUMN SMS_STAT.SUCCESS_NUM IS '成功总条数';

COMMENT ON COLUMN SMS_STAT.FAIL_NUM IS '失败总条数';

COMMENT ON COLUMN SMS_STAT.NO_REPORTNUM IS '状态未知';

COMMENT ON COLUMN SMS_STAT.STAT_TIME IS '统计时间';

COMMENT ON COLUMN SMS_STAT.CREATE_TIME IS '创建时间';

COMMENT ON COLUMN SMS_STAT.MO_NUM IS 'MO总数';

COMMENT ON COLUMN SMS_STAT.SMS_TOTAL IS '发送总数';

