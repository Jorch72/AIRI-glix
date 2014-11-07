package com.arisux.airi.lib;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.arisux.airi.AIRI;
import com.arisux.airi.Settings;

public class PlayerLib
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
		String retrieved = NetworkLib.getURLContents(String.format(AIRI.INSTANCE.properties.URL_RETRIEVE_UUID, username));
		return AIRI.instance().settings.propertyList.get(Settings.Setting.NETWORKING).getBoolean() ? retrieved != null &&  retrieved.length() >= 32 ? retrieved : username : username;
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
}