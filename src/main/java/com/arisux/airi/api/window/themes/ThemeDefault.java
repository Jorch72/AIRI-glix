package com.arisux.airi.api.window.themes;

import com.arisux.airi.api.window.Window;
import com.arisux.airi.lib.RenderUtil;

public class ThemeDefault extends Theme implements ITheme
{
	public ThemeDefault(String name)
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
		RenderUtil.drawGradientRect(window.getX(), window.getY() - 2, window.getWidth(), window.getHeight(), 0xFF000000, 0xAA001122);
	}

	@Override
	public void drawTitleBar(Window window, int mouseX, int mouseY)
	{
		RenderUtil.drawRect(window.getX(), window.getY() - 16, window.getWidth() - 15, 14, 0xBB000000);
		RenderUtil.drawString(window.getTitle(), window.getX()  + 5, window.getY() - 12, 0xFF0088FF, false);
	}

	@Override
	public void drawCloseButton(Window window, int mouseX, int mouseY)
	{
		RenderUtil.drawRect(window.getX() + window.getWidth() - 15, window.getY() - 16, 15, 14, 0x22CCCCCC);
		RenderUtil.drawString("x", window.getX() + window.getWidth() - 10, window.getY() - 14, 0x330088FF, false);
	}
}
