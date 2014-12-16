package com.arisux.airi.lib.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

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
	
	public ModelBaseExtension asModelBaseExtension()
	{
		return (ModelBaseExtension) this.model;
	}
	
	public ResourceLocation asResourceLocation()
	{
		return this.resourceLocation;
	}
}
