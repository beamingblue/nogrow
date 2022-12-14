package blue.beaming.nogrow.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import blue.beaming.nogrow.NoGrow;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StemBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
@Mixin(StemBlock.class)
public abstract class MixinStemBlock{
    private MixinStemBlock(){throw new AssertionError("Must not instantiate MixinStemBlock!");}
    @Inject(method = "canPlantOnTop(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z",at = @At("HEAD"),cancellable = true)
    private void yesYouCanPlantOnTop(BlockState floor,BlockView j,BlockPos j2,CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(floor.isOf(Blocks.FARMLAND) || floor.isOf(NoGrow.DOCTORED_FARMLAND_BLOCK));
    }
    @Inject(method = "randomTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V",at = @At("HEAD"),cancellable = true)
    private void randomTick(BlockState state,ServerWorld world,BlockPos pos,Random random,CallbackInfo ci){
        if(world.getBlockState(pos.down()).isOf(NoGrow.DOCTORED_FARMLAND_BLOCK)) ci.cancel();
    } 
}
