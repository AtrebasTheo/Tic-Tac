# Contributing


Currently not accepting contributions. Please reach out to the maintainer if you are interested in contributing.
 So ignore the rest of this file for now.
## Development setup
1. Install JDK 21.
2. Clone the repository.
3. Run the core tests once to verify the environment:
   ```bash
   ./gradlew :core:test
   ```

## Pull requests
- Keep changes small and focused.
- Add or update tests for gameplay logic changes.
- Prefer deterministic logic and clear naming.
- Run the relevant Gradle tasks before submitting a PR.

## Coding notes
- Prefer package-private visibility for internal helpers when possible.
- Keep UI thread work in the LibGDX render loop and post background work to the app thread when needed.
