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
 * GW协议服务端解码器
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
		// 读取初始buffer的位置
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
				logger.debug("无法识别的消息包，丢弃!");
				//buffer.position(oldPosition);
				return false;
			} else
			{
				out.write(message);
				// 读取到一个消息包后，若buffer中仍有数据，刚返回true,继续读取buffer中的数据
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
		// logger.info("不足包头信息长度");
		// return true;
		// }
		//		
		// in.position(oldPosition);
		// if (in.remaining() < msgLength)
		// {
		// logger.info("不足一个完整包的信息长度，返回等待");
		// return true;
		// }
		// CParam base = GwPackageFactory.getPackageType(commandId, in);
		// if (base != null)
		// {
		// out.write(base);
		// } else
		// {
		// logger.error("此包有问题，无法解析!");
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
			//logger.error("无法识别的包，丢弃!!");
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
