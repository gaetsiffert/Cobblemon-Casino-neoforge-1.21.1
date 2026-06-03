# Casino Rocket

Casino Rocket is a NeoForge 1.21.1 Cobblemon addon that adds casino-themed blocks, items, gacha machines, slot machines, blackjack, economy helpers, and casino worker villagers.

## Target Versions

- Minecraft: `1.21.1`
- NeoForge: `21.1.230`
- Java: `21`

## Required Runtime Mods

These mods are required for the project to run as currently configured.

| Mod | Why it is used | Link |
| --- | --- | --- |
| NeoForge | Loader and modding API. | https://neoforged.net/ |
| Cobblemon | Required by gameplay, Pokémon rewards, balls, held items, fossils, villager job blocks, and several configs. | https://modrinth.com/mod/cobblemon |
| Kotlin for Forge | Required transitively by Cobblemon/CobbleDollars style NeoForge packs and included in the Gradle runtime. | https://www.curseforge.com/minecraft/mc-mods/kotlin-for-forge |
| Cloth Config API | Used by AutoConfig/Cloth Config backed config screens and config serialization. | https://modrinth.com/mod/cloth-config |

## Optional Integrations

Casino Rocket contains optional shop/config references for these mods. They are not required to start the game.

| Mod | Current behavior without it | Link |
| --- | --- | --- |
| CobbleDollars | Casino Rocket can use item-based economy instead, but CobbleDollars merchant shops and money economy need this mod. | https://modrinth.com/mod/cobbledollars |
| CobbleCuisine | Snackmaster shop categories that only contain CobbleCuisine items are hidden automatically when missing. | https://modrinth.com/mod/cobblecuisine |
| TMCraft | TM Instructor shop categories are hidden automatically when missing. | https://modrinth.com/mod/tmcraft |

## External Mods Seen In Dev Logs

The local `run` instance may contain or load data from external mods that are not Casino Rocket dependencies. For example, Adorn-related recipe/loot-table errors can appear in logs if incompatible Adorn data is present in the dev environment.

| Mod | Status | Link |
| --- | --- | --- |
| Adorn | Not used directly by Casino Rocket. Treat related log errors as dev-environment noise unless a pack intentionally includes it. | https://modrinth.com/mod/adorn |

## Gachapon Defaults

The default gacha configs are intentionally valid without optional compat mods.

- Item gachapon defaults use `minecraft`, `cobblemon`, and `casinorocket` items only.
- Pokémon gachapon includes a non-empty `event` pool so event machines are usable by default.
- Plushies gachapon currently uses Casino Rocket Pokémon pin items as valid fallback rewards. The system is still named `plushies` so a pack/server can replace those entries later with real plushie items from Casino Rocket or another mod.

## Local Config Regeneration

AutoConfig does not overwrite existing generated configs. If defaults change, remove the matching files under:

```text
run/config/casinorocket/
```

Then run the client or server again to regenerate clean defaults.

Useful commands:

```powershell
.\gradlew.bat build --stacktrace
.\gradlew.bat runClient --stacktrace
.\gradlew.bat runData --stacktrace
.\run-test-server.bat
```

`run-test-server.bat` starts the NeoForge dev server with an interactive console. Use normal server commands directly in that terminal, for example `op PlayerName`, `deop PlayerName`, or `stop`.

The Gradle `runServer` task uses `run-server/` as its working directory so it can run at the same time as the dev client in `run/`.

IntelliJ also has a shared run configuration named `Server Test`. Launch it from the run configuration dropdown, then type server commands directly in the Run console.
