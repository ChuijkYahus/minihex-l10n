package ivoid.minihex.features.personalmodifiers

import ivoid.minihex.features.personalmodifiers.api.DisorientedState
import ivoid.minihex.features.personalmodifiers.api.NamelessState
import ivoid.minihex.inits.MinihexComponents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.floor

object PersonalModifiers {
    fun shouldShowInventoryIcon(player: PlayerEntity): Boolean {
        val component = MinihexComponents.PERSONAL_MODIFIERS.get(player)

        return component.state.anyActive
    }

    fun getInventoryIconTooltip(
        player: PlayerEntity,
        details: ShowDetails = ShowDetails.SHOW_HINT,
        showModifierNames: Boolean = false
    ): List<Text> {
        val state = MinihexComponents.PERSONAL_MODIFIERS.get(player).state

        if (!state.anyActive) return listOf(tr("none_active"))

        val addModifierName: (name: String) -> (text: MutableText) -> MutableText =
            if (showModifierNames) { name ->
                { text ->
                    text.append(
                        Text.literal(" (").append(tr(name)).append(Text.literal(")"))
                            .formatted(Formatting.DARK_GRAY)
                    )
                }
            }
            else { _ -> { text -> text } }

        val tooltip = mutableListOf<Text>(tr("header").formatted(Formatting.BOLD))

        val disregardTotal = state.disregardedEntityTypes.size +
                state.disregardedEntities.size
        if (disregardTotal > 0) {
            tooltip.add(
                pluralize(disregardTotal, "mod/disregard/noun_singular", "mod/disregard/noun_plural")
                    .let { if (details == ShowDetails.SHOW_DETAILS) it else it.formatted(Formatting.GRAY) }
                    .let(addModifierName("mod/disregard"))
            )

            if (details == ShowDetails.SHOW_DETAILS) {
                tooltip.addAll(state.disregardedEntityTypes.map {
                    Text.literal("- $it")
                        .formatted(Formatting.ITALIC, Formatting.GOLD)
                })

                if (state.disregardedEntities.isNotEmpty()) {
                    tooltip.add(
                        pluralize(
                            state.disregardedEntities.size,
                            "mod/disregard/entities_singular",
                            "mod/disregard/entities_plural"
                        )
                            .formatted(Formatting.ITALIC, Formatting.GRAY)
                    )
                }

                tooltip.add(Text.empty())
            }
        }

        if (state.mark.isNotEmpty()) tooltip.add(
            pluralize(state.mark.size, "mod/mark/noun_singular", "mod/mark/noun_plural")
                .formatted(Formatting.GRAY)
                .let(addModifierName("mod/mark"))
        )

        if (state.disoriented != DisorientedState.NORMAL) {
            tooltip.add(
                tr("mod/disoriented/${state.disoriented.displayString}")
                    .formatted(Formatting.GRAY)
                    .let(addModifierName("mod/disoriented"))
            )
        }

        if (state.nameless != NamelessState.NAMED) {
            tooltip.add(
                tr("mod/nameless/${state.nameless.displayString}")
                    .formatted(Formatting.GRAY)
                    .let(addModifierName("mod/nameless"))
            )
        }

        if (state.isNearsighted) {
            tooltip.add(
                tr("mod/nearsighted/distance", floor(state.nearsighted * 100) / 100)
                    .formatted(Formatting.GRAY)
                    .let(addModifierName("mod/nearsighted"))
            )
        }

        if (state.isFrail) {
            tooltip.add(
                tr("mod/frail/health", floor(state.frail * 10) / 10)
                    .formatted(Formatting.GRAY)
                    .let(addModifierName("mod/frail"))
            )
        }

        if (state.irrecovery) tooltip.add(
            tr("mod/irrecovery/desc").formatted(Formatting.GRAY)
                .let(addModifierName("mod/irrecovery"))
        )

        if (state.relaxed) tooltip.add(
            tr("mod/relaxed/desc").formatted(Formatting.GRAY)
                .let(addModifierName("mod/relaxed"))
        )

        if (state.grounded) tooltip.add(
            tr("mod/grounded/desc").formatted(Formatting.GRAY)
                .let(addModifierName("mod/grounded"))
        )

        if (state.intangible) tooltip.add(
            tr("mod/intangible/desc").formatted(Formatting.GRAY)
                .let(addModifierName("mod/intangible"))
        )

        if (state.busy) tooltip.add(
            tr("mod/busy/desc").formatted(Formatting.GRAY)
                .let(addModifierName("mod/busy"))
        )

        tooltip.add(Text.empty())

        if (details == ShowDetails.SHOW_HINT) {
            tooltip.add(
                tr("details_hint").formatted(Formatting.ITALIC, Formatting.DARK_GRAY)
            )
        }

        tooltip.add(
            tr("clear_hint").formatted(Formatting.ITALIC, Formatting.DARK_GRAY)
        )

        return tooltip
    }

    private fun pluralize(n: Number, singular: String, plural: String): MutableText {
        return if (n == 1) tr(singular, n) else tr(plural, n)
    }

    private fun tr(name: String, vararg extra: Any): MutableText =
        Text.translatable("minihex.modifier.tooltip:$name", *extra)

    enum class ShowDetails {
        HIDE_DETAILS,
        SHOW_HINT,
        SHOW_DETAILS,
    }
}
