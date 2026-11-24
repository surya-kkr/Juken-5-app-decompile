# Task: Bluetooth Permission & Connection Hardening

## Domain & State Modeling
- [ ] Update `SessionStatus` and `SessionState` in `core-domain`
- [ ] Update `BluetoothInterfaces.kt` in `core-transport`
- [ ] Define `BluetoothPermissionState` in `core-transport`

## Core Implementation
- [ ] Implement `AndroidBluetoothPermissionManager` in `core-transport-bluetooth`
- [ ] Implement `BluetoothStateMonitor` in `core-transport-bluetooth`
- [ ] Update `AndroidBluetoothScanner` with Discovery Guard
- [ ] Update `BluetoothTransport` with Connection Guard and resource safety

## UI & Feature Integration
- [ ] Update `PermissionViewModel` in `feature-connection`
- [ ] Update `PermissionScreen` in `feature-connection`

## Manifest & Audit
- [ ] Update `AndroidManifest.xml` in `app`
- [ ] Audit all Bluetooth API calls for protection

## Verification
- [ ] Run `BluetoothPermissionManagerTest`
- [ ] Run `DiscoveryGuardTest`
- [ ] Run `ConnectionGuardTest`
- [ ] Manual verification on various API levels
