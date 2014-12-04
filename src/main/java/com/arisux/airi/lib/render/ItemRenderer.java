package com.arisux.airi.lib.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import com.arisux.airi.lib.RenderUtil.PlayerResourceManager;

public abstract class ItemRenderer implements IItemRenderer
{
	protected PlayerResourceManager resourceManager;
	protected ResourceLocation resource;
	protected ModelBase model;
	private boolean rendersInFirstPerson, rendersInThirdPerson, rendersInInventory, rendersInWorld;

	public ItemRenderer(ModelBase model, ResourceLocation resource)
	{
		this.resourceManager = new PlayerResourceManager();
		this.model = model;
		this.resource = resource;

		this.rendersInFirstPerson = true;
		this.rendersInThirdPerson = true;
		this.rendersInInventory = true;
		this.rendersInWorld = true;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		switch (type)
		{
			case EQUIPPED:
				return rendersInThirdPerson;

			case EQUIPPED_FIRST_PERSON:
				return rendersInFirstPerson;

			case INVENTORY:
				return rendersInInventory;

			case ENTITY:
				return rendersInWorld;

			default:
				return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
			case EQUIPPED:
				this.renderThirdPerson(item, data);
				break;
			case EQUIPPED_FIRST_PERSON:
				this.renderFirstPerson(item, data);
				break;
			case INVENTORY:
				this.renderInInventory(item, data);
				break;
			case ENTITY:
				this.renderInWorld(item, data);
				break;
			default:
				break;
		}
	}

	public void renderThirdPerson(ItemStack item, Object... data)
	{
		;
	}

	public void renderFirstPerson(ItemStack item, Object... data)
	{
		;
	}

	public void renderInInventory(ItemStack item, Object... data)
	{
		;
	}

	public void renderInWorld(ItemStack item, Object... data)
	{
		;
	}

	public ItemRenderer setRendersInThirdPerson(boolean rendersInThirdPerson)
	{
		this.rendersInThirdPerson = rendersInThirdPerson;
		return this;
	}

	public ItemRenderer setRendersInFirstPerson(boolean rendersInFirstPerson)
	{
		this.rendersInFirstPerson = rendersInFirstPerson;
		return this;
	}

	public ItemRenderer setRendersInInventory(boolean rendersInInventory)
	{
		this.rendersInInventory = rendersInInventory;
		return this;
	}

	public ItemRenderer setRendersInWorld(boolean rendersInWorld)
	{
		this.rendersInWorld = rendersInWorld;
		return this;
	}

	public ModelBase getModel()
	{
		return model;
	}

	public ResourceLocation getResourceLocation()
	{
		return resource;
	}
	
	public void setResourceLocation(ResourceLocation resource)
	{
		this.resource = resource;
	}
}