package com.arisux.airi.api.window;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.arisux.airi.lib.GuiTypeLib.GuiCustomScreen;
import com.arisux.airi.lib.RenderLib;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WindowManager extends GuiCustomScreen
{
	private WindowAPI windowapi;
	private Window resetWindow;
	private Window draggedWindow = null;
	protected GuiScreen parentScreen;
	private int mouseXLast, mouseYLast;

	public WindowManager(WindowAPI windowapi, GuiScreen parentScreen)
	{
		this.windowapi = windowapi;
		this.parentScreen = parentScreen;
	}

	public void initGui()
	{
		this.buttonList.clear();
	}

	public GuiScreen getParentScreen()
	{
		return parentScreen;
	}

	@Override
	protected void keyTyped(char par1, int par2)
	{
		if (par2 == Keyboard.KEY_ESCAPE)
		{
			Minecraft.getMinecraft().currentScreen = parentScreen;
		}
		if (windowapi.getWindowsRegistry().size() > 0)
		{
			windowapi.getWindowsRegistry().get(0).keyTyped(par1, par2);
		}
	}

	public Window getTopWindow(int mouseX, int mouseY)
	{
		Window window = null;

		for (Window w : windowapi.getWindowsRegistry())
		{
			if (mouseX > w.getX() && mouseX < w.getX() + w.getWidth() && mouseY > w.getY() - 16 && mouseY < w.getY() + w.getHeight())
			{
				window = w;
			}
		}

		return window;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int par3)
	{
		super.mouseClicked(mouseX, mouseY, par3);

		Window top = getTopWindow(mouseX, mouseY);

		if (top != null)
		{
			if (mouseX > top.getX() + top.getWidth() - 14 && mouseX < top.getX() + top.getWidth() - 2 && mouseY < top.getY() - 2 && mouseY > top.getY() - 14)
			{
				top.close();
			}
		}

		for (int x = 0; x < windowapi.getWindowsRegistry().size(); x++)
		{
			Window window = windowapi.getWindowsRegistry().get(x);
			
			if (top == window)
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

		RenderLib.drawGradientRect(0, 0, RenderLib.scaledDisplayResolution().getScaledWidth(), RenderLib.scaledDisplayResolution().getScaledHeight(), 0xDD000000, 0x66000000);

		GL11.glPushMatrix();
		{
			if (mc.gameSettings.guiScale > 1 || mc.gameSettings.guiScale == 0)
				GL11.glScalef(0.5F, 0.5F, 0.5F);
			fontRendererObj.drawString("Press ESC to hide the window manager, and LEFT ALT + W to show the window manager.", 10, 11, 0xFF0088FF);
			GL11.glColor3f(1F, 1F, 1F);
		}
		GL11.glPopMatrix();

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
			} else
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