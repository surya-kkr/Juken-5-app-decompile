# Fix Unresolved Reference 'PermissionViewModel'

The error `Unresolved reference 'PermissionViewModel'` in `PermissionScreen.kt` is caused by a missing package declaration in that file. Although it resides in the same directory as `PermissionViewModel.kt`, without a package declaration, it's considered to be in the default package, preventing it from automatically resolving classes in `com.kkr.juken.feature.connection.presentation.permissions`.

## Proposed Changes

### feature-connection

#### [MODIFY] [PermissionScreen.kt](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/feature-connection/src/main/java/com/kkr/juken/feature/connection/presentation/permissions/PermissionScreen.kt)
- Add `package com.kkr.juken.feature.connection.presentation.permissions` at the top of the file.
- Remove redundant import `com.kkr.juken.feature.connection.presentation.permissions.PermissionEffect`.

## Verification Plan

### Automated Tests
- Run `./gradlew :feature-connection:compileDebugKotlin` to verify the build error is resolved.
