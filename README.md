# Tic Tac Toe

A LibGDX-based Tic Tac Toe game with desktop and Android targets.

## Features
- Classic tic-tac-toe gameplay
- Local 1-player and 2-player modes
- Configurable board/game settings
- UI styling with custom button textures and backgrounds
- Java unit tests for the core game model

## Project structure
- `core/` — shared game logic, screens, and UI logic
- `lwjgl3/` — desktop launcher
- `android/` — Android launcher
- `assets/` — textures, UI files, fonts, background assets

## Prerequisites
- JDK 21
- Gradle wrapper included in the repo

## Run locally

Desktop:
```bash
./gradlew :lwjgl3:run
```

Core tests:
```bash
./gradlew :core:test
```

Build the full project:
```bash
./gradlew clean build
```

## CI
This repository includes a GitHub Actions workflow at `.github/workflows/ci.yml` that runs build + tests on push and pull requests.

## Contributing
Please keep changes focused, add or update tests when changing game logic, and run the core test suite before opening a PR.

## License
See `LICENSE` for project licensing information.
