package ivoid.minihex.inits

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy
import ivoid.minihex.Minihex
import ivoid.minihex.features.personalmodifiers.api.PersonalModifierComponent
import ivoid.minihex.features.personalmodifiers.PersonalModifierComponentImpl

object MinihexComponents : EntityComponentInitializer {
    val PERSONAL_MODIFIERS: ComponentKey<PersonalModifierComponent> = ComponentRegistry.getOrCreate(
        Minihex.id("personal_modifiers"),
        PersonalModifierComponent::class.java
    )

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerForPlayers(
            PERSONAL_MODIFIERS,
            ::PersonalModifierComponentImpl,
            RespawnCopyStrategy { from: PersonalModifierComponent, to: PersonalModifierComponent, lossless: Boolean, keepInventory: Boolean, sameCharacter: Boolean ->
                // Same as RespawnCopyStrategy.LOSSLESS_ONLY, but sync after copy

                if (lossless) {
                    RespawnCopyStrategy.copy(from, to)
                }

                if (to is PersonalModifierComponentImpl) {
                    to.sync(true)
                }
            }
        )
    }
}
