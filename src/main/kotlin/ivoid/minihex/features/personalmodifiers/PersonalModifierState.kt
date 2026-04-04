package ivoid.minihex.features.personalmodifiers

import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.api.utils.hasByte
import at.petrak.hexcasting.api.utils.hasCompound
import at.petrak.hexcasting.api.utils.hasDouble
import at.petrak.hexcasting.api.utils.hasInt
import at.petrak.hexcasting.api.utils.hasList
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import ivoid.minihex.features.personalmodifiers.api.DisorientedState
import ivoid.minihex.features.personalmodifiers.api.MarkColor
import ivoid.minihex.features.personalmodifiers.api.NamelessState
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtHelper
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.util.Identifier
import java.util.UUID

data class PersonalModifierState(
    // Disregard disables rendering of certain entity types and specific entities.
    var disregardedEntityTypes: MutableSet<Identifier> = mutableSetOf(),

    /**
     * Capped to 128 entries as we do not know if an entity remains alive if it is outside of render distance,
     * and we do not want this to be a memory leak. 128 entities should be plenty.
     */
    var disregardedEntities: MutableSet<UUID> = mutableSetOf(),

    var disoriented: DisorientedState = DisorientedState.NORMAL,
    var nameless: NamelessState = NamelessState.NAMED,

    // See doc comments below.
    private var _nearsighted: Double = -1.0,
    private var _frail: Double = -1.0,

    /** Disables natural regeneration from food. */
    var irrecovery: Boolean = false,

    /** Client-side highlighting of specific entities. Capped to 128. */
    var mark: MutableMap<UUID, MarkColor> = mutableMapOf(),

    /** Disables sprinting. */
    var relaxed: Boolean = false,

    /** Disables jumping. */
    var grounded: Boolean = false,

    /** Disable collision with other players. */
    var intangible: Boolean = false,

    /** Mark the player as 'busy'. No other effect, but can be read by any casting environment. */
    var busy: Boolean = false,

    /* Adding a new Personal Modifier? Make sure to:
       - Add to constructor here (not below! relevant for data class generated methods)
       - Add to anyActive, if there are multiple 'inactive' values
       - Add to readFromNbt and writeToNbt
       - Add to fullCopy, if applicable
       - Add tooltip to PersonalModifiers
       - Add subcommand for the dev command
       - Add subcommand for the regular command
       - Implement any effects the modifier has
       - Add patterns to control the modifier
       - Add a book entry and associated translation strings
    */
) {
    /** Negative values disable the effect. Positive values represent the range of visibility. (Setting a negative value coerces to -1.) */
    var nearsighted: Double
        // Be extra sure that the value has been coerced.
        get() = if (_nearsighted < 0) -1.0 else _nearsighted
        set(distance) {
            _nearsighted = if (distance < 0) -1.0 else distance
        }

    /** Negative values (and 0) disable the effect. Positive values represent the max health applied. (Setting a negative value or 0 coerces to -1.) */
    var frail: Double
        get() = if (_frail <= 0) -1.0 else _frail
        set(maxHealth) {
            _frail = if (maxHealth <= 0) -1.0 else maxHealth
        }

    val anyActive: Boolean
        get() = this != PersonalModifierState()

    val isNearsighted: Boolean
        get() = nearsighted >= 0
    val isFrail: Boolean
        get() = frail > 0

    fun disregardEntity(uuid: UUID) {
        if (disregardedEntities.contains(uuid)) return

        if (disregardedEntities.size > 128) {
            disregardedEntities.remove(disregardedEntities.stream().findFirst().orElseThrow())
        }
        disregardedEntities.add(uuid)
    }

    fun markEntity(uuid: UUID, color: MarkColor) {
        if (mark.contains(uuid)) {
            mark[uuid] = color
            return
        }

        if (mark.size > 128) {
            mark.remove(mark.entries.stream().findFirst().orElseThrow().key)
        }
        mark[uuid] = color
    }

    /** Apply (partial) nbt data to this object. */
    fun readFromNbt(nbt: NbtCompound) {
        if (nbt.hasList("disregardedEntityTypes")) disregardedEntityTypes =
            nbt.getList("disregardedEntityTypes", NbtElement.STRING_TYPE).map {
                Identifier(it.asString())
            }.toMutableSet()

        if (nbt.hasList("disregardedEntities")) disregardedEntities =
            nbt.getList("disregardedEntities", NbtElement.INT_ARRAY_TYPE).map {
                NbtHelper.toUuid(it)
            }.toMutableSet()

        if (nbt.hasInt("disoriented")) disoriented =
            DisorientedState.entries[nbt.getInt("disoriented").coerceIn(
                0,
                DisorientedState.entries.size
            )]

        if (nbt.hasInt("nameless")) nameless =
            NamelessState.entries[nbt.getInt("nameless").coerceIn(
                0,
                NamelessState.entries.size
            )]

        if (nbt.hasDouble("nearsighted")) nearsighted = nbt.getDouble("nearsighted")
        if (nbt.hasDouble("frail")) frail = nbt.getDouble("frail")
        if (nbt.hasByte("irrecovery")) irrecovery = nbt.getByte("irrecovery") != 0.toByte()

        if (nbt.hasCompound("mark")) mark =
            nbt.getCompound("mark")
                .run { this.keys.associate { UUID.fromString(it)!! to MarkColor.fromNbt(this.getCompound(it)) } }
                .toMutableMap()

        if (nbt.hasByte("relaxed")) relaxed = nbt.getByte("relaxed") != 0.toByte()
        if (nbt.hasByte("grounded")) grounded = nbt.getByte("grounded") != 0.toByte()
        if (nbt.hasByte("intangible")) intangible = nbt.getByte("intangible") != 0.toByte()
        if (nbt.hasByte("busy")) busy = nbt.getByte("busy") != 0.toByte()
    }

    fun writeToNbt(nbt: NbtCompound, reference: PersonalModifierState? = null) {
        ifDifferent(disregardedEntityTypes, reference?.disregardedEntityTypes) {
            nbt.putList(
                "disregardedEntityTypes",
                toNbtList(it.map { id -> NbtString.of(id.toString()) })
            )
        }

        ifDifferent(disregardedEntities, reference?.disregardedEntities) {
            nbt.putList(
                "disregardedEntities",
                toNbtList(it.map { uuid -> NbtHelper.fromUuid(uuid) })
            )
        }

        ifDifferent(disoriented, reference?.disoriented) { nbt.putInt("disoriented", it.ordinal) }
        ifDifferent(nameless, reference?.nameless) { nbt.putInt("nameless", it.ordinal) }

        ifDifferent(nearsighted, reference?.nearsighted) { nbt.putDouble("nearsighted", it) }
        ifDifferent(frail, reference?.frail) { nbt.putDouble("frail", it) }
        ifDifferent(irrecovery, reference?.irrecovery) { nbt.putByte("irrecovery", asByte(it)) }

        ifDifferent(mark, reference?.mark) {
            val tag = NbtCompound()
            it.forEach { e -> tag.put(e.key.toString(), MarkColor.toNbt(e.value)) }

            nbt.putCompound("mark", tag)
        }

        ifDifferent(relaxed, reference?.relaxed) { nbt.putByte("relaxed", asByte(it)) }
        ifDifferent(grounded, reference?.grounded) { nbt.putByte("grounded", asByte(it)) }
        ifDifferent(intangible, reference?.intangible) { nbt.putByte("intangible", asByte(it)) }
        ifDifferent(busy, reference?.busy) { nbt.putByte("busy", asByte(it)) }
    }

    /** Call the callback if the current and reference are different. */
    private fun <T> ifDifferent(current: T, reference: T?, callback: (current: T) -> Unit) {
        if (current != reference) callback(current)
    }

    private fun asByte(value: Boolean): Byte {
        return if (value) 1.toByte() else 0.toByte()
    }

    private fun <T : NbtElement> toNbtList(entries: Collection<T>): NbtList {
        val list = NbtList()
        entries.forEach(list::add)

        return list
    }

    fun fullCopy(): PersonalModifierState {
        val new = copy()

        new.disregardedEntityTypes = HashSet(disregardedEntityTypes)
        new.disregardedEntities = HashSet(disregardedEntities)
        new.mark = HashMap(mark)

        return new
    }
}
