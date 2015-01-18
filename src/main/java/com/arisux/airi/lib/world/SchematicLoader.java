package com.arisux.airi.lib.world;

import java.io.*;
import java.util.Collection;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class SchematicLoader
{
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

    public static Schematic loadSchematic(File file) throws Schematic.UnsupportedSchematicFormatException
    {
        NBTTagCompound compound = null;

        try (FileInputStream fileInputStream = new FileInputStream(file))
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
