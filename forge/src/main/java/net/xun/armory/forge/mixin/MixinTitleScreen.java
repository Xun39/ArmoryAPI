package net.xun.armory.forge.mixin;

import net.xun.armory.impl.ArmoryConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {

        ArmoryConstants.LOG.info("This line is printed by an example mod mixin from Forge!");
        ArmoryConstants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
}