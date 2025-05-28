# System Zarządzania Pojazdami (Vehicle Management System)

## Informacje formalne

### Nazwa projektu
Vehicle Management System - System zarządzania pojazdami

### Skład grupy projektowej i zadania
1. Kamil Ziółkowski - backend
2. Szymon Zych - frontend
3. wspólnie wykonaliśmy moduł pobierania danych 

### Wykorzystane technologie
- **Backend**:
  - Java 17
  - Spring Boot 3.3.1
  - Spring Security - uwierzytelnianie i autoryzacja
  - Spring Data JPA - warstwa dostępu do danych
  - Lombok - redukcja kodu boilerplate

- **Frontend**:
  - Thymeleaf - silnik szablonów HTML
  - Bootstrap (CSS/JS) - stylizacja interfejsu użytkownika
  
- **Baza danych**:
  - MySQL 8.0
  
- **Infrastruktura**:
  - Docker - konteneryzacja aplikacji
  - Maven - zarządzanie zależnościami i budowanie projektu

- **Integracja**:
  - REST API - integracja z zewnętrznymi systemami
  - Jackson - przetwarzanie danych JSON

## Opis projektu
System zarządzania pojazdami, który rozwiązuje problem integracyjny pobierania dużej bazy pojazdów (ok 50000 rekordów) w dowolnym momencie z uwzględnieniem pewności, że dane będą jak najbardziej aktualne. Aplikacja pobiera dane z serwisu rządowego USA FuelEconomy gdzie znajudją się odpowiednie marki, modele i inne informacje o pojazdach. Pozwala ona pobrać dane, które są w strukturze niedopasowanej do tej aplikacji co prowadzi do konfliktu i wymaga integracji systemowej i odpowiedniego przetworzenia tych informacji.

## Proces przejścia przez ekstrakcję danych z zewnętrznego serwisu FuelEconomy:
1. Najpierw pobierane są lata 1984 - aktualny rok
2. Później pobierane są marki aut
3. Później pobierane są modele aut
4. Później pobierane sa silniki dla danych aut
Dzięki temu uzyskujemy zmapowane auta z modelami silnikami w zaleznosci od tego w jakich latach występują

Etap 2:
Na podstawie ID, które znajduje się w silnikach ekstraktowane są pozostałe dane o pojazdach i mapowane na warstwę DTO i następnie mapowane do bazy danych i udostępniane do serwisu restowego. 
Aplikacja jest w formie MVC więc udostępniane są informacje do widoków.

### Przykładowe pytania, na które odpowiada aplikacja:
1. Jakie jest zużycie paliwa w mieście dla konkretnego modelu samochodu?
2. Jakie pojazdy są dostępne na rynku?
3. Jaki silnik (typ, pojemność, paliwo) ma dany model pojazdu w zaleznosci od roku, w którym został on wyprodukowany?
4. Jakie modele są dostępne dla danej marki auta w danych latach (Te dane nie są oczywiste)?
5. Jakiego paliwa używa dany pojazd?

## Konfiguracja środowiska

### Wymagania systemowe
- Java 17 JDK
- Maven 3.6+
- Docker i Docker Compose (opcjonalnie)
- MySQL 8.0 (jeśli uruchamiane poza kontenerem)
- IDE z obsługą projektów Maven (zalecane: IntelliJ IDEA, Eclipse, VSCode)



### Uruchomienie za pomocą Docker
Aplikację można uruchomić za pomocą dockera wykonując kolejno komendy: 
- docker compose up --build
- docker compose up


## Źródła danych

System integruje dane z następujących źródeł:

1. **Baza danych wewnętrzna MySQL** - przechowuje dane użytkowników, pojazdów i relacji między nimi
2. **API Fuel Economy** - zewnętrzne API dostarczające danych o zużyciu paliwa dla różnych modeli pojazdów i informacje o nich
3. **Dane wprowadzane przez użytkowników** - informacje o pojazdach, ich parametrach i historii

Aplikacja zarządza danymi z różnych źródeł i prezentuje je w ujednolicony sposób, zapewniając spójność i łatwy dostęp do informacji.

## Dodatkowe informacje

### Obsługa serwisu
- Login i hasło do konta administratora seedowane w aplikacji: 
  - login: `adminek`
  - hasło: `admin`

### Funkcje systemu
- Rejestracja i logowanie użytkowników
- Dodawanie, edycja i usuwanie pojazdów
- Wyszukiwanie informacji o pojazdach
- Pobieranie dodatkowych danych o pojazdach z zewnętrznych API
- Eksport danych o pojazdach do różnych formatów

### Bezpieczeństwo
System wykorzystuje Spring Security do zabezpieczenia dostępu do danych. Każdy użytkownik ma dostęp tylko do swoich pojazdów, chyba że posiada rolę administratora to może on również uruchomić integrację systemu. Aplikacja korzysta z autoryzacji i autentykacji używanych za pomocą wewnętrznych mechanizmów frameworka Spring Security.
Mechanizm sesji oparty na cookie-based razem z http-only.

### Integracja z API
W przypadku problemów z dostępem do zewnętrznych API, system posiada mechanizm fallback wykorzystujący dane z pliku `fallback-makes.json`.

### Uruchomienie bez dockera
1. ```mvn clean install``` nalezy miec zainstalowanego lokalnie mavena i jave 
2. należy uruchomić za pomocą w folderze  target za pomocą```java -jar aplikacja.jar```

