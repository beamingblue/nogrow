package blue.beaming.nogrow.mixin;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import blue.beaming.nogrow.NoGrow;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
@Mixin(AttachedStemBlock.class)
public abstract class MixinAttachedStemBlock{
    @Inject(method = "canPlantOnTop(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    public void yesYouCanPlantOnTop(BlockState floor,BlockView j,BlockPos j2,CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(floor.isOf(Blocks.FARMLAND) || floor.isOf(NoGrow.DOCTORED_FARMLAND_BLOCK));
    }
}
