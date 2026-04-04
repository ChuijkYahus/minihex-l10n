package ivoid.minihex.features.personalmodifiers.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ivoid.minihex.features.personalmodifiers.PersonalModifierState
import ivoid.minihex.inits.MinihexComponents
import net.minecraft.server.network.ServerPlayerEntity

object OpClearModifiers : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.castingEntity !is ServerPlayerEntity) {
            throw MishapNotPlayer()
        }

        val component = MinihexComponents.PERSONAL_MODIFIERS.get(env.castingEntity as ServerPlayerEntity)
        component.state = PersonalModifierState()

        return listOf()
    }
}
