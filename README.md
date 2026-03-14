# minihex

A Hexcasting addon with minigame oriented spells.

Requires: [Hex Casting](https://modrinth.com/mod/hex-casting), [Cardinal Components API](https://modrinth.com/mod/cardinal-components-api)

---

Current features:
- None yet!

Upcoming features:
- Spell to damage self (overcast damage type, bypasses embedded minds)
- 'Busy' flag - any casting environment can read, but only the caster can set it on themselves. Clears on relog.
- Personal Modifiers system. These are self-debuffs and can only be applied by the caster to themselves. Persists until
   a relog or the Clear Modifiers pattern is used. Currently active modifiers can be viewed by hovering over an icon in the inventory.
  - Modifier: Disregard/Modifier: Regard - hide/show certain particle types, entity types, and entities (last one capped to 100)
  - Modifier: Disorientation - hide entity radar on Xaeros maps or disable the maps entirely
  - Modifier: Nameless - tweak nameplate rendering on your screen to fully hide, or only show with direct line of sight
    - TODO: this needs testing with figura
  - Modifier: Nearsighted - blindness, but with controllable distance. does not disable sprinting and jumping by itself
  - Modifier: Frail - reduce max health
  - Modifier: Irrecovery - disable natural/food-based regeneration
  - Modifier: Mark - client side glowing for certain entities, with configurable color
  - Modifier: Relaxed - disable sprinting
  - Modifier: Grounded - disable jumping
  - Modifier: Intangible - disable collision with other players
- (Considered) Displaying custom markers on maps/editing map markers via hex
- (Considered) More nadirs for blindness, nausea, etc.
