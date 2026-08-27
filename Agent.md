# Tic Tac Toe project briefing for agents

## Project overview
This repository is a libGDX-based Tic-Tac-Toe game 
modules:
- `core`: shared gameplay/UI code and assets logic
- `lwjgl3`: desktop launcher
- `android`: Android launcher
- `assets`: static art/fonts

## Entry points and runtime flow
### Core app bootstrap
- `core/src/main/java/Tic_Tac_Atrebas/jav/Main.java`
- `Main` extends `com.badlogic.gdx.Game`, which is the central app lifecycle controller.
- `create()` sets up:
  - `SpriteBatch`
  - `StretchViewport`
  - `AdvancedAssetManager`
  - `GameConfiguration`
  - background gradient texture
  - asset loading through `loadAssets()`
- `render()` is the main game loop hook. It does:
  1. check `Main.State` 
  2. when in `LOAD`, waits until `assetManager.update(5)` completes;
  3. then creates remaining UI assets and a `StartScreen`
  4. calls `setScreen(startScreen)`
  5. clears the buffer and draws the background
  6. calls `super.render()` so the current `Screen` can render its stage/UI



## Game loop and turn flow
High-level sequence:
1. `Main.create()` -> loads assets -> `StartScreen`
2. User chooses mode in `StartScreen`
3. `Main` switches to `new PlayScreen(game)`
4. `PlayScreen.initializeTable()` creates a ClassicFieldTable
5. Loop continues until `gameOver` is true:
   1. Input listener captures clicks in `show()`
   2. `playTable.makeMove(...)` validates and applies move
   3. `onCompletedMove()` updates player icon and checks win/draw
   4. If AI is enabled and it is AI's turn, `checkAI()` spawns a background thread
   5. After the AI selects a move, it is applied via `Gdx.app.postRunnable(...)`

### Screen lifecycle
uses libGDX `Screen` implementations:
- `StartScreen`
- `PlayScreen`
switched via `game.setScreen(...)`

## Main classes and responsibilities

### `StartScreen`
- Initial menu screen
- Creates buttons for:
  - `1 Player` (AI enabled)
  - `2 Players`
  - `3 Players` (currently unimplemented)
  - `Settings`
- Stores `GameConfiguration` values when starting a game
- Displays settings overlay with controls

### `PlayScreen`
- Active board screen for gameplay
- Builds the board via `initializeTable()`
- Installs input handlers with `InputMultiplexer`
- Handles user clicks and AI turn checks
- Updates the status icon showing whose turn it is
- Shows settings overlay/menu after a win or on demand
- Uses `Gdx.app.postRunnable(...)` to safely apply async AI decisions back on the libGDX thread

### `FieldTable` (abstract)
- Base class for board logic, extends `Actor`
- some Fields:
  - `fieldWidth`
  - `currentPlayer`
  - `drawer`
- Abstract methods as board API
- Concrete implementations for move validation, reset, win checks, hover behavior

### ClassicFieldTable
- Concrete board implementation for Tic-Tac-Toe
- Stores the grid as `int[][] fields`
- `makeMoveAtField(int x, int y)`
- player switching when game continues
- `checkWin(...)` counts contiguous markers in 4 directions: horizontal, vertical, diagonal down-right, diagonal up-right
- `draw(...)` delegates to `TableDrawer` for actual rendering

### `TableDrawer` / `PaperTableDrawer`
- `TableDrawer` is an abstract renderer for board visuals
- `PaperTableDrawer` creates textures for X and O and draws the grid and hover highlight
- Uses `ShapeRenderer` for lines and highlights and `SpriteBatch` for the symbols
- Exposes `xTexture` and `oTexture` used by `PlayScreen` for player icons

### `GameConfiguration`
- Runtime configuration holder for current match options

### `AdvancedAssetManager`
- Loads and creates fonts and button styles used by menus and controls
- `setImageButtonStyleTexture(...)` swaps button textures by color preset
- `createRemainingAssets()` after the `AssetManager` has loaded core assets

### `PixmapLibrary`
- Utility methods for generating textures and gradients used to create UI visuals
- Includes gradient generation, rounded squares, tunnel-style maps, color overlays

### `ClassicAI`
- AI implementation for the classic board
- `calculateBestMove()` chooses a move using:
  1. center opening move if board is empty
  2. immediate win/block detection (`checkImmediateWinOrBlock()`)
  3. minimax search with alpha-beta pruning
  4. fallback random valid move
- Runs in a background thread in `PlayScreen.checkAI()` and posts the result back to the main libGDX thread

## Important interfaces and contracts
- `com.badlogic.gdx.Screen` is the main interface for app screens
- `com.badlogic.gdx.Game` is the app-level controller
- `GameModel` (abstract) is the core, libGDX-free representation of game state and rules. With API for UI and AI
  - `ClassicGameModel` extends `GameModel` and implements the methods.
- `FieldTable` is the UI adapter and should delegate gameplay operations to a `GameModel` instance rather than embedding pure game logic.
- `AIPlayer` defines the AI contract; `ClassicAI` is the concrete implementation and should operate on `GameModel`-provided snapshots (via `getAiBoard()`) so it can run off the libGDX thread safely.


## Launch targets
### Desktop
- `lwjgl3/src/main/java/Tic_Tac_Atrebas/jav/lwjgl3/DesktopLauncher.java`
- Starts `Lwjgl3Application` with a `Main` instance

### Android
- `android/src/main/java/Tic_Tac_Atrebas/jav/android/AndroidLauncher.java`
- Initializes the libGDX app via `AndroidApplication`

## Build and validation commands
From the repo root:
- `chmod +x gradlew`
- `./gradlew --no-daemon :core:compileJava :lwjgl3:compileJava`
- `./gradlew --no-daemon :core:test :lwjgl3:run`

Note: Unit tests were updated — ClassicGameModel unit tests now target the refactored, libGDX-free model. Run `./gradlew :core:test` to execute them headlessly. If tests fail, re-run with `--info` for more details and ensure no libGDX dependencies are present in model-only tests.


## Practical guidance for agents
- Prefer using `Gdx.app.postRunnable(...)` when a background thread needs to touch UI or libGDX state.
- `SettingsConfiguration` is not deeply integrated yet; do not assume it is the runtime state.
- Avoid changing `build/` output or generated files directly.
- The codebase has partial/incomplete features and several rough spots; prefer surgical changes and preserve the current design patterns.

## Notable code-quality caveats
- Some transitions and settings logic are duplicated or partially commented out.
- `PlayScreen.showWinMenu()` currently triggers `showSettingsMenu()` directly after the win routine, so win UI is not fully isolated yet.
- `StartScreen` marks "1 Player" as AI mode and uses the `Classic` game mode.
- `GameMode` is currently only `Classic`; expansion would require adding new board types or variants.
- Several methods/fields are package-private, which is acceptable inside the same package but can be confusing when widening scope.
