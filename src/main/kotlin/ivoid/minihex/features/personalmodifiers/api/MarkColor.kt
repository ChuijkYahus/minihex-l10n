package ivoid.minihex.features.personalmodifiers.api

import ivoid.minihex.features.personalmodifiers.color.FixedColor
import ivoid.minihex.features.personalmodifiers.color.PigmentColor
import net.minecraft.nbt.NbtCompound

interface MarkColor {
    companion object {
        val REGISTRY: Map<String, (tag: NbtCompound) -> MarkColor> = mutableMapOf(
            FixedColor.TYPE to FixedColor::fromNbt,
            PigmentColor.TYPE to PigmentColor::fromNbt
        )

        fun toNbt(color: MarkColor): NbtCompound {
            val tag = NbtCompound()
            tag.putString("color_type", color.type)
            color.writeToNbt(tag)

            return tag
        }

        fun fromNbt(tag: NbtCompound): MarkColor {
            val type = tag.getString("color_type")
            return REGISTRY[type]?.invoke(tag) ?: FixedColor(0xffffff)
        }
    }

    val type: String

    fun getColor(timestamp: Float): Int
    fun writeToNbt(tag: NbtCompound)
}
