package ivoid.minihex.interop

import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexpose.iotas.IdentifierIota
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier

object HexposeInterop {
    val hasHexpose: Boolean
        get() = FabricLoader.getInstance().isModLoaded("hexpose")

    fun asIdentifier(iota: Iota): Identifier? {
        return if (hasHexpose) HexposeInteropImpl.asIdentifier(iota) else null
    }
}

private object HexposeInteropImpl {
    fun asIdentifier(iota: Iota): Identifier? {
        return when (iota) {
            is IdentifierIota -> iota.identifier
            else -> null
        }
    }
}
