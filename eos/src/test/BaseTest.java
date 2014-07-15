package test;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.hibernate.cfg.Configuration;

import com.surge.engine.util.TimerPool;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-16
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class BaseTest
{
	static
	{
		// ��ʼ��log4j����
		PropertyConfigurator.configure("config/log4j.properties");

		TimerPool.init();

		// ��ʼ��hibernate����
		File f = new File("config/hibernate.cfg.xml");
		Configuration cfg = new Configuration().configure(f);
	}
}
