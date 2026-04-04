package ivoid.minihex.features.personalmodifiers.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ivoid.minihex.features.personalmodifiers.PersonalModifierState
import ivoid.minihex.inits.MinihexComponents
import net.minecraft.server.network.ServerPlayerEntity

class OpModifierSingleValueGet<T>(val makeIota: (value: T) -> Iota, val get: (state: PersonalModifierState) -> T) : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.castingEntity !is ServerPlayerEntity) {
            throw MishapNotPlayer()
        }

        val component = MinihexComponents.PERSONAL_MODIFIERS.get(env.castingEntity as ServerPlayerEntity)
        return listOf(makeIota(get(component.state)))
    }
}

class OpModifierSingleValueSet<T>(val getIota: (args: List<Iota>, idx: Int, argc: Int) -> T, val set: (state: PersonalModifierState, value: T) -> Unit) : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.castingEntity !is ServerPlayerEntity) {
            throw MishapNotPlayer()
        }

        val value = getIota(args, 0, argc)

        val component = MinihexComponents.PERSONAL_MODIFIERS.get(env.castingEntity as ServerPlayerEntity)
        set(component.state, value)

        return listOf()
    }
}
