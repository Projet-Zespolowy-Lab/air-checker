package com.example.air_checker.model


data class AirQualityIndex(
  val stationId: Int, // Identyfikator stacji pomiarowej
  val calculationDate: String, // Data wykonania obliczeń indeksu
  val indexValue: Int?, // Wartość indeksu
  val indexCategoryName: String?, // Nazwa kategorii indeksu
  val sourceDataDate: String?, // Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika

  // Indeks dla wskaźnika SO2
  val so2CalculationDate: String?,
  val so2IndexValue: Int?,
  val so2IndexCategoryName: String?,
  val so2SourceDataDate: String?,

  // Indeks dla wskaźnika NO2
  val no2CalculationDate: String?,
  val no2IndexValue: Int?,
  val no2IndexCategoryName: String?,
  val no2SourceDataDate: String?,

  // Indeks dla wskaźnika PM10
  val pm10CalculationDate: String?,
  val pm10IndexValue: Int?,
  val pm10IndexCategoryName: String?,
  val pm10SourceDataDate: String?,

  // Indeks dla wskaźnika PM2.5
  val pm25CalculationDate: String?,
  val pm25IndexValue: Int?,
  val pm25IndexCategoryName: String?,
  val pm25SourceDataDate: String?,

  // Indeks dla wskaźnika O3
  val o3CalculationDate: String?,
  val o3IndexValue: Int?,
  val o3IndexCategoryName: String?,
  val o3SourceDataDate: String?,

  val generalIndexStatus: Boolean, // Status indeksu ogólnego dla stacji pomiarowej
  val criticalPollutantCode: String? // Kod zanieczyszczenia krytycznego
)




