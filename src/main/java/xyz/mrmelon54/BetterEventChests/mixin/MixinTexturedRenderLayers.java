package xyz.mrmelon54.BetterEventChests.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import xyz.mrmelon54.BetterEventChests.client.BetterChristmasChestsClient;
import xyz.mrmelon54.BetterEventChests.utils.ChristmasableSpriteIdentifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

import static net.minecraft.client.render.TexturedRenderLayers.*;

@Mixin(TexturedRenderLayers.class)
public abstract class MixinTexturedRenderLayers {
    @Shadow
    @Final
    public static SpriteIdentifier CHRISTMAS_LEFT;
    @Shadow
    @Final
    public static SpriteIdentifier CHRISTMAS_RIGHT;
    @Shadow
    @Final
    public static SpriteIdentifier NORMAL_LEFT;
    @Shadow
    @Final
    public static SpriteIdentifier NORMAL_RIGHT;
    @Shadow
    @Final
    public static SpriteIdentifier ENDER;
    @Shadow
    @Final
    public static SpriteIdentifier TRAPPED_LEFT;
    @Shadow
    @Final
    public static SpriteIdentifier TRAPPED_RIGHT;
    @Shadow
    @Final
    public static SpriteIdentifier TRAPPED;
    private static final SpriteIdentifier ENDER_CHRISTMAS;
    private static final SpriteIdentifier TRAPPED_CHRISTMAS;
    private static final SpriteIdentifier TRAPPED_CHRISTMAS_LEFT;
    private static final SpriteIdentifier TRAPPED_CHRISTMAS_RIGHT;

    static {
        ENDER_CHRISTMAS = getChristmasChestTextureId("ender_christmas");
        TRAPPED_CHRISTMAS = getChristmasChestTextureId("trapped_christmas");
        TRAPPED_CHRISTMAS_LEFT = getChristmasChestTextureId("trapped_christmas_left");
        TRAPPED_CHRISTMAS_RIGHT = getChristmasChestTextureId("trapped_christmas_right");
    }

    private static SpriteIdentifier getChristmasChestTextureId(String variant) {
        return new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier("better-christmas-chests:entity/chest/" + variant));
    }

    @Inject(at = @At("RETURN"), method = "addDefaultTextures")
    private static void addDefaultTextures(Consumer<SpriteIdentifier> adder, CallbackInfo info) {
        adder.accept(ENDER_CHRISTMAS);
        adder.accept(TRAPPED_CHRISTMAS);
        adder.accept(TRAPPED_CHRISTMAS_LEFT);
        adder.accept(TRAPPED_CHRISTMAS_RIGHT);
    }

    @Inject(at = @At("HEAD"), method = "getChestTexture(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/block/enums/ChestType;Z)Lnet/minecraft/client/util/SpriteIdentifier;", cancellable = true)
    private static void getChestTexture(BlockEntity blockEntity, ChestType type, boolean christmas, CallbackInfoReturnable<SpriteIdentifier> info) {
        ChristmasableSpriteIdentifier christmasableSpriteIdentifier = null;
        if (blockEntity instanceof TrappedChestBlockEntity) christmasableSpriteIdentifier = switch (type) {
            case LEFT ->
                    new ChristmasableSpriteIdentifier(TRAPPED_LEFT, TRAPPED_CHRISTMAS_LEFT, BetterChristmasChestsClient.getInstance().enableChristmasTrappedChest());
            case RIGHT ->
                    new ChristmasableSpriteIdentifier(TRAPPED_RIGHT, TRAPPED_CHRISTMAS_RIGHT, BetterChristmasChestsClient.getInstance().enableChristmasTrappedChest());
            default ->
                    new ChristmasableSpriteIdentifier(TRAPPED, TRAPPED_CHRISTMAS, BetterChristmasChestsClient.getInstance().enableChristmasTrappedChest());
        };
        else if (blockEntity instanceof EnderChestBlockEntity)
            christmasableSpriteIdentifier = new ChristmasableSpriteIdentifier(ENDER, ENDER_CHRISTMAS, BetterChristmasChestsClient.getInstance().enableChristmasEnderChest());
        else if (blockEntity instanceof ChestBlockEntity && BetterChristmasChestsClient.getInstance().enableChristmasChest())
            christmasableSpriteIdentifier = switch (type) {
                case LEFT ->
                        new ChristmasableSpriteIdentifier(NORMAL_LEFT, CHRISTMAS_LEFT, BetterChristmasChestsClient.getInstance().enableChristmasChest());
                case RIGHT ->
                        new ChristmasableSpriteIdentifier(NORMAL_RIGHT, CHRISTMAS_RIGHT, BetterChristmasChestsClient.getInstance().enableChristmasChest());
                default ->
                        new ChristmasableSpriteIdentifier(NORMAL, CHRISTMAS, BetterChristmasChestsClient.getInstance().enableChristmasChest());
            };
        if (christmasableSpriteIdentifier != null)
            info.setReturnValue(BetterChristmasChestsClient.getInstance().isChristmas() && christmasableSpriteIdentifier.isFeatureEnabled() ? christmasableSpriteIdentifier.getChristmas() : christmasableSpriteIdentifier.getNormal());
    }
}
