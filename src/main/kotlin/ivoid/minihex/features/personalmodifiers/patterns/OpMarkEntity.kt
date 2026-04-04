package ivoid.minihex.features.personalmodifiers.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.xplat.IXplatAbstractions
import ivoid.minihex.features.personalmodifiers.api.MarkColor
import ivoid.minihex.features.personalmodifiers.color.FixedColor
import ivoid.minihex.features.personalmodifiers.color.PigmentColor
import ivoid.minihex.inits.MinihexComponents
import ivoid.minihex.interop.HexicalInterop
import net.minecraft.server.network.ServerPlayerEntity

object OpMarkEntity : ConstMediaAction {
    override val argc: Int = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.castingEntity !is ServerPlayerEntity) {
            throw MishapNotPlayer()
        }

        val component = MinihexComponents.PERSONAL_MODIFIERS.get(env.castingEntity as ServerPlayerEntity)

        val target = args.getOrElse(0) { throw MishapNotEnoughArgs(argc, args.size) }
        val color = args.getOrElse(1) { throw MishapNotEnoughArgs(argc, args.size) }

        var colorValue: MarkColor? = null

        if (color is BooleanIota) {
            if (!color.bool) {
                // Unmark

                when (target) {
                    is EntityIota -> {
                        component.state.mark.remove(target.entity.uuid)
                    }

                    is NullIota -> {
                        component.state.mark.clear()
                    }

                    else -> {
                        throw MishapInvalidIota.ofType(target, argc - 1, "markable")
                    }
                }

                return listOf()
            }

            // Mark with default (white)
            colorValue = FixedColor()
        }

        if (target !is EntityIota) {
            throw MishapInvalidIota.ofType(target, argc - 1, "markable")
        }

        if (colorValue != null) {
        } else if (color is Vec3Iota) {
            val r = (color.vec3.x.coerceIn(0.0, 1.0) * 255).toInt()
            val g = (color.vec3.y.coerceIn(0.0, 1.0) * 255).toInt()
            val b = (color.vec3.z.coerceIn(0.0, 1.0) * 255).toInt()

            colorValue = FixedColor(r.shl(16).or(g.shl(8).or(b)))
        } else if (color is NullIota) {
            // Use the target's pigment
            colorValue = if (target.entity is ServerPlayerEntity) {
                PigmentColor(IXplatAbstractions.INSTANCE.getPigment(target.entity as ServerPlayerEntity))
            } else {
                PigmentColor(FrozenPigment.DEFAULT.get())
            }
        } else {
            HexicalInterop.getPigment(color)?.let {
                colorValue = PigmentColor(it)
            }
        }

        if (colorValue == null) {
            throw MishapInvalidIota.ofType(color, argc - 2, "color_like")
        }

        component.state.markEntity(target.entity.uuid, colorValue)
        return listOf()
    }
}

object OpQueryMark : ConstMediaAction {
    override val argc: Int = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.castingEntity !is ServerPlayerEntity) {
            throw MishapNotPlayer()
        }

        val component = MinihexComponents.PERSONAL_MODIFIERS.get(env.castingEntity as ServerPlayerEntity)
        val target = args.getEntity(0, argc)

        return listOf(BooleanIota(component.state.mark.contains(target.uuid)))
    }
}
