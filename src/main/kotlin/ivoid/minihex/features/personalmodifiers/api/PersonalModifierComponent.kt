package ivoid.minihex.features.personalmodifiers.api

import dev.onyxstudios.cca.api.v3.component.Component
import dev.onyxstudios.cca.api.v3.component.CopyableComponent
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent
import ivoid.minihex.features.personalmodifiers.PersonalModifierState

interface PersonalModifierComponent : Component, AutoSyncedComponent, ServerTickingComponent, CopyableComponent<PersonalModifierComponent> {
    var state: PersonalModifierState
    var lastSyncState: PersonalModifierState

    fun sync(full: Boolean = false)
}
