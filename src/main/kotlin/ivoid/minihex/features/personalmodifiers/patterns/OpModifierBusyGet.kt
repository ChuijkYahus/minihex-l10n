package ivoid.minihex.features.personalmodifiers.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPlayer
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.Iota
import ivoid.minihex.inits.MinihexComponents

object OpModifierBusyGet : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val target = args.getPlayer(0, argc)

        val component = MinihexComponents.PERSONAL_MODIFIERS.get(target)
        return listOf(BooleanIota(component.state.busy))
    }
}
