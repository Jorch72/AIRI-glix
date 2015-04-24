package com.arisux.airi;

import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import com.arisux.airi.api.window.gui.windows.WindowATWarning;
import com.arisux.airi.lib.ChatUtil;
import com.arisux.airi.lib.ModUtil;
import com.arisux.airi.lib.interfaces.IInitializablePre;

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
		AIRI.windowApi().onTick();
		AIRI.updaterApi().onTick();

		if (!openedInitial)
		{
			AIRI.logger.info("Verifying access transformer...");

			if (!AIRI.COREMOD_INITIALIZED && !ModUtil.isDevEnvironment())
			{
				AIRI.logger.warning("Access transformer could not be verified. If you are in an installation environment, you are going to run into problems!");

				AIRI.windowApi().showWindowManager();
				AIRI.windowApi().addWindow(new WindowATWarning("WarningATNotFound"));
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
				AIRI.windowApi().showWindowManager();
			}
		}
		
		GuiElementHandler.instance().tick();
	}
	
	@SubscribeEvent
	public void onClientPlayerLogin(PlayerLoggedInEvent event)
	{
		if (AIRI.updaterApi().isUpdateAvailable())
		{
			ChatUtil.sendTo(event.player, "&7[&aAIRI&7] &fNew updates are available. To see what updates are available, enter chat and press LEFT ALT + W.");
		}
	}

	@Override
	public void preInitialize(FMLPreInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
}
