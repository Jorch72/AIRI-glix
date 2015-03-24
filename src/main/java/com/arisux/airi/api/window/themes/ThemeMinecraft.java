package com.arisux.airi.api.window.themes;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;

import com.arisux.airi.api.window.Window;
import com.arisux.airi.lib.RenderUtil;

public class ThemeMinecraft extends Theme implements ITheme
{
	public ThemeMinecraft(String name)
	{
		super(name);
		this.setName(name);
	}

	@Override
	public void drawWindow(Window window, int mouseX, int mouseY)
	{
		super.drawWindow(window, mouseX, mouseY);
	}

	@Override
	public void drawBackground(Window window, int mouseX, int mouseY)
	{
		RenderUtil.drawBlockSide(Blocks.stone_slab, 2, window.getX(), window.getY() - 16, window.getWidth(), 16, window.getWidth() / 50f, 0.5f);
	}

	@Override
	public void drawTitleBar(Window window, int mouseX, int mouseY)
	{
		RenderUtil.drawBlockSide(Blocks.bedrock, 0, window.getX(), window.getY(), window.getWidth(), window.getHeight(), window.getWidth() / 50f, window.getHeight() / 50f);
		getWindowManager().drawCenteredString(Minecraft.getMinecraft().fontRenderer, window.getTitle(), window.getX() + window.getWidth() / 2, window.getY() - 12, 0xFFFFFF);
	}

	@Override
	public void drawCloseButton(Window window, int mouseX, int mouseY)
	{
		RenderUtil.drawBlockSide(Blocks.tnt, 2, window.getX() + window.getWidth() - 14, window.getY() - 14, 12, 12);
	}
}
