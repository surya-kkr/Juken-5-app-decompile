package com.kkr.juken.core.sync.usecase

import com.kkr.juken.core.sync.SyncEngine
import com.kkr.juken.domain.model.cache.CachePolicy
import com.kkr.juken.domain.usecase.SynchronizeEcuUseCase
import javax.inject.Inject

internal class SynchronizeEcuUseCaseImpl @Inject constructor(
    private val syncEngine: SyncEngine
) : SynchronizeEcuUseCase {
    override suspend fun invoke(policy: CachePolicy) {
        // Sync engine currently takes address, but UseCase uses it for the whole process.
        // We'll simplify and use a default for now.
        syncEngine.start("")
    }
}
