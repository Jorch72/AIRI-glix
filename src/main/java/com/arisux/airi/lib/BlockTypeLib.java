package com.arisux.airi.lib;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.arisux.airi.lib.BlockLib.BlockIconVector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTypeLib
{
	public static class HookedBlock extends Block
	{
		private boolean renderNormal, isOpaque, disableIcon;

		public HookedBlock(Material material)
		{
			super(material);
			this.renderNormal = true;
			this.isOpaque = true;
			this.disableIcon = false;
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
		public boolean isOpaqueCube()
		{
			return isOpaque;
		}
		
		@Override
		public void registerBlockIcons(IIconRegister iconRegister)
		{
			if (!disableIcon)
			{
				super.registerBlockIcons(iconRegister);
			}
		}

		public Block disableIcon()
		{
			this.disableIcon = true;
			return this;
		}
	}

	public static class HookedBlockSlab extends BlockSlab
	{
		public boolean isOpaque;
		public boolean rendersNormally;

		public HookedBlockSlab(Material par3)
		{
			super(false, par3);
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

		@Override
		public String func_150002_b(int var1)
		{
			return "slab";
		}
	}

	public static class HookedBlockStairs extends BlockStairs
	{
		public HookedBlockStairs(Block par2Block)
		{
			super(par2Block, 0);
			this.setHardness(3.0F);
			this.setResistance(3.0F);
			this.setStepSound(par2Block.stepSound);
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

	public static class HookedBlockMultiSided extends HookedBlock
	{
		private BlockIconVector vector;

		public HookedBlockMultiSided(BlockIconVector vector, Material material)
		{
			super(material);
			this.vector = vector;
		}

		@SideOnly(Side.CLIENT)
		public IIcon getIcon(int i, int meta)
		{
			switch (i)
			{
				case 0://bottom
					return vector.top;
				case 1://top
					return vector.top;
				case 2://back
					return vector.front;
				case 3://front
					return vector.front;
				case 4://left
					return vector.front;
				case 5://right
					return vector.front;
				default:
					return vector.front;
			}
		}

		@SideOnly(Side.CLIENT)
		public void registerBlockIcons(IIconRegister register)
		{
			this.vector.registerIcons(register);
			this.blockIcon = register.registerIcon(vector.frontRes);
		}
	}
}
