package blue.beaming.nogrow.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import blue.beaming.nogrow.DoctoredFarmlandBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;

@Mixin(CropBlock.class)
public abstract class MixinCropBlock{
    private MixinCropBlock(){throw new AssertionError("Must not instantiate MixinCropBlock!");}
    @Inject(method = "canPlantOnTop(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z",at = @At("HEAD"),cancellable = true)
    private void yesYouCanPlantOnTop(BlockState floor,BlockView j,BlockPos j2,CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(floor.getBlock() instanceof FarmlandBlock);
    }
    @Inject(method = "randomTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V",at = @At("HEAD"),cancellable = true)
    private void randomTick(BlockState state,ServerWorld world,BlockPos pos,Random random,CallbackInfo ci){
        if(world.getBlockState(pos.down()).getBlock() instanceof DoctoredFarmlandBlock) ci.cancel();
    } 
}