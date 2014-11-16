package com.arisux.airi.coremod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.Session;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AccessHandler
{
	@SideOnly(Side.CLIENT)
	public static float getEquippedProgress()
	{
		return Minecraft.getMinecraft().entityRenderer.itemRenderer.equippedProgress;
	}
	
	@SideOnly(Side.CLIENT)
	public static void setEquippedProgress(float value)
	{
		Minecraft.getMinecraft().entityRenderer.itemRenderer.equippedProgress = value;
	}
	
	@SideOnly(Side.CLIENT)
	public static int getRightClickDelayTimer()
	{
		return Minecraft.getMinecraft().rightClickDelayTimer;
	}
	
	@SideOnly(Side.CLIENT)
	public static void setRightClickDelayTimer(int value)
	{
		Minecraft.getMinecraft().rightClickDelayTimer = value;
	}
	
	@SideOnly(Side.CLIENT)
	public static Session getSession()
	{
		return Minecraft.getMinecraft().session;
	}
	
	@SideOnly(Side.CLIENT)
	public static float getTorchFlickerX()
	{
		return Minecraft.getMinecraft().entityRenderer.torchFlickerX;
	}
	
	@SideOnly(Side.CLIENT)
	public static void setTorchFlickerX(float value)
	{
		Minecraft.getMinecraft().entityRenderer.torchFlickerX = value;
	}
	
	@SideOnly(Side.CLIENT)
	public static float getBossColorModifier()
	{
		return Minecraft.getMinecraft().entityRenderer.bossColorModifier;
	}
	
	@SideOnly(Side.CLIENT)
	public static void setBossColorModifier(float value)
	{
		Minecraft.getMinecraft().entityRenderer.bossColorModifier = value;
	}
	
	@SideOnly(Side.CLIENT)
	public static float getBossColorModifierPrev()
	{
		return Minecraft.getMinecraft().entityRenderer.bossColorModifierPrev;
	}
	
	@SideOnly(Side.CLIENT)
	public static void getBossColorModifierPrev(float value)
	{
		Minecraft.getMinecraft().entityRenderer.bossColorModifierPrev = value;
	}
	
	@SideOnly(Side.CLIENT)
	public static int[] getLightmapColors()
	{
		return Minecraft.getMinecraft().entityRenderer.lightmapColors;
	}
	
	@SideOnly(Side.CLIENT)
	public static DynamicTexture getLightmapTexture()
	{
		return Minecraft.getMinecraft().entityRenderer.lightmapTexture;
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isLightmapUpdateNeeded()
	{
		return Minecraft.getMinecraft().entityRenderer.lightmapUpdateNeeded;
	}
	
	@SideOnly(Side.CLIENT)
	public static void setLightmapUpdateNeeded(boolean value)
	{
		Minecraft.getMinecraft().entityRenderer.lightmapUpdateNeeded = value;
	}
}
