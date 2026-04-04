package ivoid.minihex.inits

import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.xplat.IXplatAbstractions
import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import ivoid.minihex.features.personalmodifiers.PersonalModifierState
import ivoid.minihex.features.personalmodifiers.api.DisorientedState
import ivoid.minihex.features.personalmodifiers.api.NamelessState
import ivoid.minihex.features.personalmodifiers.api.PersonalModifierComponent
import ivoid.minihex.features.personalmodifiers.color.FixedColor
import ivoid.minihex.features.personalmodifiers.color.PigmentColor
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object MinihexCommands {
    val NO_HAND_PIGMENT = SimpleCommandExceptionType(Text.translatable("minihex.commands.feedback:mark/entity/pigment/hand/not_found"))

    fun init() {
        CommandRegistrationCallback.EVENT.register { dispatcher, access, environment ->
            val node = dispatcher.register(
                literal("personal-modifier")
                    .requires { it.hasPermissionLevel(2) }
                    .then(
                        argument("target", EntityArgumentType.player()).then(
                            literal("clear").executes(command { ctx, component ->
                                component.state = PersonalModifierState()
                                feedback(ctx, "clear")
                            })
                        ).then(
                            literal("disregard").executes(vcommand { ctx, component ->
                                feedback(ctx, "disregard/info", component.state.disregardedEntities.size, component.state.disregardedEntityTypes.size)
                                component.state.disregardedEntities.size + component.state.disregardedEntities.size
                            }).then(
                                literal("clear").executes(command { ctx, component ->
                                    component.state.disregardedEntities.clear()
                                    component.state.disregardedEntityTypes.clear()
                                    feedback(ctx, "disregard/clear")
                                })
                            ).then(
                                literal("type").then(
                                    literal("clear").executes(command { ctx, component ->
                                        component.state.disregardedEntityTypes.clear()
                                        feedback(ctx, "disregard/clear/types")
                                    })
                                ).then(
                                    argument("type", IdentifierArgumentType.identifier())
                                        .then(
                                            argument("ignored", BoolArgumentType.bool())
                                                .executes(command { ctx, component ->
                                                    val type = IdentifierArgumentType.getIdentifier(ctx, "type")
                                                    val ignore = BoolArgumentType.getBool(ctx, "ignored")
                                                    if (ignore) {
                                                        component.state.disregardedEntityTypes.add(type)
                                                        feedback(ctx, "disregard/type/set", type)
                                                    } else {
                                                        component.state.disregardedEntityTypes.remove(type)
                                                        feedback(ctx, "disregard/type/reset", type)
                                                    }
                                                })
                                        )
                                )
                            ).then(
                                literal("entity").then(
                                    literal("clear").executes(command { ctx, component ->
                                        component.state.disregardedEntities.clear()
                                        feedback(ctx, "disregard/clear/entities")
                                    })
                                ).then(
                                    argument("entity", EntityArgumentType.entity())
                                        .then(
                                            argument("ignored", BoolArgumentType.bool())
                                                .executes(command { ctx, component ->
                                                    val entity = EntityArgumentType.getEntity(ctx, "entity")
                                                    val ignore = BoolArgumentType.getBool(ctx, "ignored")
                                                    if (ignore) {
                                                        component.state.disregardEntity(entity.uuid)
                                                        feedback(ctx, "disregard/entity/set", entity.uuid)
                                                    } else {
                                                        component.state.disregardedEntities.remove(entity.uuid)
                                                        feedback(ctx, "disregard/entity/reset", entity.uuid)
                                                    }
                                                })
                                        )
                                )
                            )
                        ).then(
                            literal("disoriented")
                                .executes(vcommand { ctx, component ->
                                    feedback(
                                        ctx, when (component.state.disoriented) {
                                            DisorientedState.NORMAL -> "disoriented/info/normal"
                                            DisorientedState.NO_RADAR -> "disoriented/info/no_radar"
                                            DisorientedState.NO_MAP -> "disoriented/info/no_map"
                                        }
                                    )
                                    component.state.disoriented.ordinal
                                }).then(
                                    literal("normal").executes(command { ctx, component ->
                                        component.state.disoriented = DisorientedState.NORMAL
                                        feedback(ctx, "disoriented/normal")
                                    })
                                ).then(
                                    literal("no_radar").executes(command { ctx, component ->
                                        component.state.disoriented = DisorientedState.NO_RADAR
                                        feedback(ctx, "disoriented/no_radar")
                                    })
                                ).then(
                                    literal("no_map").executes(command { ctx, component ->
                                        component.state.disoriented = DisorientedState.NO_MAP
                                        feedback(ctx, "disoriented/no_map")
                                    })
                                )
                        ).then(
                            literal("nameless").executes(vcommand { ctx, component ->
                                feedback(
                                    ctx, when (component.state.nameless) {
                                        NamelessState.NAMED -> "nameless/info/named"
                                        NamelessState.LINE_OF_SIGHT -> "nameless/info/line_of_sight"
                                        NamelessState.NAMELESS -> "nameless/info/nameless"
                                    }
                                )
                                component.state.nameless.ordinal
                            }).then(
                                literal("named").executes(command { ctx, component ->
                                    component.state.nameless = NamelessState.NAMED
                                    feedback(ctx, "nameless/named")
                                })
                            ).then(
                                literal("line_of_sight").executes(command { ctx, component ->
                                    component.state.nameless = NamelessState.LINE_OF_SIGHT
                                    feedback(ctx, "nameless/line_of_sight")
                                })
                            ).then(
                                literal("nameless").executes(command { ctx, component ->
                                    component.state.nameless = NamelessState.NAMELESS
                                    feedback(ctx, "nameless/nameless")
                                })
                            )
                        ).then(
                            literal("nearsighted").executes(vcommand { ctx, component ->
                                if (component.state.isNearsighted) feedback(ctx, "nearsighted/info/is", component.state.nearsighted)
                                else feedback(ctx, "nearsighted/info/not")

                                component.state.nearsighted.toInt()
                            }).then(
                                literal("reset").executes(command { ctx, component ->
                                    component.state.nearsighted = -1.0
                                    feedback(ctx, "nearsighted/reset")
                                })
                            ).then(
                                argument(
                                    "distance",
                                    DoubleArgumentType.doubleArg(0.0)
                                ).executes(command { ctx, component ->
                                    val distance = DoubleArgumentType.getDouble(ctx, "distance")
                                    component.state.nearsighted = distance
                                    feedback(ctx, "nearsighted/set", distance)
                                })
                            )
                        ).then(
                            literal("frail").executes(vcommand { ctx, component ->
                                if (component.state.isFrail) feedback(ctx, "frail/info/is", component.state.frail)
                                else feedback(ctx, "frail/info/not")

                                component.state.frail.toInt()
                            }).then(
                                literal("reset").executes(command { ctx, component ->
                                    component.state.frail = -1.0
                                    feedback(ctx, "frail/reset")
                                })
                            ).then(
                                argument(
                                    "max_health",
                                    DoubleArgumentType.doubleArg(0.0)
                                ).executes(command { ctx, component ->
                                    val maxHealth = DoubleArgumentType.getDouble(ctx, "max_health")
                                    component.state.frail = maxHealth
                                    feedback(ctx, "frail/set", maxHealth)
                                })
                            )
                        ).then(
                            literal("irrecovery").executes(vcommand { ctx, component ->
                                feedback(ctx, if (component.state.irrecovery) "irrecovery/info/yes" else "irrecovery/info/no")
                                if (component.state.irrecovery) 1 else 0
                            }).then(
                                argument("enable", BoolArgumentType.bool()).executes(command { ctx, component ->
                                    val enable = BoolArgumentType.getBool(ctx, "enable")
                                    component.state.irrecovery = enable
                                    feedback(ctx, if (enable) "irrecovery/set" else "irrecovery/unset")
                                })
                            )
                        ).then(
                            literal("mark").executes(vcommand { ctx, component ->
                                feedback(ctx, "mark/info", component.state.mark.size)
                                component.state.mark.size
                            }).then(
                                literal("clear").executes(command { ctx, component ->
                                    component.state.mark.clear()
                                    feedback(ctx, "mark/reset")
                                })
                            ).then(
                                argument("entity", EntityArgumentType.entity()).then(
                                    literal("clear").executes(command { ctx, component ->
                                        val entity = EntityArgumentType.getEntity(ctx, "entity")
                                        component.state.mark.remove(entity.uuid)
                                        feedback(ctx, "mark/entity/reset", entity.uuid)
                                    })
                                ).then(
                                    literal("fixed").then(
                                        argument(
                                            "color",
                                            IntegerArgumentType.integer(0, 0xffffff)
                                        ).executes(command { ctx, component ->
                                            val entity = EntityArgumentType.getEntity(ctx, "entity")
                                            val color = IntegerArgumentType.getInteger(ctx, "color")
                                            component.state.markEntity(entity.uuid, FixedColor(color))
                                            feedback(ctx, "mark/entity/fixed", entity.uuid)
                                        })
                                    )
                                ).then(
                                    literal("pigment").executes(command { ctx, component ->
                                        // Use target's pigment
                                        val entity = EntityArgumentType.getEntity(ctx, "entity")

                                        val colorValue = if (entity is ServerPlayerEntity) {
                                            PigmentColor(IXplatAbstractions.INSTANCE.getPigment(entity))
                                        } else {
                                            PigmentColor(FrozenPigment.DEFAULT.get())
                                        }

                                        component.state.markEntity(entity.uuid, colorValue)
                                        feedback(ctx, "mark/entity/pigment/auto", entity.uuid)
                                    }).then(
                                        literal("from_hand").executes(command { ctx, component ->
                                            val entity = EntityArgumentType.getEntity(ctx, "entity")
                                            val source = ctx.source.playerOrThrow

                                            if (IXplatAbstractions.INSTANCE.isPigment(source.mainHandStack)) {
                                                component.state.markEntity(
                                                    entity.uuid,
                                                    PigmentColor(FrozenPigment(source.mainHandStack.copy(), entity.uuid))
                                                )
                                            } else if (IXplatAbstractions.INSTANCE.isPigment(source.offHandStack)) {
                                                component.state.markEntity(
                                                    entity.uuid,
                                                    PigmentColor(FrozenPigment(source.offHandStack.copy(), entity.uuid))
                                                )
                                            } else {
                                                throw NO_HAND_PIGMENT.create()
                                            }

                                            feedback(ctx, "mark/entity/pigment/hand", entity.uuid)
                                        })
                                    )
                                )
                            )
                        ).then(
                            literal("relaxed").executes(vcommand { ctx, component ->
                                feedback(ctx, if (component.state.relaxed) "relaxed/info/yes" else "relaxed/info/no")
                                if (component.state.relaxed) 1 else 0
                            }).then(
                                argument("enable", BoolArgumentType.bool()).executes(command { ctx, component ->
                                    val enable = BoolArgumentType.getBool(ctx, "enable")
                                    component.state.relaxed = enable
                                    feedback(ctx, if (enable) "relaxed/set" else "relaxed/unset")
                                })
                            )
                        ).then(
                            literal("grounded").executes(vcommand { ctx, component ->
                                feedback(ctx, if (component.state.grounded) "grounded/info/yes" else "grounded/info/no")
                                if (component.state.grounded) 1 else 0
                            }).then(
                                argument("enable", BoolArgumentType.bool()).executes(command { ctx, component ->
                                    val enable = BoolArgumentType.getBool(ctx, "enable")
                                    component.state.grounded = enable
                                    feedback(ctx, if (enable) "grounded/set" else "grounded/unset")
                                })
                            )
                        ).then(
                            literal("intangible").executes(vcommand { ctx, component ->
                                feedback(ctx, if (component.state.intangible) "intangible/info/yes" else "intangible/info/no")
                                if (component.state.intangible) 1 else 0
                            }).then(
                                argument("enable", BoolArgumentType.bool()).executes(command { ctx, component ->
                                    val enable = BoolArgumentType.getBool(ctx, "enable")
                                    component.state.intangible = enable
                                    feedback(ctx, if (enable) "intangible/set" else "intangible/unset")
                                })
                            )
                        ).then(
                            literal("busy").executes(vcommand { ctx, component ->
                                feedback(ctx, if (component.state.busy) "busy/info/yes" else "busy/info/no")
                                if (component.state.busy) 1 else 0
                            }).then(
                                argument("enable", BoolArgumentType.bool()).executes(command { ctx, component ->
                                    val enable = BoolArgumentType.getBool(ctx, "enable")
                                    component.state.busy = enable
                                    feedback(ctx, if (enable) "busy/set" else "busy/unset")
                                })
                            )
                        )
                    )
            )

            dispatcher.register(literal("pmodifier").redirect(node))
        }
    }

    private fun getTarget(ctx: CommandContext<ServerCommandSource>): ServerPlayerEntity {
        return EntityArgumentType.getPlayer(ctx, "target")
    }

    private fun feedback(ctx: CommandContext<ServerCommandSource>, msg: String, vararg extra: Any, broadcast: Boolean = false) {
        ctx.source.sendFeedback({
            Text.translatable("minihex.commands.feedback:$msg", *extra)
        }, broadcast)
    }

    private fun command(body: (ctx: CommandContext<ServerCommandSource>, component: PersonalModifierComponent) -> Unit): (ctx: CommandContext<ServerCommandSource>) -> Int {
        return {
            val component = MinihexComponents.PERSONAL_MODIFIERS.get(getTarget(it))
            body(it, component)

            Command.SINGLE_SUCCESS
        }
    }

    private fun vcommand(body: (ctx: CommandContext<ServerCommandSource>, component: PersonalModifierComponent) -> Int): (ctx: CommandContext<ServerCommandSource>) -> Int {
        return {
            val component = MinihexComponents.PERSONAL_MODIFIERS.get(getTarget(it))
            body(it, component)
        }
    }
}
