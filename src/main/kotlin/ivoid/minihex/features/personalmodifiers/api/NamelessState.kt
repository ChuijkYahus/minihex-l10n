package ivoid.minihex.features.personalmodifiers.api

/** Nameless Personal Modifier - tweak nametag rendering */
enum class NamelessState(val displayString: String) {
    NAMED("named"),
    LINE_OF_SIGHT("line_of_sight"),
    NAMELESS("nameless"),
}
