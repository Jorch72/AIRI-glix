package com.arisux.airi;

import com.arisux.airi.api.updater.Updater;
import com.arisux.airi.lib.interfaces.IInitializable;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FMLEventHandler implements IInitializable
{
	@Override
	public void initialize()
	{
		FMLCommonHandler.instance().bus().register(this);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientPlayerLogin(final PlayerLoggedInEvent event)
	{
		for (Updater updater : AIRI.instance().updaterapi.getUpdaterRegistry())
		{
			updater.printUpdateInformation(event.player);
		}
	}
}
