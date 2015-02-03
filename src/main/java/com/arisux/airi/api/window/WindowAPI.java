package com.arisux.airi.api.window;

import java.util.*;

import net.minecraft.client.Minecraft;

import com.arisux.airi.AIRI;
import com.arisux.airi.api.window.themes.*;
import com.arisux.airi.lib.ModUtil;

public class WindowAPI
{
	private WindowManager windowManager;
	private Theme currentTheme;
	private ArrayList<Window> windows = new ArrayList<Window>();
	private HashMap<String, Theme> themes = new HashMap<String, Theme>();

	public static final Theme themeDefault = new ThemeDefault("default");
	public static final Theme themeModern = new ThemeModern("modern");
	public static final Theme themeMinecraft = new ThemeMinecraft("minecraft");

	public WindowAPI()
	{
		this.windowManager = new WindowManager(this, null);
		this.registerTheme(themeDefault);
		this.registerTheme(themeModern);
		this.registerTheme(themeMinecraft);
		this.currentTheme = getThemeForName("modern");
	}

	public void onTick()
	{
		if (getWindowsRegistry().size() <= 0 && Minecraft.getMinecraft().currentScreen instanceof WindowManager)
		{
			Minecraft.getMinecraft().displayGuiScreen(this.getWindowManager().parentScreen);
		}

		if (this.getWindowManager().parentScreen != Minecraft.getMinecraft().currentScreen && !(Minecraft.getMinecraft().currentScreen instanceof WindowManager))
		{
			this.getWindowManager().parentScreen = Minecraft.getMinecraft().currentScreen;
		}
	}

	public WindowManager getWindowManager()
	{
		return windowManager;
	}
	
	public void drawWindow(Window window, int mouseX, int mouseY)
	{
		this.getCurrentTheme().drawWindow(window, mouseX, mouseY);
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

	/**
	 * ReadOnly method for getting an array of values stored in the Theme Registry
	 **/
	public ArrayList<Theme> getThemes()
	{
		return new ArrayList<Theme>(this.themes.values());
	}

	/** Read/Write method for getting the Window Registry hashmap **/
	public ArrayList<Window> getWindowsRegistry()
	{
		return this.windows;
	}

	/** Read/Write method for getting the Theme Registry hashmap **/
	public HashMap<String, Theme> getThemeRegistry()
	{
		return this.themes;
	}

	public boolean canWindowManagerOpen()
	{
		return (Minecraft.getMinecraft().currentScreen != null && !(Minecraft.getMinecraft().currentScreen instanceof WindowManager));
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
		Iterator<Window> iterator = this.windows.iterator();
		Window window;

		do
		{
			if (!iterator.hasNext())
			{
				return false;
			}

			window = iterator.next();
		}
		while (!(window == windowObj));

		return true;
	}
}
