package com.github.sechanakira.ecocashdataimporter.persistence.service

import com.github.sechanakira.ecocashdataimporter.persistence.entity.CustomerData
import com.github.sechanakira.ecocashdataimporter.persistence.entity.ImportStatus
import com.github.sechanakira.ecocashdataimporter.persistence.repository.CustomerDataRepository
import com.github.sechanakira.ecocashdataimporter.persistence.repository.ImportStatusRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.logging.Logger


@Service
class DataImportService(
    @Value("\${datafile.path}") private val dataFilePath: String,
    private val customerDataRepository: CustomerDataRepository,
    private val importStatusRepository: ImportStatusRepository
) {

    var logger: Logger = Logger.getLogger(this::class.java.name)

    private val path: Path = Paths.get(dataFilePath)

    companion object {
        const val HEADER_LINE_TO_SKIP: Long = 1
        const val DATE_INDEX = 0

        const val MSISDN_INDEX = 0
        const val COUNTRY_OF_ORIGIN_INDEX = 1
        const val DOB_INDEX = 2
        const val DOD_INDEX = 3
        const val FIRST_NAME_INDEX = 4
        const val LAST_NAME_INDEX = 5
        const val ISSUING_COUNTRY_INDEX = 6
        const val REGISTRAR_STATUS_INDEX = 7
        const val STATUS_INDEX = 8
        const val POSTAL_ADDRESS_INDEX = 9
        const val PHYSICAL_ADDRESS_INDEX = 10
        const val TITLE_INDEX = 11
        const val ID_NUMBER_INDEX = 12
        const val COMPANY_NAME_INDEX = 13
        const val GENDER_INDEX = 14
        const val OCCUPATION_INDEX = 15
    }

    fun importData() {
        val startTime = LocalDateTime.now()

        Files.lines(path)
            .skip(HEADER_LINE_TO_SKIP)
            .forEach { line ->
                try {
                    customerDataRepository.saveAndFlush(parseLine(line.replace("\"", "")))
                } catch (e: Exception) {
                    logger.info("Encountered exception ${e.message} while parsing line: $line")
                }

            }

        val endTime = LocalDateTime.now()

        val importStatus = ImportStatus(null, startTime, endTime)
        importStatusRepository.save(importStatus)
    }

    fun parseLine(line: String): CustomerData {
        logger.info("Parsing line: $line")

        val splitLine = line.split("|")
        val msisdn = splitLine[MSISDN_INDEX]
        val countryOfOrigin = splitLine[COUNTRY_OF_ORIGIN_INDEX]

        var dateOfBirth: LocalDate? = null
        if (!splitLine[DOB_INDEX]?.isNullOrBlank()) {
            dateOfBirth = LocalDate.parse(splitLine[DOB_INDEX].split(" ")[DATE_INDEX])
        }

        var dateOfDeath: LocalDate? = null
        if (!splitLine[DOD_INDEX]?.isNullOrBlank()) {
            try {
                dateOfDeath = LocalDate.parse(splitLine[DOD_INDEX].split(" ")[DATE_INDEX])
            } catch (ex: Exception) {
                logger.info("Encountered exception parsing date of death from ${splitLine[DOD_INDEX]} ")
            }

        }

        val firstName = splitLine[FIRST_NAME_INDEX]
        val lastName = splitLine[LAST_NAME_INDEX]
        val issuingCountry = splitLine[ISSUING_COUNTRY_INDEX]
        val registrarStatus = splitLine[REGISTRAR_STATUS_INDEX]
        val status = splitLine[STATUS_INDEX]
        val postalAddress = splitLine[POSTAL_ADDRESS_INDEX]
        val physicalAddress = splitLine[PHYSICAL_ADDRESS_INDEX]

        var title: String? = null
        var idNumber: String? = null
        var companyName: String? = null
        var gender: String? = null
        var occupation: String? = null

        if (splitLine.size >= TITLE_INDEX + 1 && !splitLine[TITLE_INDEX]?.isNullOrBlank()) {
            title = splitLine[TITLE_INDEX]
        }

        if (splitLine.size >= ID_NUMBER_INDEX + 1 && !splitLine[ID_NUMBER_INDEX]?.isNullOrBlank()) {
            idNumber = splitLine[ID_NUMBER_INDEX]
        }

        if (splitLine.size >= COMPANY_NAME_INDEX + 1 && !splitLine[COMPANY_NAME_INDEX]?.isNullOrBlank()) {
            companyName = splitLine[COMPANY_NAME_INDEX]
        }

        if (splitLine.size >= GENDER_INDEX + 1 && !splitLine[GENDER_INDEX]?.isNullOrBlank()) {
            gender = splitLine[GENDER_INDEX]
        }

        if (splitLine.size >= OCCUPATION_INDEX + 1 && !splitLine[OCCUPATION_INDEX]?.isNullOrBlank()) {
            occupation = splitLine[OCCUPATION_INDEX]
        }

        return CustomerData(
            null,
            msisdn,
            countryOfOrigin,
            dateOfBirth,
            dateOfDeath,
            firstName,
            lastName,
            issuingCountry,
            registrarStatus,
            status,
            postalAddress,
            physicalAddress,
            title,
            idNumber,
            companyName,
            gender,
            occupation
        )
    }
}