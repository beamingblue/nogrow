package blue.beaming.nogrow.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import blue.beaming.nogrow.DoctoredFarmlandBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.event.GameEvent;

@Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$20")
public abstract class MixinDispenserBehavior{
    private MixinDispenserBehavior(){throw new AssertionError("Must not instantiate MixinDispenserBehavior!");}
    @Inject(method = "dispenseSilently(Lnet/minecraft/util/math/BlockPointer;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;",at = @At(value = "INVOKE",target = "Lnet/minecraft/util/math/BlockPointer;getWorld()Lnet/minecraft/server/world/ServerWorld;"),cancellable = true)
    private void doctoredFarmlandCountsToo(BlockPointer pointer,ItemStack stack,CallbackInfoReturnable<ItemStack> cir){
        ServerWorld serverWorld = pointer.getWorld();
        BlockPos blockPos = pointer.getPos();
        Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
        BlockPos blockPosOffset = blockPos.offset(direction);
        if(serverWorld.getBlockState(blockPosOffset).getBlock() instanceof DoctoredFarmlandBlock && direction != Direction.UP && direction != Direction.DOWN){
            if(!serverWorld.isClient){
                for(int i = 0;i < 5;i++){
                    serverWorld.spawnParticles(ParticleTypes.SPLASH,blockPos.getX() + serverWorld.random.nextDouble(),blockPos.getY() + 1D,blockPos.getZ() + serverWorld.random.nextDouble(),1,0.0,0.0,0.0,1.0);
                }
            }
            serverWorld.playSound(null,blockPos,SoundEvents.ITEM_BOTTLE_EMPTY,SoundCategory.BLOCKS,1.0f,1.0f);
            serverWorld.emitGameEvent(null,GameEvent.FLUID_PLACE,blockPos);
            serverWorld.setBlockState(blockPosOffset,Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE,7));
            cir.setReturnValue(new ItemStack(Items.GLASS_BOTTLE));
        }
    }
}