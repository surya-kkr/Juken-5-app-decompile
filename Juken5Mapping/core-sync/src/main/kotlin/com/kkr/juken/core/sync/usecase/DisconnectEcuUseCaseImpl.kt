package com.kkr.juken.core.sync.usecase

import com.kkr.juken.domain.repository.EcuRepository
import com.kkr.juken.domain.usecase.DisconnectEcuUseCase
import javax.inject.Inject

internal class DisconnectEcuUseCaseImpl @Inject constructor(
    private val repository: EcuRepository
) : DisconnectEcuUseCase {
    override suspend fun invoke() = repository.disconnect()
}
