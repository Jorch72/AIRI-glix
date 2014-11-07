package com.arisux.airi.schematic;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import com.arisux.airi.lib.BlockLib.CoordData;
import com.arisux.airi.lib.BlockLib.CoordSelection;
import com.arisux.airi.lib.*;

public class SchematicFile
{
	public short width, height, length;
	public CoordData weOrigin;
	public Block[] blocks;
	public byte[] metadatas;
	public List<NBTTagCompound> tileEntityCompounds = new ArrayList<>();

	public SchematicFile(NBTTagCompound tagCompound) throws UnsupportedSchematicFormatException
	{
		String materials = tagCompound.getString("Materials");

		if (!(materials.equals("Alpha")))
		{
			throw new UnsupportedSchematicFormatException(materials);
		}

		this.width = tagCompound.getShort("Width");
		this.height = tagCompound.getShort("Height");
		this.length = tagCompound.getShort("Length");

		this.weOrigin = new CoordData(tagCompound.getShort("WEOriginX"), tagCompound.getShort("WEOriginY"), tagCompound.getShort("WEOriginZ"));

		this.metadatas = tagCompound.getByteArray("Data");
		byte[] blockIDs = tagCompound.getByteArray("Blocks");
		byte[] addBlocks = tagCompound.getByteArray("AddBlocks");

		this.blocks = new Block[blockIDs.length];

		for (int i = 0; i < blockIDs.length; i++)
		{
			int blockID = blockIDs[i] & 0xff;

			if (addBlocks.length == blockIDs.length / 2)
			{
				boolean lowerNybble = (i & 1) == 0;
				blockID |= lowerNybble ? ((addBlocks[i >> 1] & 0x0F) << 8) : ((addBlocks[i >> 1] & 0xF0) << 4);
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
		Map<CoordData, TileEntity> tileEntities = new HashMap<>();

		for (NBTTagCompound tileTagCompound : tileEntityCompounds)
		{
			TileEntity tileEntity = TileEntity.createAndLoadEntity(tileTagCompound);

			if (tileEntity != null)
			{
				tileEntities.put(new CoordData(tileEntity), tileEntity);
			}
		}

		CoordSelection blockArea = CoordSelection.areaFromSize(new CoordData(0, 0, 0), new int[] { width, height, length });

		for (int pass = 0; pass < 2; pass++)
		{
			for (CoordData srcCoord : blockArea)
			{
				int index = srcCoord.posX + (srcCoord.posY * length + srcCoord.posZ) * width;
				Block block = blocks[index];
				byte meta = metadatas[index];

				if (block != null && getPass(block, meta) == pass)
				{
					CoordData pos = new CoordData(data.posX + srcCoord.posX, data.posY + srcCoord.posY, data.posZ + srcCoord.posZ, block, meta);
					world.setBlock(pos.posX, pos.posY, pos.posZ, pos.block, pos.meta, 3);

					TileEntity tileEntity = tileEntities.get(srcCoord);

					if (tileEntity != null)
					{
						world.setBlockMetadataWithNotify(pos.posX, pos.posY, pos.posZ, meta, 2);

						WorldLib.setTileEntityPosition(tileEntity, pos);
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
		public final String format;

		public UnsupportedSchematicFormatException(String format)
		{
			this.format = format;
		}
	}
}
