package com.surge.engine.server;

import com.missian.common.beanlocate.BeanLocator;

public class ExampleBeanLocator implements BeanLocator
{

	public Object lookup(String beanName)
	{
		return new ServerStatChannelImpl();
	}

}
