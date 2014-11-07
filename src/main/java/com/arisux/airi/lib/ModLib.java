package com.arisux.airi.lib;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.arisux.airi.AIRI;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ModLib
{
	/**
	 * Returns if the current Minecraft installation is running 
	 * in a development environment or normal environment.
	 * 
	 * @return Returns true if in a dev environment. Returns false if other.
	 */
	public static boolean isDevelopmentEnvironment()
	{
		return (Boolean) net.minecraft.launchwrapper.Launch.blackboard.get("fml.deobfuscatedEnvironment");
	}

	/**
	 * Extracts a file with specified location from the specified 
	 * mod's java archive to the specified file instance.
	 * 
	 * @param modClass - Mod class to retrieve files from.
	 * @param filePath - File path to retrieve a file from.
	 * @param to - File instance that the information will be saved to.
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private static void copyFileFromModJarAssets(Class<?> modClass, String filePath, File to) throws IOException
	{
		AIRI.logger.info("Extracting %s from %s jar", filePath, modClass.getSimpleName());
		URL url = modClass.getResource("/assets/" + filePath);
		FileUtils.copyURLToFile(url, to);
	}

	/** 
	 * Retrieve the ModContainer instance for a mod with the specified ID.
	 * 
	 * @param id - ID of the mod retrieving an instance from.
	 * @return An instance of ModContainer that is assigned to this ID.
	 */
	public static ModContainer getModContainerForId(String id)
	{
		for (ModContainer container : Loader.instance().getModList())
		{
			if (container.getModId().equalsIgnoreCase(id))
				return container;
		}

		return null;
	}
}
