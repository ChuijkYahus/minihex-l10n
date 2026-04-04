package ivoid.minihex.interop

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.fabricmc.loader.api.FabricLoader
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles

object HexicalInterop {
    val hasHexical: Boolean
        get() = FabricLoader.getInstance().isModLoaded("hexical")

    fun getPigment(iota: Iota): FrozenPigment? {
        return if (hasHexical) HexicalInteropImpl.getPigment(iota) else null
    }
}

private object HexicalInteropImpl {
    private val pigmentIotaClass: Class<*>? = runCatching {
        Class.forName("miyucomics.hexical.casting.iota.PigmentIota")
    }.getOrElse {
        runCatching {
            Class.forName("miyucomics.hexical.features.pigments.PigmentIota")
        }.getOrNull()
    }

    private val pigmentIotaGetPigment: MethodHandle? = runCatching {
        pigmentIotaClass?.let { MethodHandles.lookup().unreflect(it.getMethod("getPigment")) }
    }.getOrNull()

    fun getPigment(iota: Iota): FrozenPigment? {
        // Unlike the other two mods, there are two major versions of hexical in use.
        // PigmentIota was renamed, but otherwise the pigment getter remains the same across the two versions
        // Use reflection as the solution
        if (pigmentIotaClass != null && pigmentIotaGetPigment != null) {
            if (pigmentIotaClass.isInstance(iota)) {
                return pigmentIotaGetPigment.invoke(iota) as FrozenPigment?
            }
        }

        return null
    }
}
