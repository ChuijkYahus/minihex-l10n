package ivoid.minihex.features.personalmodifiers.color

import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.putCompound
import ivoid.minihex.features.personalmodifiers.api.MarkColor
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d

class PigmentColor(val pigment: FrozenPigment) : MarkColor {
    companion object {
        const val TYPE = "pigment_color"
        val ORIGIN = Vec3d(0.0, 0.0, 0.0)

        fun fromNbt(tag: NbtCompound): PigmentColor {
            return PigmentColor(FrozenPigment.fromNBT(tag.getCompound("pigment")))
        }
    }

    override val type = TYPE
    override fun getColor(timestamp: Float): Int = pigment.colorProvider.getColor(timestamp, ORIGIN)

    override fun writeToNbt(tag: NbtCompound) {
        tag.putCompound("pigment", pigment.serializeToNBT())
    }
}
