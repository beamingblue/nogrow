package blue.beaming.nogrow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.client.particle.SuspendParticle;

public class NoGrowClient implements ClientModInitializer{
    @Override
    public void onInitializeClient(){
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> registry.register(new Identifier(NoGrow.ID, "particle/poisoned_happy_villager"))));
        ParticleFactoryRegistry.getInstance().register(NoGrow.POISONED_HAPPY_VILLAGER,SuspendParticle.HappyVillagerFactory::new);
    }
}