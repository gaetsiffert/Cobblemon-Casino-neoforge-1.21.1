package net.andrespr.casinorocket.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererAccessor {
    @Accessor("features")
    List<RenderLayer<?, ?>> casinorocket$getFeatures();
}

