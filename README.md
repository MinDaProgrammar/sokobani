# Sokobani — Sokoban in Java & JavaFX

A tile-based Sokoban puzzle game built step by step over the **Object-Oriented Programming** course (BSc Data Science & AI, Maastricht University, 2025). Push every box onto a target to win.

## Controls

| Key | Action |
|-----|--------|
| `W` `A` `S` `D` / arrow keys | Move |

## Design

The code is organised around a class hierarchy and an event system, so the model never talks to the UI directly:

- **`GameObject`** (abstract) → `Wall`, `Floor`, `Target`, `Box`, `Player`; movable objects implement **`Moveable`**
- **`GameMap`** owns the level state: parses the level file, validates it, handles moves and pushes, checks the win condition and autosaves the board after every move
- **Observer pattern**: `GameMap` emits `GameEvent`s (`MOVE`, `PUSH`, `INVALID_MOVE`, `RESTART`, `WIN`) to registered `GameEventListener`s; `SokobaniApp` listens and re-renders
- **`InputHandler`** turns key presses into game intents (UI → model)
- **Custom checked exception** `InvalidLevelFormatException` for malformed levels: unknown characters, uneven rows, no player or more than one player

## Level format

Levels are plain text files in `src/main/resources/`:

```
#######
#TB BT#
#     #
#B P  #
#   BT#
#######
```

`#` wall · `P` player · `B` box · `T` target · space = floor. Autosaves also use `*` (box on target) and `+` (player on target).

## Running

Requires Java 21 and Maven.

```bash
mvn clean javafx:run
```

## Known limitations

- Restart (`R`) is wired up as an event but not implemented yet
- Only one level is bundled; the autosave is written to `level1.txt` in the working directory

## Credits

Tile graphics from the [Sokoban pack by Kenney](https://kenney.nl/assets/sokoban) (CC0).
