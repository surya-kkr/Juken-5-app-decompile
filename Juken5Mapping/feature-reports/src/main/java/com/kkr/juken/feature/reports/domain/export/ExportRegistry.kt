package com.kkr.juken.feature.reports.domain.export

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExportRegistry @Inject constructor() {
    
    enum class Format { PDF, CSV, JSON, BINARY_WORKSPACE, SESSION_BUNDLE }

    fun getExporter(format: Format): Any? {
        return when (format) {
            Format.CSV -> CsvExporter()
            Format.JSON -> JsonExporter()
            else -> null
        }
    }
}
