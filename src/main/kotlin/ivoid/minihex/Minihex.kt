package ivoid.minihex

import ivoid.minihex.features.personalmodifiers.PersonalModifierState
import ivoid.minihex.inits.MinihexActions
import ivoid.minihex.inits.MinihexCommands
import ivoid.minihex.inits.MinihexComponents
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Minihex : ModInitializer {
    const val MOD_ID = "minihex"
    val logger: Logger = LoggerFactory.getLogger("minihex")

    fun id(name: String): Identifier = Identifier(MOD_ID, name)

    override fun onInitialize() {
        MinihexActions.init()
        MinihexCommands.init()

        ServerPlayConnectionEvents.JOIN.register { handler, sender, server ->
            // Reset state on relog
            MinihexComponents.PERSONAL_MODIFIERS.get(handler.player).state = PersonalModifierState()
        }

        logger.info("Making modifications to your minigames!")
    }
}
