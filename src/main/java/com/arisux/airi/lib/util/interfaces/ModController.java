package com.arisux.airi.lib.util.interfaces;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.JsonUtils;

import com.arisux.airi.engine.ModEngine;
import com.google.gson.JsonElement;

import cpw.mods.fml.common.ModContainer;

public abstract class ModController implements IInitializablePre, IInitializable, IInitializablePost
{
	private String modId, changelogUrl, beta;
	private ModContainer modContainer;
	private JsonElement modInfoJson;
	
	public abstract CreativeTabs getCreativeTab();
	
	public Object mod()
	{
		return this.container().getMod();
	}
	
	public ModContainer container()
	{
		return this.modContainer == null ? this.modContainer = ModEngine.getModContainerForId(this.id()) : this.modContainer;
	}
	
	public String id()
	{
		return this.modId == null ? this.modId = ModEngine.getAnnotatedModId(this.getClass()) : this.modId;
	}
	
	public String domain()
	{
		return this.modId + ":";
	}
	
	public String channel()
	{
		return "CHANNEL_" + this.id().toUpperCase();
	}
	
	public String changelogUrl()
	{
		return this.changelogUrl == null ? this.getModInfoJson() != null ? this.changelogUrl = JsonUtils.getJsonElementAsJsonObject(this.getModInfoJson(), "changelogUrl").get("changelogUrl").getAsString() : "" : this.changelogUrl;
	}
	
	public boolean isBetaRelease()
	{
		return Boolean.parseBoolean(this.beta == null ? this.beta = String.valueOf(JsonUtils.getJsonElementAsJsonObject(this.getModInfoJson(), "beta").get("beta").getAsBoolean()) : this.beta);
	}
	
	public JsonElement getModInfoJson()
	{
		return this.modInfoJson == null ? this.modInfoJson = ModEngine.parseJsonFromFile(new File(getClass().getResource("/mcmod.info").getFile())) : this.modInfoJson;
	}
}
