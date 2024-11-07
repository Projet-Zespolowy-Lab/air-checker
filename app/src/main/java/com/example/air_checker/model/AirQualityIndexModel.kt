package com.example.air_checker.model


import com.google.gson.annotations.SerializedName

data class AirQualityIndex(
  @SerializedName("Identyfikator stacji pomiarowej")
  val stationId: Int,

  @SerializedName("Data wykonania obliczeń indeksu")
  val calculationDate: String,

  @SerializedName("Wartość indeksu")
  val indexValue: Int?,

  @SerializedName("Nazwa kategorii indeksu")
  val indexCategoryName: String?,

  @SerializedName("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika st")
  val sourceDataDate: String?,

  // Dane dla wskaźnika SO2
  @SerializedName("Data wykonania obliczeń indeksu dla wskaźnika SO2")
  val so2CalculationDate: String?,

  @SerializedName("Wartość indeksu dla wskaźnika SO2")
  val so2IndexValue: Int?,

  @SerializedName("Nazwa kategorii indeksu dla wskażnika SO2")
  val so2IndexCategoryName: String?,

  @SerializedName("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika SO2")
  val so2SourceDataDate: String?,

  // Dane dla wskaźnika NO2
  @SerializedName("Data wykonania obliczeń indeksu dla wskaźnika NO2")
  val no2CalculationDate: String?,

  @SerializedName("Wartość indeksu dla wskaźnika NO2")
  val no2IndexValue: Int?,

  @SerializedName("Nazwa kategorii indeksu dla wskażnika NO2")
  val no2IndexCategoryName: String?,

  @SerializedName("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika NO2")
  val no2SourceDataDate: String?,

  // Dane dla wskaźnika PM10
  @SerializedName("Data wykonania obliczeń indeksu dla wskaźnika PM10")
  val pm10CalculationDate: String?,

  @SerializedName("Wartość indeksu dla wskaźnika PM10")
  val pm10IndexValue: Int?,

  @SerializedName("Nazwa kategorii indeksu dla wskażnika PM10")
  val pm10IndexCategoryName: String?,

  @SerializedName("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika PM10")
  val pm10SourceDataDate: String?,

  // Dane dla wskaźnika PM2.5
  @SerializedName("Data wykonania obliczeń indeksu dla wskaźnika PM2.5")
  val pm25CalculationDate: String?,

  @SerializedName("Wartość indeksu dla wskaźnika PM2.5")
  val pm25IndexValue: Int?,

  @SerializedName("Nazwa kategorii indeksu dla wskażnika PM2.5")
  val pm25IndexCategoryName: String?,

  @SerializedName("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika PM2.5")
  val pm25SourceDataDate: String?,

  // Dane dla wskaźnika O3
  @SerializedName("Data wykonania obliczeń indeksu dla wskaźnika O3")
  val o3CalculationDate: String?,

  @SerializedName("Wartość indeksu dla wskaźnika O3")
  val o3IndexValue: Int?,

  @SerializedName("Nazwa kategorii indeksu dla wskażnika O3")
  val o3IndexCategoryName: String?,

  @SerializedName("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika O3")
  val o3SourceDataDate: String?,

  // Dodatkowe informacje
  @SerializedName("Status indeksu ogólnego dla stacji pomiarowej")
  val generalIndexStatus: Boolean,

  @SerializedName("Kod zanieczyszczenia krytycznego")
  val criticalPollutantCode: String?
) {
  // Funkcja członkowska konwertująca AirQualityIndex na AirQualityCategories
  fun toAirQualityCategories(): AirQualityCategories {
    val categories = mutableListOf<AirQualityCategory>()

    // Dodajemy krajowy indeks jakości powietrza
    indexCategoryName?.let {
      categories.add(AirQualityCategory("Krajowy indeks jakości powietrza", it))
    }

    // Dodajemy indeks SO2
    categories.add(AirQualityCategory("SO2", so2IndexCategoryName))

    // Dodajemy indeks NO2
    categories.add(AirQualityCategory("NO2", no2IndexCategoryName))

    // Dodajemy indeks PM10
    categories.add(AirQualityCategory("PM10", pm10IndexCategoryName))

    // Dodajemy indeks PM2.5
    categories.add(AirQualityCategory("PM2.5", pm25IndexCategoryName))

    // Dodajemy indeks O3
    categories.add(AirQualityCategory("O3", o3IndexCategoryName))

    // Zwracamy jako obiekt AirQualityCategories
    return AirQualityCategories(categories)
  }
}







