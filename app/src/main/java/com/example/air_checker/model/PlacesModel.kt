package com.example.air_checker.model

data class Place(
  val name: String,
  val county: String,
  val voivodeship: String,
  val lat: Double,
  val lon: Double
)

val places = listOf(
  Place("Warszawa", "Warszawa", "mazowieckie", 52.2297, 21.0122),
  Place("Łódź", "Łódź", "łódzkie", 51.7592, 19.4560),
  Place("Kraków", "Kraków", "małopolskie", 50.0647, 19.9450),
  Place("Wrocław", "Wrocław", "dolnośląskie", 51.1079, 17.0385),
  Place("Poznań", "Poznań", "wielkopolskie", 52.4064, 16.9252),
  Place("Gdańsk", "Gdańsk", "pomorskie", 54.3520, 18.6466),
  Place("Szczecin", "Szczecin", "zachodniopomorskie", 53.4289, 14.5530),
  Place("Bydgoszcz", "Bydgoszcz", "kujawsko-pomorskie", 53.1235, 18.0084),
  Place("Lublin", "Lublin", "lubelskie", 51.2465, 22.5684),
  Place("Katowice", "Katowice", "śląskie", 50.2590, 19.0216),
  Place("Białystok", "Białystok", "podlaskie", 53.1325, 23.1688),
  Place("Gdynia", "Gdynia", "pomorskie", 54.5189, 18.5305),
  Place("Częstochowa", "Częstochowa", "śląskie", 50.8118, 19.1203),
  Place("Radom", "Radom", "mazowieckie", 51.4026, 21.1471),
  Place("Toruń", "Toruń", "kujawsko-pomorskie", 53.0138, 18.5984),
  Place("Kielce", "Kielce", "świętokrzyskie", 50.8661, 20.6286),
  Place("Rzeszów", "Rzeszów", "podkarpackie", 50.0413, 21.9990),
  Place("Zielona Góra", "Zielona Góra", "lubuskie", 51.9356, 15.5062),
  Place("Olsztyn", "Olsztyn", "warmińsko-mazurskie", 53.7784, 20.4801),
  Place("Opole", "Opole", "opolskie", 50.6751, 17.9213),
  Place("Koszalin", "Koszalin", "zachodniopomorskie", 54.1944, 16.1725),
  Place("Legnica", "Legnica", "dolnośląskie", 51.2070, 16.1557),
  Place("Elbląg", "Elbląg", "warmińsko-mazurskie", 54.1561, 19.4045),
  Place("Płock", "Płock", "mazowieckie", 52.5463, 19.7065),
  Place("Wałbrzych", "Wałbrzych", "dolnośląskie", 50.7714, 16.2843),
  Place("Kalisz", "Kalisz", "wielkopolskie", 51.7602, 18.0915),
  Place("Jelenia Góra", "Jelenia Góra", "dolnośląskie", 50.8992, 15.7280),
  Place("Siedlce", "Siedlce", "mazowieckie", 52.1621, 22.2719),
  Place("Nowy Sącz", "Nowy Sącz", "małopolskie", 49.6211, 20.6972),
  Place("Tarnów", "Tarnów", "małopolskie", 50.0124, 20.9858)
)
