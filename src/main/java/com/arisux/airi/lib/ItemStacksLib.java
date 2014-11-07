
package com.arisux.airi.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStacksLib
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
}