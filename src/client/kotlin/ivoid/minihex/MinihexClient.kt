package ivoid.minihex

import ivoid.minihex.features.personalmodifiers.PersonalModifierState
import ivoid.minihex.inits.MinihexComponents
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import java.util.Optional

object MinihexClient : ClientModInitializer {
    override fun onInitializeClient() {
    }

    fun getClientState(): Optional<PersonalModifierState> {
        return Optional.ofNullable(MinecraftClient.getInstance().player).map {
            MinihexComponents.PERSONAL_MODIFIERS.get(it).state
        }
    }
}

