package com.github.sechanakira.ecocashdataimporter.persistence.entity

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class CustomerData(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    val msisdn: String,
    val countryOfOrigin: String,
    val dateOfBirth: LocalDate?,
    val dateOfDeath: LocalDate?,
    val firstName: String,
    val lastName: String,
    val issuingCountry: String,
    val registrarStatus: String,
    val status: String,
    val postalAddress: String,
    val physicalAddress: String,
    val title: String?,
    val idNumber: String?,
    val companyName: String?,
    val gender: String?,
    val occupation: String?
)

@Entity
data class ImportStatus(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)