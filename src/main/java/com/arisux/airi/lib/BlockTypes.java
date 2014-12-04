package com.arisux.airi.lib;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.arisux.airi.lib.RenderUtil.IconSet;
import com.arisux.airi.lib.enums.IconSides;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTypes
{
	public static class HookedBlock extends Block
	{
		protected boolean renderNormal;
		protected boolean isOpaque;
		private boolean disableIcon;
		private ISimpleBlockRenderingHandler renderType;
		private RenderUtil.IconSet iconSet;

		public HookedBlock(Material material)
		{
			super(material);
			this.renderNormal = true;
			this.isOpaque = true;
			this.disableIcon = false;
		}

		@SideOnly(Side.CLIENT)
		public Block setRenderType(ISimpleBlockRenderingHandler renderType)
		{
			this.renderType = renderType;
			return this;
		}

		public Block setRenderNormal(boolean renderNormal)
		{
			this.renderNormal = renderNormal;
			return this;
		}

		public String getBlockTextureName()
		{
			return this.textureName;
		}

		@Override
		public boolean renderAsNormalBlock()
		{
			return renderNormal;
		}

		public Block setOpaque(boolean opaque)
		{
			this.isOpaque = opaque;
			return this;
		}

		@Override
		public int getRenderType()
		{
			return this.renderType == null ? 0 : this.renderType.getRenderId();
		}

		@Override
		public boolean isOpaqueCube()
		{
			return isOpaque;
		}
		
		public Block setIconSet(IconSet iconSet)
		{
			this.iconSet = iconSet;
			return this;
		}

		@Override
		public void registerBlockIcons(IIconRegister iconRegister)
		{
			if (disableIcon)
			{
				return;
			}
			
			if (this.iconSet != null)
			{
				this.iconSet.registerIcons(iconRegister);
				this.blockIcon = iconRegister.registerIcon(iconSet.frontRes);
			}
			else
			{
				super.registerBlockIcons(iconRegister);
			}
		}

		@Override
		@SideOnly(Side.CLIENT)
		public IIcon getIcon(int side, int meta)
		{
			if (this.iconSet != null)
			{
				IconSides iconSide = IconSides.getSideFor(side);

				switch (iconSide)
				{
					case BOTTOM:
						return iconSet.top;
					case TOP:
						return iconSet.top;
					case BACK:
						return iconSet.front;
					case FRONT:
						return iconSet.front;
					case LEFT:
						return iconSet.front;
					case RIGHT:
						return iconSet.front;
					default:
						return iconSet.front;
				}
			}
			
			return super.getIcon(side, meta);
		}

		public Block disableIcon()
		{
			return this.disableIcon(true);
		}
		
		public Block disableIcon(boolean disableIcon)
		{
			this.disableIcon = disableIcon;
			return this;
		}

		public float getHardness()
		{
			return this.blockHardness;
		}

		public float getResistance()
		{
			return this.blockResistance;
		}
	}

	public static class HookedBlockSlab extends BlockSlab
	{
		public HookedBlockSlab(Material material)
		{
			super(false, material);
			this.setBlockName(getLocalizedName() + "Slab");
		}

		@Override
		public int getRenderType()
		{
			return 0;
		}

		@Override
		public boolean renderAsNormalBlock()
		{
			return false;
		}

		@Override
		public boolean isOpaqueCube()
		{
			return false;
		}

		/**
		 * OBF: func_150002_b
		 * DEOBF: getFullSlabName
		 */
		@Override
		public String func_150002_b(int type)
		{
			return getUnlocalizedName() + "-slab";
		}
	}

	public static class HookedBlockStairs extends BlockStairs
	{
		public HookedBlockStairs(Block parentBlock)
		{
			super(parentBlock, 0);
			this.setHardness(3.0F);
			this.setResistance(3.0F);
			this.setStepSound(parentBlock.stepSound);
		}

		@Override
		public boolean isOpaqueCube()
		{
			return false;
		}

		@Override
		public boolean renderAsNormalBlock()
		{
			return false;
		}

		@Override
		public int getRenderType()
		{
			return 10;
		}
	}

	public static abstract class HookedBlockContainer extends HookedBlock implements ITileEntityProvider
	{
		public HookedBlockContainer(Material material)
		{
			super(material);
			this.isBlockContainer = true;
			this.isOpaque = false;
			this.renderNormal = false;
		}

		@Override
		public void breakBlock(World world, int posX, int posY, int posZ, Block blockBroken, int meta)
		{
			super.breakBlock(world, posX, posY, posZ, blockBroken, meta);
			world.removeTileEntity(posX, posY, posZ);
		}

		@Override
		public boolean onBlockEventReceived(World world, int posX, int posY, int posZ, int eventId, int eventData)
		{
			super.onBlockEventReceived(world, posX, posY, posZ, eventId, eventData);
			TileEntity tileentity = world.getTileEntity(posX, posY, posZ);
			return tileentity != null ? tileentity.receiveClientEvent(eventId, eventData) : false;
		}
	}

	public static class GhostBlock extends HookedBlock
	{
		private Block parentBlock;

		public GhostBlock(Block parentBlock)
		{
			super(parentBlock.getMaterial());
			this.parentBlock = parentBlock;
		}

		@Override
		public int getRenderType()
		{
			return -1;
		}

		public Block getParentBlock()
		{
			return parentBlock;
		}

		public GhostBlock setAttributesFrom(HookedBlock block)
		{
			this.setHardness(block.getHardness());
			this.setResistance(block.getResistance());
			this.setStepSound(block.stepSound);
			return this;
		}

		@Override
		public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int side, float subX, float subY, float subZ)
		{
			return parentBlock.onBlockActivated(world, posX, posY, posZ, player, side, subX, subY, subZ);
		}

		@Override
		public void breakBlock(World world, int posX, int posY, int posZ, Block blockBroken, int meta)
		{
			parentBlock.breakBlock(world, posX, posY, posZ, blockBroken, meta);
		}
	}
}