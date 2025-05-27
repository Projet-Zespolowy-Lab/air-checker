# Air Checker

# Spis Treści

1. [🖋️ Autorzy](#️-autorzy)
2. [📖 Opis projektu](#-opis-projektu)
3. [🛠 Wykorzystane technologie i narzędzia](#️wykorzystane-technologie-i-narzędzia)
4. [Spotkania projektowe](#spotkania-projektowe)

<hr>


## 🖋️ Autorzy
- Marek Gołąbek
- Bartosz Siedlecki
- Piotr Sobieraj
- Wiktor Szczepanik

## 📖 Opis projektu
Aplikacja mobilna informująca użytkownika o stanie czystości powietrza w jego otoczeniu. 
### Wyświetlanie pomiarów

```
Wyświetlanie następujących pomiarów jakości powietrza: 
- Krajowy Indeks Jakości Powietrza
- PM 10
- PM 2.5
- NO2
- SO2
- O3
```

### Wybór miejscowości, dla której chce się poznać pomiar

`Domyślnie pomiar jest odczytywany ze stacji pogodowej znajdującej się najbliżej odczytanej z GPS lokalizacji
`

`Wybór miejscowości z listy`

`Wyszukiwanie po początku nazwy miejscowości`

`Powrót do domyślnej lokalizacji (dane GPS)`

 


### Zarządzanie historią pomiarów
`Zapisywanie bieżącego pomiaru
`

`Wyświetlanie historii pomiarów
`

`Usuwanie pomiaru z historii
`

`Eksport historii pomiarów do pliku `
### Zrzuty ekranu

<img width="100" alt="image" src="https://github.com/user-attachments/assets/3e46c10a-77cf-4e6d-b44e-5bc8e7eb8500" />
<img width="100" alt="image" src="https://github.com/user-attachments/assets/b02831df-f842-45f0-9747-885a4bd6dcdf" />
<img width="100" alt="image" src="https://github.com/user-attachments/assets/5c54c0ad-9d3c-42e8-bfff-d76deb1303af" />
<img width="100" alt="image" src="https://github.com/user-attachments/assets/127f850c-c366-48b2-a3d5-1122a513554f" />
<img width="100" alt="image" src="https://github.com/user-attachments/assets/2f076e5b-314e-4535-9745-c82ae6a4fd74" />
<img width="100" alt="image" src="https://github.com/user-attachments/assets/d863a99f-8202-4a0a-bc6f-0a8cf2941e07" />
<img width="100" alt="image" src="https://github.com/user-attachments/assets/67f9a7a3-0d3a-42f0-b5ef-e6a91d1d2286" />
<img width="100" alt="image" src="https://github.com/user-attachments/assets/ec1f285c-4df6-46e1-a482-7e37ed70e1e5" />
<br>

### Eksport do pliku <br>
<img width="300" alt="image" src="https://github.com/user-attachments/assets/eda06113-18fa-4574-bf5f-e13fc049cee5" />




 ## 🛠️Wykorzystane technologie i narzędzia
- Android Studio
- Kotlin

### Pozostałe
- GitHub
- Figma
- API Głównego Inspektoratu Ochrony Środowiska (https://powietrze.gios.gov.pl/pjp/content/api)
- ~~Replit~~ - używany do mockowania testów
- Miro
- Python do obróbki danych miejscowości
- [Dane miejscowości](https://github.com/jjbartek/polskie-miejscowosci?tab=CC0-1.0-1-ov-file)


<br>

## Spotkania projektowe

### Czwartek 9 stycznia 19:00
- Nowe tła
- Dynamiczne tło dzień / noc
- Kilka poprawek
- Wydanie wersji 1.8.0


### Sobota 4 stycznia 13:00
- Eksport historii pomiarów do pliku


### Sobota 28 grudnia 13:00
- Historia pomiarów - UI, baza i backend
- Wszystkie miejscowości są w bazie, łącznie z wsiami, ponad 40k miejsc

### Sobota 21 grudnia 13:00
- Zreleasowanie wersji 1.4.2
- Rozplanowanie zadań odn. do zapisu historii pomiarów...
- ... i eksportu historii

### Piątek 13 grudnia 20:30
- Odczytywanie danych lokalizacyjnych z GPS...
- zamiast używania Google geolocation API, które zostało usunięte
- Dodanie paska menu
- Dodanie wyszukiwarki miejscowości (WiP)
- Logo i ikona aplikacji

### Piątek 6 grudnia 20:30
- Zakończony research nt. odczytywania danych z GPS telefonu
- Kolor w aplikacji zmienia się w zależności od jakości powietrza

### Piątek 6 grudnia 20:30
- Zakończony research nt. odczytywania danych z GPS telefonu
- Kolor w aplikacji zmienia się w zależności od jakości powietrza 

### Piątek 29 listopada 20:30
- Ekran startowy z instrukcją użytkowania
- Instrukcja jest też dostępna z wnętrza aplikacji


### Piątek 22 listopada 20:30
- Otagowanie i zreleasowanie wersji 1.0.0

### Piętek 15 listopada 20:30
- Integracja z interfejsem użytkownika
  
<img width="100" alt="image" src="https://github.com/user-attachments/assets/32861865-db41-499d-83bc-2507cefb4812">

- Informowanie kolorem o jakości powietrza
- Przygotowanie logo aplikacji
  
  <img width="50" src="https://github.com/user-attachments/assets/5c42005f-cfe8-4e36-8135-183f7c863d3f">

- Rozwinięcie diagramu sekwencji

  <img width="100" alt="image" src="https://github.com/user-attachments/assets/a1ecbf5b-c688-4a67-85e0-3c0ad408ba7d">




### Piętek 8 listopada 20:30
- Odczytywanie geolokacji użytkownika
- Wystawienie nazwy stacji, z której pochodzą dane
- Wyświetlenie danych najbliższej stacji
<img width="100" alt="image" src="https://github.com/user-attachments/assets/4d240427-26b8-474d-a3b1-82b76d427b1f">




### Sobota 2 listopada 11:00
- Doprowadzenie do stanu, gdzie projekt buduje się u wszystkich programistów
  - Dodana funkcja znajdująca najbliższą stację względem podanych współrzęcnych
  - Dodana obsługa sekretów (klucza API) 

### Piątek 18 października 20:30
- Pierwszy mock-up
- Zaproszenie dr Fulmańskiego do organizacji
- Decyzja o użyciu API geolocation do zlokalizowania użytkownika
<img width="100" alt="image" src="https://github.com/user-attachments/assets/6987de72-d354-4046-a26a-22cd4db28a20">


### Piątek 11 października 20:30
- Utworzenie projektu w GitHubie
- Utowrzenie repozytorium
- Ustalenie strategii branczowania
- Utworzenie pierwszych historyjek użytkownika
- Utworzenie [tablicy projektu](https://miro.com/app/board/uXjVLWJwX2Y=/)

### Niedziela 6 października 17:00
- Utworzenie organizacji na GitHubie - Projekt Zespołowy Lab
- Omówienie projektu 
- Wybranie zakresu i wstępnych funkcjonalności aplikacji
- Pierwszy diagram sekwencji
<img width="350" alt="image" src="https://github.com/user-attachments/assets/38f30f50-826e-4a29-9ffc-dd05b0247c50">
