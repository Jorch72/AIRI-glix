package com.arisux.airi.lib.util.interfaces;

import com.arisux.airi.lib.util.ModProperties;

import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IStandardMod extends IInitializablePre, IInitializable, IInitializablePost
{
	public ModProperties getModProperties();
	
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTab();
}
