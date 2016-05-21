package com.arisux.airi.lib.client;

import com.arisux.airi.lib.RenderUtil;

import net.minecraft.util.ResourceLocation;

public class Texture extends ResourceLocation
{
    public Texture(ResourceLocation resource)
    {
        this(resource.getResourceDomain(), resource.getResourcePath());
    }
    
    public Texture(String location)
    {
        super(location);
    }
    
    public Texture(String domain, String location)
    {
        super(domain, location);
    }
    
    public void bindTexture()
    {
        RenderUtil.bindTexture(this);
    }
}