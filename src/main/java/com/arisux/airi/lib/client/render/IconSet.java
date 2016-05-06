package com.arisux.airi.lib.client.render;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class IconSet
{
    public IIcon defaultIcon;
    public IIcon top;
    public IIcon bottom;
    public IIcon front;
    public IIcon back;
    public IIcon left;
    public IIcon right;
    public IIcon flowing;
    public IIcon still;
    public String resourceDefault;
    public String resourceTop;
    public String resourceBottom;
    public String resourceFront;
    public String resourceBack;
    public String resourceLeft;
    public String resourceRight;
    public String resourceFlowing;
    public String resourceStill;

    public IconSet(String defaultIcon)
    {
        this(defaultIcon, null, null, null, null, null, null);
    }

    public IconSet(String icon, String top, String bottom, String front, String back, String left, String right)
    {
        this.resourceDefault = icon;
        this.resourceTop = top;
        this.resourceBottom = bottom;
        this.resourceFront = front;
        this.resourceBack = back;
        this.resourceLeft = left;
        this.resourceRight = right;
    }

    public IconSet(IIcon icon, IIcon top, IIcon bottom, IIcon front, IIcon back, IIcon left, IIcon right)
    {
        this.defaultIcon = icon;
        this.top = top;
        this.bottom = bottom;
        this.front = front;
        this.back = back;
        this.left = left;
        this.right = right;
    }

    public IconSet(String icon, String flowing, String still)
    {
        this.resourceDefault = icon;
        this.resourceFlowing = flowing;
        this.resourceStill = still;
    }

    public IconSet(IIcon icon, IIcon flowing, IIcon still)
    {
        this.defaultIcon = icon;
        this.flowing = flowing;
        this.still = still;
    }

    public void registerIcons(IIconRegister register)
    {
        this.defaultIcon = (register.registerIcon(resourceDefault));
        this.top = (register.registerIcon(resourceTop == null ? resourceDefault : resourceTop));
        this.bottom = (register.registerIcon(resourceBottom == null ? resourceDefault : resourceBottom));
        this.front = (register.registerIcon(resourceFront == null ? resourceDefault : resourceFront));
        this.back = (register.registerIcon(resourceBack == null ? resourceDefault : resourceBack));
        this.left = (register.registerIcon(resourceLeft == null ? resourceDefault : resourceLeft));
        this.right = (register.registerIcon(resourceRight == null ? resourceDefault : resourceRight));
        this.flowing = (register.registerIcon(resourceFlowing == null ? resourceDefault : resourceFlowing));
        this.still = (register.registerIcon(resourceStill == null ? resourceDefault : resourceStill));
    }
}
