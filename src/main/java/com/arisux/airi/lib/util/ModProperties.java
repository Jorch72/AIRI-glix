package com.arisux.airi.lib.util;

import java.util.List;

import com.arisux.airi.engine.ModEngine;

import cpw.mods.fml.common.ModContainer;

public abstract class ModProperties
{
	public ModContainer modContainer;
	
	public ModProperties(String modid)
	{
		this.modContainer = ModEngine.getModContainerForId(modid);
	}

	public ModContainer getModContainer()
	{
		return (ModContainer) (this.modContainer == null ? this.modContainer == ModEngine.getModContainerForId(this.getId()) : this.modContainer);
	}
	
	public String getId()
	{
		return this.getModContainer().getModId();
	}
	
	public String getName()
	{
		return this.getModContainer().getName();
	}
	
	public String getVersion()
	{
		return this.getModContainer().getVersion();
	}
	
	public String getDomain()
	{
		return this.getId() + ":";
	}
	
	public String getChannel()
	{
		return "CHANNEL_" + this.getId().toUpperCase();
	}
	
	public abstract String getDescription();
	
	public abstract String getCredits();
	
	public abstract List<String> getAuthors();
	
	public abstract String getUrl();
	
	public abstract String getUpdateStringUrl();
	
	public abstract String getChangelogUrl();
}