package com.arisux.airi.api.updater;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;

import com.arisux.airi.AIRI;
import com.arisux.airi.Settings;
import com.arisux.airi.engine.WorldEngine;
import com.arisux.airi.lib.util.NetworkUtil;
import com.arisux.airi.lib.util.interfaces.IInitializablePost;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Updater implements IInitializablePost
{
	private HashMap<String, String> versionData = new HashMap<String, String>();
	private String modId, curVer, updateUrl, downloadUrl, changelogUrl;
	private Thread changelogDownloadThread;
	private Changelog changelog;
	private int updaterId;

	public Updater(int updaterId, String modId, String curVer, String updateUrl, String downloadUrl, String changelogUrl)
	{
		this.updaterId = updaterId;
		this.modId = modId;
		this.curVer = curVer;
		this.updateUrl = updateUrl;
		this.downloadUrl = downloadUrl;
		this.changelogUrl = changelogUrl;
	}

	@Override
	public void postInitialize(FMLPostInitializationEvent event)
	{
		if (AIRI.instance().settings.propertyList.get(Settings.Setting.NETWORKING).getBoolean())
		{
			downloadVersionInformation();
		}
	}

	public Updater register()
	{
		AIRI.updaterApi().getUpdaterRegistry().add(this);
		return this;
	}

	@SideOnly(Side.CLIENT)
	public void printUpdateInformation(EntityPlayer thePlayer)
	{
		if (isVersionDataValid() && isUpdateAvailable())
		{
			String updateString = "Update: " + getVersionData().get("MODID") + " " + getVersionData().get("MODVER") + " for Minecraft " + getVersionData().get("MCVER");
			AIRI.logger.info(updateString);
			WorldEngine.Entities.Players.sendToChat(thePlayer, updateString);
		}
	}

	public void tick()
	{
		if (this.changelog == null && this.changelogDownloadThread == null)
		{
			this.changelogDownloadThread = new Thread()
			{
				@Override
				public void run()
				{
					downloadChangelog();
				}
			};
			this.changelogDownloadThread.start();
		}
	}

	public void downloadVersionInformation()
	{
		this.isVersionDataValid();

		String retrieved = NetworkUtil.getURLContents(updateUrl);

		if (retrieved != null)
		{
			String[] parsed = retrieved.split(":");

			getVersionData().put("MCVER", parsed[0]);
			getVersionData().put("MODVER", parsed[1]);
			getVersionData().put("FORGEVER", parsed[2]);
			getVersionData().put("MODID", parsed[3]);
		} else
		{
			this.printConnectionError();
		}
	}

	public void downloadChangelog()
	{
		try
		{
			if (isUpdateAvailable() && changelogUrl != null)
			{
				String preParsedChangelog = NetworkUtil.getURLContents(changelogUrl, true);

				if (preParsedChangelog != null)
				{
					changelog = new Changelog(preParsedChangelog);
				} else
				{
					printConnectionError();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public int getUpdaterId()
	{
		return updaterId;
	}

	public HashMap<String, String> getVersionData()
	{
		return this.versionData;
	}

	public boolean isVersionDataValid()
	{
		this.versionData = this.versionData == null ? new HashMap<String, String>() : this.versionData;
		return (getVersionData() != null && getVersionData().get("MCVER") != null && getVersionData().get("MODVER") != null && getVersionData().get("FORGEVER") != null && getVersionData().get("MODID") != null);
	}

	public boolean isUpdateAvailable()
	{
		return (isVersionDataValid() && getVersionData().get("MODID").toString().equalsIgnoreCase(modId) && !getVersionData().get("MODVER").toString().equalsIgnoreCase(curVer));
	}

	public void printConnectionError()
	{
		AIRI.logger.warning("Could not check for updates for mod with ID " + modId);
	}

	public void clearCaches()
	{
		this.versionData = null;
	}

	public Changelog getChangelog()
	{
		return changelog;
	}

	public String getChangelogUrl()
	{
		return changelogUrl;
	}

	public String getCurVer()
	{
		return curVer;
	}

	public String getDownloadUrl()
	{
		return downloadUrl;
	}

	public String getModId()
	{
		return modId;
	}

	public String getUpdateUrl()
	{
		return updateUrl;
	}
}
