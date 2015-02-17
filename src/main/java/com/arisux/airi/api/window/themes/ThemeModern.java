package com.arisux.airi.api.window.themes;

import com.arisux.airi.api.window.Window;
import com.arisux.airi.lib.RenderUtil;

public class ThemeModern extends Theme implements ITheme
{
	public ThemeModern(String name)
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
		int shadowWidth = 6;

		for (int shadowLayer = 0; shadowLayer < shadowWidth; shadowLayer += 2)
		{
			RenderUtil.drawRect(window.getX() - shadowWidth + shadowLayer, window.getY() - 16 - shadowWidth + shadowLayer, window.getWidth() + shadowWidth * 2 - shadowLayer * 2, window.getHeight() + shadowWidth * 2 + 16 - shadowLayer * 2, 0x11000000);
		}

		RenderUtil.drawGradientRect(window.getX(), window.getY() - 2, window.getWidth(), window.getHeight(), 0xFF888888, this.getBackgroundColor());
	}

	@Override
	public void drawTitleBar(Window window, int mouseX, int mouseY)
	{
		RenderUtil.drawRect(window.getX(), window.getY() - 16, window.getWidth() - 15, 14, 0xAAFFFFFF);
		RenderUtil.drawString(window.getTitle(), window.getX() + 5, window.getY() - 12, this.getTextColor(), false);
	}

	@Override
	public void drawCloseButton(Window window, int mouseX, int mouseY)
	{
		RenderUtil.drawRect(window.getX() + window.getWidth() - 15, window.getY() - 16, 15, 14, this.getButtonColor());
		RenderUtil.drawString("x", window.getX() + window.getWidth() - 10, window.getY() - 14, 0xFFFFFFFF, false);
	}

	@Override
	public int getBackgroundColor()
	{
		return 0xFFFFFFFF;
	}

	@Override
	public int getForegroundColor()
	{
		return 0xFFFFFFFF;
	}

	@Override
	public int getTextColor()
	{
		return 0xFF222222;
	}

	@Override
	public int getButtonColor()
	{
		return 0xFFCC1111;
	}
}
