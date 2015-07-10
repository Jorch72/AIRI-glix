package com.arisux.airi.api.wavefrontapi;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.arisux.airi.AIRI;
import com.arisux.airi.lib.RenderUtil.UV;
import com.arisux.airi.lib.RenderUtil.Vertex;

public class WavefrontModel
{
	String pathName, mtlName, mod, directory;

	public ArrayList<Vertex> vertex = new ArrayList<Vertex>();
	public ArrayList<UV> uv = new ArrayList<UV>();
	public Hashtable<String, String> nameToStringHash = new Hashtable<String, String>();
	public Hashtable<String, Part> nameToPartHash = new Hashtable<String, Part>();
	
	public float xDim, yDim, zDim;
	public float xMin, yMin, zMin;
	public float xMax, yMax, zMax;
	public float dimMax, dimMaxInv;

	public WavefrontModel()
	{
		;
	}

	public static class FaceGroup
	{
		public String mtlName;
		public ResourceLocation resource;
		public ArrayList<Face> face = new ArrayList<Face>();
		public boolean listReady = false;
		public int glList;

		public void bindTexture()
		{
			Minecraft.getMinecraft().renderEngine.bindTexture(resource);
		}

		public void draw()
		{
			if (resource != null)
			{
				bindTexture();
				drawNoBind();
			}
			else
			{
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				drawNoBind();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
		}

		private void drawVertex()
		{
			int mode = 0;

			for (Face f : face)
			{
				if (f.vertexNbr != mode)
				{
					if (mode != 0)
						GL11.glEnd();

					switch (f.vertexNbr)
					{
						case 3:
							GL11.glBegin(GL11.GL_TRIANGLES);
							break;
						case 4:
							GL11.glBegin(GL11.GL_QUADS);
							break;
						case 6:
							GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
							break;
						case 8:
							GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
							break;
					}

					mode = f.vertexNbr;
				}

				GL11.glNormal3f(f.normal.x, f.normal.y, f.normal.z);
				for (int idx = 0; idx < mode; idx++)
				{
					if (f.uv[idx] != null)
					{
						GL11.glTexCoord2f(f.uv[idx].u, f.uv[idx].v);
					}
					GL11.glVertex3f(f.vertex[idx].x, f.vertex[idx].y, f.vertex[idx].z);
				}
			}

			if (mode != 0)
				GL11.glEnd();
		}

		public void drawNoBind()
		{
			if (listReady == false)
			{
				listReady = true;
				glList = GL11.glGenLists(1);

				GL11.glNewList(glList, GL11.GL_COMPILE);
				this.drawVertex();
				GL11.glEndList();

			}

			GL11.glCallList(glList);

		}

	}

	public class Part
	{
		ArrayList<Vertex> vertex;
		ArrayList<UV> uv;

		public double xMin, yMin, zMin;
		public double xMax, yMax, zMax;
		private float ox, oy, oz;
		private float ox2, oy2, oz2;

		void addVertex(Vertex v)
		{
			vertex.add(v);
			xMin = Math.min(xMin, v.x);
			yMin = Math.min(yMin, v.y);
			zMin = Math.min(zMin, v.z);
			xMax = Math.max(xMax, v.x);
			yMax = Math.max(yMax, v.y);
			zMax = Math.max(zMax, v.z);
		}

		ArrayList<FaceGroup> faceGroup = new ArrayList<FaceGroup>();
		Hashtable<String, Float> nameToFloatHash = new Hashtable<String, Float>();

		public Part(ArrayList<Vertex> vertex, ArrayList<UV> uv)
		{
			this.vertex = vertex;
			this.uv = uv;
		}

		public float getFloat(String name)
		{
			return nameToFloatHash.get(name);
		}

		public void draw(float angle, float x, float y, float z)
		{
			GL11.glPushMatrix();

			GL11.glTranslatef(ox, oy, oz);
			GL11.glRotatef(angle, x, y, z);
			GL11.glTranslatef(-ox, -oy, -oz);
			this.draw();

			GL11.glPopMatrix();
		}

		public void draw(float angle, float x, float y, float z, float angle2, float x2, float y2, float z2)
		{
			GL11.glPushMatrix();

			GL11.glTranslatef(ox, oy, oz);
			GL11.glRotatef(angle, x, y, z);
			GL11.glTranslatef(ox2, oy2, oz2);
			GL11.glRotatef(angle2, x2, y2, z2);
			GL11.glTranslatef(-ox2, -oy2, -oz2);
			GL11.glTranslatef(-ox, -oy, -oz);
			this.draw();

			GL11.glPopMatrix();
		}

		public void drawNoBind(float angle, float x, float y, float z)
		{
			GL11.glPushMatrix();

			GL11.glTranslatef(ox, oy, oz);
			GL11.glRotatef(angle, x, y, z);
			GL11.glTranslatef(-ox, -oy, -oz);
			this.drawNoBind();

			GL11.glPopMatrix();
		}

		public void drawNoBind()
		{

			for (FaceGroup fg : faceGroup)
			{
				fg.drawNoBind();
			}
		}

		public void draw()
		{
			for (FaceGroup fg : faceGroup)
			{
				fg.draw();
			}
		}
	}

	public class Normal
	{
		public float x, y, z;

		public Normal(float x, float y, float z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Normal(String[] value)
		{
			x = Float.parseFloat(value[0]);
			y = Float.parseFloat(value[1]);
			z = Float.parseFloat(value[2]);
		}

		public Normal(Vertex o, Vertex a, Vertex b)
		{
			float a_x = a.x - o.x;
			float a_y = a.y - o.y;
			float a_z = a.z - o.z;

			float b_x = b.x - o.x;
			float b_y = b.y - o.y;
			float b_z = b.z - o.z;

			x = a_y * b_z - a_z * b_y;
			y = a_z * b_x - a_x * b_z;
			z = a_x * b_y - a_y * b_x;

			float norme = (float) Math.sqrt(x * x + y * y + z * z);

			x /= norme;
			y /= norme;
			z /= norme;
		}
	}

	public class Face
	{
		public Vertex[] vertex;
		public UV[] uv;
		private Normal normal;
		public int vertexNbr;

		public Face(Vertex[] vertex, UV[] uv, Normal normal)
		{
			this.vertex = vertex;
			this.uv = uv;
			this.normal = normal;
			vertexNbr = vertex.length;
		}
	}

	public ResourceLocation getAlternativeTexture(String name)
	{
		ResourceLocation resource = new ResourceLocation(mod, directory.substring(1) + name);
		return resource;
	}

	public boolean loadFile(String modid, String path)
	{
		int lastSlashId = path.lastIndexOf('/');
		this.directory = path.substring(0, lastSlashId + 1);
		this.pathName = path.substring(lastSlashId + 1, path.length());
		Part part = null;
		FaceGroup fg = null;
		mod = modid;

		try
		{
			{
				File file = new File(path);
				InputStream stream = new FileInputStream(file);
				
				if (stream == null || file == null || path == null)
				{
					AIRI.logger.bug("OBJ Loading Failed: " + path);
					return false;
				}

				new StringBuilder();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

				String line;

				while ((line = bufferedReader.readLine()) != null)
				{
					String[] words = line.split(" ");
					part = new Part(vertex, uv);

					if (words[0].equals("o"))
					{
						nameToPartHash.put(words[1], part);
					}
					else if (words[0].equals("v"))
					{
						Vertex v;
						part.addVertex(v = new Vertex(Float.parseFloat(words[1]),
							Float.parseFloat(words[2]),
							Float.parseFloat(words[3])
							));

						xMin = Math.min(xMin, v.x);
						yMin = Math.min(yMin, v.y);
						zMin = Math.min(zMin, v.z);
						xMax = Math.max(xMax, v.x);
						yMax = Math.max(yMax, v.y);
						zMax = Math.max(zMax, v.z);
					}
					else if (words[0].equals("vt"))
					{
						part.uv.add(new UV(Float.parseFloat(words[1]),
							1 - Float.parseFloat(words[2])
							));
					}
					else if (words[0].equals("f"))
					{
						int vertexNbr = words.length - 1;
						if (vertexNbr == 3)
						{
							Vertex[] verticeId = new Vertex[vertexNbr];
							UV[] uvId = new UV[vertexNbr];
							for (int idx = 0; idx < vertexNbr; idx++)
							{
								String[] id = words[idx + 1].split("/");

								verticeId[idx] = part.vertex.get(Integer.parseInt(id[0]) - 1);
								if (id.length > 1 && !id[1].equals(""))
								{
									uvId[idx] = part.uv.get(Integer.parseInt(id[1]) - 1);
								}
								else
								{
									uvId[idx] = null;
								}
							}

							fg.face.add(new Face(verticeId, uvId, new Normal(verticeId[0], verticeId[1], verticeId[2])));
						}
					}
					else if (words[0].equals("mtllib"))
					{
						mtlName = words[1];
					}
					else if (words[0].equals("usemtl"))
					{
						fg = new FaceGroup();
						fg.mtlName = words[1];
						
						if (part != null && part.faceGroup != null)
						{
							part.faceGroup.add(fg);
						}
					}
				}
			}

			part = null;
			
			{
				File file = new File(path);
				InputStream stream = new FileInputStream(file);

				if (stream == null || file == null || path == null)
				{
					AIRI.logger.bug("MTL Loading failed: " + path);
					return false;
				}

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				String mtlName = "";
				String line;
				while ((line = bufferedReader.readLine()) != null)
				{
					String[] words = line.split(" ");
					if (words[0].equals("newmtl"))
					{
						mtlName = words[1];

					}
					else if (words[0].equals("map_Kd"))
					{
						for (Part partPtr : nameToPartHash.values())
						{
							for (FaceGroup faceGroup : partPtr.faceGroup)
							{
								if (faceGroup.mtlName != null && faceGroup.mtlName.equals(mtlName))
								{
									faceGroup.resource = new ResourceLocation(modid, directory.substring(1) + words[1]);
								}
							}
						}
					}

				}
			}
		}
		catch (Exception e)
		{
			AIRI.logger.bug("[WavefrontAPI] " + e.toString());
			e.printStackTrace();
			return false;
		}

		part = null;

		try
		{
			InputStream stream = AIRI.class.getResourceAsStream("/assets/" + modid + directory + pathName.replace(".obj", ".txt").replace(".OBJ", ".txt"));
			if (stream != null)
			{
				BufferedReader bufferedReader;

				bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

				String line;
				while ((line = bufferedReader.readLine()) != null)
				{
					String[] words = line.split(" ");
					if (words[0].equals("o"))
					{
						part = nameToPartHash.get(words[1]);
					}
					else if (words[0].equals("f"))
					{
						if (words[1].equals("originX"))
						{
							part.ox = Float.valueOf(words[2]);
						}
						else if (words[1].equals("originY"))
						{
							part.oy = Float.valueOf(words[2]);
						}
						else if (words[1].equals("originZ"))
						{
							part.oz = Float.valueOf(words[2]);
						}
						else if (words[1].equals("originX2"))
						{
							part.ox2 = Float.valueOf(words[2]);
						}
						else if (words[1].equals("originY2"))
						{
							part.oy2 = Float.valueOf(words[2]);
						}
						else if (words[1].equals("originZ2"))
						{
							part.oz2 = Float.valueOf(words[2]);
						}
						else
						{
							part.nameToFloatHash.put(words[1], Float.valueOf(words[2]));
						}
					}
					else if (words[0].equals("s"))
					{
						nameToStringHash.put(words[1], words[2]);
					}
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		xDim = xMax - xMin;
		yDim = yMax - yMin;
		zDim = zMax - zMin;

		dimMax = Math.max(Math.max(xMax, yMax), zMax);
		dimMaxInv = 1.0f / dimMax;
		return true;
	}

	public Part getPart(String part)
	{
		return nameToPartHash.get(part);
	}

	public void draw(String part)
	{
		Part partPtr = getPart(part);

		if (partPtr != null)
			partPtr.draw();
	}

	public String getString(String name)
	{
		return nameToStringHash.get(name);
	}
}
