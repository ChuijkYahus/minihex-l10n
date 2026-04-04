package ivoid.minihex.features.personalmodifiers.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getIntBetween
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import ivoid.minihex.features.personalmodifiers.PersonalModifierState
import ivoid.minihex.inits.MinihexComponents
import net.minecraft.server.network.ServerPlayerEntity

class OpModifierMultistateGet<T : Enum<T>>(val get: (state: PersonalModifierState) -> T) : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.castingEntity !is ServerPlayerEntity) {
            throw MishapNotPlayer()
        }

        val component = MinihexComponents.PERSONAL_MODIFIERS.get(env.castingEntity as ServerPlayerEntity)
        val value = get(component.state)

        return listOf(DoubleIota(value.ordinal.toDouble()))
    }
}

class OpModifierMultistateSet<T : Enum<T>>(
    val values: List<T>,
    val set: (state: PersonalModifierState, value: T) -> Unit
) : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.castingEntity !is ServerPlayerEntity) {
            throw MishapNotPlayer()
        }

        val value = args.getIntBetween(0, 0, values.size - 1, argc)

        val component = MinihexComponents.PERSONAL_MODIFIERS.get(env.castingEntity as ServerPlayerEntity)
        set(component.state, values[value])

        return listOf()
    }
}
