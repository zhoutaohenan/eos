package com.surge.engine.protocol.sms.cmpp.codec;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2ConnectResp;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2Deliver;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2KeepActive;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2KeepActiveResp;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2SubmitResp;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2TerminateResp;
import com.surge.engine.protocol.sms.cmpp.pmsg.Common;

/**
 * Cmpp协议服务端解码器
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class CmppDecoder extends CumulativeProtocolDecoder
{

	private static final Logger logger = Logger.getLogger(CmppDecoder.class);

	@Override
	protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out)
			throws Exception
	{

		// int msgLength = in.getInt();
		// int commandId = in.getInt();
		// int seq_id = in.getInt();
		//
		// byte[] body = new byte[msgLength - 12];
		// in.get(body);
		// PMessage base = decodeCmpp(commandId, seq_id, body);
		// if(base!=null)
		// {
		// out.write(base);
		// }
		// return false;
		int oldPosition = buffer.position();
		try
		{
			int msgLength = buffer.getInt();
			int commandId = buffer.getInt();
			int seq_id = buffer.getInt();
			byte[] body = new byte[msgLength - 12];
			buffer.get(body);
			PMessage message = decodeCmpp(commandId, seq_id, body);
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
	}
	public static PMessage decodeCmpp(int command, int seq_id, byte[] body) throws Exception
	{

		PMessage base = null;
		switch (command)
		{
		case Common.CMPP_CONNECT_RESP:
			base = new Cmpp2ConnectResp(body);
			break;
		case Common.CMPP_SUBMIT_RESP:
			base = new Cmpp2SubmitResp(body, seq_id);
			break;
		case Common.CMPP_ACTIVE_TEST_RESP:
			base = new Cmpp2KeepActiveResp(seq_id);
			break;
		case Common.CMPP_DELIVER:
			base = new Cmpp2Deliver(body, seq_id);
			break;
		case Common.CMPP_ACTIVE_TEST:
			base = new Cmpp2KeepActive();
			break;
		case Common.CMPP_TERMINATE:
			base = new Cmpp2TerminateResp(seq_id);
			break;
		default:
			//logger.error("无法识别的包，丢弃!!");
			break;
		}
		return base;
	}
}
