# Task: Fix Configuration Cache Error in Gradle 9.5.0

- `[x]` Update `libs.versions.toml` with new plugin and library versions
- `[x]` Update root `build.gradle.kts` plugins
- `[x]` Update all Android module `build.gradle.kts` files
    - `[x]` Update Java/JVM target to 17
    - `[x]` Apply Kotlin Compose plugin
    - `[x]` Remove `composeOptions`
    - `[x]` Migrate `kotlinOptions` to `compilerOptions`
- `[x]` Update `core-common` (JVM module) to Java 17
- `[x]` Verify build and configuration cache
