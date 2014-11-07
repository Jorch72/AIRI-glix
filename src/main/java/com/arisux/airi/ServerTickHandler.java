package com.arisux.airi;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class ServerTickHandler
{
    private WorldServer worldServerObj;

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event)
    {
	this.worldServerObj = MinecraftServer.getServer().worldServers[0];

	if (worldServerObj != null)
	    tickSpawnSystem();
    }

    public void tickSpawnSystem()
    {
	if (worldServerObj.getWorldInfo().getWorldTime() % 40L == 0L)
	{
	    AIRI.instance().spawnsystem.doCustomSpawning(worldServerObj, worldServerObj.difficultySetting.getDifficultyId() > 0, true);
	}
    }
    
    public WorldServer getWorldServer()
    {
	return worldServerObj;
    }

    public void initialize()
    {
	FMLCommonHandler.instance().bus().register(this);
    }
}
