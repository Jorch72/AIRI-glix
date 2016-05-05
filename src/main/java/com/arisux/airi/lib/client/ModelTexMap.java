package com.arisux.airi.lib.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTexMap
{
	private ResourceLocation resourceLocation;
	private ModelBase model;
	
	public ModelTexMap(ModelBase model, ResourceLocation resourceLocation)
	{
		this.model = model;
		this.resourceLocation = resourceLocation;
	}
	
	public ModelBase asModelBase()
	{
		return this.model;
	}
	
	public ModelBaseWrapper asModelBaseExtension()
	{
		return (ModelBaseWrapper) this.model;
	}
	
	public ResourceLocation asResourceLocation()
	{
		return this.resourceLocation;
	}
}
