package ivoid.minihex.interop

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import ram.talia.moreiotas.api.casting.iota.EntityTypeIota
import ram.talia.moreiotas.api.casting.iota.IotaTypeIota

object MoreIotasInterop {
    val hasMoreIotas: Boolean
        get() = FabricLoader.getInstance().isModLoaded("moreiotas")

    fun asIdentifier(iota: Iota): Identifier? {
        return if (hasMoreIotas) MoreIotasInteropImpl.asIdentifier(iota) else null
    }
}

// Only load this class if MoreIotas is present!
private object MoreIotasInteropImpl {
    fun asIdentifier(iota: Iota): Identifier? {
        return when (iota) {
            is IotaTypeIota -> {
                HexIotaTypes.REGISTRY.getKey(iota.iotaType)
                    .map { it.value }
                    .orElse(null)
            }

            is EntityTypeIota -> {
                Registries.ENTITY_TYPE.getKey(iota.entityType)
                    .map { it.value }
                    .orElse(null)
            }

            else -> {
                // We do not use item iota, so we do not implement it for now
                null
            }
        }
    }
}
