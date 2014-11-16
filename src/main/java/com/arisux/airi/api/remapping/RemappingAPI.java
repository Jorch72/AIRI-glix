package com.arisux.airi.api.remapping;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import com.arisux.airi.AIRI;
import com.arisux.airi.lib.util.interfaces.IInitializablePre;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.registry.GameRegistry;

public class RemappingAPI implements IInitializablePre
{
	private ArrayList<RemappedMod> modsToRemap = new ArrayList<RemappedMod>();

	public RemappingAPI()
	{
		;
	}
	
	@Override
	public void preInitialize(FMLPreInitializationEvent event)
	{
		;
	}

	public void onLoadMissingMapping(FMLMissingMappingsEvent event)
	{
		AIRI.logger.warning("Invalid mappings found, searching re-mapping registry for re-mapped mods...");

		for (MissingMapping mapping : event.getAll())
		{
			for (RemappedMod mod : this.modsToRemap)
			{
				if (mapping.name.contains(mod.getOldID()))
				{
					try
					{
						if (Class.forName(mod.getClassLocation()) != null)
						{
							moveMappingToModByID(mapping, mod.getOldID() + ":", mod.getNewID() + ":");
						}
					}
					catch (ClassNotFoundException e)
					{
						AIRI.logger.warning("Invalid mappings were detected, but the mod targetted for the new mappings was not present: " + mod.classLocation);
					}
				}
			}
		}
	}

	public void registerRemappedMod(String oldID, String newID, String modClassLocation)
	{
		this.modsToRemap.add(new RemappedMod(oldID, newID, modClassLocation));
	}

	public void moveMappingToModByID(MissingMapping mapping, String oldID, String newID)
	{
		String newName = (mapping.name).replace(oldID, newID);

		/** Check for and replace missing item mappings **/
		if (mapping.type == GameRegistry.Type.ITEM)
		{
			AIRI.logger.info("Converting item mapping [" + mapping.name + "@" + mapping.id + "] -> [" + newName + "@" + mapping.id + "]");

			Item item = (Item) Item.itemRegistry.getObject(newName);

			if (item != null)
				mapping.remap(item);
			else
				AIRI.logger.warning("Error converting item mapping [" + mapping.name + "@" + mapping.id + "]");
		}

		/** Check for and replace missing block mappings **/
		if (mapping.type == GameRegistry.Type.BLOCK)
		{
			Block block = (Block) Block.blockRegistry.getObject(newName);

			AIRI.logger.info("Converting block mapping [" + mapping.name + "@" + mapping.id + "] -> [" + newName + "@" + mapping.id + "]");

			if (block != null)
				mapping.remap(block);
			else
				AIRI.logger.warning("Error converting block mapping. [" + mapping.name + "@" + mapping.id + "]");
		}
	}
}
