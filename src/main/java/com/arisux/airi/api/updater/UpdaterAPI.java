package com.arisux.airi.api.updater;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

import com.arisux.airi.AIRI;
import com.arisux.airi.api.window.Window;
import com.arisux.airi.api.window.WindowManager;
import com.arisux.airi.api.window.windows.WindowUpdates;
import com.arisux.airi.lib.ChatUtil;
import com.arisux.airi.lib.interfaces.IMod;

public class UpdaterAPI
{
	private ArrayList<Updater> updaters = new ArrayList<Updater>();
	private int currentUpdaterId;
	private boolean recheckUpdates, showWindowManager, canRecheckForUpdates;
	private Window updaterWindow;

	public UpdaterAPI()
	{
		this.showWindowManager = true;
	}

	public void onTick()
	{
		if (AIRI.settings().isNetworkingEnabled())
		{
			if (Minecraft.getMinecraft().theWorld != null && !Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().theWorld.getWorldTime() % (20 * 60 * 30) == 1)
			{
				this.recheckUpdates = true;
				
				if (AIRI.updaterApi().isUpdateAvailable())
				{
					ChatUtil.sendTo(Minecraft.getMinecraft().thePlayer, "");
					ChatUtil.sendTo(Minecraft.getMinecraft().thePlayer, "&7[&aAIRI&7] &fNew updates are available. To see what updates are available, enter chat and press &aLEFT ALT&f + &aW&f.");
				}
			}
			
			if (!(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) && !(Minecraft.getMinecraft().currentScreen instanceof WindowManager))
			{
				this.canRecheckForUpdates = true;
			}

			if (canRecheckForUpdates)
			{
				if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu)
				{
					this.recheckUpdates = true;
					this.canRecheckForUpdates = false;
				}
			}

			if (AIRI.updaterApi().isUpdateAvailable())
			{
				if (this.updaterWindow == null)
				{
					this.updaterWindow = new WindowUpdates("UpdateWindow", "Update Available", -100, 20, 250, 200);

					if (!AIRI.windowApi().getWindowsRegistry().contains(this.updaterWindow))
					{
						AIRI.windowApi().addWindow(this.updaterWindow);
					}
				}
			}

			for (final Updater updater : this.updaters)
			{
				if (!(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) && !(Minecraft.getMinecraft().currentScreen instanceof WindowManager))
				{
					if (updater.isUpdateAvailable())
					{
						if (!AIRI.windowApi().getWindowsRegistry().contains(this.updaterWindow))
						{
							AIRI.windowApi().addWindow(this.updaterWindow);
						}
						
						this.showWindowManager = true;
					}
				}

				updater.tick();

				if (this.recheckUpdates || this.showWindowManager)
				{
					if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu)
					{
						if (recheckUpdates)
						{
							(new Thread()
							{
								@Override
								public void run()
								{
									super.run();
									updater.downloadVersionInformation();
								}
							}).start();
						}

						if (updater.isUpdateAvailable() && !(AIRI.windowApi().getWindowsRegistry().size() <= 0))
						{
							AIRI.windowApi().showWindowManager();
						}

						this.showWindowManager = false;
					}
				}
			}

			this.recheckUpdates = false;
		}
	}

	public Updater createNewUpdater(IMod mod, String changelogUrl)
	{
		return createNewUpdater(mod.container().getModId(), mod.container().getMetadata().version, mod.container().getMetadata().updateUrl, mod.container().getMetadata().url, changelogUrl);
	}

	public Updater createNewUpdater(String modId, String curVer, String updateUrl, String downloadUrl, String changelogUrl)
	{
		return (new Updater(currentUpdaterId++, modId, curVer, updateUrl, downloadUrl, changelogUrl)).register();
	}

	public Updater getUpdaterByID(String ID)
	{
		Iterator<Updater> iterator = updaters.iterator();
		Updater updater;

		do
		{
			if (!iterator.hasNext())
				return null;

			updater = iterator.next();
		}
		while (!(updater.getVersionData().get("MODID").equalsIgnoreCase(ID)));

		return updater;
	}

	public ArrayList<Updater> getAvailableUpdates()
	{
		ArrayList<Updater> updates = new ArrayList<Updater>();

		for (Updater updater : this.updaters)
		{
			if (updater.isUpdateAvailable())
			{
				updates.add(updater);
			}
		}

		return updates;
	}

	public ArrayList<Updater> getUpdaterRegistry()
	{
		return this.updaters;
	}

	public void setRecheckForUpdates(boolean b)
	{
		this.recheckUpdates = b;
	}

	public boolean isUpdateAvailable()
	{
		return this.getAvailableUpdates().size() > 0;
	}

	public Window getUpdaterWindow()
	{
		return updaterWindow;
	}
}
