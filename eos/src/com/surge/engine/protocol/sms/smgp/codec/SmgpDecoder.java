package com.surge.engine.protocol.sms.smgp.codec;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.cmpp.pmsg.Common;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpConnectResp;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpDeliver;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpKeepActiveTest;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpKeepActiveTestResp;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpSubmitResp;

/**
 * SMGP协议服务端解码器
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SmgpDecoder extends CumulativeProtocolDecoder
{

	private static final Logger logger = Logger.getLogger(SmgpDecoder.class);

	@Override
	protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out)
			throws Exception
	{
		int oldPosition = buffer.position();
		// in.getInt();
		// int commandId = in.getInt();
		// in.position(oldPosition);
		try
		{
			int msgLength = buffer.getInt();
			int commandId = buffer.getInt();
			int seq_id = buffer.getInt();

			byte[] body = new byte[msgLength - 12];
			buffer.get(body);
			PMessage message = decodeSmgp(commandId, body, seq_id);
			if (message == null)
			{
				logger.debug("无法识别的消息包，丢弃!");
				// buffer.position(oldPosition);
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

	public static PMessage decodeSmgp(int command, byte[] body, int seq_id)
	{

		PMessage base = null;

		switch (command)
		{
		case Common.SMGP_CONNECT_RESP:

			base = new SmgpConnectResp(body);
			// base = new SmgpConnectResp(buffer);
			break;
		case Common.SMGP_SUBMIT_RESP:
			base = new SmgpSubmitResp(body, seq_id);
			break;
		case Common.SMGP_ACTIVE_TEST_RESP:
			base = new SmgpKeepActiveTestResp();
			break;
		case Common.SMGP_DELIVER:
			base = new SmgpDeliver(body, seq_id);
			break;
		case Common.SMGP_ACTIVE_TEST:
			base = new SmgpKeepActiveTest();
			break;
		default:
			// logger.error("无法识别的包，丢弃!!");
			break;
		}
		return base;
	}
}
