package com.arisux.airi.lib;

import java.util.ArrayList;
import java.util.List;

import com.arisux.airi.lib.BlockTypeLib.HookedBlock;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RegistryLib
{
	/**
	 * Used for easier management of large quantities of Item and Block instances.
	 */
	public interface IBHandler
	{
		public String getDomain();
		public CreativeTabs getCreativeTab();
		public ArrayList<Object> getHandledObjects();
	}
	
	/**
	 * Wrapper method for the registerBlock method found in GameRegistry. Allows for simplified
	 * registration of Blocks. Using this method will result in the Block being automatically assigned
	 * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
	 * 
	 * Automatically assigned texture IDs are set to the default resource location of Blocks in the
	 * mod's domain specified in the IBHandler. Texture names are based off of the Block's unlocalized name.
	 * 
	 * @param block - The Block instance to register.
	 * @param reference - The reference ID to register the block under.
	 * @param handler - The IBHandler instance that is registerring this block.
	 * @return Returns the Block instances originally provided in the block parameter.
	 */
	public static Block registerBlock(Block block, String reference, IBHandler handler)
	{
		return registerBlock(block, reference, handler, true);
	}

	/**
	 * Wrapper method for the registerBlock method found in GameRegistry. Allows for simplified
	 * registration of Blocks. Using this method will result in the Block being automatically assigned
	 * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
	 * 
	 * Automatically assigned texture IDs are set to the default resource location of Blocks in the
	 * mod's domain specified in the IBHandler. Texture names are based off of the Block's unlocalized name.
	 * 
	 * @param block - The Block instance to register.
	 * @param reference - The reference ID to register the block under.
	 * @param handler - The IBHandler instance that is registerring this block.
	 * @param visibleOnTab - If set true, the Block will automatically be registered to the CreativeTab
	 * specified in the IBHandler instance this Block was registered from.
	 * @return Returns the Block instances originally provided in the block parameter.
	 */
	public static Block registerBlock(Block block, String reference, IBHandler handler, boolean visibleOnTab)
	{
		return registerBlock(block, reference, null, handler, visibleOnTab);
	}
	
	/**
	 * Wrapper method for the registerBlock method found in GameRegistry. Allows for simplified
	 * registration of Blocks. Using this method will result in the Block being automatically assigned
	 * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
	 * 
	 * Automatically assigned texture IDs are set to the default resource location of Blocks in the
	 * mod's domain specified in the IBHandler. Texture names are based off of the Block's unlocalized name.
	 * 
	 * @param block - The Block instance to register.
	 * @param reference - The reference ID to register the block under.
	 * @param texture - The path to the texture assigned to this block.
	 * @param handler - The IBHandler instance that is registerring this block.
	 * @return Returns the Block instances originally provided in the block parameter.
	 */
	public static Block registerBlock(Block block, String reference, String texture, IBHandler handler)
	{
		return registerBlock(block, reference, texture, handler, true);
	}
	
	/**
	 * Wrapper method for the registerBlock method found in GameRegistry. Allows for simplified
	 * registration of Blocks. Using this method will result in the Block being automatically assigned
	 * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
	 * 
	 * Automatically assigned texture IDs are set to the default resource location of Blocks in the
	 * mod's domain specified in the IBHandler. Texture names are based off of the Block's unlocalized name.
	 * 
	 * This method allows you to provide a parent texture block as a parameter. The block you provide this
	 * method with will allow the block currently being registered to use the parent block's texture, but
	 * only on the condition that the block extends HookedBlock.
	 * 
	 * @param block - The Block instance to register.
	 * @param reference - The reference ID to register the block under.
	 * @param textureBlock - The parent block of which this block will receive its texture from.
	 * @param handler - The IBHandler instance that is registerring this block.
	 * @return Returns the Block instances originally provided in the block parameter.
	 */
	public static Block registerBlock(Block block, String reference, Block textureBlock, IBHandler handler)
	{
		return registerBlock(block, reference, (textureBlock instanceof HookedBlock ? ((HookedBlock) textureBlock).getBlockTextureName() : null), handler, true);
	}

	/**
	 * Wrapper method for the registerBlock method found in GameRegistry. Allows for simplified
	 * registration of Blocks. Using this method will result in the Block being automatically assigned
	 * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
	 * 
	 * Automatically assigned texture IDs are set to the default resource location of Blocks in the
	 * mod's domain specified in the IBHandler. Texture names are based off of the Block's unlocalized name.
	 * 
	 * @param block - The Block instance to register.
	 * @param reference - The reference ID to register the block under.
	 * @param texture - The path to the texture assigned to this block.
	 * @param handler - The IBHandler instance that is registerring this block.
	 * @param visibleOnTab - If set true, the Block will automatically be registered to the CreativeTab
	 * specified in the IBHandler instance this Block was registered from.
	 * @return Returns the Block instances originally provided in the block parameter.
	 */
	public static Block registerBlock(Block block, String reference, String texture, IBHandler handler, boolean visibleOnTab)
	{
		block.setBlockName(handler.getDomain() + reference);

		if (texture == null)
		{
			block.setBlockTextureName((block.getUnlocalizedName()).replace("tile.", ""));
		}
		else
		{
			block.setBlockTextureName(texture);
		}

		if (handler.getCreativeTab() != null && visibleOnTab)
		{
			block.setCreativeTab(handler.getCreativeTab());
		}

		if (handler.getHandledObjects() != null)
		{
			handler.getHandledObjects().add(block);
		}

		GameRegistry.registerBlock(block, reference);

		return block;
	}

	/**
	 * Wrapper method for the registerItem method found in GameRegistry. Allows for simplified
	 * registration of Items. Using this method will result in the Item being automatically assigned
	 * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
	 * 
	 * Automatically assigned texture IDs are set to the default resource location of Items in the
	 * mod's domain specified in the IBHandler. Texture names are based off of the Item's unlocalized name.
	 * 
	 * Unlocalized names are based off of the specified String reference IDs.
	 * 
	 * @param item - The Block instance to register.
	 * @param reference - The reference ID to register the item under.
	 * @param handler - The IBHandler instance that is registerring this item.
	 * @return Returns the Item instances originally provided in the item parameter.
	 */
	public static Item registerItem(Item item, String reference, IBHandler handler)
	{
		return registerItem(item, reference, handler, true, null);
	}

	/**
	 * Wrapper method for the registerItem method found in GameRegistry. Allows for simplified
	 * registration of Items. Using this method will result in the Item being automatically assigned
	 * a texture location, creative tab, and added to the ArrayList of objects in the specified IBHandler.
	 * 
	 * Automatically assigned texture IDs are set to the default resource location of Items in the
	 * mod's domain specified in the IBHandler. Texture names are based off of the Item's unlocalized name.
	 * 
	 * Unlocalized names are based off of the specified String reference IDs.
	 * 
	 * @param item - The Block instance to register.
	 * @param reference - The reference ID to register the item under.
	 * @param handler - The IBHandler instance that is registerring this item.
	 * @param visibleOnPrimaryTab - If set to true, the Item will be automatically added to the CreativeTab
	 * specified in the IBHandler. If set to false, you may specifiy a different CreativeTab instance for
	 * the Item to be added to.
	 * @param tab - If specified, the Item will be assigned to the specified CreativeTab. Set to null for
	 * no creative tab.
	 * @return Returns the Item instances originally provided in the item parameter.
	 */
	public static Item registerItem(Item item, String reference, IBHandler handler, boolean visibleOnPrimaryTab, CreativeTabs tab)
	{
		GameRegistry.registerItem(item, reference);

		item.setUnlocalizedName(handler.getDomain() + reference);
		item.setTextureName((item.getUnlocalizedName()).replace("item.", ""));

		if (handler.getCreativeTab() != null && visibleOnPrimaryTab)
		{
			item.setCreativeTab(handler.getCreativeTab());
		}
		else if (tab != null)
		{
			item.setCreativeTab(tab);
		}

		if (handler.getHandledObjects() != null)
		{
			handler.getHandledObjects().add(item);
		}
		
		return item;
	}

	/**
	 * Wrapper method for the registerKeyBinding method found in ClientRegistry. Allows for
	 * more efficient registration of a KeyBinding. 
	 * 
	 * @param keyName - Name of the KeyBinding to be registered.
	 * @param key - Integer assigned to each individual keyboard key in the Keyboard class.
	 * @param keyGroup - Group of KeyBindings to assign this KeyBinding to.
	 * @return The KeyBinding Instance created from the provided parameters.
	 */
	@SideOnly(Side.CLIENT)
	public static KeyBinding registerKeybinding(String keyName, int key, String keyGroup)
	{
		KeyBinding keybind = new KeyBinding(String.format("key.%s", keyName), key, keyGroup);
		ClientRegistry.registerKeyBinding(keybind);
		return keybind;
	}
	
	/**
	 * Finds the first IRecipe instance registered to a specific Item or Block instance.
	 * 
	 * @param obj - Item or Block instance to scan for recipes.
	 * @return First found instance of an IRecipe registered to the specified Item or Block.
	 */
	@SuppressWarnings("unchecked")
	public static IRecipe getRecipe(Object obj)
	{
		ItemStack stack = ItemStacksLib.newStack(obj);
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();

		if (stack != null)
		{
			for (IRecipe recipe : recipes)
			{
				if (recipe != null && recipe.getRecipeOutput() != null && recipe.getRecipeOutput().getItem() == stack.getItem())
				{
					return recipe;
				}
			}
		}

		return null;
	}
	
	/**
	 * Finds all IRecipe instances registered to a specific Item or Block instance.
	 * 
	 * @param obj - Item or Block instance to scan for recipes.
	 * @return All found instances of IRecipes registered to the specified Item or Block.
	 */
	@SuppressWarnings("unchecked")
	public static List<IRecipe> getRecipes(Object obj)
	{
		ItemStack stack = ItemStacksLib.newStack(obj);
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		List<IRecipe> foundRecipes = new ArrayList<IRecipe>();

		if (stack != null)
		{
			for (IRecipe recipe : recipes)
			{
				if (recipe != null && recipe.getRecipeOutput() != null && recipe.getRecipeOutput().getItem() == stack.getItem())
				{
					foundRecipes.add(recipe);
				}
			}
		}

		return foundRecipes;
	}
}
