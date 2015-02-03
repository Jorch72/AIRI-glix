package com.arisux.airi.api.window.themes;

import com.arisux.airi.api.window.Window;

public interface ITheme
{
    public void drawWindow(Window window, int mouseX, int mouseY);

    public void drawBackground(Window w, int mouseX, int mouseY);

    public void drawTitleBar(Window w, int mouseX, int mouseY);

    public void drawCloseButton(Window w, int mouseX, int mouseY);
    
    public int getTextColor();

    public int getForegroundColor();
    
    public int getBackgroundColor();
    
    public int getButtonColor();
}
