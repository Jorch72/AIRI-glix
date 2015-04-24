package com.arisux.airi.api.window.themes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import com.arisux.airi.AIRI;
import com.arisux.airi.api.window.gui.windows.Window;
import com.arisux.airi.api.window.gui.DesktopWindowManager;

public abstract class Theme implements ITheme
{
	private String themeName;

	public Theme(String name)
	{
		this.themeName = name;
	}

	@Override
	public void draw(Window window, int mouseX, int mouseY)
	{
		this.drawBackground(window, mouseX, mouseY);
		this.drawTitleBar(window, mouseX, mouseY);
		this.drawCloseButton(window, mouseX, mouseY);
		this.drawContents(window, mouseX, mouseY);
	}

	@Override
	public void drawBackground(Window window, int mouseX, int mouseY)
	{
		;
	}

	@Override
	public void drawTitleBar(Window window, int mouseX, int mouseY)
	{
		;
	}

	@Override
	public void drawCloseButton(Window window, int mouseX, int mouseY)
	{
		;
	}

	public void drawContents(Window window, int mouseX, int mouseY)
	{
		if (getWindowManager() != null)
		{
			window.draw(mouseX, mouseY);

			int x = -1;
			int y = -1;

			if (getWindowManager().getTopWindow(mouseX, mouseY) == window)
			{
				x = mouseX;
				y = mouseY;
			}

			for (GuiButton button : window.getButtonList())
			{
				button.drawButton(Minecraft.getMinecraft(), x, y);
				GL11.glColor3f(1f, 1f, 1f);
			}
		} else
		{
			AIRI.logger.info("Window Manager returned null.");
		}
	}

	public String getName()
	{
		return this.themeName;
	}

	public void setName(String themeName)
	{
		this.themeName = themeName;
	}

	public DesktopWindowManager getWindowManager()
	{
		return AIRI.windowApi().getWindowManager();
	}
}
