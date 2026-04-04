package ivoid.minihex.inits

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.getBool
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import ivoid.minihex.Minihex
import ivoid.minihex.features.personalmodifiers.api.DisorientedState
import ivoid.minihex.features.personalmodifiers.api.NamelessState
import ivoid.minihex.features.personalmodifiers.patterns.OpClearModifiers
import ivoid.minihex.features.personalmodifiers.patterns.OpDisregard
import ivoid.minihex.features.personalmodifiers.patterns.OpMarkEntity
import ivoid.minihex.features.personalmodifiers.patterns.OpModifierBusyGet
import ivoid.minihex.features.personalmodifiers.patterns.OpModifierSingleValueSet
import ivoid.minihex.features.personalmodifiers.patterns.OpModifierMultistateGet
import ivoid.minihex.features.personalmodifiers.patterns.OpModifierMultistateSet
import ivoid.minihex.features.personalmodifiers.patterns.OpModifierSingleValueGet
import ivoid.minihex.features.personalmodifiers.patterns.OpQueryDisregard
import ivoid.minihex.features.personalmodifiers.patterns.OpQueryMark
import ivoid.minihex.features.selfdamage.OpSelfDamage
import net.minecraft.registry.Registry

object MinihexActions {
    fun init() {
        register(
            "modifier/disregard/get",
            "qaqqqwqwqqqaqqqwqwq",
            HexDir.NORTH_EAST,
            OpQueryDisregard
        )

        register(
            "modifier/disregard/set",
            "qaqqqwqwqaedeaqwqwq",
            HexDir.NORTH_EAST,
            OpDisregard
        )

        register(
            "modifier/mark/get",
            "qaqqqwqwqaddaqwqwq",
            HexDir.NORTH_EAST,
            OpQueryMark
        )

        register(
            "modifier/mark/set",
            "qaqqqwqwqqaaqqwqwq",
            HexDir.NORTH_EAST,
            OpMarkEntity
        )

        register(
            "modifier/disoriented/get",
            "qaqqqwqwqdaadqwqwq",
            HexDir.NORTH_EAST,
            OpModifierMultistateGet { it.disoriented }
        )

        register(
            "modifier/disoriented/set",
            "qaqqqwqwqeddeqwqwq",
            HexDir.NORTH_EAST,
            OpModifierMultistateSet(DisorientedState.entries) { state, value -> state.disoriented = value }
        )

        register(
            "modifier/nameless/get",
            "qaqeaadqwqwqwqwqwq",
            HexDir.NORTH_EAST,
            OpModifierMultistateGet { it.nameless }
        )

        register(
            "modifier/nameless/set",
            "qaqwddeqwqwqwqwqwq",
            HexDir.NORTH_EAST,
            OpModifierMultistateSet(NamelessState.entries) { state, value -> state.nameless = value }
        )

        register(
            "modifier/nearsighted/get",
            "qaqqqqqwqqq",
            HexDir.NORTH_EAST,
            OpModifierSingleValueGet(::DoubleIota) { it.nearsighted }
        )

        register(
            "modifier/nearsighted/set",
            "qaqdeeeweee",
            HexDir.NORTH_EAST,
            OpModifierSingleValueSet(List<Iota>::getDouble) { state, value -> state.nearsighted = value }
        )

        register(
            "modifier/frail/get",
            "qaqqqqeaeaeaeqq",
            HexDir.NORTH_EAST,
            OpModifierSingleValueGet(::DoubleIota) { it.frail }
        )

        register(
            "modifier/frail/set",
            "qaqdeeqdqdqdqee",
            HexDir.NORTH_EAST,
            OpModifierSingleValueSet(List<Iota>::getDouble) { state, value -> state.frail = value }
        )

        register(
            "modifier/irrecovery/get",
            "qaqqqwqqaaqqwqwqwq",
            HexDir.NORTH_EAST,
            OpModifierSingleValueGet(::BooleanIota) { it.irrecovery }
        )

        register(
            "modifier/irrecovery/set",
            "qaqqqwqaddaqwqwqwq",
            HexDir.NORTH_EAST,
            OpModifierSingleValueSet(List<Iota>::getBool) { state, value -> state.irrecovery = value }
        )

        register(
            "modifier/relaxed/get",
            "qaqqqwqwqwqqaaqqwq",
            HexDir.NORTH_EAST,
            OpModifierSingleValueGet(::BooleanIota) { it.relaxed }
        )

        register(
            "modifier/relaxed/set",
            "qaqqqwqwqwqaddaqwq",
            HexDir.NORTH_EAST,
            OpModifierSingleValueSet(List<Iota>::getBool) { state, value -> state.relaxed = value }
        )

        register(
            "modifier/grounded/get",
            "qaqdqawwaq",
            HexDir.NORTH_EAST,
            OpModifierSingleValueGet(::BooleanIota) { it.grounded }
        )

        register(
            "modifier/grounded/set",
            "qaqqedwwde",
            HexDir.NORTH_EAST,
            OpModifierSingleValueSet(List<Iota>::getBool) { state, value -> state.grounded = value }
        )

        register(
            "modifier/intangible/get",
            "qwqwqwqwqwq",
            HexDir.EAST,
            OpModifierSingleValueGet(::BooleanIota) { it.intangible }
        )

        register(
            "modifier/intangible/set",
            "ewewewewewe",
            HexDir.WEST,
            OpModifierSingleValueSet(List<Iota>::getBool) { state, value -> state.intangible = value }
        )

        register(
            "modifier/busy/get",
            "qaqqqwqwqwqwqwq",
            HexDir.NORTH_EAST,
            OpModifierBusyGet
        )

        register(
            "modifier/busy/set",
            "qaqdewewewewewe",
            HexDir.NORTH_EAST,
            OpModifierSingleValueSet(List<Iota>::getBool) { state, value -> state.busy = value }
        )

        register("clear_modifiers", "qaqqqaddaqaddaqaddaqaddaqaddaq", HexDir.NORTH_EAST, OpClearModifiers)

        register("damage_self", "qaqwwwqaq", HexDir.WEST, OpSelfDamage)
    }

    private fun register(name: String, signature: String, startDir: HexDir, action: Action) {
        Registry.register(
            HexActions.REGISTRY,
            Minihex.id(name),
            ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action)
        )
    }
}
