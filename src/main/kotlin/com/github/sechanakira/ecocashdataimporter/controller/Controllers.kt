package com.github.sechanakira.ecocashdataimporter.controller

import com.github.sechanakira.ecocashdataimporter.persistence.service.DataImportService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/data-import")
class ImportController(val service: DataImportService) {

    @PostMapping
    fun importData() {
        service.importData()
    }
}