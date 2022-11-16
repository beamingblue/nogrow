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