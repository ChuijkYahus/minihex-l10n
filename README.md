# minihex

A Hexcasting addon with minigame oriented spells.

Requires: [Hex Casting](https://modrinth.com/mod/hex-casting), [Cardinal Components API](https://modrinth.com/mod/cardinal-components-api)

Recommended, to enable all minihex features: [MoreIotas](https://modrinth.com/mod/moreiotas) or [Hexpose](https://modrinth.com/mod/hexpose), [Hexical](https://modrinth.com/mod/hexical), Xaero's [Minimap](https://modrinth.com/mod/xaeros-minimap) and [World Map](https://modrinth.com/mod/xaeros-world-map)

Also has interop with [Hierophantics](https://modrinth.com/mod/hierophantics).

---

Current features:
- Spell to damage self (in a manner similar to overcasting)
- Personal Modifiers system. These are self-debuffs and can only be applied by the caster to themselves. Persists until
  a relog or the Clear Modifiers pattern is used. Currently active modifiers can be viewed by hovering over an icon in the inventory.
  Personal Modifiers can also be managed via commands.
  - Disregard: hide certain entity types or specific entities (latter capped to 128)
  - Disoriented: hide entity radar on Xaeros maps or disable the maps entirely
  - Nameless: tweak nametag rendering on your screen to fully hide, or only show with direct line of sight
  - Nearsighted: blindness, but with controllable distance. does not disable sprinting
  - Frail: reduce max health
  - Irrecovery: disable natural/food-based regeneration
  - Mark: client side glowing for certain entities (capped to 128), with configurable color - fixed and pigment-based
  - Relaxed: disable sprinting
  - Grounded: disable jumping
  - Intangible: disable collision with other players
  - Busy: simply a flag that resets on re-login. can be read by any casting environment

Upcoming features:
- None confirmed yet!

Possible future features:
- Proper gradients when marking with a pigment
- Displaying custom markers on maps/editing map markers via hex
- More nadirs for blindness, nausea, etc.
