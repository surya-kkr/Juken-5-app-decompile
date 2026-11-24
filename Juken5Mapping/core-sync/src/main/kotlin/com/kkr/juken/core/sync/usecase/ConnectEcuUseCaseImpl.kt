package com.kkr.juken.core.sync.usecase

import com.kkr.juken.domain.repository.EcuRepository
import com.kkr.juken.domain.usecase.ConnectEcuUseCase
import javax.inject.Inject

internal class ConnectEcuUseCaseImpl @Inject constructor(
    private val repository: EcuRepository
) : ConnectEcuUseCase {
    override suspend fun invoke(address: String) = repository.connect(address)
}
