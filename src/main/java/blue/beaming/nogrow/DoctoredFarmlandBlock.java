package blue.beaming.nogrow;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class DoctoredFarmlandBlock extends Block{
    public static final IntProperty MOISTURE = Properties.MOISTURE;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0,0.0,0.0, 16.0,15.0,16.0);
    public static final int MAX_MOISTURE = 7;
    public DoctoredFarmlandBlock(AbstractBlock.Settings settings){
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(MOISTURE, 0));
    }
    @Override
    protected void appendProperties(Builder<Block,BlockState> builder){
        builder.add(MOISTURE);
    }
    @Deprecated @Override
    public boolean canPathfindThrough(BlockState state,BlockView world,BlockPos pos,NavigationType type){
        return false;
    }
    @Deprecated @Override
    public VoxelShape getOutlineShape(BlockState state,BlockView world,BlockPos pos,ShapeContext context){
        return SHAPE;
    }
    @Deprecated @Override
    public boolean canPlaceAt(BlockState state,WorldView world,BlockPos pos){
        BlockState blockState = world.getBlockState(pos.up());
        return !blockState.getMaterial().isSolid() || blockState.getBlock() instanceof FenceGateBlock || blockState.getBlock() instanceof PistonExtensionBlock;
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx){
        if(!this.getDefaultState().canPlaceAt(ctx.getWorld(),ctx.getBlockPos())){
            return Blocks.DIRT.getDefaultState();
        }
        return super.getPlacementState(ctx);
    }
    @Deprecated @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos,Random random){
        if(!state.canPlaceAt(world, pos)){
            DoctoredFarmlandBlock.setToDirt(state, world, pos);
        }
    }
    public static void setToDirt(BlockState state, ServerWorld world, BlockPos pos){
        world.setBlockState(pos,Block.pushEntitiesUpBeforeBlockChange(state,Blocks.DIRT.getDefaultState(),world,pos));
    }
    @Deprecated @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !state.canPlaceAt(world, pos)) {
            world.createAndScheduleBlockTick(pos, this, 1);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
