package ivoid.minihex.features.personalmodifiers.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBool
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import ivoid.minihex.inits.MinihexComponents
import ivoid.minihex.interop.HexposeInterop
import ivoid.minihex.interop.MoreIotasInterop
import net.minecraft.registry.Registries
import net.minecraft.server.network.ServerPlayerEntity

object OpDisregard : ConstMediaAction {
    override val argc: Int = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.castingEntity !is ServerPlayerEntity) {
            throw MishapNotPlayer()
        }

        val component = MinihexComponents.PERSONAL_MODIFIERS.get(env.castingEntity as ServerPlayerEntity)

        val target = args.getOrElse(0) { throw MishapNotEnoughArgs(argc, args.size) }
        val ignore = args.getBool(1, argc)

        if (target is EntityIota) {
            if (ignore) component.state.disregardEntity(target.entity.uuid)
            else component.state.disregardedEntities.remove(target.entity.uuid)

            return listOf()
        } else if (!ignore) {
            when (target) {
                is NullIota -> {
                    // unignore all
                    component.state.disregardedEntities.clear()
                    component.state.disregardedEntityTypes.clear()

                    return listOf()
                }

                is BooleanIota if target.bool -> {
                    component.state.disregardedEntities.clear()

                    return listOf()
                }

                is BooleanIota -> {
                    component.state.disregardedEntityTypes.clear()

                    return listOf()
                }
            }
        }

        val identifier = MoreIotasInterop.asIdentifier(target) ?: HexposeInterop.asIdentifier(target)

        if (identifier == null) throw MishapInvalidIota.ofType(target, argc - 1, "disregardable")

        if (Registries.ENTITY_TYPE.containsId(identifier)) {
            if (ignore) component.state.disregardedEntityTypes.add(identifier)
            else component.state.disregardedEntityTypes.remove(identifier)
            return listOf()
        }

        throw MishapInvalidIota.ofType(target, argc - 1, "disregardable")
    }
}

object OpQueryDisregard : ConstMediaAction {
    override val argc: Int = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (env.castingEntity !is ServerPlayerEntity) {
            throw MishapNotPlayer()
        }

        val component = MinihexComponents.PERSONAL_MODIFIERS.get(env.castingEntity as ServerPlayerEntity)

        val target = args.getOrElse(0) { throw MishapNotEnoughArgs(argc, args.size) }
        if (target is EntityIota) {
            return listOf(BooleanIota(component.state.disregardedEntities.contains(target.entity.uuid)))
        }

        val identifier = MoreIotasInterop.asIdentifier(target) ?: HexposeInterop.asIdentifier(target)
        if (identifier != null) {
            return listOf(BooleanIota(component.state.disregardedEntityTypes.contains(identifier)))
        }

        throw MishapInvalidIota.ofType(target, argc - 1, "disregardable")
    }
}
