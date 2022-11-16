package blue.beaming.nogrow;
/*
 *  Don't Grow NOW!: Prevent crops from growing by doctoring farmland
 *  Copyright (C) 2022 BeamingBlue
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.Material;
import net.minecraft.block.AbstractBlock.ContextPredicate;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

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
            private final ItemDispenserBehavior fallback = new ItemDispenserBehavior();
            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack){
                this.setSuccess(true);
                ServerWorld world = pointer.getWorld();
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
                if(!FermentedBoneMealItem.useOnFarmland(stack,world,blockPos,pointer.getBlockState().get(DispenserBlock.FACING).getOpposite())){
                    return this.fallback.dispense(pointer, stack);
                }
                return stack;
            }
        });
        DispenserBlock.registerBehavior(Items.POTION,new ItemDispenserBehavior(){
            private final ItemDispenserBehavior fallback = new ItemDispenserBehavior();
            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack){
                if(PotionUtil.getPotion(stack) != Potions.WATER){
                    return this.fallback.dispense(pointer, stack);
                }
                ServerWorld serverWorld = pointer.getWorld();
                BlockPos blockPos = pointer.getPos();
                BlockPos blockPos2 = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
                if(serverWorld.getBlockState(blockPos2).isOf(DOCTORED_FARMLAND_BLOCK) && pointer.getBlockState().get(DispenserBlock.FACING) != Direction.UP){
                    waterEvent(serverWorld, blockPos);
                    serverWorld.setBlockState(blockPos2, Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE,7));
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                else if(serverWorld.getBlockState(blockPos2).isIn(BlockTags.CONVERTABLE_TO_MUD)){
                    waterEvent(serverWorld, blockPos);
                    serverWorld.setBlockState(blockPos2, Blocks.MUD.getDefaultState());
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                return this.fallback.dispense(pointer, stack);
            }
            /**
             * Cosmetic audio and visuals for water bottle-related events
             * @param serverWorld
             * @param blockPos
             */
            private void waterEvent(ServerWorld serverWorld, BlockPos blockPos){
                if(!serverWorld.isClient){
                    for(int i = 0; i < 5; i++){
                        serverWorld.spawnParticles(ParticleTypes.SPLASH, blockPos.getX() + serverWorld.random.nextDouble(), blockPos.getY() + 1, blockPos.getZ() + serverWorld.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                    }
                }
                serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                serverWorld.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
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
