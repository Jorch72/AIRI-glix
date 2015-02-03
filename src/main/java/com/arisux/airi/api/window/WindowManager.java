package com.arisux.airi.api.window;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.arisux.airi.lib.*;
import com.arisux.airi.lib.GuiElements.GuiCustomScreen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WindowManager extends GuiCustomScreen
{
	private WindowAPI windowapi;
	private Window resetWindow = null;
	private Window draggedWindow = null;
	private Window topWindow = null;
	protected GuiScreen parentScreen;
	private int mouseXLast, mouseYLast;

	public WindowManager(WindowAPI windowapi, GuiScreen parentScreen)
	{
		this.windowapi = windowapi;
		this.parentScreen = parentScreen;
	}

	@Override
	public void initGui()
	{
		this.buttonList.clear();
	}

	public GuiScreen getParentScreen()
	{
		return parentScreen;
	}

	@Override
	protected void keyTyped(char par1, int key)
	{
		if (key == Keyboard.KEY_ESCAPE)
		{
			Minecraft.getMinecraft().currentScreen = parentScreen;
		}

		if (topWindow != null)
		{
			topWindow.keyTyped(par1, key);
		}
	}

	public Window getTopWindow(int mouseX, int mouseY)
	{
		Window top = null;

		for (Window window : windowapi.getWindowsRegistry())
		{
			if (mouseX > window.getX() && mouseX < window.getX() + window.getWidth() && mouseY > window.getY() - 16 && mouseY < window.getY() + window.getHeight())
			{
				top = window;
			}
		}

		return top;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int par3)
	{
		super.mouseClicked(mouseX, mouseY, par3);

		topWindow = getTopWindow(mouseX, mouseY);

		if (topWindow != null)
		{
			topWindow.onActivated();

			if (mouseX > topWindow.getX() + topWindow.getWidth() - 14 && mouseX < topWindow.getX() + topWindow.getWidth() - 2 && mouseY < topWindow.getY() - 2 && mouseY > topWindow.getY() - 14)
			{
				topWindow.close();
			}
		}

		for (Window window : windowapi.getWindowsRegistry())
		{
			if (topWindow == window)
			{
				resetWindow = window;

				window.onMousePressed(mouseX, mouseY);

				if (mouseX > window.getX() && mouseX < window.getX() + window.getWidth() && mouseY > window.getY() - 16 && mouseY < window.getY())
				{
					draggedWindow = window;
					mouseXLast = mouseX;
					mouseYLast = mouseY;
				}

				for (GuiButton button : window.getButtonList())
				{
					if (button.mousePressed(mc, mouseX, mouseY))
					{
						button.func_146113_a(mc.getSoundHandler());
						window.onButtonPress(button);
					}
				}
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3)
	{
		if (parentScreen != null)
		{
			parentScreen.drawScreen(mouseX, mouseY, par3);
		}

		RenderUtil.drawRect(0, 0, RenderUtil.scaledDisplayResolution().getScaledWidth(), RenderUtil.scaledDisplayResolution().getScaledHeight(), 0x88000000);

		int barHeight = 15;
		int shadowWidth = 6;
		for (int shadowLayer = 0; shadowLayer < shadowWidth; shadowLayer += 2)
		{
			RenderUtil.drawRect(0 - shadowWidth + shadowLayer, 0 - 16 - shadowWidth + shadowLayer, RenderUtil.scaledDisplayResolution().getScaledWidth() + shadowWidth * 2 - shadowLayer * 2, barHeight + shadowWidth * 2 + 16 - shadowLayer * 2, 0x11000000);
		}
		RenderUtil.drawRect(0, 0, RenderUtil.scaledDisplayResolution().getScaledWidth(), barHeight, 0xFFFFFFFF);

		fontRendererObj.drawString(ChatUtil.format("&8AIRI &7Press ESC to hide. Press LEFT ALT + W to show."), 4, 4, this.getWindowAPI().getCurrentTheme().getForegroundColor());
		GL11.glColor3f(1F, 1F, 1F);

		int iconPadding = 16;
		int index = 0;
		int maxWindowTitleWidth = 100;
		for (Window window : this.getWindowAPI().getWindowsRegistry())
		{
			if (RenderUtil.getStringRenderWidth(window.getTitle()) > maxWindowTitleWidth)
			{
				maxWindowTitleWidth = RenderUtil.getStringRenderWidth(window.getTitle());
			}
		}
		for (Window window : this.getWindowAPI().getWindowsRegistry())
		{
			int y = (20 + iconPadding * index++);
			RenderUtil.drawRect(4, y, maxWindowTitleWidth + 5, 13, 0xAA000000);
			RenderUtil.drawString(window.getTitle(), 7, y + 3, this.getWindowAPI().getCurrentTheme().getButtonColor(), false);
		}

		if (resetWindow != null)
		{
			windowapi.getWindowsRegistry().remove(resetWindow);
			windowapi.getWindowsRegistry().add(windowapi.getWindowsRegistry().size(), resetWindow);
			resetWindow = null;
		}

		for (int x = windowapi.getWindowsRegistry().size() - 1; x >= 0; x--)
		{
			Window window = windowapi.getWindowsRegistry().get(x);

			if (Mouse.isButtonDown(0))
			{
				if (draggedWindow == window)
				{
					int diffX = mouseX - mouseXLast;
					int diffY = mouseY - mouseYLast;
					window.setPosition(window.getX() + diffX, window.getY() + diffY);
					mouseXLast = mouseX;
					mouseYLast = mouseY;
				}
			}
			else
			{
				draggedWindow = null;
			}
		}

		for (Window window : new ArrayList<Window>(windowapi.getWindowsRegistry()))
		{
			if (window != null && windowapi.getWindowsRegistry().contains(window))
			{
				windowapi.drawWindow(window, mouseX, mouseY);
			}
		}

		super.drawScreen(mouseX, mouseY, par3);
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height)
	{
		if (parentScreen != null)
		{
			parentScreen.setWorldAndResolution(mc, width, height);
		}

		super.setWorldAndResolution(mc, width, height);
	}

	@Override
	protected void actionPerformed(GuiButton b)
	{
		for (Window window : windowapi.getWindowsRegistry())
		{
			window.onButtonPress(b);
		}
	}

	public WindowAPI getWindowAPI()
	{
		return windowapi;
	}
}
