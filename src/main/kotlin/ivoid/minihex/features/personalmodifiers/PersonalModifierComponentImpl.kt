package ivoid.minihex.features.personalmodifiers

import ivoid.minihex.features.personalmodifiers.api.PersonalModifierComponent
import ivoid.minihex.inits.MinihexComponents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity

class PersonalModifierComponentImpl(val provider: PlayerEntity) : PersonalModifierComponent {
    override var state: PersonalModifierState = PersonalModifierState()
    override var lastSyncState: PersonalModifierState = PersonalModifierState()

    private var fullSync: Boolean = false

    override fun readFromNbt(tag: NbtCompound) {
        state.readFromNbt(tag)
    }

    override fun writeToNbt(tag: NbtCompound) {
        state.writeToNbt(tag)
    }

    override fun shouldSyncWith(player: ServerPlayerEntity): Boolean {
        return provider == player
    }

    override fun sync(full: Boolean) {
        if (!full && state == lastSyncState) return
        fullSync = full

        MinihexComponents.PERSONAL_MODIFIERS.sync(provider)
        lastSyncState = state.fullCopy()
    }

    override fun writeSyncPacket(buf: PacketByteBuf, recipient: ServerPlayerEntity) {
        val tag = NbtCompound()

        if (fullSync) state.writeToNbt(tag)
        else state.writeToNbt(tag, lastSyncState)

        buf.writeNbt(tag)
    }

    // No need to override applySyncPacket as PersonalModifierState.readFromNbt handles partial data already

    override fun serverTick() {
        sync()

        if (state.isFrail && provider.health > state.frail) {
            provider.health = state.frail.toFloat()
        }
    }

    override fun copyFrom(other: PersonalModifierComponent) {
        state = other.state.fullCopy()
    }
}
