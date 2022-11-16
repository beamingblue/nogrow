package blue.beaming.nogrow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FermentedBoneMealItem extends Item{
    public FermentedBoneMealItem(Settings settings){
        super(settings);
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context){
        World world = context.getWorld();
        BlockPos position = context.getBlockPos();
        BlockState blockState = world.getBlockState(position);
        if(useOnFarmland(getDefaultStack(), world, position,context.getSide())){
            return doctor(world, position, blockState);
        }
        return ActionResult.PASS;
    }
    public static ActionResult doctor(World world, BlockPos position, BlockState blockState) {
        world.setBlockState(position,NoGrow.DOCTORED_FARMLAND_BLOCK.getDefaultState().with(DoctoredFarmlandBlock.MOISTURE,blockState.get(FarmlandBlock.MOISTURE)),Block.NOTIFY_ALL);
        if(!world.isClient){
            world.playSound(null,position,SoundEvents.ITEM_BONE_MEAL_USE,SoundCategory.PLAYERS, 1.0f, 1.0f);if (!world.isClient){
                ServerWorld serverWorld = (ServerWorld) world;
                for(int j = 0; j < 5; j++){
                    serverWorld.spawnParticles(NoGrow.POISONED_HAPPY_VILLAGER,position.getX() + world.random.nextDouble(), position.getY() + 1, position.getZ() + world.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                }
            }
        }
        return ActionResult.success(world.isClient);
    }
    public static boolean useOnFarmland(ItemStack stack, World world, BlockPos blockPos,Direction direction){
        BlockState blockState = world.getBlockState(blockPos);
        if(blockState.getBlock() instanceof FarmlandBlock && direction != Direction.DOWN){
            if(world instanceof ServerWorld){
                doctor(world,blockPos,blockState);
                stack.decrement(1);
            }
            return true;
        }
        return false;
    }
}
