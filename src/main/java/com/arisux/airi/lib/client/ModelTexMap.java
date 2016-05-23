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
    
    public void bindTexture()
    {
        this.getTexture().bind();
    }
    
    public void drawStandaloneModel()
    {
        this.drawStandaloneModel(null);
    }
    
    public void drawStandaloneModel(Object o)
    {
        this.getModel().render(o);
    }
    
    public void draw()
    {
        this.draw(null);
    }
    
    public void draw(Object o)
    {
        if (this.model != null && this.texture != null)
        {
            this.bindTexture();
            this.drawStandaloneModel(o);
        }
    }
}
