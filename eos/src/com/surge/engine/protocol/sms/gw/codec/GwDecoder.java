package com.surge.engine.protocol.sms.gw.codec;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.common.SmsProtocolConstant;
import com.surge.engine.protocol.sms.gw.pmsg.CFeeRet;
import com.surge.engine.protocol.sms.gw.pmsg.CLinkReq;
import com.surge.engine.protocol.sms.gw.pmsg.CLinkRet;
import com.surge.engine.protocol.sms.gw.pmsg.CLoginRet;
import com.surge.engine.protocol.sms.gw.pmsg.CMoReq;
import com.surge.engine.protocol.sms.gw.pmsg.CParam;
import com.surge.engine.protocol.sms.gw.pmsg.CReportReq;
import com.surge.engine.protocol.sms.gw.pmsg.CSendRet;

/**
 * GWЭ�����˽�����
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class GwDecoder extends CumulativeProtocolDecoder
{

	private static final Logger logger = Logger.getLogger(GwDecoder.class);

	@Override
	protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out)
			throws Exception
	{
		// int oldPosition = in.position();
		// in.getInt();
		// int commandId = in.get();
		// in.position(oldPosition);
		// CParam base = getPackageType(commandId, in);
		// if(base!=null)
		// {
		// out.write(base);
		// }
		// ��ȡ��ʼbuffer��λ��
		int oldPosition = buffer.position();
		try
		{
			int packageSize=buffer.getInt();
			int commandId = buffer.get();
			buffer.position(oldPosition);
			if(packageSize > buffer.limit())
			{
				logger.debug("the package is not complete,wait...");
				return false;
			}
			PMessage message = getPackageType(commandId, buffer);
			if (message == null)
			{
				logger.debug("�޷�ʶ�����Ϣ��������!");
				//buffer.position(oldPosition);
				return false;
			} else
			{
				out.write(message);
				// ��ȡ��һ����Ϣ������buffer���������ݣ��շ���true,������ȡbuffer�е�����
				if (buffer.hasRemaining())
				{
					return true;
				} else
				{
					return false;
				}
			}
		} catch (Exception e)
		{
			buffer.position(oldPosition);
			logger.debug("the package is not complete,wait...");
			return false;
		}

		// while (in.hasRemaining())
		// {
		// if (in.remaining() < MESSAGE_LENGTH)
		// {
		// logger.info("�����ͷ��Ϣ����");
		// return true;
		// }
		//		
		// in.position(oldPosition);
		// if (in.remaining() < msgLength)
		// {
		// logger.info("����һ������������Ϣ���ȣ����صȴ�");
		// return true;
		// }
		// CParam base = GwPackageFactory.getPackageType(commandId, in);
		// if (base != null)
		// {
		// out.write(base);
		// } else
		// {
		// logger.error("�˰������⣬�޷�����!");
		// in.position(initPosition);
		// return true;
		// }
		// oldPosition = in.position();
		// }
		// return false;
	}
	public static CParam getPackageType(int command, IoBuffer buffer) throws Exception
	{
		CParam base = null;
		byte contents[] = bufferToBytes(buffer);
		switch (command)
		{
		case SmsProtocolConstant.GW_LOGIN_RESP:
			base = new CLoginRet(contents);
			break;
		case SmsProtocolConstant.GW_SEND_RESP:
			base = new CSendRet(contents);
			break;
		case SmsProtocolConstant.GW_FEE_RESP:
			base = new CFeeRet(contents);
			break;
		case SmsProtocolConstant.GW_MO:
			base = new CMoReq(contents);
			break;
		case SmsProtocolConstant.GW_REPORT:
			base = new CReportReq(contents);
			break;
		case SmsProtocolConstant.GW_LINK_CHECK:
			base = new CLinkReq(contents);
			break;
		case SmsProtocolConstant.GW_LINK_CHECK_RESP:
			base = new CLinkRet(contents);
			break;
		default:
			//logger.error("�޷�ʶ��İ�������!!");
			break;
		}
		return base;
	}
	private static byte[] bufferToBytes(IoBuffer buffer)
	{
		int length = buffer.getInt() - 4;
		byte[] bytes = new byte[length];
		buffer.get(bytes);
		return bytes;
	}

}
