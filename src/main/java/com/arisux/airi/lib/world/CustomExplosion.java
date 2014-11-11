package com.arisux.airi.lib.world;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class CustomExplosion
{
	private int posX, posY, posZ;
	private int radiusX, radiusY, radiusZ;
	private World worldObj;
	private Random random;

	public CustomExplosion(World worldObj, double radiusX, double radiusY, double radiusZ, int posX, int posY, int posZ, long randLong)
	{
		this.worldObj = worldObj;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.radiusX = (int) Math.ceil(radiusX);
		this.radiusY = (int) Math.ceil(radiusY);
		this.radiusZ = (int) Math.ceil(radiusZ);
		this.random = new Random(randLong);
	}

	@SuppressWarnings("unchecked")
	public void start()
	{
		worldObj.playSoundEffect((double) posX, (double) posY, (double) posZ, "random.old_explode", 4.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

		List<Entity> var40 = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox((double) posX - radiusX, (double) posY - radiusY, (double) posZ - radiusY, (double) posX + radiusY, (double) posY + radiusY, (double) posZ + radiusY));

		for (int var39 = 0; var39 < var40.size(); ++var39)
		{
			((Entity) var40.get(var39)).attackEntityFrom(DamageSource.generic, 100);
		}

		radiusX += 0.5D;
		radiusY += 0.5D;
		radiusZ += 0.5D;

		double var22 = 0.0D;

		for (int curX = 0; curX <= radiusX; ++curX)
		{
			double var25 = var22;
			var22 = (double) (curX + 1) * (1D / radiusX);

			for (int curY = 0; curY <= radiusY; ++curY)
			{
				double var30 = 0.0D;
				double var27 = (double) (curY + 1) * (1D / radiusY);
				double var32 = 0.0D;

				for (int curZ = 0; curZ <= radiusZ; ++curZ)
				{
					double var35 = var32;
					var32 = (double) (curZ + 1) * (1D / radiusZ);
					double var37 = this.lengthSq(var25, var30, var35);

					if (var37 > 1.0D)
						if (curZ == 0)
							if (curY == 0)
								break;

					if (this.lengthSq(var22, var30, var35) <= 1.0D && this.lengthSq(var25, var27, var35) <= 1.0D && this.lengthSq(var25, var30, var32) <= 1.0D || !this.random.nextBoolean())
					{
						addBlock(curX, curY, curZ);
						addBlock(-curX, curY, curZ);
						addBlock(curX, -curY, curZ);
						addBlock(curX, curY, -curZ);
						addBlock(-curX, -curY, curZ);
						addBlock(curX, -curY, -curZ);
						addBlock(-curX, curY, -curZ);
						addBlock(-curX, -curY, -curZ);
					}
				}
			}
		}
	}

	private final double lengthSq(double posX, double posY, double posZ)
	{
		return (posX * posX) + (posY * posY) + (posZ * posZ);
	}

	private final void addBlock(double posX, double posY, double posZ)
	{
		Block block = this.worldObj.getBlock((int) posX + this.posX, (int) posY + this.posY, (int) posZ + this.posZ);

		if (block != Blocks.air && block != Blocks.bedrock && block != Blocks.stone)
		{
			int newX = (int) posX + this.posX, newY = (int) posY + this.posY, newZ = (int) posZ + this.posZ;
			block.onBlockDestroyedByExplosion(this.worldObj, (int) posX, (int) posY, (int) posZ, null);
			this.worldObj.setBlockToAir(newX, newY, newZ);
		}
	}
}