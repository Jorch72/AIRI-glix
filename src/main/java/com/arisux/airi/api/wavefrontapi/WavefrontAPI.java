package com.arisux.airi.api.wavefrontapi;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.arisux.airi.AIRI;

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
	 * @param resource - The URL path we are extracting the resource from.
	 * @return - The model that was loaded. Null if the model was not loaded.
	 */
	public WavefrontModel loadModel(String modid, File path, URL resource)
	{
		if (!getModelsDirectory().exists())
		{
			getModelsDirectory().mkdirs();
		}
		
		try
		{
		    FileUtils.copyURLToFile(resource, path);
			AIRI.logger.info("Extracted %s", path.getAbsoluteFile().getPath());
		}
		catch (Exception e)
		{
			AIRI.logger.info("Error while extracting %s: %s", path, e);
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
        return new File("./", "models");
    }
}
