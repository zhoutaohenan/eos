package com.surge.communication.framework.filter.BaseFilter;

import org.apache.mina.core.filterchain.IoFilter;

/**
 * mina处理消息 过滤器
 * @project: nioframe
 * @Date:2010-7-29
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class BaseFilter
{
	private final String filterName;
	private final IoFilter ioFilter;

	public BaseFilter(String filterName, IoFilter ioFilter)
	{
		this.filterName = filterName;
		this.ioFilter = ioFilter;
	}

	public String getFilterName()
	{
		return filterName;
	}

	public IoFilter getIoFilter()
	{
		return ioFilter;
	}
}
