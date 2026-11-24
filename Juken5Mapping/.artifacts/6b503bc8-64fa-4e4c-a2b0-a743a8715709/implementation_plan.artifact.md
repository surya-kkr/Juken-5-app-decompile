# Implementation Plan - Fix Missing Dagger/Hilt Bindings

The project is failing to build due to missing Dagger/Hilt bindings for `ConnectionSession`, `SyncEngine`, and `LiveSessionManager`. These are core communication and synchronization components that were declared but not provided in any Hilt module.

## User Review Required

> [!IMPORTANT]
> I am proposing to add these bindings to `CoreModule` in the `:app` module. This module already handles the selection of `Transport` (Bluetooth vs Mock) and provides other core components like `ProtocolCodec` and the global `CoroutineScope`.

## Proposed Changes

### [app]

#### [MODIFY] [CoreModule.kt](file:///E:/Project App Juken/Juken 5 Mapping APP/Juken5Mapping/app/src/main/java/com/kkr/juken/app/di/CoreModule.kt)
- Add `@Provides` method for `ConnectionSession` using its factory method.
- Add `@Provides` method for `SyncEngine`.
- Add `@Provides` method for `LiveSessionManager`.
- Add necessary imports for `LiveSessionManager` and `SyncDataListener`.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:hiltJavaCompileDebug` to verify that Dagger code generation succeeds.
- Run existing tests in `:core-transport` and `:core-sync` to ensure no regressions in logic (though only DI configuration is changing).

### Manual Verification
- Deploy the app to a device/emulator and verify that connection and synchronization features are working as expected (this requires hardware or a mock setup).
