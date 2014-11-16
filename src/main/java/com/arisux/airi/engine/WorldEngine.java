package com.arisux.airi.engine;

import java.io.*;
import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.*;

import com.arisux.airi.AIRI;
import com.arisux.airi.Settings;
import com.arisux.airi.engine.BlockLib.CoordData;
import com.arisux.airi.lib.util.NetworkUtil;
import com.arisux.airi.lib.world.CustomExplosion;

public class WorldEngine
{
	/**
	 * Create an explosion in the specified world, at the specified coordinates, with the specified effects.
	 * 
	 * @param entity - The entity that triggered the explosion.
	 * @param worldObj - The world that the explosion should be created in.
	 * @param data - The CoordData containing the coordinates to create an explosion at.
	 * @param strength - The strength of the explosion
	 * @param isFlaming - Set to true if the explosion causes surrounding blocks to catch on fire.
	 * @param isSmoking - Set to true if the explosion emitts smoke particles.
	 * @param doesBlockDamage - Set to true if the explosion does physical Block damage.
	 * @return Return the instance of the explosion that was just created.
	 */
	public static Explosion createExplosion(Entity entity, World worldObj, CoordData data, float strength, boolean isFlaming, boolean isSmoking, boolean doesBlockDamage)
	{
		Explosion explosion = new Explosion(worldObj, entity, data.posX, data.posY, data.posZ, strength);
		explosion.isFlaming = isFlaming;
		explosion.isSmoking = isSmoking;

		if (doesBlockDamage)
		{
			explosion.doExplosionA();
		}

		explosion.doExplosionB(true);

		return explosion;
	}

	// TODO: Needs work. Wait until Minecraft Forge for 1.8 Final is released.
	public static CustomExplosion createThreadedExplosion(Entity var1, World worldObj, int posX, int posY, int posZ, float var5)
	{
		CustomExplosion explosion = new CustomExplosion(worldObj, var5, var5, var5, posX, posY, posZ, (new Random()).nextLong());
		explosion.start();

		return explosion;
	}

	/**
	 * Get the light intensity as an Integer at the specified coordinates in the specified world.
	 * 
	 * @param worldObj - World to check for brightness values in.
	 * @param data - CoordData containing coordinates of the location to check brightness at.
	 * @return Returns light intensity of a block as an Integer.
	 */
	public static int getLight(World worldObj, CoordData data)
	{
		int block = worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Block, data.posX, data.posY, data.posZ);
		int sky = worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, data.posX, data.posY, data.posZ) - worldObj.calculateSkylightSubtracted(0f);

		return Math.max(block, sky);
	}
	
	/**
	 * Move the specified TileEntity by the specified amount of coordinates.
	 * 
	 * @param tileEntity - The TileEntity to be moved.
	 * @param coord - The CoordData instance containing the coordinates to add.
	 */
	public static void moveTileEntity(TileEntity tileEntity, CoordData coord)
	{
		setTileEntityPosition(tileEntity, (new CoordData(tileEntity)).add(coord));
	}

	/**
	 * Move the specified TileEntity to the specified CoordData coordinates.
	 * 
	 * @param tileEntity - The TileEntity to change the position of.
	 * @param coord - The CoordData instance containing the new set of coordinates.
	 */
	public static void setTileEntityPosition(TileEntity tileEntity, CoordData coord)
	{
		tileEntity.xCoord = coord.posX;
		tileEntity.yCoord = coord.posY;
		tileEntity.zCoord = coord.posZ;
	}

	public static class NBT
	{
		/**
		 * Copies an ArrayList of Strings into an NBTTagList of Strings.
		 * 
		 * @param list - The ArrayList to be converted.
		 * @return NBTTagList created from the ArrayList.
		 */
		public static NBTTagList newStringNBTList(ArrayList<String> list)
		{
			NBTTagList nbttaglist = new NBTTagList();

			for (int j = 0; j < list.size(); ++j)
			{
				String s1 = list.get(j);
				nbttaglist.appendTag(new NBTTagString(s1));
			}

			return nbttaglist;
		}

		/**
		 * Copies an ArrayList of NBTTagCompounds into an NBTTagList of NBTTagCompounds.
		 * 
		 * @param list - The ArrayList to be converted.
		 * @return NBTTagList created from the ArrayList.
		 */
		public static NBTTagList newCompoundNBTList(ArrayList<NBTTagCompound> list)
		{
			NBTTagList nbttaglist = new NBTTagList();

			for (int j = 0; j < list.size(); ++j)
			{
				NBTTagCompound nbt = list.get(j);
				nbttaglist.appendTag(nbt);
			}

			return nbttaglist;
		}
		
		/**
		 * Writes an uncompressed NBTTagCompound to the specified File
		 * 
		 * @param nbt - The NBTTagCompound to be written.
		 * @param file - The File to write to.
		 */
		public static void write(NBTTagCompound nbt, File file)
		{
			try
			{
				CompressedStreamTools.write(nbt, file);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		/**
		 * @param file - The File to read an NBTTagCompound from.
		 * @return The uncompressed NBTTagCompound being read from the specified File.
		 */
		public static NBTTagCompound read(File file)
		{
			NBTTagCompound nbtTagCompound = null;

			try
			{
				nbtTagCompound = CompressedStreamTools.read(file);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return nbtTagCompound;
		}

		/**
		 * Writes a compressed NBTTagCompound to the specified File
		 * 
		 * @param nbt - The NBTTagCompound to be written.
		 * @param file - The File to write to.
		 */
		public static void writeCompressed(NBTTagCompound nbt, File file)
		{
			try
			{
				FileOutputStream stream = new FileOutputStream(file);
				CompressedStreamTools.writeCompressed(nbt, stream);
				stream.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		/**
		 * @param file - The File to read an NBTTagCompound from.
		 * @return The compressed NBTTagCompound being read from the specified File.
		 */
		public static NBTTagCompound readCompressed(File file)
		{
			NBTTagCompound nbtTagCompound = null;

			try
			{
				FileInputStream stream = new FileInputStream(file);
				nbtTagCompound = CompressedStreamTools.readCompressed(stream);
				stream.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return nbtTagCompound;
		}
	}

	public static class Entities
	{
		/**
		 * Get the first Entity instance of the specified class type found, 
		 * within specified range, at the specified world coordinates.
		 * 
		 * @param worldObj - World instance to scan for entities in
		 * @param entityClass - Entity class type to scan for.
		 * @param data - The CoordData containing the coordinates to start scanning at.
		 * @param range - Range of blocks to scan within.
		 * @return First Entity instance found using the specified parameters.
		 */
		public static Entity getEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range)
		{
			return getEntityInCoordsRange(worldObj, entityClass, data, range, 16);
		}

		/**
		 * Get the first Entity instance of the specified class type found, 
		 * within specified range, at the specified world coordinates, within specified height.
		 * 
		 * @param worldObj - World instance to scan for entities in
		 * @param entityClass - Entity class type to scan for.
		 * @param data - The CoordData containing the coordinates to start scanning at.
		 * @param range - Range of blocks to scan within.
		 * @param height - Height to scan for entities within
		 * @return First Entity instance found using the specified parameters.
		 */
		public static Entity getEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range, int height)
		{
			List<? extends Entity> entities = getEntitiesInCoordsRange(worldObj, entityClass, data, range, height);

			return entities.size() > 1 ? (Entity) entities.get(0) : null;
		}

		/**
		 * Get a random Entity instance of the specified class type found, 
		 * within specified range, at the specified world coordinates, within specified height.
		 * 
		 * @param worldObj - World instance to scan for entities in
		 * @param entityClass - Entity class type to scan for.
		 * @param data - The CoordData containing the coordinates to start scanning at.
		 * @param range - Range of blocks to scan within.
		 * @return First Entity instance found using the specified parameters.
		 */
		public static Entity getRandomEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range)
		{
			return getRandomEntityInCoordsRange(worldObj, entityClass, data, range, 16);
		}

		/**
		 * Get a random Entity instance of the specified class type found, 
		 * within specified range, at the specified world coordinates, within specified height.
		 * 
		 * @param worldObj - World instance to scan for entities in
		 * @param entityClass - Entity class type to scan for.
		 * @param data - The CoordData containing the coordinates to start scanning at.
		 * @param range - Range of blocks to scan within.
		 * @param height - Height to scan for entities within
		 * @return First Entity instance found using the specified parameters.
		 */
		public static Entity getRandomEntityInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range, int height)
		{
			List<? extends Entity> entities = getEntitiesInCoordsRange(worldObj, entityClass, data, range, height);

			return entities.size() > 1 ? (Entity) entities.get((new Random()).nextInt(entities.size())) : null;
		}

		/**
		 * Gets all Entity instances of the specified class type found, 
		 * within specified range, at the specified world coordinates, within specified height.
		 * 
		 * @param worldObj - World instance to scan for entities in
		 * @param entityClass - Entity class type to scan for.
		 * @param data - The CoordData containing the coordinates to start scanning at.
		 * @param range - Range of blocks to scan within.
		 * @return All the Entity instances found using the specified parameters.
		 */
		public static List<? extends Entity> getEntitiesInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range)
		{
			return getEntitiesInCoordsRange(worldObj, entityClass, data, range, 16);
		}

		/**
		 * Gets all Entity instances of the specified class type found, 
		 * within specified range, at the specified world coordinates, within specified height.
		 * 
		 * @param worldObj - World instance to scan for entities in
		 * @param entityClass - Entity class type to scan for.
		 * @param data - The CoordData containing the coordinates to start scanning at.
		 * @param range - Range of blocks to scan within.
		 * @param height - Height to scan for entities within
		 * @return All the Entity instances found using the specified parameters.
		 */
		@SuppressWarnings("unchecked")
		public static List<? extends Entity> getEntitiesInCoordsRange(World worldObj, Class<? extends Entity> entityClass, CoordData data, int range, int height)
		{
			return worldObj.getEntitiesWithinAABB(entityClass, AxisAlignedBB.getBoundingBox(data.posX, data.posY, data.posZ, data.posX + 1, data.posY + 1, data.posZ + 1).expand(range * 2, height, range * 2));
		}

		/**
		 * @param entity - The entity that entityLooking is looking for.
		 * @param entityLooking - The entity that is looking for the first entity.
		 * @return Returns true if the first Entity can be seen by the second Entity.
		 */
		public static boolean canEntityBeSeenBy(Entity entity, Entity entityLooking)
		{
			return entity.worldObj.rayTraceBlocks(Vec3.createVectorHelper(entity.posX, entity.posY + (entity.height / 2), entity.posZ), Vec3.createVectorHelper(entityLooking.posX, entityLooking.posY + entityLooking.getEyeHeight(), entityLooking.posZ)) == null;
		}

		/**
		 * Constructs a new Entity instance from the specified class name in the specified world.
		 * 
		 * @param worldObj - World to construct the Entity instance in.
		 * @param name - String name of the entity class of which will be constructed.
		 * @return Entity instance constructed using this method.
		 */
		@SuppressWarnings("unchecked")
		public static Entity constructEntityViaClasspath(World worldObj, String name)
		{
			try
			{
				return constructEntity(worldObj, (Class<? extends Entity>) Class.forName(name));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * Constructs a new Entity instance from the specified class in the specified world.
		 * 
		 * @param worldObj - World to construct the Entity instance in.
		 * @param c - The entity class of which will be constructed.
		 * @return Entity instance constructed using this method.
		 */
		public static Entity constructEntity(World worldObj, Class<? extends Entity> c)
		{
			try
			{
				return (c.getConstructor(World.class)).newInstance(new Object[] { worldObj });
			}
			catch (Exception e)
			{
				AIRI.logger.bug("Failed to construct entity: " + c.getName());
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * Causes the facer entity to face the faced entity.
		 * 
		 * @param facer - The Entity that is going to be facing the faced entity.
		 * @param faced - The Entity that is going to be faced.
		 * @param maxYaw - The max rotation yaw that the facer can rotate.
		 * @param maxPitch - The max rotation pitch that the facer can rotate.
		 */
		public static void faceEntity(Entity facer, Entity faced, float maxYaw, float maxPitch)
		{
			double xDistance = faced.posX - facer.posX;
			double zDistance = faced.posZ - facer.posZ;
			double yDistance;

			if (faced instanceof EntityLivingBase)
			{
				EntityLivingBase entitylivingbase = (EntityLivingBase) faced;
				yDistance = entitylivingbase.posY + entitylivingbase.getEyeHeight() - (facer.posY + facer.getEyeHeight());
			}
			else
			{
				yDistance = (faced.boundingBox.minY + faced.boundingBox.maxY) / 2.0D - (facer.posY + facer.getEyeHeight());
			}

			double d3 = MathHelper.sqrt_double(xDistance * xDistance + zDistance * zDistance);
			float f2 = (float) (Math.atan2(zDistance, xDistance) * 180.0D / Math.PI) - 90.0F;
			float f3 = (float) (-(Math.atan2(yDistance, d3) * 180.0D / Math.PI));
			facer.rotationPitch = updateRotation(facer.rotationPitch, f3, maxPitch);
			facer.rotationYaw = updateRotation(facer.rotationYaw, f2, maxYaw);
		}

		/**
		 * @param currentRotation - The current rotation value.
		 * @param targetRotation - The target rotation value.
		 * @param maxChange - The maximum rotation change allowed.
		 * @return Amount of rotation that is results based on the current, target, and max rotation.
		 */
		public static float updateRotation(float currentRotation, float targetRotation, float maxChange)
		{
			float newRotation = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);
			return currentRotation + (newRotation > maxChange ? maxChange : newRotation < -maxChange ? -maxChange : maxChange);
		}
		
		public static class Players
		{
			/**
			 * @param player - Instance of the receiving player
			 * @param message - The string to be sent to the player
			 */
			public static void sendToChat(EntityPlayer player, String message)
			{
				player.addChatComponentMessage(new ChatComponentText(message));
			}

			/**
			 * This method sends an HTTP request to the Arisux web servers, which retrieve 
			 * the UUID from Mojang's auth servers, and relays it back to this method.
			 * 
			 * @param username - The username of the player to retrieve a UUID from
			 * @return The UUID of the specified player.
			 */
			public static String getUUID(String username)
			{
				String retrieved = NetworkUtil.getURLContents(String.format(AIRI.INSTANCE.properties.URL_RETRIEVE_UUID, username));
				return AIRI.instance().settings.propertyList.get(Settings.Setting.NETWORKING).getBoolean() ? retrieved != null && retrieved.length() >= 32 ? retrieved : username : username;
			}

			/**
			 * Returns an instance of EntityPlayer for the first player found with the specified username.
			 * 
			 * @param worldObj - World instance to scan for players in.
			 * @param username - Username to scan players for.
			 * @return EntityPlayer instance of the specified player's username.
			 */
			public static EntityPlayer getPlayerForUsername(World worldObj, String username)
			{
				for (Object player : worldObj.playerEntities)
				{
					if (((EntityPlayer) player).getCommandSenderName().equalsIgnoreCase(username))
					{
						return (EntityPlayer) player;
					}
				}

				return null;
			}

			/**
			 * Returns an instance of EntityPlayer for the first player found with the specified ICommandSender details
			 * 
			 * @param commandSender - The ICommandSender instance to get details from
			 * @return EntityPlayer instance of the specified ICommandSender
			 */
			public static EntityPlayer getPlayerForCommandSender(ICommandSender commandSender)
			{
				return getPlayerForUsername(commandSender.getEntityWorld(), commandSender.getCommandSenderName());
			}
			
			public static class Inventories
			{
				/** 
				 * @param obj - Item or Block instance 
				 * @return A new ItemStack instance of the specified Object
				 **/
				public static ItemStack newStack(Object obj)
				{
					return obj instanceof Block ? new ItemStack((Block) obj) : obj instanceof Item ? new ItemStack((Item) obj) : null;
				}
				
				/** 
				 * @param stack - Instance of ItemStack 
				 * @return the Item instance of the specified ItemStack
				 **/
				public static Item getItem(ItemStack stack)
				{
					return stack.getItem();
				}
				
				/**
				 * @param stack - Instance of ItemStack 
				 * @return the Block instance of the specified ItemStack
				 **/
				public static Block getBlock(ItemStack stack)
				{
					return Block.getBlockFromItem(stack.getItem());
				}
				
				/**
				 * Consumes a single item of the specified type from the specified player's inventory.
				 * Only comsumes an item if the player is in survival mode.
				 * 
				 * @param player - The player to consume an item from.
				 * @param item - The item to consume.
				 */
				public static void consumeItem(EntityPlayer player, Item item)
				{
					consumeItem(player, item, false);
				}

				/**
				 * Consumes a single item of the specified type from the specified player's inventory.
				 * Only comsumes an item if the player is in survival mode, unless forced.
				 * 
				 * @param player - The player to consume an item from.
				 * @param item - The item to consume.
				 * @param force - Forces an item to be consumed, regardless of gamemode.
				 */
				public static void consumeItem(EntityPlayer player, Item item, boolean force)
				{
					if (!player.capabilities.isCreativeMode || force)
					{
						player.inventory.consumeInventoryItem(item);
					}
				}

				/**
				 * Returns the ItemStack residing in the specified player's helm slot.
				 * 
				 * @param player - The player to return an ItemStack from
				 * @return An ItemStack residing in the helm slot.
				 */
				public static ItemStack getHelmSlotItemStack(EntityPlayer player)
				{
					return player.inventory.armorItemInSlot(3);
				}

				/**
				 * Returns the ItemStack residing in the specified player's chestplate slot.
				 * 
				 * @param player - The player to return an ItemStack from
				 * @return An ItemStack residing in the chestplate slot.
				 */
				public static ItemStack getChestSlotItemStack(EntityPlayer player)
				{
					return player.inventory.armorItemInSlot(2);
				}

				/**
				 * Returns the ItemStack residing in the specified player's leggings slot.
				 * 
				 * @param player - The player to return an ItemStack from
				 * @return An ItemStack residing in the leggings slot.
				 */
				public static ItemStack getLegsSlotItemStack(EntityPlayer player)
				{
					return player.inventory.armorItemInSlot(1);
				}

				/**
				 * Returns the ItemStack residing in the specified player's boots slot.
				 * 
				 * @param player - The player to return an ItemStack from
				 * @return An ItemStack residing in the boots slot.
				 */
				public static ItemStack getBootSlotItemStack(EntityPlayer player)
				{
					return player.inventory.armorItemInSlot(0);
				}
			}
		}
	}
}
