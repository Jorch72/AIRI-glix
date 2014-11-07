package com.arisux.airi.coremod;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion(value="1.7.10")
public class FMLPlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { AIRIAccessTransformer.class.getName(), ASMTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public String getAccessTransformerClass()
	{
		return (AIRIAccessTransformer.class).getName();
	}
	
	@Override
	public void injectData(Map<String, Object> data)
	{
		;
	}
}