package com.arisux.airi.lib.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import com.arisux.airi.AIRI;
import com.arisux.airi.lib.RenderUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ModelBaseWrapper extends ModelBase
{
    /**
     * Set the width and height of this ModelBaseExtension's texture.
     * 
     * @param textureWidth - The texture width in pixels
     * @param textureHeight - The texture height in pixels
     */
    public void setTextureDimensions(int textureWidth, int textureHeight)
    {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    /**
     * Set the rotation angles of the specified ModelRenderer instance.
     */
    public void setRotation(ModelRenderer model, float rotateAngleX, float rotateAngleY, float rotateAngleZ)
    {
        model.rotateAngleX = rotateAngleX;
        model.rotateAngleY = rotateAngleY;
        model.rotateAngleZ = rotateAngleZ;
    }

    /**
     * A standard rendering argument. Does not call any super class methods.
     * @param boxTranslation - Box translation offset.
     */
    public void render(float boxTranslation)
    {
        ;
    }

    /**
     * A standard rendering argument. Does not call any super class methods.
     */
    public void render()
    {
        this.render(RenderUtil.DEFAULT_BOX_TRANSLATION);
    }

    /**
     * The standard render method from ModelBase with correct parameter mappings. Calls the superclass method.
     * 
     * @param entity - The Entity instance being rendered.
     * @param swingProgress - The arm swing progress of the Entity being rendered.
     * @param swingProgressPrev - The previous tick's arm swing progress of the Entity being rendered.
     * @param idleProgress - The idle arm swing progress of the Entity being rendered.
     * @param headYaw - The head rotation yaw of the Entity being rendered.
     * @param headPitch - The head rotation pitch of the Entity being rendered.
     * @param boxTranslation - The box translation offset. Default value is 0.0625F
     */
    @Override
    public void render(Entity entity, float swingProgress, float swingProgressPrev, float idleProgress, float headYaw, float headPitch, float boxTranslation)
    {
        super.render(entity, swingProgress, swingProgressPrev, idleProgress, headYaw, headPitch, boxTranslation);
    }

    /**
     * The standard setRotationAngles method from ModelBase with correct parameter mappings. Calls the superclass method.
     * 
     * @param swingProgress - The arm swing progress of the Entity being rendered.
     * @param swingProgressPrev - The previous tick's arm swing progress of the Entity being rendered.
     * @param idleProgress - The idle arm swing progress of the Entity being rendered.
     * @param headYaw - The head rotation yaw of the Entity being rendered.
     * @param headPitch - The head rotation pitch of the Entity being rendered.
     * @param boxTranslation - The box translation offset. Default value is 0.0625F
     * @param entity - The Entity instance being rendered.
     */
    @Override
    public void setRotationAngles(float swingProgress, float swingProgressPrev, float idleProgress, float headYaw, float headPitch, float boxTranslation, Entity entity)
    {
        super.setRotationAngles(swingProgress, swingProgressPrev, idleProgress, headYaw, headPitch, boxTranslation, entity);
    }

    /**
    * The standard setLivingAnimations method from ModelBase with correct parameter mappings. Calls the superclass method.
    * 
    * @param entityLiving - The EntityLiving instance currently being rendered.
    * @param swingProgress - The arm swing progress of the Entity being rendered.
    * @param swingProgressPrev - The previous tick's arm swing progress of the Entity being rendered.
    * @param renderPartialTicks - Render partial ticks
    */
    @Override
    public void setLivingAnimations(EntityLivingBase entityLiving, float swingProgress, float swingProgressPrev, float renderPartialTicks)
    {
        super.setLivingAnimations(entityLiving, swingProgress, swingProgressPrev, renderPartialTicks);
    }

    /**
     * Constructs a standard ModelBase instance from the specified class.
     * 
     * @param modelClass - A class extending ModelBase which will be instantaniated. 
     * @return Instance of the class specified in the modelClass parameter.
     */
    public static ModelBase createModelBase(Class<? extends ModelBase> modelClass)
    {
        try
        {
            return (modelClass.getConstructor()).newInstance(new Object[] {});
        }
        catch (Exception e)
        {
            AIRI.logger.bug("Error creating new model instance.");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Constructs a ModelBaseExtension instance from the specified class.
     * 
     * @param modelClass - A class extending ModelBaseExtension which will be instantaniated. 
     * @return Instance of the class specified in the modelClass parameter.
     */
    public static ModelBaseWrapper createExtendedModelBase(Class<? extends ModelBaseWrapper> modelClass)
    {
        try
        {
            return (modelClass.getConstructor()).newInstance(new Object[] {});
        }
        catch (Exception e)
        {
            AIRI.logger.bug("Error creating new model instance.");
            e.printStackTrace();
        }

        return null;
    }

    public float getIdleProgress(Entity entity, float renderPartialTicks)
    {
        return ((float) entity.ticksExisted + renderPartialTicks);
    }
}