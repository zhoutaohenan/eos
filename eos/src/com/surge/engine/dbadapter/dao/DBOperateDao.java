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
	 *             ������Ӧ�����������Ų����뵽SMS_SENT�� ��Ҫ���õ��ֶ���: ISNEEDREPORT: �Ƿ���Ҫ״̬����
	 *             CHANNEL/RESEND_CHANNEL:ͨ��/�ط�ͨ�� ISNEED_RESEND:�Ƿ���Ҫ�ط�
	 *             RESEND_STATUS:�ط�״̬���Ѿ��ط�/δ�ط� ������Ӧ״̬������ TODO
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
				// ������ط����ţ���CHANNELΪ��,RESEND_CHANNEL��Ϊ��
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
				/** �����gwͨ�����ţ� �ƷѶ�����Ҫ���⴦�� **/
				if (sms.getChannel().getChanneId().startsWith("gw")) {
					// int smsLength = sms.getContents()[sms.getSmsIndex() -
					// 1].length();
					// int smsStandCnLength = 0;
					// GwConfig config =
					// SmsConfig.getInstance().getGwConfigIsmgs().get(
					// sms.getChannel().getChanneId());
					// // �����ų���
					// int longsmsLength = config.getLongsmsLength();
					// /** Ĭ��ȡ����ǩ������ **/
					// int smsSignLength = config.getIsmgSignCn().length();
					// /** Ӣ��ǩ������ **/
					// int smsSignEnLength = config.getIsmgSignEn().length();
					// /** �����Ƿ��Ǵ�Ӣ�ĵ� */
					// boolean isEnglish =
					// CharsetTools.isStringValidate(sms.getContents()[sms
					// .getSmsIndex() - 1]);
					// /** Ӣ�Ķ��ŵ�����ǩ���Ƿ��Ǵ�Ӣ�ĵ� */
					// boolean ismgSignEnIsEn =
					// CharsetTools.isStringValidate(config.getIsmgSignEn());
					// /** ��Ӣ�Ķ��ţ�ȡӢ��ǩ������ **/
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
					// // �Ʒ�����=(�������ݳ���+����ǩ������)/�����ų���
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
					pstmt.setInt(j++, 0);// ���ͳɹ�
				} else {
					// ʧ����Ҫ�ط����
					if (sms.isNeedReSend()) {
						pstmt.setInt(j++, 1);
						String resendChannel = agentService
								.getReSendChannel(sms.getChannel()
										.getChanneId());
						if (resendChannel == null
								|| resendChannel.length() <= 0) {
							pstmt.setInt(j++, 4);// ��û���ط�ͨ��������Ҫ�ط�
						} else {
							pstmt.setInt(j++, 2);// ��Ҫ�ط�
						}
						resendChannel = null;
					} else {
						pstmt.setInt(j++, 0);
						pstmt.setInt(j++, 2);// ��ֵ����û��
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
			logger.debug("�ɹ�����" + smsList.size() + "�����ŵ�SMS_SENT_TEMP�� ,��ʱ:"
					+ String.valueOf(System.currentTimeMillis() - beginT));
			smsList = null;
		} catch (Exception e) {
			logger.error("������Ӧ��������SMS_SENT_TEMP��ʧ��", e);
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

	}

	/**
	 * @throws Exception
	 *             ״̬�������������SMS_SENT�� ��Ҫ���ã� ISNEED_RESEND:�Ƿ���Ҫ�ط�
	 *             RESEND_STATUS:�ط�״̬ TODO
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
					state.setInt(j++, 0);// ���ͳɹ�
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
						logger.info("��������������״̬���淵�����ǳɹ���,���ӱ������ŷ��ͳɹ�!");
						continue;
					}
					state.setInt(j++, 1);
					String resendChannel = agentService.getReSendChannel(report
							.getChannelId());
					if (resendChannel == null || resendChannel.length() <= 0) {
						state.setInt(j++, 4);// ��û�������ط�ͨ��������Ҫ�ط�
					} else {
						state.setInt(j++, 2);// ��Ҫ�ط�
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
			logger.debug("״̬�������,����SMS_SENT_TEMP��" + reportList.size()
					+ "����¼,time=" + (System.currentTimeMillis() - beginTime));
			reportList = null;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.info("״̬�������,����SMS_SENT_TEMP��ʧ��!", e);
			throw e;
		} finally {
			DBUtil.closeConn3(state, conn, session);
		}
	}

	/**
	 * ��ѯ��Ҫ�ط��Ķ��� ��ѯ������: ISNEED_RESEND=1 ��Ҫ�ط� RESEND_STATUS=2 ʧ��δ����
	 * RESEND_CHANNEL is null �ط�ͨ��Ϊ�� ÿ�ζ�ȡ3�� void
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
					// �����һ���������Ƿ�ȫ��ʧ��,����һ���ɹ����Ͳ��ط�
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
				logger.debug("�ɹ�����" + resendQueue.size() + "������ʧ�ܶ���, time="
						+ (System.currentTimeMillis() - beginL));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("��ѯ����ʧ�ܶ���ʧ��", e);
			HibernateUtil.rollbackTransaction();
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/**
	 * ����SMS_SENT������һ����¼��SMS_MT_TASK�� TODO
	 * 
	 * @param listSms
	 *            ��Ҫ�ط��Ķ���
	 * @param conn
	 *            һ�����ݿ�����
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
	 * �־û�MO�����ݿ� TODO
	 * 
	 * @param messageList
	 *            void
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	public void saveMo(List<SmsMessage> messageList) {
		String moSql = dbconfig.moSql;
		/** ��ѯ�û��˿� **/
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
				// ���Ƚ�ȡ����MO���ŵ���չ��
				String spcode = agentService.getSpCode(message.getChannelId());
				String extend = null;
				// ��չ�벻Ϊ��ʱ
				if (spcode != null && (spcode.length() > 0)) {
					if (message.getChannelId().startsWith("gw"))// ����gwͨ����mo��Ϣ�����⴦��
					{
						extend = message.getDest().substring(
								Integer.parseInt(spcode));
					} else {
						if (message.getDest().length() < spcode.length()) {
							logger.info("�յ�������������MO:" + message.toString(),
									null);
							continue;

						} else {
							extend = message.getDest().substring(
									spcode.length());
						}

					}
				}
				// ��չ��Ϊ��ʱ��ֱ�Ӹ���߼�����Ա
				if (extend == null || extend.length() <= 0) {
					this.executeMoPre(message, insertState, extend,
							"24a1d741-2209-4d5b-9fdf-c4a136bf9920",
							"3163530e-0f0c-43ae-bc0f-0c353d317c75", time);
				} else {

					if (message.getChannelId().equalsIgnoreCase("����-����")) {
						// ������ر��û�
						this.executeMoPre(message, insertState, extend,
								"24a1d741-2209-4d5b-9fdf-c4a136bf9920",
								"0e524776-80fd-4998-b489-e4176a48ea17", time);

					} else if (message.getChannelId().equalsIgnoreCase("����-��ͨ")) {
						// ������ر��û�
						this.executeMoPre(message, insertState, extend,
								"24a1d741-2209-4d5b-9fdf-c4a136bf9920",
								"9c272f68-b2aa-4f02-bd8d-1234af467691", time);

					}
					else {
						queryState.setString(1, extend);
						rs = queryState.executeQuery();

						if (rs.next()) {
							do {// ��ƥ�䵽����û���������ÿ���û�
								this.executeMoPre(message, insertState, extend,
										rs.getString("orgid"),// id,orgid
										rs.getString("id"), time);
							} while (rs.next());
						} else {
							// û��ƥ�䵽�û�������߼�����Ա
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
			logger.debug("�ɹ�����һ��" + messageList.size() + "��MO��Ϣ");
		} catch (Exception e) {
			logger.error("����MO����ʧ��!", e);
			HibernateUtil.rollbackTransaction();

		} finally {
			HibernateUtil.closeSession();
		}
	}

	/**
	 * ����MOԤ������������ TODO
	 * 
	 * @param message
	 *            ��MO��Ϣ
	 * @param insertState
	 *            ������Ԥ����
	 * @param extend
	 *            ����չ��
	 * @param orgid
	 *            ������ID
	 * @param userid
	 *            ���û�ID
	 * @param moid
	 *            ��ƥ������MOʱ����ʶ��һ��MO��Ϣ��
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
