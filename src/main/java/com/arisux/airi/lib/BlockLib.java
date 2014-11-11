package com.arisux.airi.lib;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class BlockLib
{
	public static class IconSet
	{
		public IIcon icon, top, bottom, front, back, left, right;
		public String iconRes, topRes, bottomRes, frontRes, backRes, leftRes, rightRes;

		public IconSet(String icon)
		{
			this(icon, null, null, null, null, null, null);
		}

		public IconSet(String icon, String top, String bottom, String front, String back, String left, String right)
		{
			this.iconRes = icon;
			this.topRes = top;
			this.bottomRes = bottom;
			this.frontRes = front;
			this.backRes = back;
			this.leftRes = left;
			this.rightRes = right;
		}

		public void registerIcons(IIconRegister register)
		{
			this.icon = (register.registerIcon(iconRes));
			this.top = (register.registerIcon(topRes));
			this.bottom = (register.registerIcon(bottomRes));
			this.front = (register.registerIcon(frontRes));
			this.back = (register.registerIcon(backRes));
			this.left = (register.registerIcon(leftRes));
			this.right = (register.registerIcon(rightRes));
		}
	}

	public static class CoordData
	{
		public int posX, posY, posZ, meta;
		public Block block;
		public UniqueIdentifier identifier;

		public CoordData(Entity entity)
		{
			this(Math.round(entity.posX), Math.round(entity.posY), Math.round(entity.posZ));
		}
		
		public CoordData(TileEntity tileEntity)
		{
			this(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord), tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
		}
		
		public CoordData(int posX, int posY, int posZ, World world)
		{
			this(posX, posY, posZ, world.getBlock(posX, posY, posZ), world.getBlockMetadata(posX, posY, posZ));
		}
		
		public CoordData(int posX, int posY, int posZ, Block block)
		{
			this(posX, posY, posZ, block, 0);
		}

		public CoordData(int posX, int posY, int posZ, Block block, int meta)
		{
			this(posX, posY, posZ, GameRegistry.findUniqueIdentifierFor(block));
			this.block = block;
		}

		public CoordData(int posX, int posY, int posZ, String id)
		{
			this(posX, posY, posZ, id, 0);
		}

		public CoordData(int posX, int posY, int posZ, String id, int meta)
		{
			this(posX, posY, posZ, new UniqueIdentifier(id), 0);
		}
		
		public CoordData(int posX, int posY, int posZ, UniqueIdentifier identifier)
		{
			this(posX, posY, posZ, identifier, 0);
		}

		public CoordData(int posX, int posY, int posZ, UniqueIdentifier identifier, int meta)
		{
			this.identifier = identifier;
			this.posX = posX;
			this.posY = posY;
			this.posZ = posZ;
			this.meta = meta;
		}
		
		public CoordData(int posX, int posY, int posZ)
		{
			this.posX = posX;
			this.posY = posY;
			this.posZ = posZ;
		}
		
		public CoordData(long posX, long posY, long posZ)
		{
			this.posX = (int) posX;
			this.posY = (int) posY;
			this.posZ = (int) posZ;
		}
		
		public CoordData(float posX, float posY, float posZ)
		{
			this.posX = (int) posX;
			this.posY = (int) posY;
			this.posZ = (int) posZ;
		}
		
		public CoordData(double posX, double posY, double posZ)
		{
			this.posX = (int) posX;
			this.posY = (int) posY;
			this.posZ = (int) posZ;
		}
		
		public boolean equals(CoordData data)
		{
			if (data == null || this != data || !(data instanceof CoordData) || data.posX != this.posX || data.posY != this.posY || data.posZ != this.posZ)
			{
				return false;
			}
			
			return true;
		}

		public Block getBlock()
		{
			return this.block;
		}
		
		public Block getBlock(World world)
		{
			return this.block == null && world != null ? world.getBlock(this.posX, this.posY, this.posZ) : this.block;
		}
		
		public int getBlockMetadata()
		{
			return this.meta;
		}

		public int getBlockMetadata(World world)
		{
			return this.meta == 0 && world != null ? world.getBlockMetadata(this.posX, this.posY, this.posZ) : 0;
		}
		
		public TileEntity getTileEntity(World world)
		{
			return world.getTileEntity(this.posX, this.posY, this.posZ);
		}
		
		public CoordData min(CoordData data)
		{
			return new CoordData(Math.min(this.posX, data.posX), Math.min(this.posY, data.posY), Math.min(this.posZ, data.posZ));
		}
		
		public CoordData max(CoordData data)
		{
			return new CoordData(Math.max(this.posX, data.posX), Math.max(this.posY, data.posY), Math.max(this.posZ, data.posZ));
		}
		
		public CoordData add(CoordData data)
		{
			return new CoordData(this.posX + data.posX, this.posY + data.posY, this.posZ + data.posZ);
		}
		
		public CoordData add(int posX, int posY, int posZ)
		{
			return this.add(new CoordData(posX, posY, posZ));
		}
		
		public CoordData subtract(CoordData data)
		{
			return new CoordData(this.max(data).posX - this.min(data).posX, this.max(data).posY - this.min(data).posY, this.max(data).posZ - this.min(data).posZ);
		}
		
		public CoordData subtract(int posX, int posY, int posZ)
		{
			return this.add(new CoordData(posX, posY, posZ));
		}
		
		public CoordData offsetX(int amount)
		{
			this.posX = this.posX + amount;
			return this;
		}
		
		public CoordData offsetY(int amount)
		{
			this.posY = this.posY + amount;
			return this;
		}
		
		public CoordData offsetZ(int amount)
		{
			this.posZ = this.posZ + amount;
			return this;
		}
		
		public void set(CoordData data)
		{
			this.identifier = data.identifier;
			this.block = data.block;
			this.posX = data.posX;
			this.posY = data.posY;
			this.posZ = data.posZ;
		}
		
		public UniqueIdentifier identity()
		{
			return this.identity(null);
		}
		
		public UniqueIdentifier identity(World world)
		{
			return this.identifier == null ? GameRegistry.findUniqueIdentifierFor(this.getBlock(world)) : this.identifier;
		}
		
		public NBTTagCompound writeToNBT()
		{
			return this.writeToNBT(null);
		}
		
		public NBTTagCompound writeToNBT(NBTTagCompound nbt)
		{
			return this.writeToNBT(nbt, "Id", "PosX", "PosY", "PosZ");
		}
		
		public NBTTagCompound writeToNBT(NBTTagCompound nbt, String labelId, String labelX, String labelY, String labelZ)
		{
			NBTTagCompound dataTag = nbt == null ? new NBTTagCompound() : nbt;
			
			dataTag.setString(labelId, String.format("%s:%s", this.identity().modId, this.identity().name));
			dataTag.setInteger(labelX, this.posX);
			dataTag.setInteger(labelY, this.posY);
			dataTag.setInteger(labelZ, this.posZ);
			
			return dataTag;
		}
		
		public CoordData readFromNBT(NBTTagCompound nbt)
		{
			return this.readFromNBT(nbt, "Id", "PosX", "PosY", "PosZ");
		}
		
		public CoordData readFromNBT(NBTTagCompound nbt, String labelId, String labelX, String labelY, String labelZ)
		{
			return new CoordData(nbt.getInteger(labelX), nbt.getInteger(labelY), nbt.getInteger(labelZ), nbt.getString(labelId));
		}
	}
	
	public static class CoordSelection implements Iterable<CoordData>
	{
	    private CoordData pos1;
	    private CoordData pos2;

	    public CoordSelection(CoordData pos1, CoordData pos2)
	    {
	        this.pos1 = pos1;
	        this.pos2 = pos2;
	    }

	    public static CoordSelection areaFromSize(CoordData coord, int[] size)
	    {
	        if (size[0] <= 0 || size[1] <= 0 || size[2] <= 0)
	        {
	            throw new IllegalArgumentException();
	        }

	        return new CoordSelection(coord, new CoordData(coord.posX + size[0] - 1, coord.posY + size[1] - 1, coord.posZ + size[2] - 1));
	    }

	    public CoordData getPos1()
	    {
	        return pos1;
	    }

	    public void setPos1(CoordData point1)
	    {
	        this.pos1 = point1;
	    }

	    public CoordData getPos2()
	    {
	        return pos2;
	    }

	    public void setPos2(CoordData point2)
	    {
	        this.pos2 = point2;
	    }

	    public CoordData min()
	    {
	        return pos1.min(pos2);
	    }

	    public CoordData max()
	    {
	        return pos1.max(pos2);
	    }

	    public int[] areaSize()
	    {
	        return new int[]{max().subtract(min()).posX + 1, max().subtract(min()).posY + 1, max().subtract(min()).posZ + 1};
	    }

	    public boolean contains(CoordData coord)
	    {
	        return coord.posX >= min().posX && coord.posY >= min().posY && coord.posZ >= min().posZ && coord.posX <= max().posX && coord.posY <= max().posY && coord.posZ <= max().posZ;
	    }

	    public AxisAlignedBB asAxisAlignedBB()
	    {
	        return AxisAlignedBB.getBoundingBox(min().posX, min().posY, min().posZ, max().posX, max().posY, max().posZ);
	    }

	    @Override
	    public Iterator<CoordData> iterator()
	    {
	        return new CoordSelectionIterator(min(), max());
	    }
	}
	
	public static class CoordSelectionIterator implements Iterator<CoordData>
	{
	    private CoordData min;
	    private CoordData max;

	    private int curX;
	    private int curY;
	    private int curZ;

	    public CoordSelectionIterator(CoordData min, CoordData max)
	    {
	        this.min = min;
	        this.max = max;
	        this.curX = min.posX;
	        this.curY = min.posY;
	        this.curZ = min.posZ;
	    }

	    @Override
	    public boolean hasNext()
	    {
	        return this.curX <= this.max.posX && this.curY <= this.max.posY && this.curZ <= this.max.posZ;
	    }

	    @Override
	    public CoordData next()
	    {
	        CoordData coord = this.hasNext() ? new CoordData(this.curX, this.curY, this.curZ) : null;

	        this.curX++;

	        if (this.curX > this.max.posX)
	        {
	        	this.curX = this.min.posX;
	        	this.curY++;

	            if (this.curY > this.max.posY)
	            {
	            	this.curY = this.min.posY;
	            	this.curZ++;
	            }
	        }

	        return coord;
	    }

	    @Override
	    public void remove()
	    {
	        throw new UnsupportedOperationException();
	    }
	}

	public static String getDomain(Block block)
	{
		String domain = "minecraft:";

		if (block.getUnlocalizedName().contains(":"))
		{
			domain = (block.getUnlocalizedName().split(":")[0] + ":").replace("tile.", "");
		}

		return domain;
	}
}
