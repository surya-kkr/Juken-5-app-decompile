# Fix Configuration Cache Error in Gradle 9.5.0

The project is currently experiencing a Configuration Cache error due to an incompatibility between the Kotlin Gradle Plugin (1.9.22) and Gradle 9.5.0. Specifically, the error "Unsupported provider is registered as a task completion listener" indicates that the plugin is using legacy Gradle APIs that are no longer supported in the configuration cache for Gradle 9.x.

## Proposed Changes

To resolve this, I will upgrade the Kotlin and Android Gradle Plugins to versions that are compatible with Gradle 9.5.0 and support the updated configuration cache requirements.

### 1. Build Configuration & Dependencies

#### [MODIFY] [libs.versions.toml](file:///E:/Project App Juken/Juken 5 Mapping APP/Juken5Mapping/gradle/libs.versions.toml)
- Upgrade `agp` from `8.5.0` to `9.1.0`.
- Upgrade `kotlin` from `1.9.22` to `2.1.0`.
- Upgrade `ksp` from `1.9.22-1.0.17` to `2.1.0-1.0.29`.
- Add `kotlin-compose` plugin: `{ id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }`.

#### [MODIFY] [build.gradle.kts](file:///E:/Project App Juken/Juken 5 Mapping APP/Juken5Mapping/build.gradle.kts) (Root)
- Update plugin versions to match `libs.versions.toml`.
- Add `id(libs.plugins.kotlin.compose.get().pluginId) version libs.versions.kotlin.get() apply false`.

### 2. Module Updates

For all Android modules (app and libraries):
#### [MODIFY] All `build.gradle.kts` files
- Update `JavaVersion` from `VERSION_11` to `VERSION_17`.
- Update `jvmTarget` from `"11"` to `"17"`.
- Apply `alias(libs.plugins.kotlin.compose)` for modules using Compose.
- Remove the `composeOptions` block (deprecated in Kotlin 2.0+).

For JVM-only modules (e.g., `:core-common`):
#### [MODIFY] `build.gradle.kts`
- Update `JavaVersion` to `VERSION_17`.
- Update `jvmToolchain(11)` to `jvmToolchain(17)`.

## User Review Required

> [!IMPORTANT]
> **Kotlin 2.1.0 Upgrade**: This upgrade enables the K2 compiler by default. While generally compatible, it may surface new compiler warnings or errors in edge cases.
> **JDK 17 Requirement**: Gradle 9.5 and AGP 9.1 require JDK 17 or higher to run the build. Please ensure your IDE and environment are configured to use JDK 17+.
> **Compose Compiler Plugin**: With Kotlin 2.0+, the Compose compiler is now a standard Kotlin plugin. I will be removing the old `kotlinCompilerExtensionVersion` configuration and applying the new plugin.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify the build completes successfully with configuration cache enabled.
- Run `./gradlew help --configuration-cache` to verify that the configuration cache can be stored without errors.

### Manual Verification
- Verify that the app still renders Compose UI correctly after the compiler plugin transition.
