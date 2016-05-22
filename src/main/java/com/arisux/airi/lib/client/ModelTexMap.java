package com.arisux.airi.lib.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ModelTexMap
{
    private Texture texture;
    private ModelBaseWrapper model;

    public ModelTexMap(ModelBase model, ResourceLocation resource)
    {
        this.model = new ModelBaseWrapper(model);
        this.texture = new Texture(resource);
    }
    
    public ModelTexMap(ModelBaseWrapper model, Texture texture)
    {
        this.model = model;
        this.texture = texture;
    }

    public ModelBaseWrapper getModel()
    {
        return this.model;
    }

    public Texture getTexture()
    {
        return this.texture;
    }
    
    public void draw()
    {
        if (this.model != null && this.texture != null)
        {
            this.texture.bindTexture();
            this.model.render();
        }
    }
    
    public void draw(Object o)
    {
        if (this.model != null && this.texture != null)
        {
            this.texture.bindTexture();
            this.model.render(o);
        }
    }
}
