package com.arisux.airi.coremod;

import net.minecraft.client.Minecraft;

public class GlobalSettings
{
	public float gammaValue = Minecraft.getMinecraft().gameSettings.gammaSetting;
	
	public float getGammaValue()
	{
		return gammaValue = -200F;
	}
}
