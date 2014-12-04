package com.arisux.airi.api.window;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import com.arisux.airi.AIRI;
import com.arisux.airi.lib.BasicMarkupParser;
import com.arisux.airi.lib.RenderUtil;
import com.arisux.airi.lib.GuiElements.GuiCustomButton;

public abstract class Window implements IWindow
{
	protected WindowManager manager;
	private String title, id, defaultText, previousText;
	private ArrayList<String> defaultTextLines = new ArrayList<String>();
	protected ArrayList<GuiButton> buttonList = new ArrayList<GuiButton>();
	protected ArrayList<GuiCustomButton> customButtonList = new ArrayList<GuiCustomButton>();
	protected int xPos, yPos, width, height;
	private int defaultTextColor;
	protected int padding, lineSpacing;

	public Window(String id, String title, int xPos, int yPos, int width, int height)
	{
		this.manager = AIRI.windowApi().getWindowManager();
		this.id = id;
		this.title = title;
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.padding = 5;
		this.lineSpacing = 10;
		this.defaultTextColor = 0xFFFFFFFF;
		this.defaultText = "";
	}

	public void tick()
	{

	}

	@Override
	public void draw(int mouseX, int mouseY)
	{
		AIRI.windowApi().setCurrentTheme(WindowAPI.themeDefault);
		
		if (this.isOffScreen())
		{
			this.setWindowCentered(true);
		}

		if (this.defaultText.contains("/n"))
		{
			if (this.previousText != null && !this.previousText.equalsIgnoreCase(defaultText))
				this.defaultTextLines = new ArrayList<String>(Arrays.asList(defaultText.split("/n")));

			if (this.defaultTextLines.size() != 0)
			{
				for (int x = 0; x < this.defaultTextLines.size(); x++)
				{
					int tagValue = BasicMarkupParser.parseColorTagValue(this.defaultTextLines.get(x), defaultTextColor);
					String line = BasicMarkupParser.removeColorTag(this.defaultTextLines.get(x));
					
					Minecraft.getMinecraft().fontRenderer.drawSplitString(line, padding + xPos, padding + yPos + (x * lineSpacing), width - getPadding() * 2, tagValue);
				}
			}
		}
		else
		{
			Minecraft.getMinecraft().fontRenderer.drawSplitString(this.defaultText, padding + xPos, padding + yPos, width - getPadding() * 2, defaultTextColor);
		}

		this.previousText = defaultText;
	}

	@Override
	public abstract void onButtonPress(GuiButton button);

	@Override
	public abstract void keyTyped(char c, int id);

	@Override
	public void close()
	{
		this.manager.getWindowAPI().getWindowsRegistry().remove(this);
	}

	public void onMousePressed(int mouseX, int mouseY)
	{
		for (GuiCustomButton button : customButtonList)
		{
			button.mouseReleased(mouseX, mouseY);
		}
	}

	public void setPosition(int x, int y)
	{
		this.xPos = x;
		this.yPos = y;
	}

	public void setDimensions(int w, int h)
	{
		this.width = w;
		this.height = h;
	}

	public void setTitle(String t)
	{
		this.setTitle(t, false);
	}
	
	public void setTitle(String t, boolean format)
	{
		this.title = format ? I18n.format(t) : t;
	}

	public int getX()
	{
		return this.xPos;
	}

	public int getY()
	{
		return this.yPos;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public String getTitle()
	{
		return this.title;
	}

	public ArrayList<GuiButton> getButtonList()
	{
		return this.buttonList;
	}

	public int getWindowMidX()
	{
		return this.xPos + this.width / 2;
	}

	public int getWindowMidY()
	{
		return this.yPos + this.height / 2;
	}

	public int getDisplayMidX()
	{
		return RenderUtil.scaledDisplayResolution().getScaledWidth() / 2 - this.width / 2;
	}

	public int getDisplayMidY()
	{
		return RenderUtil.scaledDisplayResolution().getScaledHeight() / 2 - this.height / 2;
	}

	public void setWindowCentered()
	{
		this.setWindowCentered(false);
	}

	public void setWindowCentered(boolean force)
	{
		if (this.xPos <= -100 || force)
		{
			this.xPos = getDisplayMidX();
			this.yPos = getDisplayMidY();
		}
	}

	public boolean isTopWindow()
	{
		if (this.manager.getWindowAPI().getWindowsRegistry().size() > 0)
		{
			return this.manager.getWindowAPI().getWindowsRegistry().get(0).equals(this);
		}

		return false;
	}

	public String getDefaultText()
	{
		return this.defaultText;
	}

	public void setDefaultText(String defaultText)
	{
		this.setDefaultText(defaultText, false);
	}
	
	public void setDefaultText(String defaultText, boolean format)
	{
		this.defaultText = format ? I18n.format(defaultText) : defaultText;
	}

	public int getDefaultTextColor()
	{
		return this.defaultTextColor;
	}

	public void setDefaultTextColor(int defaultTextColor)
	{
		this.defaultTextColor = defaultTextColor;
	}

	public int getPadding()
	{
		return this.padding;
	}

	public void setPadding(int padding)
	{
		this.padding = padding;
	}

	public int getLineSpacing()
	{
		return this.lineSpacing;
	}

	public void setLineSpacing(int lineSpacing)
	{
		this.lineSpacing = lineSpacing;
	}

	public String getID()
	{
		return this.id;
	}

	public boolean isOffScreen()
	{
		return this.xPos >= RenderUtil.scaledDisplayResolution().getScaledWidth() || this.yPos >= RenderUtil.scaledDisplayResolution().getScaledHeight();
	}
}
