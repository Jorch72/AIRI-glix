package com.arisux.airi;

import com.arisux.airi.lib.ModLib;
import com.arisux.airi.lib.interfaces.IInitializablePost;
import org.lwjgl.input.Keyboard;

import com.arisux.airi.api.window.windows.WindowATWarning;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientTickHandler implements IInitializablePost
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

			if (!AIRI.ASM_INITIALIZED && !ModLib.isDevelopmentEnvironment())
			{
				AIRI.logger.warning("Access transformer was not initialized properly or you are in a development environment. If you are not in a development environment, and you are seeing this message, you are going to run into problems!");

				AIRI.instance().windowapi.showWindowManager();
				new WindowATWarning("WarningATNotFound");
			}
			else
			{
				AIRI.logger.info("Access transformer verified.");
			}

			openedInitial = true;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))
			if (Keyboard.isKeyDown(Keyboard.KEY_W))
				AIRI.instance().windowapi.showWindowManager();
	}

	public void postInitialize()
	{
		FMLCommonHandler.instance().bus().register(this);
	}
}
