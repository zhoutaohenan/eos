package com.surge.engine.dbadapter.config;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.surge.engine.sms.conf.Config;
import com.surge.engine.util.TimeUtil;
import com.surge.engine.util.XmlUtils;

/**
 * ���ݿ�������Ϣ
 * 
 * @description
 * @project: esk
 * @Date:2010-9-2
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class DbConfig
{
	private static final Logger logger = Logger.getLogger(DbConfig.class);

	private static final String CONFIG_FILE = "config/dbconfig.xml";

	/** ת�Ʋ�ѯSQL **/
	public String querySql;

	/** ����MO��SQL **/
	public String moSql;

	/** �޷���ͨ����ֱ�Ӳ���SMS_SENT��SQL **/
	public String errInsertSql;

	/** ��Ӧ��������SMS_SENT_TEMP��SQL **/
	public String insertSql;

	/** ʧ���ط���ѯSQL **/
	public String failSmsSql;

	/** ת�Ƶ�SMS_SENT��SQL **/
	public String insertSentSql;

	/** ��ѯSMS_SENT_TEMP���з���ת��������SQL **/
	public String queryTempSql;

	/** ת�Ƴ���ʱ��SMS_SENT_TEMP���е����� **/
	 String queryExpireSql;

	public String hibernate_Config_File;

	/** ���ݿ���� **/
	public String dbcode;

	/** ϵͳ���� **/
	public String syscode;

	/** ��ѯ�û�������ID **/
	public String queryIDSql;
	
	/**ͨ���쳣ʱ����������**/
	public String errorQueueInsert;

	public static DbConfig instance = new DbConfig();

	private boolean oracle_run;

	private boolean sybase_run;

	private boolean sqlserver_run;

	private boolean mySql_run;

	private Element dbElement;

	private DbConfig()
	{
	}

	@SuppressWarnings("deprecation")
	public void init() throws Exception
	{
		URL url = DbConfig.class.getClassLoader().getResource(CONFIG_FILE);
		if (url == null)
		{
			try
			{
				url = new File(CONFIG_FILE).toURL();
			} catch (MalformedURLException e)
			{
				Logger.getLogger(Config.class).error("", e);
				throw e;
			}
		}

		SAXBuilder builder = new SAXBuilder();
		try
		{
			InputStream is = url.openStream();
			Document document = builder.build(is);
			Element db = document.getRootElement();
			this.oracle_run = XmlUtils.getBoolean(db, "oracle_run");
			this.sybase_run = XmlUtils.getBoolean(db, "sybase_run");
			this.sqlserver_run = XmlUtils.getBoolean(db, "sqlserver_run");
			this.mySql_run = XmlUtils.getBoolean(db, "mysql_run");
			this.dbcode = XmlUtils.getString(db, "dbcode");
			this.syscode = XmlUtils.getString(db, "syscode");
			
			if (this.oracle_run)
			{
				dbElement = db.getChild("oracle");
				queryExpireSql = "select * from SMS_SENT_TEMP where (to_date('"
						+ TimeUtil.getDateTimeStr() + "','yyyy-MM-dd HH24:mi:ss')- SEND_TIME)>=2 and  rownum<=1000";
				hibernate_Config_File = "config/hibernate-oracle.cfg.xml";
				this.mySql_run = false;
			} else if (this.sybase_run)
			{
				dbElement = db.getChild("sybase");
				queryExpireSql = "select * from SMS_SENT_TEMP where DATEDIFF(day,SEND_TIME, convert(datetime,'"
						+ TimeUtil.getDateTimeStr() + "')) >=2 ";
				hibernate_Config_File = "config/hibernate-sybase.cfg.xml";
				this.mySql_run = false;
			} else if (this.sqlserver_run)
			{
				dbElement = db.getChild("sqlserver");
				queryExpireSql = "select top 1000  * from SMS_SENT_TEMP with(nolock) where DATEDIFF(day,SEND_TIME, convert(datetime,'"
						+ TimeUtil.getDateTimeStr() + "')) >=2";
				hibernate_Config_File = "config/hibernate-sqlserver.cfg.xml";
				this.mySql_run = false;
			} else if (this.mySql_run)
			{
				dbElement = db.getChild("mySql");
				queryExpireSql = "select * from SMS_SENT_TEMP where (TO_DAYS('"
						+ TimeUtil.getDateTimeStr() + "')-TO_DAYS(SEND_TIME)) >=2  limit 1000";
				hibernate_Config_File = "config/hibernate-mysql.cfg.xml";
			} else
			{
				logger.error("û�������κ����ݿ�");
				throw new Exception();
			}
			parseXML();
		} catch (Exception e)
		{
			Logger.getLogger(Config.class).error("", e);
			throw e;
		}

	}

	/**
	 * ����������SQL��� TODO
	 * 
	 * @throws Exception void
	 * @throws
	 */
	private void parseXML() throws Exception
	{
		if (dbElement == null)
		{
			throw new Exception();
		}
		this.querySql = XmlUtils.getString(dbElement, "querySql");
		this.insertSql = XmlUtils.getString(dbElement, "insertSql");
		this.moSql = XmlUtils.getString(dbElement, "moSql");
		this.errInsertSql = XmlUtils.getString(dbElement, "errorInsertSql");
		this.failSmsSql = XmlUtils.getString(dbElement, "failSmsSql");
		this.insertSentSql = XmlUtils.getString(dbElement, "insertSentSql");
		this.queryTempSql = XmlUtils.getString(dbElement, "queryTempSql");
		this.queryIDSql = XmlUtils.getString(dbElement, "queryIDSql");
		this.errorQueueInsert=XmlUtils.getString(dbElement, "errorQueueInsert");
	}

	public boolean isOracle_run()
	{
		return oracle_run;
	}

	public boolean isSybase_run()
	{
		return sybase_run;
	}

	public boolean isSqlserver_run()
	{
		return sqlserver_run;
	}

	public boolean isMySql_run()
	{
		return mySql_run;
	}

    public String getQueryExpireSql() {
        queryExpireSql = "select * from SMS_SENT_TEMP where (TO_DAYS('"
                + TimeUtil.getDateTimeStr() + "')-TO_DAYS(SEND_TIME)) >=2  limit 1000";
        return queryExpireSql;
    }

    public void setQueryExpireSql(String queryExpireSql) {
        this.queryExpireSql = queryExpireSql;
    }

}
