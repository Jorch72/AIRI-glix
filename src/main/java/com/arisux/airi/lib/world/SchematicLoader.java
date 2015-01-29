package com.arisux.airi.lib.world;

import java.io.*;
import java.nio.file.Files;
import java.util.Collection;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.arisux.airi.AIRI;
import com.arisux.airi.lib.world.Schematic.UnsupportedSchematicFormatException;

public class SchematicLoader
{
	public static Schematic extractAndLoadSchematic(File extractionPath, InputStream resourceToExtract) throws UnsupportedSchematicFormatException
	{
		if (!getSchematicsDirectory().exists())
		{
			getSchematicsDirectory().mkdirs();
		}
		
		if (!extractionPath.exists())
		{
			try
			{
				Files.copy(resourceToExtract, extractionPath.getAbsoluteFile().toPath());
				AIRI.logger.info("Extracted %s", extractionPath.getAbsoluteFile().toPath());
			}
			catch (Exception e)
			{
				AIRI.logger.info("Error while extracting %s: %s", extractionPath, e);
			}
		}
		
		return loadSchematic(extractionPath);
	}
	
    public static Schematic loadSchematic(String name) throws Schematic.UnsupportedSchematicFormatException
    {
        if (FilenameUtils.getExtension(name).length() == 0)
        {
            name = name + ".schematic";
        }

        for (File file : getSchematicFiles())
        {
            if (file.getPath().endsWith(name) && name.endsWith(file.getName()))
            {
                return loadSchematic(file);
            }
        }

        return null;
    }

    public static Schematic loadSchematic(File extractionPath) throws Schematic.UnsupportedSchematicFormatException
    {
        NBTTagCompound compound = null;

        try (FileInputStream fileInputStream = new FileInputStream(extractionPath))
        {
            compound = CompressedStreamTools.readCompressed(fileInputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (compound != null)
        {
            return new Schematic(compound);
        }

        return null;
    }

    public static String[] currentSchematicFileNames()
    {
        Collection<File> files = getSchematicFiles();
        String[] filenames = new String[files.size()];
        int i = 0;
        
        for (File file : files)
        {
            filenames[i++] = FilenameUtils.getBaseName(file.getName());
        }
        
        return filenames;
    }

    public static Collection<File> getSchematicFiles()
    {
        return FileUtils.listFiles(getSchematicsDirectory(), new String[]{"schematic"}, true);
    }

    public static File getSchematicsDirectory()
    {
        return new File("./", "schematics");
    }
}
