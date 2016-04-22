package com.arisux.airi.api.updater;

import java.util.ArrayList;
import java.util.Iterator;

import com.arisux.airi.AIRI;
import com.arisux.airi.api.window.gui.OverlayWindowManager;
import com.arisux.airi.api.window.gui.windows.Window;
import com.arisux.airi.api.window.gui.windows.WindowUpdates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class UpdaterAPI
{
	private ArrayList<Updater> updaters = new ArrayList<Updater>();
	private boolean recheckUpdates, canRecheckForUpdates;
	private Window updaterWindow;

	public void onTick()
	{
		if (AIRI.settings().isNetworkingEnabled())
		{
			if (Minecraft.getMinecraft().theWorld != null && !Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().theWorld.getWorldTime() % (20 * 60 * 30) == 1)
			{
				this.recheckUpdates = true;
			}
			
			if (!(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) && !(Minecraft.getMinecraft().currentScreen instanceof OverlayWindowManager))
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
				if (!(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) && !(Minecraft.getMinecraft().currentScreen instanceof OverlayWindowManager))
				{
					if (updater.isUpdateAvailable())
					{
						if (!AIRI.windowApi().getWindowsRegistry().contains(this.updaterWindow))
						{
							AIRI.windowApi().addWindow(this.updaterWindow);
						}
					}
				}

				updater.tick();

				if (this.recheckUpdates)
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
					}
				}
			}

			this.recheckUpdates = false;
		}
	}
	
	public void register(Updater updater)
	{
		this.updaters.add(updater);
	}

	public Updater getUpdaterByModIdentifier(String modid)
	{
		Iterator<Updater> iterator = updaters.iterator();
		Updater updater;

		do
		{
			if (!iterator.hasNext())
				return null;

			updater = iterator.next();
		}
		while (!(updater.getVersionData().get("MODID").equalsIgnoreCase(modid)));

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
