/*
MySQL Data Transfer
Source Host: localhost
Source Database: esk2
Target Host: localhost
Target Database: esk2
Date: 2010-9-26 ÉÏÎç 11:05:45
*/
DROP TABLE IF EXISTS SMS_MO;
DROP TABLE IF EXISTS SMS_MT;
DROP TABLE IF EXISTS SMS_MT_TASK;
DROP TABLE IF EXISTS SMS_SENT;
DROP TABLE IF EXISTS SMS_SENT_TEMP;


SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for sms_mo
-- ----------------------------
CREATE TABLE `SMS_MO` (
  `MO_ID` bigint(20) NOT NULL auto_increment,
  `SOURCEADDR` varbinary(50) NOT NULL,
  `RECEIVETIME` datetime NOT NULL,
  `CONTENT` varchar(200) character set utf8 default NULL,
  `EXTCODE` varchar(30) collate utf8_unicode_ci default NULL,
  `CHANNEL` varchar(30) collate utf8_unicode_ci NOT NULL,
  `DESTADDR` varchar(50) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`MO_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for sms_mt
-- ----------------------------
CREATE TABLE `SMS_MT` (
  `MT_ID` bigint(20) NOT NULL auto_increment,
  `SMSID` varchar(50) collate utf8_unicode_ci NOT NULL,
  `DESTADDR` varchar(1000) collate utf8_unicode_ci NOT NULL,
  `MESSAGE` varchar(2000) collate utf8_unicode_ci NOT NULL,
  `ISNEEDREPORT` int(11) NOT NULL default '0',
  `CREATE_TIME` datetime NOT NULL,
  `CHANNEL` varchar(50) collate utf8_unicode_ci default NULL,
  `PRIORTY` int(11) NOT NULL default '5',
  `PRESEND_TIME` datetime NOT NULL,
  `VALID_TIME` int(11) default NULL,
  `EXTCODE` varchar(20) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`MT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for sms_mt_task
-- ----------------------------
CREATE TABLE `SMS_MT_TASK` (
  `MT_ID` bigint(20) NOT NULL,
  `SMSID` varchar(50) collate utf8_unicode_ci NOT NULL,
  `DESTADDR` varchar(1000) collate utf8_unicode_ci NOT NULL,
  `MESSAGE` varchar(2000) collate utf8_unicode_ci NOT NULL,
  `ISNEEDREPORT` int(11) NOT NULL default '0',
  `CREATE_TIME` datetime NOT NULL,
  `CHANNEL` varchar(50) collate utf8_unicode_ci default NULL,
  `PRIORTY` int(11) NOT NULL default '5',
  `PRESEND_TIME` datetime NOT NULL,
  `VALID_TIME` int(11) default NULL,
  `EXTCODE` varchar(20) collate utf8_unicode_ci default NULL,
  `IS_RESEND` int(11) default '0',
  PRIMARY KEY  (`MT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for sms_sent
-- ----------------------------
CREATE TABLE `SMS_SENT` (
  `SENT_ID` bigint(20) NOT NULL auto_increment,
  `MT_ID` bigint(20) NOT NULL,
  `SMSID` varchar(50) collate utf8_unicode_ci NOT NULL,
  `DESTADDR` varchar(2000) collate utf8_unicode_ci NOT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `ISNEEDREPORT` int(11) NOT NULL,
  `SEND_FLAG` int(11) NOT NULL,
  `SEND_TIME` datetime default NULL,
  `CHANNEL` varchar(50) collate utf8_unicode_ci default NULL,
  `RESEND_CHANNEL` varchar(50) collate utf8_unicode_ci default NULL,
  `MESSAGE` varchar(2000) collate utf8_unicode_ci NOT NULL,
  `SPLIT_MESSAGE` varchar(2000) collate utf8_unicode_ci NOT NULL,
  `PK_TOTAL` int(11) NOT NULL,
  `PK_NUMBER` int(11) NOT NULL,
  `ISNEED_RESEND` int(11) default NULL,
  `RESEND_STATUS` int(11) default NULL,
  `MESSAGE_ID` varchar(50) collate utf8_unicode_ci default NULL,
  `SEND_RET` int(11) default NULL,
  `STATUS` varchar(255) collate utf8_unicode_ci default NULL,
  `REPORT_TIME` datetime default NULL,
  `EXTCODE` varchar(20) collate utf8_unicode_ci default NULL,
  `DESCRIPTION` varchar(100) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`SENT_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=12559 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for sms_sent_temp
-- ----------------------------
CREATE TABLE `SMS_SENT_TEMP` (
  `SENT_TEMP_ID` bigint(20) NOT NULL auto_increment,
  `MT_ID` bigint(20) NOT NULL,
  `SMSID` varchar(50) collate utf8_unicode_ci NOT NULL,
  `DESTADDR` varchar(2000) collate utf8_unicode_ci NOT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `ISNEEDREPORT` int(11) NOT NULL,
  `SEND_FLAG` int(11) NOT NULL,
  `SEND_TIME` datetime NOT NULL,
  `CHANNEL` varchar(50) collate utf8_unicode_ci default NULL,
  `RESEND_CHANNEL` varchar(50) collate utf8_unicode_ci default NULL,
  `MESSAGE` varchar(2000) collate utf8_unicode_ci NOT NULL,
  `SPLIT_MESSAGE` varchar(2000) collate utf8_unicode_ci NOT NULL,
  `PK_TOTAL` int(11) NOT NULL,
  `PK_NUMBER` int(11) NOT NULL,
  `ISNEED_RESEND` int(11) default NULL,
  `RESEND_STATUS` int(11) default NULL,
  `MESSAGE_ID` varchar(50) collate utf8_unicode_ci default NULL,
  `SEND_RET` int(11) default NULL,
  `STATUS` varchar(255) collate utf8_unicode_ci default NULL,
  `REPORT_TIME` datetime default NULL,
  `DESCRIPTION` varchar(100) collate utf8_unicode_ci default NULL,
  `EXTCODE` varchar(20) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`SENT_TEMP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE  index Messageid_index on SMS_SENT_TEMP(MESSAGE_ID) ;

CREATE  index channel_index on SMS_MT_TASK(CHANNEL) ;

CREATE  index priority_index on SMS_MT_TASK(PRIORTY) ;
-- ----------------------------
-- Records 
-- ----------------------------
