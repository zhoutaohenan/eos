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
  is '������������';
comment on column SMS_MT.SMSID
  is '�ͻ�д��Ķ���ID��ʶ';
comment on column SMS_MT.DESTADDR
  is '�����ֻ����룬��ʱ֧��һ������';
comment on column SMS_MT.MESSAGE
  is '��������';
comment on column SMS_MT.ISNEEDREPORT
  is '�Ƿ���Ҫ״̬����';
comment on column SMS_MT.CREATE_TIME
  is '���ʱ�䣬�Զ�����';
comment on column SMS_MT.CHANNEL
  is 'ָ������ͨ��';
comment on column SMS_MT.PRIORTY
  is '���ȼ�����ֵ��Χ0~9����ֵԽС�����ȼ�Խ��';
comment on column SMS_MT.PRESEND_TIME
  is '��ʱ����ʱ�䣬Ĭ���Զ����ɵ�ǰʱ��';
comment on column SMS_MT.VALID_TIME
  is '��Ч�ڴ����������Ŵ���ڣ��೤ʱ�䷢����ȥ�����ٷ��ͣ�(��λ����)';
comment on column SMS_MT.EXTCODE
  is '��չ�ֶ�';


  -- Add comments to the columns 
comment on column SMS_SENT.SENT_ID
  is '�Զ�����ID';
comment on column SMS_SENT.MT_ID
  is '��Ӧ SMS_MT ���е� ID';
comment on column SMS_SENT.SMSID
  is '�ͻ��ύ����ʱ���ɵ�ID';
comment on column SMS_SENT.DESTADDR
  is '�ֻ�����';
comment on column SMS_SENT.CREATE_TIME
  is '���ʱ�䣬��SMS_MT ����';
comment on column SMS_SENT.ISNEEDREPORT
  is '�Ƿ���Ҫ״̬����
0:����Ҫ
1:��Ҫ
';
comment on column SMS_SENT.SEND_FLAG
  is '�ύ��������Ӧ
0:�ɹ�
1:ʧ��
';
comment on column SMS_SENT.SEND_TIME
  is '�ύ�����ص�ʱ��';
comment on column SMS_SENT.CHANNEL
  is '�������ߵ�ͨ��';
comment on column SMS_SENT.RESEND_CHANNEL
  is '�ط����ߵ�ͨ��';
comment on column SMS_SENT.MESSAGE
  is '��������';
comment on column SMS_SENT.SPLIT_MESSAGE
  is '��ֺ������';
comment on column SMS_SENT.PK_TOTAL
  is 'ͬһ�������Ų�ֺ������';
comment on column SMS_SENT.PK_NUMBER
  is '���ڲ�ֺ�ĵڼ���';
comment on column SMS_SENT.ISNEED_RESEND
  is '0������Ҫ�ط�
1����Ҫ�ط�
';
comment on column SMS_SENT.RESEND_STATUS
  is 'Isneedresend Ϊ1ʱ���ֶ���Ч
1��ʧ�����ط�
2��ʧ��δ�ط�
';
comment on column SMS_SENT.MESSAGE_ID
  is '�ύ�����ط��ص�ID';
comment on column SMS_SENT.SEND_RET
  is '�ύ�����صķ��ؽ��';
comment on column SMS_SENT.STATUS
  is '����û���Ҫ״̬���棬���ͳɹ���״̬���汣���ڴ��ֶ�';
comment on column SMS_SENT.REPORT_TIME
  is '�յ�״̬�����ʱ��';
comment on column SMS_SENT.DESCRIPTION
  is '����';


  -- Add comments to the columns 
comment on column SMS_MT_TASK.MT_ID
  is '��Ӧ SMS_MT�� �е� MT_ID';
comment on column SMS_MT_TASK.SMSID
  is '�ͻ�д��Ķ���ID���ʶ';
comment on column SMS_MT_TASK.DESTADDR
  is '�����ֻ����룬��ʱ֧��һ���ֻ����롣';
comment on column SMS_MT_TASK.MESSAGE
  is '��������';
comment on column SMS_MT_TASK.ISNEEDREPORT
  is '�Ƿ���Ҫ״̬����
0:����Ҫ
1:��Ҫ
';
comment on column SMS_MT_TASK.CREATE_TIME
  is '���ʱ��(���ŷ�������ʱ�䣬���ݿ�Ĭ�ϵ�ǰʱ��)';
comment on column SMS_MT_TASK.CHANNEL
  is '·�ɺ��͵�ͨ��,���Զ��,�ԡ�,������';
comment on column SMS_MT_TASK.PRIORTY
  is '���ȼ�����ֵ��Χ0~9����ֵԽС�����ȼ�Խ��';
comment on column SMS_MT_TASK.PRESEND_TIME
  is '��ʱ����ʱ��';
comment on column SMS_MT_TASK.VALID_TIME
  is '��Ч�ڴ����������Ŵ���ڣ��೤ʱ�䷢����ȥ�����ٷ��ͣ�(��λ����)';
comment on column SMS_MT_TASK.IS_RESEND
  is 'IS_RESEND';


COMMENT ON COLUMN SMS_MO.MO_ID IS '�Զ�����ID';

COMMENT ON COLUMN SMS_MO.SOURCEADDR IS 'Դ����';

COMMENT ON COLUMN SMS_MO.RECEIVETIME IS '�յ�MO��Ϣʱ��';

COMMENT ON COLUMN SMS_MO.CONTENT IS 'MO��Ϣ����';

COMMENT ON COLUMN SMS_MO.EXTCODE IS '��չ��';

COMMENT ON COLUMN SMS_MO.CHANNEL IS 'ͨ��';

COMMENT ON COLUMN SMS_MO.DESTADDR IS 'Ŀ�ĺ���';





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

COMMENT ON COLUMN SMS_STAT.STAT_ID IS 'ͳ��ID';

COMMENT ON COLUMN SMS_STAT.ORGID IS '����ID';

COMMENT ON COLUMN SMS_STAT.USERID IS '�û�ID';

COMMENT ON COLUMN SMS_STAT.CHANNEL_NAME IS 'ͨ����';

COMMENT ON COLUMN SMS_STAT.SUCCESS_NUM IS '�ɹ�������';

COMMENT ON COLUMN SMS_STAT.FAIL_NUM IS 'ʧ��������';

COMMENT ON COLUMN SMS_STAT.NO_REPORTNUM IS '״̬δ֪';

COMMENT ON COLUMN SMS_STAT.STAT_TIME IS 'ͳ��ʱ��';

COMMENT ON COLUMN SMS_STAT.CREATE_TIME IS '����ʱ��';

COMMENT ON COLUMN SMS_STAT.MO_NUM IS 'MO����';

COMMENT ON COLUMN SMS_STAT.SMS_TOTAL IS '��������';

