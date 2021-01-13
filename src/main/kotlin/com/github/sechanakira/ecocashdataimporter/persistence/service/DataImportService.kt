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


@Service
class DataImportService(
    @Value("datafile.path") val dataFilePath: String,
    val customerDataRepository: CustomerDataRepository,
    val importStatusRepository: ImportStatusRepository
) {

    val path: Path = Paths.get(dataFilePath)

    companion object {
        const val HEADER_LINE_NUMBER: Long = 0
        const val MSISDN_INDEX = 0
        const val DATE_INDEX = 1
        const val COUNTRY_OF_ORIGIN_INDEX = 2
        const val DOB_INDEX = 3
        const val DOD_INDEX = 4
        const val FIRST_NAME_INDEX = 5
        const val LAST_NAME_INDEX = 6
        const val ISSUING_COUNTRY_INDEX = 7
        const val REGISTRAR_STATUS_INDEX = 8
        const val STATUS_INDEX = 9
        const val POSTAL_ADDRESS_INDEX = 10
        const val PHYSICAL_ADDRESS_INDEX = 11
        const val TITLE_INDEX = 12
        const val ID_NUMBER_INDEX = 13
        const val COMPANY_NAME_INDEX = 14
        const val GENDER_INDEX = 15
        const val OCCUPATION_INDEX = 16
    }

    fun importData() {
        val startTime = LocalDateTime.now()

        val importedData = mutableListOf<CustomerData>()
        Files.lines(path)
            .skip(HEADER_LINE_NUMBER)
            .forEach { line ->
                importedData.add(parseLine(line))
            }

        customerDataRepository.saveAll(importedData)

        val endTime = LocalDateTime.now()

        val importStatus = ImportStatus(null, startTime, endTime)
        importStatusRepository.save(importStatus)
    }

    fun parseLine(line: String): CustomerData {
        val splitLine = line.split("|")
        val msisdn = splitLine[MSISDN_INDEX]
        val countryOfOrigin = splitLine[COUNTRY_OF_ORIGIN_INDEX]
        val dateOfBirth = LocalDate.parse(splitLine[DOB_INDEX].split(" ")[DATE_INDEX])
        val dateOfDeath = LocalDate.parse(splitLine[DOD_INDEX].split(" ")[DATE_INDEX])
        val firstName = splitLine[FIRST_NAME_INDEX]
        val lastName = splitLine[LAST_NAME_INDEX]
        val issuingCountry = splitLine[ISSUING_COUNTRY_INDEX]
        val registrarStatus = splitLine[REGISTRAR_STATUS_INDEX]
        val status = splitLine[STATUS_INDEX]
        val postalAddress = splitLine[POSTAL_ADDRESS_INDEX]
        val physicalAddress = splitLine[PHYSICAL_ADDRESS_INDEX]
        val title = splitLine[TITLE_INDEX]
        val idNumber = splitLine[ID_NUMBER_INDEX]
        val companyName = splitLine[COMPANY_NAME_INDEX]
        val gender = splitLine[GENDER_INDEX]
        val occupation = splitLine[OCCUPATION_INDEX]
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