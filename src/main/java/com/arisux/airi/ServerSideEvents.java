package com.arisux.airi;

import com.arisux.airi.lib.interfaces.IInitializablePre;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ServerSideEvents implements IInitializablePre
{
	@Override
	public void preInitialize(FMLPreInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(this);
	}
}
