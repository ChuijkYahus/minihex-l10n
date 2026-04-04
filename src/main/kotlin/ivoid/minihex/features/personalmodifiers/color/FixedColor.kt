package ivoid.minihex.features.personalmodifiers.color

import ivoid.minihex.features.personalmodifiers.api.MarkColor
import net.minecraft.nbt.NbtCompound

class FixedColor(val color: Int = 0xffffff) : MarkColor {
    companion object {
        const val TYPE = "fixed_color"

        fun fromNbt(tag: NbtCompound): FixedColor {
            return FixedColor(tag.getInt("color"))
        }
    }

    override val type = TYPE
    override fun getColor(timestamp: Float) = color

    override fun writeToNbt(tag: NbtCompound) {
        tag.putInt("color", color)
    }
}
