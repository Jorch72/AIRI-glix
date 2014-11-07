package com.arisux.airi.api.updater;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

import com.arisux.airi.AIRI;
import com.arisux.airi.Settings;
import com.arisux.airi.api.window.Window;
import com.arisux.airi.api.window.WindowManager;
import com.arisux.airi.api.window.windows.WindowUpdates;

public class UpdaterAPI
{
	private ArrayList<Updater> updaters = new ArrayList<Updater>();
	private int currentUpdaterId;
	private boolean recheckUpdates, showWindowManager;
	private Window updaterWindow;

	public UpdaterAPI()
	{
		this.showWindowManager = true;
	}

	public void onTick()
	{
		if (AIRI.instance().settings.propertyList.get(Settings.Setting.NETWORKING).getBoolean())
		{
			if (AIRI.instance().updaterapi.isUpdateAvailable())
			{
				if (this.updaterWindow == null)
				{
					this.updaterWindow = new WindowUpdates("UpdateWindow", "Update Available", -100, 20, 250, 200);
					AIRI.instance().windowapi.addWindow(this.updaterWindow);
				}
			}
			
			for (final Updater updater : updaters)
			{
				if (!(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) && !(Minecraft.getMinecraft().currentScreen instanceof WindowManager))
				{
					if (updater.isUpdateAvailable())
					{
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
							(new Thread(){
								@Override
								public void run()
								{
									super.run();

									updater.clearCaches();
									updater.downloadVersionInformation();
								}
							}).start();
							this.recheckUpdates = false;
						}

						if (updater.isUpdateAvailable() && !(AIRI.instance().windowapi.getWindowsRegistry().size() <= 0))
						{
							AIRI.instance().windowapi.showWindowManager();
						}

						this.recheckUpdates = false;
						this.showWindowManager = false;
					}
				}

			}
		}
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

			updater = (Updater) iterator.next();
		} while (!(updater.getVersionData().get("MODID").equalsIgnoreCase(ID)));

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
