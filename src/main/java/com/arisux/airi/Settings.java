package com.arisux.airi;

import net.minecraftforge.common.config.Configuration;

import com.arisux.airi.lib.ModUtil;
import com.arisux.airi.lib.interfaces.IInitializablePre;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Settings implements IInitializablePre
{
	private final String CATEGORY_URLS = "URLS";
	private String serverMain;
	private String serverDev;
	private boolean networking;

	@Override
	@Mod.EventHandler
	public void preInitialize(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			config.load();

			serverMain = config.get(CATEGORY_URLS, "SERVER_MAIN", "http://arisux.x10.mx", "").getString();
			serverDev = config.get(CATEGORY_URLS, "SERVER_DEV", "http://arisux.x10.mx", "").getString();
			networking = config.get(Configuration.CATEGORY_GENERAL, "NETWORKING", true, "Toggles networking for mods that route external network access through AIRI.").getBoolean();
		} finally
		{
			config.save();
		}
	}
	
	public String getServer()
	{
		return ModUtil.isDevEnvironment() ? serverDev : serverMain;
	}
	
	public boolean isNetworkingEnabled()
	{
		return networking;
	}
	
	public void setNetworking(boolean networking)
	{
		this.networking = networking;
	}
}