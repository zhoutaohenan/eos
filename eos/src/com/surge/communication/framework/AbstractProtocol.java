
package com.surge.communication.framework;

/**
 * @description
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public abstract class AbstractProtocol implements Protocol
{

	protected String protocolId;

	public AbstractProtocol(String protocolId)
	{

		this.protocolId = protocolId;
	}

	public String getProtocolId()
	{

		return protocolId;
	}
}
