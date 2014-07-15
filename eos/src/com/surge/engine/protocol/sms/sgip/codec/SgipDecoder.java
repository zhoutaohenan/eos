package com.surge.engine.protocol.sms.sgip.codec;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.surge.engine.protocol.sms.common.SmsProtocolConstant;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipBind;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipBindResp;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipDeliver;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipReport;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipSendBase;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipSubmitResp;

/**
 * Sgip协议服务端解码器
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SgipDecoder extends CumulativeProtocolDecoder
{

	private static final Logger logger = Logger.getLogger(SgipDecoder.class);

	@Override
	protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out)
			throws Exception
	{
		int oldPosition = buffer.position();
		try
		{
			int packageSize = buffer.getInt();
			int commandId = buffer.getInt();
			buffer.position(oldPosition);
			if(packageSize>buffer.limit())
			{
				logger.debug("the package is not complete,wait...");
				return false;
			}
			SgipSendBase message = decodeSgip(commandId, buffer);
			if (message == null)
			{
				buffer.position(oldPosition);
				int limit = buffer.limit();
				logger.debug("无法识别的消息包，丢弃!,包大小," + packageSize + "commandID=" + commandId
						+ "limit=" + limit + ",内容" + buffer.array());
				buffer.clear();
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
			logger.debug("the package is not complete,wait...");
			buffer.position(oldPosition);
			return false;
		}
	}

	public static SgipSendBase decodeSgip(int command, IoBuffer buffer)
	{

		SgipSendBase base = null;
		switch (command)
		{
		case SmsProtocolConstant.SGIP_BIND:
			base = new SgipBind(buffer);
			break;
		case SmsProtocolConstant.SGIP_BIND_RESP:
			base = new SgipBindResp(buffer);
			break;
		case SmsProtocolConstant.SGIP_DELIVER:
			base = new SgipDeliver(buffer);
			break;
		case SmsProtocolConstant.SGIP_REPORT:
			base = new SgipReport(buffer);
			break;
		case SmsProtocolConstant.SGIP_SUBMIT_RESP:
			base = new SgipSubmitResp(buffer);
			break;
		default:
			// logger.error("无法识别的包，丢弃command:" + command);
			break;
		}
		return base;
	}
}
