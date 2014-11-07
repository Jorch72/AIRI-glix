package com.arisux.airi.lib.interfaces;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public interface IProxy
{
	public IMessage handlePacket(IMessage message, MessageContext ctx);
	public void initialize();
}