package ivoid.minihex

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object Minihex : ModInitializer {
    const val MOD_ID = "minihex";
    val logger = LoggerFactory.getLogger("minihex")

    fun id(name: String): Identifier = Identifier(MOD_ID, name)

	override fun onInitialize() {
        // TODO
		logger.info("Minihex initialized")
	}
}
