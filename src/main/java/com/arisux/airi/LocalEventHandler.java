package com.arisux.airi;

import com.arisux.airi.lib.util.interfaces.IInitializablePre;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LocalEventHandler implements IInitializablePre
{
	private IInitializablePre clientEvents, serverEvents;

	@Override
	public void preInitialize(FMLPreInitializationEvent event)
	{
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			FMLCommonHandler.instance().bus().register(clientEvents = new ClientSideEvents());
			clientEvents.preInitialize(event);
		}
		
		if (FMLCommonHandler.instance().getSide() == Side.SERVER)
		{
			FMLCommonHandler.instance().bus().register(serverEvents = new ServerSideEvents());
			serverEvents.preInitialize(event);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public ClientSideEvents getClientEvents()
	{
		return (ClientSideEvents) clientEvents;
	}
	
	@SideOnly(Side.SERVER)
	public ServerSideEvents getServerEvents()
	{
		return (ServerSideEvents) serverEvents;
	}
}
