package com.arisux.airi.api.wavefrontapi;

import com.arisux.airi.AIRI;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class WavefrontAPI
{
	private HashMap<String, WavefrontModel> tagToWavefrontHash = new HashMap<String, WavefrontModel>();

	public WavefrontModel get3DObj(String objName)
	{
		return tagToWavefrontHash.get(objName);
	}

	public WavefrontModel.Obj3DPart get3DObjPart(String objName, String partName)
	{
		WavefrontModel obj = get3DObj(objName);
		return obj != null ? obj.getPart(partName) : null;
	}

	public void draw(String objName, String partName)
	{
		WavefrontModel.Obj3DPart part = get3DObjPart(objName, partName);
		if (part != null) part.draw();
	}

	public void registerWavefrontModel(String modId, String objPath)
	{
		WavefrontModel obj = new WavefrontModel();

		if (obj.loadFile(modId, objPath))
		{
			String tag = objPath.replaceAll(".obj", "").replaceAll(".OBJ", "");
			tag = tag.substring(tag.lastIndexOf('/') + 1, tag.length());
			tagToWavefrontHash.put(tag, obj);

			AIRI.logger.info("[WavefrontAPI] Registered wavefront model: " + objPath);
		} else
		{
			AIRI.logger.info("[WavefrontAPI] Unable to register wavefront model: " + objPath);
		}
	}

	public void registerWavefrontModelDirectory(String modId, String objPath)
	{
		try
		{
			URL url = (AIRI.class.getResource("/assets/" + modId + objPath));
			System.out.println(url);
			System.out.println(url.toURI());

			if (url != null)
			{
				for (File file : (new File(url.toURI())).listFiles())
				{
					if (file.isFile() && file.getName().endsWith(".obj") || file.getName().endsWith(".OBJ"))
					{
						this.registerWavefrontModel(modId, objPath + "/" + file.getName());
					}

					if (file.isDirectory())
					{
						this.registerWavefrontModelDirectory(modId, objPath + "/" + file.getName());
					}
				}
			}
		} catch (Exception e)
		{
			AIRI.logger.bug("[Obj3DAPI] " + e.toString());
			e.printStackTrace();
		}
	}

	public HashMap<String, WavefrontModel> getRegisteredWavefrontHash()
	{
		return tagToWavefrontHash;
	}
}
