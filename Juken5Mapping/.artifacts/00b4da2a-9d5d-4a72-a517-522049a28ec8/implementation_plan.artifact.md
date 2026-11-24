# Implementation Plan - Bluetooth Permission & Connection Hardening

This plan outlines the complete overhaul of the Bluetooth permission and connection architecture to eliminate `SecurityException` crashes and ensure production-grade reliability across Android 10-15.

## User Review Required

> [!IMPORTANT]
> This architecture enforces a strict **"Check-Validate-Execute"** pattern. No Bluetooth API (Discovery, Connect, Scan) will be invoked without explicit permission and hardware validation.

> [!CAUTION]
> `SessionState` will be significantly expanded to include granular connection statuses. UI components must be updated to handle new states like `PermissionPermanentlyDenied` and `BluetoothDisabled`.

## Proposed Changes

### 1. Domain & State Modeling (`core-domain`, `core-transport`)

#### [MODIFY] [SessionState.kt](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/core-domain/src/main/java/com/kkr/juken/domain/model/state/SessionState.kt)
- Add `status: SessionStatus`.
- Define `SessionStatus` sealed interface:
  - `Idle`, `CheckingPermissions`, `PermissionRequired`, `PermissionDenied`, `PermissionPermanentlyDenied`, `NoBluetoothAdapter`, `BluetoothDisabled`, `Ready`, `Scanning`, `Connecting`, `Connected`, `Disconnected`, `Failed`.

#### [MODIFY] [BluetoothInterfaces.kt](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/core-transport/src/main/kotlin/com/kkr/juken/core/transport/BluetoothInterfaces.kt)
- Rename `BluetoothPermissionHandler` to `BluetoothPermissionManager`.
- Add comprehensive methods: `hasAllPermissions()`, `shouldShowRationale()`, `openAppSettings()`, `checkPermissions()`.
- Add `BluetoothStateMonitor` interface to observe adapter/hardware changes.

---

### 2. Implementation Layer (`core-transport-bluetooth`)

#### [NEW] [AndroidBluetoothPermissionManager.kt](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/core-transport-bluetooth/src/main/kotlin/com/kkr/juken/core/transport/bluetooth/AndroidBluetoothPermissionManager.kt)
- Handle Android 10-15 logic.
- Support `maxSdkVersion` requirements.
- Manage `neverForLocation` flag logic.
- Provide rationale detection and app settings navigation.

#### [NEW] [BluetoothStateMonitor.kt](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/core-transport-bluetooth/src/main/kotlin/com/kkr/juken/core/transport/bluetooth/BluetoothStateMonitor.kt)
- Observe `BluetoothAdapter.ACTION_STATE_CHANGED`.
- Detect Airplane mode and permission revocation.
- Automatically trigger `SessionState` updates via `SessionStateManager`.

#### [MODIFY] [AndroidBluetoothScanner.kt](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/core-transport-bluetooth/src/main/kotlin/com/kkr/juken/core/transport/bluetooth/AndroidBluetoothScanner.kt)
- **Discovery Guard**:
  - Verify permissions and adapter state before `startDiscovery()`.
  - Ensure `cancelDiscovery()` completes before starting a new one.
  - Wrap all calls in `MissingPermission` suppression only *after* manual checks.

#### [MODIFY] [BluetoothTransport.kt](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/core-transport-bluetooth/src/main/kotlin/com/kkr/juken/core/transport/bluetooth/BluetoothTransport.kt)
- **Connection Guard**:
  - Permission validation before socket creation.
  - Automatic resource cleanup using `use {}` for sockets and streams.
  - Timeout management and discovery state verification.

---

### 3. UI & Feature Layer (`feature-connection`)

#### [MODIFY] [PermissionViewModel.kt](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/feature-connection/src/main/java/com/kkr/juken/feature/connection/presentation/permissions/PermissionViewModel.kt)
- Delegate all logic to `BluetoothPermissionManager`.
- Expose granular effects for navigation to settings.

#### [MODIFY] [PermissionScreen.kt](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/feature-connection/src/main/java/com/kkr/juken/feature/connection/presentation/permissions/PermissionScreen.kt)
- Use `rememberLauncherForActivityResult` with `RequestMultiplePermissions`.
- Implement distinct UI states for Denied vs Permanently Denied.

---

### 4. Manifest & Audit

#### [MODIFY] [AndroidManifest.xml](file:///E:/Project%20App%20Juken/Juken%205%20Mapping%20APP/Juken5Mapping/app/src/main/AndroidManifest.xml)
- Add version-specific permissions with `maxSdkVersion`.
- Configure `neverForLocation` for `BLUETOOTH_SCAN`.

#### [Bluetooth API Audit]
- Search and protect all occurrences of:
  - `startDiscovery`, `connectGatt`, `createBond`, `BluetoothSocket`, `BluetoothAdapter`, `BluetoothManager`.

## Verification Plan

### Automated Tests
- `BluetoothPermissionManagerTest`: API level branch testing.
- `BluetoothStateMonitorTest`: Broadcast receiver verification.
- `DiscoveryGuardTest`: Concurrent discovery prevention.
- `ConnectionGuardTest`: Permission-gated connection logic.

### Manual Verification
- **Android 10-15**: Verify correct permission dialogs per version.
- **Permission Cycles**: Allow -> Revoke -> Re-request.
- **Hardware Cycles**: Disable Bluetooth during active scan/connection.
- **Settings Path**: Verify navigation to App Settings when permanently denied.
