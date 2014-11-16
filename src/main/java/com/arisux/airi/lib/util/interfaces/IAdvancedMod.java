package com.arisux.airi.lib.util.interfaces;

import cpw.mods.fml.common.Mod;

public interface IAdvancedMod extends IStandardMod
{
	@Mod.EventHandler
	public void listenOnInterModComms(cpw.mods.fml.common.event.FMLInterModComms event);
	
	@Mod.EventHandler
	public void missingMappingEvent(cpw.mods.fml.common.event.FMLMissingMappingsEvent event);
	
	@Mod.EventHandler
	public void modDisabledEvent(cpw.mods.fml.common.event.FMLModDisabledEvent event);
	
	@Mod.EventHandler
	public void modIdMappingEvent(cpw.mods.fml.common.event.FMLModIdMappingEvent event);
	
	@Mod.EventHandler
	public void serverAboutToStart(cpw.mods.fml.common.event.FMLServerAboutToStartEvent event);
	
	@Mod.EventHandler
	public void serverStarting(cpw.mods.fml.common.event.FMLServerStartingEvent event);
	
	@Mod.EventHandler
	public void serverStarted(cpw.mods.fml.common.event.FMLServerStartedEvent event);
	
	@Mod.EventHandler
	public void serverStopping(cpw.mods.fml.common.event.FMLServerStoppingEvent event);
	
	@Mod.EventHandler
	public void serverStopped(cpw.mods.fml.common.event.FMLServerStoppedEvent event);
}
