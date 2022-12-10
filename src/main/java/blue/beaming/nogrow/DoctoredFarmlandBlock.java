package blue.beaming.nogrow;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DoctoredFarmlandBlock extends FarmlandBlock{
    public DoctoredFarmlandBlock(Settings settings){
        super(settings);
    }
    /**
     * @deprecated
     */
    @Deprecated @Override
    public void onLandedUpon(World world,BlockState state,BlockPos pos,Entity entity,float fallDistance){
        // Do nothing
    }
    /**
     * @deprecated
     */
    @Deprecated @Override
    public void randomTick(BlockState state,ServerWorld world,BlockPos pos,Random random) {
        // Do nothing
    }
    /**
     * @deprecated
     */
    @Deprecated @Override
    public boolean hasRandomTicks(BlockState state) {
        return false;
    }
}
