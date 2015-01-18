package com.arisux.airi.lib.world;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import com.arisux.airi.AIRI;
import com.arisux.airi.lib.WorldUtil.Blocks;
import com.arisux.airi.lib.WorldUtil.Blocks.CoordData;
import com.arisux.airi.lib.WorldUtil.TileEntities;

public class Schematic
{
	public short width, height, length;
	public CoordData origin;
	public Block[] blocks;
	public byte[] metadata;
	public ArrayList<NBTTagCompound> tileEntityCompounds = new ArrayList<NBTTagCompound>();

	public Schematic(NBTTagCompound tagCompound) throws UnsupportedSchematicFormatException
	{
		String materials = tagCompound.getString("Materials");

		if (!materials.equals("Alpha"))
		{
			throw new UnsupportedSchematicFormatException(materials);
		}

		this.origin = new CoordData(tagCompound.getShort("WEOriginX"), tagCompound.getShort("WEOriginY"), tagCompound.getShort("WEOriginZ"));
		this.width = tagCompound.getShort("Width");
		this.height = tagCompound.getShort("Height");
		this.length = tagCompound.getShort("Length");
		this.metadata = tagCompound.getByteArray("Data");
		byte[] blockIds = tagCompound.getByteArray("Blocks");
		byte[] addBlocks = tagCompound.getByteArray("AddBlocks");

		this.blocks = new Block[blockIds.length];

		for (int i = 0; i < blockIds.length; i++)
		{
			int blockID = blockIds[i] & 0xff;

			if (addBlocks.length == blockIds.length / 2)
			{
				blockID |= (i & 1) == 0 ? ((addBlocks[i >> 1] & 0x0F) << 8) : ((addBlocks[i >> 1] & 0xF0) << 4);
			}

			this.blocks[i] = Block.getBlockById(blockID);
		}

		NBTTagList tileEntities = tagCompound.getTagList("TileEntities", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < tileEntities.tagCount(); i++)
		{
			tileEntityCompounds.add(tileEntities.getCompoundTagAt(i));
		}
	}

	public void generate(World world, CoordData data)
	{
		HashMap<CoordData, TileEntity> tileEntities = new HashMap<CoordData, TileEntity>();

		for (NBTTagCompound tileTagCompound : tileEntityCompounds)
		{
			TileEntity tileEntity = TileEntity.createAndLoadEntity(tileTagCompound);

			if (tileEntity != null)
			{
				tileEntities.put(new CoordData(tileEntity), tileEntity);
			}
		}

		Blocks.CoordSelection blockArea = Blocks.CoordSelection.areaFromSize(new CoordData(0, 0, 0), new int[] { width, height, length });

		for (int pass = 0; pass < 2; pass++)
		{
			for (CoordData srcCoord : blockArea)
			{
				int index = srcCoord.posX + (srcCoord.posY * length + srcCoord.posZ) * width;
				Block block = blocks[index];
				byte meta = metadata[index];

				if (block != null && getPass(block, meta) == pass && block != AIRI.WORLDGEN_GHOST)
				{
					CoordData pos = new CoordData(data.posX + srcCoord.posX, data.posY + srcCoord.posY, data.posZ + srcCoord.posZ, block, meta);
					world.setBlock(pos.posX, pos.posY, pos.posZ, pos.block, pos.meta, 3);

					TileEntity tileEntity = tileEntities.get(srcCoord);

					if (tileEntity != null)
					{
						world.setBlockMetadataWithNotify(pos.posX, pos.posY, pos.posZ, meta, 2);

						TileEntities.setTileEntityPosition(tileEntity, pos);
						world.setTileEntity(pos.posX, pos.posY, pos.posZ, tileEntity);
						tileEntity.updateContainingBlockInfo();
					}
				}
			}
		}
	}

	private int getPass(Block block, int metadata)
	{
		return (block.isNormalCube() || block.getMaterial() == Material.air) ? 0 : 1;
	}

	public static class UnsupportedSchematicFormatException extends Exception
	{
		private static final long serialVersionUID = -7318855261807377764L;
		public final String format;

		public UnsupportedSchematicFormatException(String format)
		{
			this.format = format;
		}
	}
}
