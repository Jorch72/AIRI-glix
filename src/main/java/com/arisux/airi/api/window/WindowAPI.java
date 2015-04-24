package com.arisux.airi.api.window;

import com.arisux.airi.AIRI;
import com.arisux.airi.api.window.gui.DesktopWindowManager;
import com.arisux.airi.api.window.gui.windows.Window;
import com.arisux.airi.api.window.themes.Theme;
import com.arisux.airi.api.window.themes.ThemeDefault;
import com.arisux.airi.api.window.themes.ThemeMinecraft;
import com.arisux.airi.api.window.themes.ThemeModern;
import com.arisux.airi.lib.ModUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;

public class WindowAPI
{
	private DesktopWindowManager windowManager;
	private Theme currentTheme;
	private ArrayList<Window> windows = new ArrayList<Window>();
	private HashMap<String, Theme> themes = new HashMap<String, Theme>();

	public static final Theme themeDefault = new ThemeDefault("default");
	public static final Theme themeModern = new ThemeModern("modern");
	public static final Theme themeMinecraft = new ThemeMinecraft("minecraft");

	public WindowAPI()
	{
		this.windowManager = new DesktopWindowManager(this, null);
		this.registerTheme(themeDefault);
		this.registerTheme(themeModern);
		this.registerTheme(themeMinecraft);
		this.currentTheme = getThemeForName("modern");
	}

	public void onTick()
	{
		if (getWindowsRegistry().size() <= 0 && Minecraft.getMinecraft().currentScreen instanceof DesktopWindowManager)
		{
			Minecraft.getMinecraft().displayGuiScreen(this.getWindowManager().parentScreen);
		}

		if (this.getWindowManager().parentScreen != Minecraft.getMinecraft().currentScreen && !(Minecraft.getMinecraft().currentScreen instanceof DesktopWindowManager))
		{
			this.getWindowManager().parentScreen = Minecraft.getMinecraft().currentScreen;
		}
	}

	public DesktopWindowManager getWindowManager()
	{
		return windowManager;
	}
	
	public void drawWindow(Window window, int mouseX, int mouseY)
	{
		GL11.glPushMatrix();
		{
			this.getCurrentTheme().draw(window, mouseX, mouseY);
		}
		GL11.glPopMatrix();
	}

	public void registerTheme(Theme theme)
	{
		this.themes.put(theme.getName(), theme);
	}

	public void addWindow(Window window)
	{
		if (!isWindowRegistered(window))
		{
			this.windows.add(window);
		}
		else if (ModUtil.isDevEnvironment())
		{
			AIRI.logger.info("Tried injecting a window with an ID that already exists: " + window.getID());
		}
	}

	public void removeWindowWithID(String id)
	{
		this.windows.remove(id);
	}
	
	public void removeWindow(Window window)
	{
		this.windows.remove(window);
	}

	public Theme getThemeForName(String name)
	{
		return this.themes.get(name);
	}

	public Theme getThemeNameForTheme(Theme theme)
	{
		return this.themes.get(theme);
	}

	public Theme getCurrentTheme()
	{
		return currentTheme;
	}

	public void setCurrentTheme(Theme currentTheme)
	{
		this.currentTheme = currentTheme;
	}

	public ArrayList<Window> getWindowsRegistry()
	{
		return this.windows;
	}

	public HashMap<String, Theme> getThemeRegistry()
	{
		return this.themes;
	}

	public ArrayList<Theme> getThemes()
	{
		return new ArrayList<Theme>(this.themes.values());
	}

	public boolean canWindowManagerOpen()
	{
		return (Minecraft.getMinecraft().currentScreen != null && !(Minecraft.getMinecraft().currentScreen instanceof DesktopWindowManager));
	}

	public void showWindowManager()
	{
		this.showWindowManager(false);
	}

	public void showWindowManager(boolean force)
	{
		if (canWindowManagerOpen() || force)
			Minecraft.getMinecraft().displayGuiScreen(getWindowManager());
	}

	public boolean isWindowRegistered(Window windowObj)
	{
		for (Window window : this.windows)
		{
			if (window == windowObj)
			{
				return true;
			}
		}

		return false;
	}
}
