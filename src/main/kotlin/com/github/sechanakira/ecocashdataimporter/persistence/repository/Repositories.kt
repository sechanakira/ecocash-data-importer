package com.github.sechanakira.ecocashdataimporter.persistence.repository

import com.github.sechanakira.ecocashdataimporter.persistence.entity.CustomerData
import com.github.sechanakira.ecocashdataimporter.persistence.entity.ImportStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerDataRepository : JpaRepository<CustomerData, Long>

interface ImportStatusRepository : JpaRepository<ImportStatus, Long>