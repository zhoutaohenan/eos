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
		// 初始化log4j配置
		PropertyConfigurator.configure("config/log4j.properties");

		TimerPool.init();

		// 初始话hibernate配置
		File f = new File("config/hibernate.cfg.xml");
		Configuration cfg = new Configuration().configure(f);
	}
}
