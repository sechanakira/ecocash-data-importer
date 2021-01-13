package com.github.sechanakira.ecocashdataimporter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EcocashDataImporterApplication

fun main(args: Array<String>) {
    runApplication<EcocashDataImporterApplication>(*args)
}
