package ivoid.minihex.features.selfdamage

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveDouble
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import ivoid.minihex.features.personalmodifiers.patterns.MishapNotPlayer
import ivoid.minihex.inits.MinihexDamageTypes
import net.minecraft.server.network.ServerPlayerEntity

object OpSelfDamage : SpellAction {
    var IS_DAMAGING = false
        private set

    override val argc: Int = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        if (env.castingEntity !is ServerPlayerEntity) {
            throw MishapNotPlayer()
        }

        val amount = args.getPositiveDouble(0, argc)

        return SpellAction.Result(Spell(amount), 0, listOf())
    }

    private data class Spell(val amount: Double) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val player = env.castingEntity as ServerPlayerEntity

            try {
                // need to communicate with the hierophantics mixin to disable triggers
                // we rely on the fact that the server is single threaded
                IS_DAMAGING = true

                Mishap.trulyHurt(
                    player,
                    player.damageSources.create(MinihexDamageTypes.OVERCAST_SELF),
                    amount.toFloat()
                )
            } finally {
                IS_DAMAGING = false
            }
        }
    }
}
