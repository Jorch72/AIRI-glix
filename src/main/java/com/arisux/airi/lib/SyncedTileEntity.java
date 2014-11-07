package com.arisux.airi.lib;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SyncedTileEntity extends TileEntity
{
	protected Entity syncedEntity;
	public float rotationYaw, rotationPitch;

	public class SyncedEntity extends Entity
	{
		public SyncedEntity(World par1World)
		{
			super(par1World);
			this.setSize(1.0F, 1.0F);
		}

		@Override
		protected void entityInit()
		{
			;
		}

		@Override
		protected void readEntityFromNBT(NBTTagCompound var1)
		{
			;
		}

		@Override
		protected void writeEntityToNBT(NBTTagCompound var1)
		{
			;
		}
	}

	public SyncedTileEntity()
	{
		this.syncedEntity = new SyncedEntity(this.worldObj);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (this.worldObj != null && this.getEntity() != null)
		{
			this.getEntity().setLocationAndAngles(this.xCoord + 0.15 + getEntity().width / 2, this.yCoord + 1, this.zCoord + getEntity().width / 2, this.rotationYaw, this.rotationPitch);
		}
	}

	public Entity getEntity()
	{
		return this.syncedEntity;
	}

	public void setEntity(Entity syncedEntity)
	{
		this.syncedEntity = syncedEntity;
	}

	public float getRotationYaw()
	{
		return rotationYaw;
	}

	public float getRotationPitch()
	{
		return rotationPitch;
	}

	public void setRotationYaw(float rotationYaw)
	{
		this.rotationYaw = rotationYaw;
	}

	public void setRotationPitch(float rotationPitch)
	{
		this.rotationPitch = rotationPitch;
	}
}
