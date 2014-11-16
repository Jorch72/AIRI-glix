package com.arisux.airi.engine;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.arisux.airi.lib.util.interfaces.IActionPerformed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiTypeLib
{
	@SideOnly(Side.CLIENT)
	public static class GuiCustomButton extends GuiButton
	{
		public class ActionPerformed implements IActionPerformed
		{
			@Override
			public void actionPerformed(GuiCustomButton button)
			{
				;
			}
		}

		private IActionPerformed action;
		public int baseColor = 0xEE000000;
		public int overlayColorNormal = 0x44000000;
		public int overlayColorHover = 0x00000000;
		public int overlayColorPressed = 0x66000000;
		public String tooltip = "";

		public GuiCustomButton(ArrayList<GuiCustomButton> buttonList, int id, int xPosition, int yPosition, int width, int height, String displayString, IActionPerformed action)
		{
			this(id, xPosition, yPosition, width, height, displayString, action);
			buttonList.add(this);
		}

		public GuiCustomButton(int id, int xPosition, int yPosition, int width, int height, String displayString, IActionPerformed action)
		{
			super(id, xPosition, yPosition, width, height, displayString);
			this.width = 200;
			this.height = 20;
			this.enabled = true;
			this.visible = true;
			this.id = id;
			this.xPosition = xPosition;
			this.yPosition = yPosition;
			this.width = width;
			this.height = height;
			this.displayString = displayString;
			this.action = action;
		}

		public void drawButton()
		{
			this.drawButton(Minecraft.getMinecraft(), (int) RenderEngine.scaledMousePosition().x, (int) RenderEngine.scaledMousePosition().y);
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY)
		{
			if (this.visible)
			{
				FontRenderer fontrenderer = mc.fontRenderer;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
				int k = this.getHoverState(this.field_146123_n);

				int overlayColor = k == 2 ? (Mouse.isButtonDown(0) ? overlayColorPressed : overlayColorHover) : overlayColorNormal;

				RenderEngine.drawRect(this.xPosition, this.yPosition, this.width, this.height, baseColor);
				RenderEngine.drawRect(this.xPosition, this.yPosition, this.width, this.height, overlayColor);

				this.mouseDragged(mc, mouseX, mouseY);
				this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xFFFFFFFF);
			
				if (this.isMouseOver() && !tooltip.equalsIgnoreCase(""))
				{
					RenderEngine.drawToolTip((int) RenderEngine.scaledMousePosition().x + 10, (int) RenderEngine.scaledMousePosition().y, tooltip);
				}
			}
		}

		@Override
		public void mouseReleased(int mouseX, int mouseY)
		{
			super.mouseReleased(mouseX, mouseY);

			if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height)
			{
				this.action.actionPerformed(this);
			}
		}

		@Override
		public void mouseDragged(Minecraft mc, int mouseX, int mouseY)
		{
			super.mouseDragged(mc, mouseX, mouseY);
		}

		public void handleInput()
		{
			if (this.isMouseOver())
			{
				GuiTypeLib.handleMouseInputForCustomButton(this);
			}
		}

		public boolean isMouseOver()
		{
			return this.field_146123_n;
		}

		public IActionPerformed getAction()
		{
			return action;
		}

		public GuiCustomButton setAction(IActionPerformed action)
		{
			this.action = action;
			return this;
		}
	}

	public static class GuiCustomTextbox extends GuiTextField
	{
		public GuiCustomScreen parentScreen;

		public GuiCustomTextbox(GuiCustomScreen parentScreen, int x, int y, int width, int height)
		{
			this(x, y, width, height);
			this.parentScreen = parentScreen;
			this.parentScreen.customTextfieldList.add(this);
		}

		public GuiCustomTextbox(int x, int y, int width, int height)
		{
			super(Minecraft.getMinecraft().fontRenderer, x, y, width, height);
			this.xPosition = x;
			this.yPosition = y;
			this.width = width;
			this.height = height;
		}

		@Override
		public void drawTextBox()
		{
			super.drawTextBox();
		}

		public boolean isMouseInside()
		{
			int mouseX = (int) RenderEngine.scaledMousePosition().x;
			int mouseY = (int) RenderEngine.scaledMousePosition().y;
			return mouseX >= (xPosition) && mouseX <= (xPosition + width) && mouseY >= (yPosition) && mouseY <= (yPosition + height);
		}

		public void handleInput()
		{
			if (Mouse.isButtonDown(0))
			{
				if (isMouseInside())
				{
					this.setFocused(true);
				} else
				{
					this.setFocused(false);
				}
			}
		}
	}

	public static class GuiCustomSlider extends GuiCustomButton
	{
		public String label;
		public float sliderValue = 1.0F;
		public float sliderMaxValue = 1.0F;
		public boolean dragging = false;
		public int sliderVariable;
		public int sliderButtonColor = 0xFF00AAFF;

		public GuiCustomSlider(int id, int x, int y, String label, float startingValue, float maxValue)
		{
			super(id, x, y, 150, 20, label, null);
			this.sliderValue = startingValue;
			this.sliderMaxValue = maxValue;
			this.label = label;
		}

		@Override
		public int getHoverState(boolean par1)
		{
			return 0;
		}

		@Override
		public void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
		{
			super.mouseDragged(Minecraft.getMinecraft(), par2, par3);
			
			if (this.visible)
			{
				if (this.dragging)
				{
					this.displayString = label + ": " + (int) (sliderValue * sliderMaxValue);
					this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);

					if (this.sliderValue < 0.0F)
					{
						this.sliderValue = 0.0F;
					}

					if (this.sliderValue > 1.0F)
					{
						this.sliderValue = 1.0F;
					}

					this.sliderVariable = (int) (this.sliderValue * this.sliderMaxValue);
				}
			}
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY)
		{
			super.drawButton(mc, mouseX, mouseY);
			
			if (this.visible)
			{
				RenderEngine.drawRectWithOutline(this.xPosition - 1, this.yPosition - 1, this.width + 2, this.height + 2, 1, 0x00000000, 0xAAFFFFFF);
				RenderEngine.drawRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 8, this.height, sliderButtonColor);
			}
		}

		@Override
		public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
		{
			if (super.mousePressed(par1Minecraft, par2, par3))
			{
				this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);

				if (this.sliderValue < 0.0F)
				{
					this.sliderValue = 0.0F;
				}

				if (this.sliderValue > 1.0F)
				{
					this.sliderValue = 1.0F;
				}

				this.dragging = true;
				return true;
			} else
			{
				return false;
			}
		}

		@Override
		public void mouseReleased(int par1, int par2)
		{
			this.dragging = false;
		}

		@Override
		public void handleInput()
		{
			super.handleInput();
			
			if (!Mouse.getEventButtonState() && Mouse.getEventButton() != -1)
			{
				this.dragging = false;
			}
		}
	}

	public static class GuiCustomScreen extends GuiScreen
	{
		public ArrayList<GuiCustomButton> customButtonList = new ArrayList<GuiCustomButton>();
		public ArrayList<GuiCustomTextbox> customTextfieldList = new ArrayList<GuiCustomTextbox>();

		@Override
		protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);

			for (GuiCustomButton button : customButtonList)
			{
				button.mouseReleased(mouseX, mouseY);
			}
		}
		
		@Override
		public void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
		{
			super.drawGradientRect(par1, par2, par3, par4, par5, par6);
		}
		
		public void setZLevel(float f)
		{
			this.zLevel = f;
		}

		public float getZLevel()
		{
			return this.zLevel;
		}

		public void incZLevel(float f)
		{
			this.zLevel += f;
		}
	}

	public static int handleMouseInputForCustomButton(GuiCustomButton button)
	{
		if (Mouse.isCreated())
		{
			Vector2d mousePosition = RenderEngine.scaledMousePosition();

			while (Mouse.next())
			{
				if (Mouse.getEventButtonState())
				{
					button.mousePressed(Minecraft.getMinecraft(), (int) mousePosition.x, (int) mousePosition.y);
				} else if (Mouse.getEventButton() != -1)
				{
					button.mouseReleased((int) mousePosition.x, (int) mousePosition.y);
				}
				
				if (Mouse.isButtonDown(0))
				{
					button.mouseDragged(Minecraft.getMinecraft(), (int) mousePosition.x, (int) mousePosition.y);
				}
			}
		}

		return Mouse.getEventButton();
	}

	// TODO: Broken - Manually handle input until fixed
	public static void handleKeyboardInputForTextbox(GuiCustomTextbox textbox)
	{
		if (Keyboard.getEventKeyState())
		{
			int eventKey = Keyboard.getEventKey();
			textbox.textboxKeyTyped(Keyboard.getEventCharacter(), eventKey);

			if (eventKey == 1)
			{
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen) null);
				Minecraft.getMinecraft().setIngameFocus();
			}
		}
	}
}
