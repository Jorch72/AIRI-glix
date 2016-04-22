package com.arisux.airi;

import org.lwjgl.input.Keyboard;

import com.arisux.airi.api.window.gui.windows.WindowATWarning;
import com.arisux.airi.lib.ChatUtil;
import com.arisux.airi.lib.GlStateManager;
import com.arisux.airi.lib.GuiElements.GuiCustomButton;
import com.arisux.airi.lib.ModUtil;
import com.arisux.airi.lib.RenderUtil;
import com.arisux.airi.lib.interfaces.IActionPerformed;
import com.arisux.airi.lib.interfaces.IInitializablePre;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.common.MinecraftForge;

public class ClientSideEvents implements IInitializablePre
{
	private boolean openedInitial;
	private GuiCustomButton buttonUpdates;

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

	@SubscribeEvent
	public void onRenderTitleScreen(TickEvent.RenderTickEvent event)
	{

		if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu)
		{
			int amountOfUpdates = AIRI.updaterApi().getAvailableUpdates().size();

			if (amountOfUpdates > 0)
			{
				if (buttonUpdates == null && GuiElementHandler.instance() != null)
				{
					buttonUpdates = new GuiCustomButton(0, 0, 0, 0, 0, "", null);
				}

				if (buttonUpdates != null)
				{
					String updates = String.valueOf(amountOfUpdates);
					String updateString = amountOfUpdates > 1 ? "Updates" : "Update";
					int renderWidth = RenderUtil.getStringRenderWidth(updates);
					int backgroundWidth = 30 + renderWidth + RenderUtil.getStringRenderWidth(updateString);

					RenderUtil.drawRect(0, 2, backgroundWidth, 23, 0x77000000);
					GlStateManager.enableBlend();
					RenderUtil.drawRectWithOutline(0, 2, 18 + renderWidth, 23, 2, 0x33000000, 0x00000000);
					RenderUtil.drawString(updates, 10, 10, AIRI.windowApi().getCurrentTheme().getButtonColor(), false);
					RenderUtil.drawString(updateString, 24 + renderWidth, 10, 0xFFFFFFFF, false);

					buttonUpdates.displayString = "";
					buttonUpdates.baseColor = 0x00000000;
					buttonUpdates.overlayColorHover = 0x00000000;
					buttonUpdates.overlayColorNormal = 0x00000000;
					buttonUpdates.overlayColorPressed = 0x00000000;
					buttonUpdates.fontColor = 0xFFFF3333;
					buttonUpdates.xPosition = 0;
					buttonUpdates.yPosition = 2;
					buttonUpdates.width = backgroundWidth;
					buttonUpdates.height = 23;
					buttonUpdates.drawButton();
					buttonUpdates.setAction(new IActionPerformed()
					{
						@Override
						public void actionPerformed(GuiCustomButton button)
						{
							AIRI.windowApi().addWindow(AIRI.updaterApi().getUpdaterWindow());
							AIRI.windowApi().showWindowManager();
						}
					});
				}
			}
		}
	}

	@Override
	public void preInitialize(FMLPreInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
}
