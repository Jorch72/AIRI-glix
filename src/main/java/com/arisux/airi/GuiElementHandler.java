package com.arisux.airi;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Mouse;

import com.arisux.airi.lib.GuiElements.GuiCustomButton;
import com.arisux.airi.lib.GuiElements.GuiCustomSlider;
import com.arisux.airi.lib.GuiElements.GuiCustomTextbox;
import com.arisux.airi.lib.*;
import com.arisux.airi.lib.interfaces.IInitializablePre;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;

public class GuiElementHandler implements IInitializablePre
{
	private ArrayList<GuiCustomButton> customButtons = new ArrayList<GuiCustomButton>();
	private ArrayList<GuiCustomTextbox> customTextboxes = new ArrayList<GuiCustomTextbox>();
	private ArrayList<GuiCustomSlider> customSliders = new ArrayList<GuiCustomSlider>();

	public static GuiElementHandler instance()
	{
		return AIRI.instance().guiElementHandler;
	}
	
	@Override
	public void preInitialize(FMLPreInitializationEvent event)
	{
		;
	}
	
	public void tick()
	{
		Vector2d mousePosition = RenderUtil.scaledMousePosition();

		for (GuiCustomButton button : customButtons)
		{
			handleButtonInput(null, button, mousePosition);
		}

		for (GuiCustomTextbox textbox : customTextboxes)
		{
			if (Mouse.isButtonDown(0))
			{
				if (textbox.isMouseInside())
				{
					textbox.setFocused(true);
				}
				else
				{
					textbox.setFocused(false);
				}
			}
		}

		for (GuiCustomSlider slider : customSliders)
		{
			handleButtonInput(null, slider, mousePosition);

			if (!Mouse.getEventButtonState() && Mouse.getEventButton() != -1)
			{
				slider.dragging = false;
			}
		}
	}
	
	private static void handleButtonInput(MouseInputEvent event, GuiCustomButton button, Vector2d mousePosition)
	{
		if (button.isMouseOver())
		{
			if (Mouse.getEventButton() == 0)
			{
				if (Mouse.getEventButtonState())
				{
					button.mousePressed(Minecraft.getMinecraft(), (int) mousePosition.x, (int) mousePosition.y);
				}
				else if (Mouse.isButtonDown(0))
				{
					button.mouseReleased((int) mousePosition.x, (int) mousePosition.y);
				}
			}

			if (Mouse.isButtonDown(0))
			{
				button.mouseDragged(Minecraft.getMinecraft(), (int) mousePosition.x, (int) mousePosition.y);
			}
		}
	}
	
	public static void addButton(GuiCustomButton button)
	{
		instance().customButtons.add(button);
	}

	public static void addTextbox(GuiCustomTextbox textbox)
	{
		instance().customTextboxes.add(textbox);
	}

	public static void addSlider(GuiCustomSlider slider)
	{
		instance().customSliders.add(slider);
	}

	public static void removeButton(GuiCustomButton button)
	{
		instance().customButtons.remove(button);
	}

	public static void removeTextbox(GuiCustomTextbox textbox)
	{
		instance().customTextboxes.remove(textbox);
	}

	public static void removeSlider(GuiCustomSlider slider)
	{
		instance().customSliders.remove(slider);
	}
}
