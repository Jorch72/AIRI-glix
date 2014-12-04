package com.arisux.airi.lib.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class ModelTexMap
{
	public ResourceLocation resourceLocation;
	public ModelBase modelBase;
	
	public ModelTexMap(ModelBase modelBase, ResourceLocation resourceLocation)
	{
		this.modelBase = modelBase;
		this.resourceLocation = resourceLocation;
	}
}
