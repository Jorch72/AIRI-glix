package com.arisux.airi.api.obj3dapi;

import com.arisux.airi.AIRI;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class Obj3DAPI
{
	private HashMap<String, Obj3D> tagTo3DObjHash = new HashMap<String, Obj3D>();

	public Obj3D get3DObj(String objName)
	{
		return tagTo3DObjHash.get(objName);
	}

	public Obj3D.Obj3DPart get3DObjPart(String objName, String partName)
	{
		Obj3D obj = get3DObj(objName);
		return obj != null ? obj.getPart(partName) : null;
	}

	public void draw(String objName, String partName)
	{
		Obj3D.Obj3DPart part = get3DObjPart(objName, partName);
		if (part != null) part.draw();
	}

	public void register3DObj(String modId, String objPath)
	{
		Obj3D obj = new Obj3D();

		if (obj.loadFile(modId, objPath))
		{
			String tag = objPath.replaceAll(".obj", "").replaceAll(".OBJ", "");
			tag = tag.substring(tag.lastIndexOf('/') + 1, tag.length());
			tagTo3DObjHash.put(tag, obj);

			AIRI.logger.info("[3DObjAPI] Registered model: " + objPath);
		} else
		{
			AIRI.logger.info("[3DObjAPI] Unable to register model: " + objPath);
		}
	}

	public void register3DObjDirectory(String modId, String objPath)
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
						this.register3DObj(modId, objPath + "/" + file.getName());
					}

					if (file.isDirectory())
					{
						this.register3DObjDirectory(modId, objPath + "/" + file.getName());
					}
				}
			}
		} catch (Exception e)
		{
			AIRI.logger.bug("[Obj3DAPI] " + e.toString());
			e.printStackTrace();
		}
	}

	public HashMap<String, Obj3D> getRegistered3DObjHash()
	{
		return tagTo3DObjHash;
	}
}
