package com.surge.engine.dbadapter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.dbadapter.util.DBUtil;
import com.surge.engine.sms.pojo.Report;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.pojo.SmsMessage;
import com.surge.engine.sms.service.SmsAgentService;
import com.surge.engine.util.HibernateUtil;
import com.surge.engine.util.StringUtils;
import com.surge.engine.util.TimeUtil;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-8-16
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class DBOperateDao {

	private final static Logger logger = Logger.getLogger(DBOperateDao.class);

	private SmsAgentService agentService;

	private DbConfig dbconfig = DbConfig.instance;

	public DBOperateDao(SmsAgentService agentService) {
		this.agentService = agentService;
	}

	/**
	 * @throws Exception
	 *             短信响应回来，将短信插曲入到SMS_SENT表， 主要设置的字段有: ISNEEDREPORT: 是否需要状态报告
	 *             CHANNEL/RESEND_CHANNEL:通道/重发通道 ISNEED_RESEND:是否需要重发
	 *             RESEND_STATUS:重发状态，已经重发/未重发 根据响应状态，设置 TODO
	 * 
	 * @param smsList
	 *            void
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	public void saveResponse(List<Sms> smsList) throws Exception {
		try {
			long beginT = System.currentTimeMillis();
			HibernateUtil.beginTransaction();
			String insertSql = dbconfig.insertSql;
			PreparedStatement pstmt = HibernateUtil.getSession().connection()
					.prepareStatement(insertSql);
			for (Sms sms : smsList) {
				int j = 1;
				pstmt.setString(j++, sms.getMtId());
				pstmt.setString(j++, sms.getSmsid());
				pstmt.setString(j++, sms.getDest());
				try {
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date utilDate = format.parse(sms.getCreateTime());
					pstmt.setTimestamp(j++, new Timestamp(utilDate.getTime()));
				} catch (ParseException e) {
					pstmt.setTimestamp(j++, new Timestamp(new Date().getTime()));
				}

				pstmt.setInt(j++, sms.getIsNeedReport());
				if (sms.getSendResult() == 0) {
					pstmt.setInt(j++, 0);
				} else {
					pstmt.setInt(j++, 1);
				}
				pstmt.setTimestamp(j++, new Timestamp(sms.getSubmit2IsmgTime()));
				// 如果是重发短信，则CHANNEL为空,RESEND_CHANNEL不为空
				if (sms.isReSend()) {
					pstmt.setString(j++, "");
					pstmt.setString(j++, sms.getChannel().getChanneId());
				} else {
					pstmt.setString(j++, sms.getChannel().getChanneId());
					pstmt.setString(j++, "");
				}
				pstmt.setString(j++, StringUtils.strConvertEncode(
						sms.getContent(), dbconfig.syscode, dbconfig.dbcode));
				pstmt.setString(j++, StringUtils.strConvertEncode(
						sms.getContents()[sms.getSmsIndex() - 1],
						dbconfig.syscode, dbconfig.dbcode));
				pstmt.setInt(j++, sms.getContents().length);
				/** 如果是gw通道短信， 计费短信需要特殊处理 **/
				if (sms.getChannel().getChanneId().startsWith("gw")) {
					// int smsLength = sms.getContents()[sms.getSmsIndex() -
					// 1].length();
					// int smsStandCnLength = 0;
					// GwConfig config =
					// SmsConfig.getInstance().getGwConfigIsmgs().get(
					// sms.getChannel().getChanneId());
					// // 长短信长度
					// int longsmsLength = config.getLongsmsLength();
					// /** 默认取中文签名长度 **/
					// int smsSignLength = config.getIsmgSignCn().length();
					// /** 英文签名长度 **/
					// int smsSignEnLength = config.getIsmgSignEn().length();
					// /** 内容是否是纯英文的 */
					// boolean isEnglish =
					// CharsetTools.isStringValidate(sms.getContents()[sms
					// .getSmsIndex() - 1]);
					// /** 英文短信的网关签名是否是纯英文的 */
					// boolean ismgSignEnIsEn =
					// CharsetTools.isStringValidate(config.getIsmgSignEn());
					// /** 是英文短信，取英文签名长度 **/
					// if ((sms.getMsg_fmt() != 0 && (smsLength + smsSignLength)
					// > 70)
					// || (sms.getMsg_fmt() == 0 && (smsLength +
					// smsSignEnLength) > 140))
					// {
					//
					// if (isEnglish && ismgSignEnIsEn)
					// {
					// smsSignLength = config.getIsmgSignEn().length();
					// }
					// // 计费条数=(短信内容长度+短信签名长度)/长短信长度
					// smsStandCnLength = (smsLength + smsSignLength) /
					// longsmsLength;
					// if ((smsLength + smsSignLength) % longsmsLength != 0)
					// {
					// smsStandCnLength += 1;
					// }
					// } else
					// {
					// smsStandCnLength = 1;
					// }
					//
					// pstmt.setInt(j++, smsStandCnLength);
				} else {
					pstmt.setInt(j++, 1);
				}
				if (sms.getSendResult() == 0) {
					pstmt.setInt(j++, 0);
					pstmt.setInt(j++, 0);// 发送成功
				} else {
					// 失败需要重发情况
					if (sms.isNeedReSend()) {
						pstmt.setInt(j++, 1);
						String resendChannel = agentService
								.getReSendChannel(sms.getChannel()
										.getChanneId());
						if (resendChannel == null
								|| resendChannel.length() <= 0) {
							pstmt.setInt(j++, 4);// 因没有重发通道，不需要重发
						} else {
							pstmt.setInt(j++, 2);// 需要重发
						}
						resendChannel = null;
					} else {
						pstmt.setInt(j++, 0);
						pstmt.setInt(j++, 2);// 此值设置没用
					}

				}
				pstmt.setString(j++, sms.getMessageId());
				pstmt.setInt(j++, sms.getSendResult());
				pstmt.setString(j++, sms.getExtcode());
				pstmt.setString(j++, sms.getOrgid());
				pstmt.setString(j++, sms.getUserid());
				pstmt.setString(j++, sms.getUserSign());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			HibernateUtil.commitTransaction();
			logger.debug("成功保存" + smsList.size() + "条短信到SMS_SENT_TEMP表 ,用时:"
					+ String.valueOf(System.currentTimeMillis() - beginT));
			smsList = null;
		} catch (Exception e) {
			logger.error("发送响应回来插入SMS_SENT_TEMP表失败", e);
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

	}

	/**
	 * @throws Exception
	 *             状态报告回来，更新SMS_SENT表 主要设置： ISNEED_RESEND:是否需要重发
	 *             RESEND_STATUS:重发状态 TODO
	 * 
	 * @param reportList
	 *            void
	 * @throws
	 */

	@SuppressWarnings("deprecation")
	public void updateReport(List<Report> reportList) throws Exception {
		long beginTime = System.currentTimeMillis();
		Session session = null;
		Connection conn = null;
		PreparedStatement state = null;
		Transaction transaction = null;
		Statement checkStat = null;
		ResultSet rs = null;
		try {
			String updateSql = "update SMS_SENT_TEMP  set ISNEED_RESEND=?,RESEND_STATUS=?,SEND_RET=?,"
					+ "STATUS=?,DESCRIPTION=?,REPORT_TIME=? where MESSAGE_ID =? and DESTADDR=?";
			session = HibernateUtil.getNewSession();
			transaction = session.beginTransaction();
			conn = session.connection();
			state = conn.prepareStatement(updateSql);
			for (Report report : reportList) {
				int j = 1;
				if (report.getResult() == 0) {
					state.setInt(j++, 0);
					state.setInt(j++, 0);// 发送成功
				} else {
					String querySql = "select SEND_RET,STATUS from SMS_SENT_TEMP where MESSAGE_ID = '"
							+ report.getMessageId() + "'";
					checkStat = conn.createStatement();
					rs = checkStat.executeQuery(querySql);
					int flag = -1;
					while (rs.next()) {
						String statuT = rs.getString("STATUS");
						if (statuT != null && statuT.length() >= 1) {
							flag = rs.getInt("SEND_RET");
							if (flag == 0) {
								break;
							}
						}
					}
					if (flag == 0) {
						logger.info("本批长短信已有状态报告返回且是成功的,就视本批短信发送成功!");
						continue;
					}
					state.setInt(j++, 1);
					String resendChannel = agentService.getReSendChannel(report
							.getChannelId());
					if (resendChannel == null || resendChannel.length() <= 0) {
						state.setInt(j++, 4);// 因没有配置重发通道，不需要重发
					} else {
						state.setInt(j++, 2);// 需要重发
					}
				}
				state.setInt(j++, report.getResult());
				state.setString(j++, report.getStats());
				state.setString(j++, StringUtils.strConvertEncode(
						report.getDesc(), dbconfig.syscode, dbconfig.dbcode));
				state.setTimestamp(j++, new Timestamp(report.getReciveTime()));
				state.setString(j++, report.getMessageId());
				state.setString(j++, report.getMobile());
				state.addBatch();
			}
			state.executeBatch();
			transaction.commit();
			logger.debug("状态报告回来,更新SMS_SENT_TEMP表" + reportList.size()
					+ "条记录,time=" + (System.currentTimeMillis() - beginTime));
			reportList = null;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.info("状态报告回来,更新SMS_SENT_TEMP表失败!", e);
			throw e;
		} finally {
			DBUtil.closeConn3(state, conn, session);
		}
	}

	/**
	 * 查询需要重发的短信 查询条件是: ISNEED_RESEND=1 需要重发 RESEND_STATUS=2 失败未发送
	 * RESEND_CHANNEL is null 重发通道为空 每次读取3条 void
	 * 
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	public void queryFailSms() {
		long beginL = System.currentTimeMillis();
		ArrayList<SmsData> resendList = new ArrayList<SmsData>();
		String querySql = dbconfig.failSmsSql;
		try {
			HibernateUtil.beginTransaction();
			Connection connection = HibernateUtil.getSession().connection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(querySql);
			SmsData sms = null;
			Map<Long, SmsData> resendQueue = new ConcurrentHashMap<Long, SmsData>();
			while (resultSet.next()) {
				Long mtid = resultSet.getLong("MT_ID");
				int totalSms = resultSet.getInt("PK_TOTAL");
				if (totalSms > 1) {
					// 检查是一条长短信是否全部失败,若有一条成功，就不重发
					String checkSql = "select count(*) n from SMS_SENT_TEMP where MT_ID="
							+ mtid
							+ " and ((ISNEEDREPORT=1 and STATUS is not null) "
							+ "or(ISNEEDREPORT=0) or (ISNEEDREPORT=1 and SEND_FLAG=1))";
					Statement checkSta = connection.createStatement();
					ResultSet checkRs = checkSta.executeQuery(checkSql);
					checkRs.next();
					int count = checkRs.getInt("n");
					if (totalSms > count) {
						continue;
					}
				}
				sms = new SmsData();
				sms.mtid = mtid;
				sms.smsid = resultSet.getString("SMSID");
				sms.destaddr = resultSet.getString("DESTADDR");
				sms.message = resultSet.getString("MESSAGE");
				sms.channel = agentService.getReSendChannel(resultSet
						.getString("CHANNEL"));
				sms.priorty = 0;
				sms.isneedreport = resultSet.getInt("ISNEEDREPORT");
				sms.extcode = resultSet.getString("EXTCODE");
				sms.orgid = resultSet.getLong("ORGID");
				sms.userid = resultSet.getLong("SEND_USERID");
				sms.userSign = resultSet.getString("USER_SIGN");
				resendQueue.put(sms.mtid, sms);
			}
			if (resendQueue.size() > 0) {
				Set<Long> keySet = resendQueue.keySet();
				for (Long key : keySet) {
					resendList.add(resendQueue.get(key));
				}
				updateResendSms(resendList, connection);
				logger.debug("成功操作" + resendQueue.size() + "条发送失败短信, time="
						+ (System.currentTimeMillis() - beginL));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("查询发送失败短信失败", e);
			HibernateUtil.rollbackTransaction();
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/**
	 * 更新SMS_SENT表并新增一条记录到SMS_MT_TASK表 TODO
	 * 
	 * @param listSms
	 *            需要重发的短信
	 * @param conn
	 *            一条数据库连接
	 * @throws SQLException
	 *             void
	 * @throws
	 */
	private void updateResendSms(ArrayList<SmsData> listSms, Connection conn)
			throws SQLException {

		String updateSql = "update SMS_SENT_TEMP set RESEND_STATUS=1 where MT_ID=?";
		PreparedStatement updateStat = conn.prepareStatement(updateSql);
		String insertSql = "insert into SMS_MT_TASK "
				+ "(MT_ID,SMSID,DESTADDR,MESSAGE,ISNEEDREPORT,CHANNEL,PRIORTY,IS_RESEND,EXTCODE,SMS_SIGN,ORGID,SEND_USERID) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
		if (dbconfig.isMySql_run()) {
			insertSql = "insert into SMS_MT_TASK "
					+ "(MT_ID,SMSID,DESTADDR,MESSAGE,ISNEEDREPORT,CHANNEL,PRIORTY,IS_RESEND,PRESEND_TIME,CREATE_TIME,EXTCODE,SMS_SIGN,ORGID,SEND_USERID) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}
		PreparedStatement insertState = conn.prepareStatement(insertSql);
		for (SmsData data : listSms) {
			updateStat.setLong(1, data.mtid);
			updateStat.addBatch();
			int j = 1;
			insertState.setLong(j++, data.mtid);
			insertState.setString(j++, data.smsid);
			insertState.setString(j++, data.destaddr);
			insertState.setString(j++, data.message);
			insertState.setInt(j++, data.isneedreport);
			insertState.setString(j++, data.channel);
			insertState.setInt(j++, data.priorty);
			insertState.setInt(j++, 1);
			if (dbconfig.isMySql_run()) {
				insertState.setString(j++, TimeUtil.getDateTimeStr());
				insertState.setString(j++, TimeUtil.getDateTimeStr());
			}
			insertState.setString(j++, data.extcode);
			insertState.setString(j++, data.userSign);
			insertState.setLong(j++, data.orgid);
			insertState.setLong(j++, data.userid);
			insertState.addBatch();
		}
		updateStat.executeBatch();
		insertState.executeBatch();

	}

	/**
	 * 持久化MO到数据库 TODO
	 * 
	 * @param messageList
	 *            void
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	public void saveMo(List<SmsMessage> messageList) {
		String moSql = dbconfig.moSql;
		/** 查询用户端口 **/
		String queryUsrPort = dbconfig.queryIDSql;
		// String queryUsrPort =
		// "select USERID,ORGID from sms_user where (ORGPORT||USER_PORT) = ?";
		try {
			HibernateUtil.beginTransaction();
			PreparedStatement insertState = HibernateUtil.getSession()
					.connection().prepareStatement(moSql);
			PreparedStatement queryState = HibernateUtil.getSession()
					.connection().prepareStatement(queryUsrPort);
			ResultSet rs = null;
			for (SmsMessage message : messageList) {
				long time = System.nanoTime();
				// 首先截取此条MO短信的扩展码
				String spcode = agentService.getSpCode(message.getChannelId());
				String extend = null;
				// 扩展码不为空时
				if (spcode != null && (spcode.length() > 0)) {
					if (message.getChannelId().startsWith("gw"))// 若是gw通道的mo信息，特殊处理
					{
						extend = message.getDest().substring(
								Integer.parseInt(spcode));
					} else {
						if (message.getDest().length() < spcode.length()) {
							logger.info("收到不符合条件的MO:" + message.toString(),
									null);
							continue;

						} else {
							extend = message.getDest().substring(
									spcode.length());
						}

					}
				}
				// 扩展码为空时，直接给最高级管理员
				if (extend == null || extend.length() <= 0) {
					this.executeMoPre(message, insertState, extend,
							"24a1d741-2209-4d5b-9fdf-c4a136bf9920",
							"3163530e-0f0c-43ae-bc0f-0c353d317c75", time);
				} else {

					if (message.getChannelId().equalsIgnoreCase("艾派-电信")) {
						// 分配给特别用户
						this.executeMoPre(message, insertState, extend,
								"24a1d741-2209-4d5b-9fdf-c4a136bf9920",
								"0e524776-80fd-4998-b489-e4176a48ea17", time);

					} else if (message.getChannelId().equalsIgnoreCase("艾派-联通")) {
						// 分配给特别用户
						this.executeMoPre(message, insertState, extend,
								"24a1d741-2209-4d5b-9fdf-c4a136bf9920",
								"9c272f68-b2aa-4f02-bd8d-1234af467691", time);

					}
					else {
						queryState.setString(1, extend);
						rs = queryState.executeQuery();

						if (rs.next()) {
							do {// 若匹配到多个用户，则分配给每个用户
								this.executeMoPre(message, insertState, extend,
										rs.getString("orgid"),// id,orgid
										rs.getString("id"), time);
							} while (rs.next());
						} else {
							// 没有匹配到用户，给最高级管理员
							this.executeMoPre(message, insertState, extend,
									"24a1d741-2209-4d5b-9fdf-c4a136bf9920",
									"3163530e-0f0c-43ae-bc0f-0c353d317c75",
									time);
						}
					}

				}

			}
			insertState.executeBatch();
			HibernateUtil.commitTransaction();
			logger.debug("成功保存一批" + messageList.size() + "条MO信息");
		} catch (Exception e) {
			logger.error("保存MO短信失败!", e);
			HibernateUtil.rollbackTransaction();

		} finally {
			HibernateUtil.closeSession();
		}
	}

	/**
	 * 保存MO预处理，公共操作 TODO
	 * 
	 * @param message
	 *            　MO信息
	 * @param insertState
	 *            　保存预处理
	 * @param extend
	 *            　扩展码
	 * @param orgid
	 *            　机构ID
	 * @param userid
	 *            　用户ID
	 * @param moid
	 *            　匹配出多个MO时，标识是一条MO信息　
	 * @throws SQLException
	 *             void
	 * @throws
	 */
	private void executeMoPre(SmsMessage message,
			PreparedStatement insertState, String extend, String orgid,
			String userid, long moid) throws SQLException {
		int j = 1;
		insertState.setString(j++, message.getSrc_id());
		insertState.setString(j++, StringUtils.strConvertEncode(
				message.getContent(), dbconfig.syscode, dbconfig.dbcode));
		insertState.setString(j++, message.getChannelId());

		insertState.setString(j++, message.getDest());
		if (dbconfig.isMySql_run()) {
			insertState.setString(j++, TimeUtil.getDateTimeStr());
		}
		insertState.setString(j++, extend);
		insertState.setString(j++, orgid);
		insertState.setString(j++, userid);
		insertState.setString(j++, String.valueOf(moid));
		insertState.addBatch();
	}

	class SmsData {
		long mtid;

		String smsid;

		String destaddr;

		String message;

		int isneedreport;

		String channel;

		int priorty;

		String extcode;

		long orgid;

		long userid;

		String userSign;

	}
}
