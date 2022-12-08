package blue.beaming.nogrow;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.Material;
import net.minecraft.block.AbstractBlock.ContextPredicate;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class NoGrow implements ModInitializer{
    public static final ContextPredicate ALWAYS = (a,b,c) -> true;
    public static final String ID = "nogrow";
    public static final FermentedBoneMealItem FERMENTED_BONE_MEAL = new FermentedBoneMealItem(new FabricItemSettings().group(ItemGroup.MISC));
    public static final DoctoredFarmlandBlock DOCTORED_FARMLAND_BLOCK = new DoctoredFarmlandBlock(FabricBlockSettings.of(Material.SOIL).strength(0.6f).sounds(BlockSoundGroup.GRAVEL).suffocates(ALWAYS).blockVision(ALWAYS));
    public static final DefaultParticleType POISONED_HAPPY_VILLAGER = FabricParticleTypes.simple();
    @Override
    public void onInitialize(){
        registerItems();
        DispenserBlock.registerBehavior(FERMENTED_BONE_MEAL,new FallibleItemDispenserBehavior(){
            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack){
                ServerWorld world = pointer.getWorld();
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
                this.setSuccess(FermentedBoneMealItem.useOnFarmland(stack,world,blockPos,pointer.getBlockState().get(DispenserBlock.FACING).getOpposite()));
                return stack;
            }
        });
    }
    /**
     * Registers block and items.
     */
    private void registerItems(){
        Registry.register(Registry.ITEM,new Identifier(ID,"fermented_bone_meal"),FERMENTED_BONE_MEAL);
        Registry.register(Registry.BLOCK,new Identifier(ID, "doctored_farmland"),DOCTORED_FARMLAND_BLOCK);
        Registry.register(Registry.ITEM,new Identifier(ID, "doctored_farmland"),new BlockItem(DOCTORED_FARMLAND_BLOCK,new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ID, "poisoned_happy_villager"),POISONED_HAPPY_VILLAGER);
    }
}
