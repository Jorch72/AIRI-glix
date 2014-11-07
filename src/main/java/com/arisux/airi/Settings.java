package com.arisux.airi;

import java.util.HashMap;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Settings
{
	public Configuration config;
	public HashMap<Setting, Property> propertyList = new HashMap<Setting, Property>();

	public enum Setting
	{
		NETWORKING(),
		MAX_MOBS(),
		MAX_ANIMALS(),
		MAX_AQUATIC();

		public static String get(Setting field)
		{
			return field.toString();
		}
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		config = new Configuration(evt.getSuggestedConfigurationFile());
		try
		{
			config.load();

			propertyList.put(Setting.NETWORKING, config.get(Configuration.CATEGORY_GENERAL, Setting.get(Setting.NETWORKING), true, "Toggles all networking for mods developed with AIRI."));
			propertyList.put(Setting.MAX_MOBS, config.get(Configuration.CATEGORY_GENERAL, Setting.get(Setting.MAX_MOBS), 130, "Maximum amount of monsters that can spawn using the AIRI spawn system."));
			propertyList.put(Setting.MAX_ANIMALS, config.get(Configuration.CATEGORY_GENERAL, Setting.get(Setting.MAX_ANIMALS), 50, "Maximum amount of animals that can spawn using the AIRI spawn system."));
			propertyList.put(Setting.MAX_AQUATIC, config.get(Configuration.CATEGORY_GENERAL, Setting.get(Setting.MAX_AQUATIC), 50, "Maximum amount of aquatic mobs that can spawn using the AIRI spawn system."));
		} finally
		{
			config.save();
		}
	}
}