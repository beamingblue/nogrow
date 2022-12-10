package blue.beaming.nogrow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SuspendParticle;

public class NoGrowClient implements ClientModInitializer{
    @Override
    public void onInitializeClient(){
        ParticleFactoryRegistry.getInstance().register(NoGrow.POISONED_HAPPY_VILLAGER,SuspendParticle.HappyVillagerFactory::new);
    }
}