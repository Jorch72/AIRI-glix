package com.arisux.airi.api.wavefrontapi;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.utils.URIUtils;

import com.arisux.airi.AIRI;
import com.sun.jndi.toolkit.url.UrlUtil;

import cpw.mods.fml.common.ZipperUtil;
import sun.net.util.URLUtil;

public class WavefrontAPI
{
	private HashMap<String, WavefrontModel> modelMap = new HashMap<String, WavefrontModel>();

	public WavefrontModel getModel(String objName)
	{
		return modelMap.get(objName);
	}

	public WavefrontModel.Part getPart(String objName, String partName)
	{
		WavefrontModel model = getModel(objName);
		return model != null ? model.getPart(partName) : null;
	}

	public void renderPart(String objName, String partName)
	{
		WavefrontModel.Part part = getPart(objName, partName);
		if (part != null) part.draw();
	}
	
	/**
	 * Extract a wavefront model from the provided URL to the provided File path and then load it.
	 * 
	 * @param path - The path we are extracting to and loading the model from.
	 * @param url - The URL path we are extracting the resource from.
	 * @return - The model that was loaded. Null if the model was not loaded.
	 */
	public WavefrontModel loadModel(Class<?> c, String modid, String model, String url)
	{
		File baseDir = new File(getModelsDirectory(), String.format("%s/", modid));
		File path = new File(baseDir, model);
		
		if (!getModelsDirectory().exists())
		{
			getModelsDirectory().mkdirs();
		}
		
		if (!baseDir.exists())
		{
			baseDir.mkdirs();
		}
		
		try
		{
			URL urlDirectory = c.getResource(url);
			File pathDirectory = path;
			URL urlModel = c.getResource(url + ".obj");
			File pathModel = new File(path.getAbsolutePath() + ".obj");
			URL urlTexture = c.getResource(url + ".mtl");
			File pathTexture = new File(path.getAbsolutePath() + ".mtl");

			if (!pathDirectory.exists())
			{
				FileUtils.copyDirectoryToDirectory(org.apache.logging.log4j.core.helpers.FileUtils.fileFromURI(urlDirectory.toURI()), baseDir);
				AIRI.logger.info("Extracted resource directory: %s", pathDirectory.getAbsoluteFile().getPath());
			}
			
			if (!pathModel.exists())
			{
				FileUtils.copyURLToFile(urlModel, pathModel);
				AIRI.logger.info("Extracted wavefront model: %s", pathModel.getAbsoluteFile().getPath());
			}
			
			if (!pathTexture.exists())
			{
			    FileUtils.copyURLToFile(urlTexture, pathTexture);
				AIRI.logger.info("Extracted wavefront texture: %s", pathTexture.getAbsoluteFile().getPath());
			}
		}
		catch (Exception e)
		{
			AIRI.logger.info("Error while extracting %s: %s", path, e);
			e.printStackTrace();
		}
		
		return loadModel(modid, path);
	}

	/**
	 * Load a wavefront model from the provided path.
	 * 
	 * @param path - The path we are extracting to and loading the model from.
	 * @param resource - The URL path we are extracting the resource from.
	 * @return - The model that was loaded. Null if the model was not loaded.
	 */
	public WavefrontModel loadModel(String modid, File path)
	{
		WavefrontModel model = new WavefrontModel();

		if (model.loadFile(modid, path.getAbsolutePath()))
		{
			String tag = path.getAbsolutePath().replaceAll(".obj", "").replaceAll(".OBJ", "");
			tag = tag.substring(tag.lastIndexOf('/') + 1, tag.length());
			modelMap.put(tag, model);

			AIRI.logger.info("[WavefrontAPI] Loaded wavefront model: " + path);
		}
		else
		{
			AIRI.logger.info("[WavefrontAPI] Unable to load wavefront model: " + path);
		}
		
		return model;
	}

//	public void loadModels(String modId, String objPath)
//	{
//		try
//		{
//			URL url = (AIRI.class.getResource("/assets/" + modId + objPath));
//			System.out.println(url);
//			System.out.println(url.toURI());
//
//			if (url != null)
//			{
//				for (File file : (new File(url.toURI())).listFiles())
//				{
//					if (file.isFile() && file.getName().endsWith(".obj") || file.getName().endsWith(".OBJ"))
//					{
//						this.loadModel(modId, objPath + "/" + file.getName());
//					}
//
//					if (file.isDirectory())
//					{
//						this.loadModels(modId, objPath + "/" + file.getName());
//					}
//				}
//			}
//		} catch (Exception e)
//		{
//			AIRI.logger.bug("[WavefrontAPI] " + e.toString());
//			e.printStackTrace();
//		}
//	}

	public HashMap<String, WavefrontModel> getModelMap()
	{
		return modelMap;
	}
	
    /**
     * @return - The File instance of the models directory.
     */
    public static File getModelsDirectory()
    {
        return new File("models");
    }
}
