# Walkthrough - Fix Unresolved Reference 'PermissionViewModel'

I have fixed the unresolved reference error in `PermissionScreen.kt` by adding the correct package declaration.

## Changes Made

### feature-connection

#### [PermissionScreen.kt](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/feature-connection/src/main/java/com/kkr/juken/feature/connection/presentation/permissions/PermissionScreen.kt)

- Added `package com.kkr.juken.feature.connection.presentation.permissions`.
- Removed the redundant import of `PermissionEffect`.

## Verification Results

### Automated Tests
- Ran `./gradlew :feature-connection:compileDebugKotlin`
- **Result**: Build finished successfully.
