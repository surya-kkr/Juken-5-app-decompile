# Fix ECU Connection and Synchronization Flow

The user reports that selecting a device navigates to the dashboard but doesn't show connection signs or actually connect. This is due to:
1.  **Race condition in `DelegatingTransport`**: `setTransport` is non-blocking, so `connect` starts using the old transport before the update propagates.
2.  **Missing Synchronization**: `connect` only establishes the physical link; it doesn't trigger the ECU handshake or data sync required to populate the UI.
3.  **Disconnected Progress Reporting**: `EcuRepositoryImpl` only observes the low-level `ConnectionSession` state, ignoring the `SyncEngine` progress.
4.  **Missing Live Streaming**: `LiveSessionManager` is never started after connection.

## Proposed Changes

### core-domain

#### [MODIFY] [StateManagers.kt](file:///E:/Project App Juken/Juken 5 Mapping APP/Juken5Mapping/core-domain/src/main/java/com/kkr/juken/domain/state/StateManagers.kt)
- Change `setTransport`, `updateTheme`, and `setDeveloperMode` to `suspend` functions.

### core-state

#### [MODIFY] [SettingsStateManagerImpl.kt](file:///E:/Project App Juken/Juken 5 Mapping APP/Juken5Mapping/core-state/src/main/kotlin/com/kkr/juken/core/state/internal/SettingsStateManagerImpl.kt)
- Implement `suspend` versions of the setter functions using `dataStore.edit` directly.

### core-sync

#### [MODIFY] [SyncEngine.kt](file:///E:/Project App Juken/Juken 5 Mapping APP/Juken5Mapping/core-sync/src/main/kotlin/com/kkr/juken/core/sync/SyncEngine.kt)
- Change `SyncEngine` constructor to accept a `LiveSessionManager` to start streaming automatically after sync.
- Or better, expose a completion signal.

### core-data

#### [MODIFY] [EcuRepositoryImpl.kt](file:///E:/Project App Juken/Juken 5 Mapping APP/Juken5Mapping/core-data/src/main/kotlin/com/kkr/juken/core/data/repository/EcuRepositoryImpl.kt)
- Inject `SyncEngine`.
- Update `connectionState` to combine both `session.state` and `syncEngine.progress`.
- Update `connect()` to call `syncEngine.start(address)`.

### feature-connection

#### [MODIFY] [ScannerViewModel.kt](file:///E:/Project App Juken/Juken 5 Mapping APP/Juken5Mapping/feature-connection/src/main/java/com/kkr/juken/feature/connection/presentation/scanner/ScannerViewModel.kt)
- Update `onDeviceSelected` to await `setTransport` before calling `connect`.

## Verification Plan

### Automated Tests
- Build project to verify Dagger/Hilt and API changes.

### Manual Verification
- Select a simulator or bluetooth device.
- Verify that the `SyncOverlay` appears with "ESTABLISHING LINK" followed by "SYNCHRONIZING ECU" and progress updates.
- Verify that once sync hits 100%, the overlay disappears and gauges start showing live data.
