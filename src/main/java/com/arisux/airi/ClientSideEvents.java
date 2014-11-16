package com.arisux.airi;

import org.lwjgl.input.Keyboard;

import com.arisux.airi.api.updater.Updater;
import com.arisux.airi.api.window.windows.WindowATWarning;
import com.arisux.airi.engine.ModEngine;
import com.arisux.airi.lib.util.interfaces.IInitializablePre;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientSideEvents implements IInitializablePre
{
	private boolean openedInitial;
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent event)
	{
		AIRI.instance().windowapi.onTick();
		AIRI.instance().updaterapi.onTick();

		if (!openedInitial)
		{
			AIRI.logger.info("Verifying access transformer...");

			if (!AIRI.COREMOD_INITIALIZED && !ModEngine.isDevelopmentEnvironment())
			{
				AIRI.logger.warning("Access transformer could not be verified. If you are in an installation environment, you are going to run into problems!");

				AIRI.instance().windowapi.showWindowManager();
				AIRI.instance().windowapi.addWindow(new WindowATWarning("WarningATNotFound"));
			}
			else
			{
				AIRI.logger.info("Access transformer verified.");
			}

			openedInitial = true;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_W))
			{
				AIRI.instance().windowapi.showWindowManager();
			}
		}
	}

	@SubscribeEvent
	public void onClientPlayerLogin(final PlayerLoggedInEvent event)
	{
		for (Updater updater : AIRI.instance().updaterapi.getUpdaterRegistry())
		{
			updater.printUpdateInformation(event.player);
		}
	}
	
	@Override
	public void preInitialize(FMLPreInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(this);
	}
}