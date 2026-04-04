package ivoid.minihex.inits

import ivoid.minihex.Minihex
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object MinihexDamageTypes {
    val OVERCAST_SELF: RegistryKey<DamageType> = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Minihex.id("overcast_self"))
}
