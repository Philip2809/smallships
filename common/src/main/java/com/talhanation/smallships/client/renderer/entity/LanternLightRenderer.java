package com.talhanation.smallships.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.talhanation.smallships.client.renderer.entity.state.CannonBallRenderState;
import com.talhanation.smallships.client.renderer.entity.state.LanternLightRenderState;
import com.talhanation.smallships.world.entity.LanternLightEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class LanternLightRenderer extends EntityRenderer<LanternLightEntity, LanternLightRenderState> {
    public LanternLightRenderer(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(LanternLightRenderState state, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
 /*       var mc = Minecraft.getInstance();
        var blockRenderer = mc.getBlockRenderer();
        //var lantern = Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true);
        var lantern = Blocks.LANTERN.defaultBlockState();

        poseStack.pushPose();
        poseStack.translate(0, 0, 0);
        //SHIELD_POSITIONS.add(new Shieldable.ShieldPosition(2.1, 0.8, -1.0, true));

        blockRenderer.renderSingleBlock(
                lantern,
                poseStack,
                multiBufferSource,
                15 << 4,
                OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();

        super.render(state, poseStack, multiBufferSource, packedLight);*/
    }


    @Override
    public @NotNull LanternLightRenderState createRenderState() {
        return new LanternLightRenderState();
    }
}
