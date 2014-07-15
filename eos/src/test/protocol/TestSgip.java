package test.protocol;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import test.BaseTest;

import com.surge.engine.protocol.sms.sgip.SgipProtocol;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipSubmit;
import com.surge.engine.sms.conf.SgipConfig;
import com.surge.engine.sms.conf.SmsConfig;

public class TestSgip extends BaseTest
{

	private static final Logger logger=Logger.getLogger(TestSgip.class);
	/**
	 * TODO
	 * 
	 * @param args
	 *            void
	 * @throws
	 */

	public static void main(String[] args)
	{

	}
	@Test
	public void testBind()
	{
		SgipConfig config = this.init();
//		SmsReceiveHandler handler=new SmsReceiveHandler();
		SgipProtocol protocol = new SgipProtocol("sgip_0", config, null);
		protocol.start();

		for (int i = 0; i < 100000; i++)
		{
			SgipSubmit submit = getSubmit(config);
			int result = protocol.sendPMessage(submit);
			System.out.println("发送结果:" + result);
			while(result != 0)

			{
				try
				{
					logger.info("********暂不能发送***** result="+result);
					Thread.currentThread().sleep(500);
					result=protocol.sendPMessage(getSubmit(config));
					

				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			System.out.println("i的值为：" + i);

		}
		try
		{
			Thread.currentThread().sleep(8888888 * 1000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	private SgipSubmit getSubmit(SgipConfig config)
	{
		// String spNumber=config.gets;
		String chargeNumber = config.getChargeNumber();
		int userCount = 1;
		String userNumber = "13500000000";
		String corpId = config.getSpId();
		String content = "测试";
		SgipSubmit submit = new SgipSubmit(1008L, config.getSpCode(), chargeNumber, userNumber,
				corpId, config.getServiceid(), (byte) config.getFeeType(), config.getFeeValue(),
				config.getGivenValue(), (byte) config.getAgentFlag(), (byte) 2, (byte) 0, "", "",
				(byte) 1, (byte) 0, (byte) 0, (byte) 15, (byte) 0, content.getBytes());
		return submit;
	}
	private SgipConfig init()
	{
		SmsConfig config = SmsConfig.getInstance();
		SgipConfig sgip = config.getSgip12Ismgs().get("sgip_0");

		// SgipConfig config = new SgipConfig();
		// config.setConnCount(1);
		// config.setIp("10.0.30.53");
		// config.setPort(8801);
		// config.setListenPort(8802);
		// config.setProtocolId("sgip");
		// config.setConnCount(1);
		// return config;
		return sgip;
	}
}
