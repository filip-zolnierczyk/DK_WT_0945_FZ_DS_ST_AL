# Szpital

Projekt obejmuje podstawowe operacje związane z dodawaniem, usuwaniem oraz odczytywaniem lekarzy.

## Struktura projektu

Projekt został podzielony na dwie części - frontend oraz backend, a komunikacja między nimi odbywa się poprzez zapytania Rest API. Backend tworzy bazę danych w pliku `database.mv.db` znajdującego się w katalogu `backend`.

### Schemat bazy danych

<img width="646" height="933" alt="Zrzut ekranu 2026-01-12 225528" src="https://github.com/user-attachments/assets/65ba682f-9560-4276-887f-2ae4580070f8" />


### Frontend

Użyte technologie:

- React

### Backend

Oparty o Jave w wersji 25.

Użyte technologie:

- Spring Boot
- Lombok
- OpenAPI
- H2 Database

## Wymagania wstępne

- npm
- java

## Instalacja

- Frontend

```sh
cd frontend
npm install
npm run dev
```

- Backend
  Kompilujemy kod przy użyciu Javy, a zarządzanie zależnościami realizowane jest przez Gradle.

## Generowanie przykładowych danych

Generowanie danych odbywa się poprzez ustawienie parametru

```properties
example.database=true
```

w pliku `application.properties`. Dane nie zostaną wygenerowane, gdy w bazie istnieje co najmniej jeden lekarz, aby zachować integralność danych.

## API

Api jest dostępne na backendzie z adresu `/swagger-ui.html`. Domyślnie jest to więc `localhost:8080/swagger-ui.html`.

## Opis wywołania funkcjonalności:

Na stronie głównej (zakładka Lekarze) znajduje się lista wszystkich lekarzy wraz z przyciskami które umożliwiają wykonanie określonej akcji na danym lekarzu.

<img width="855" height="325" alt="Zrzut ekranu 2026-01-09 040839" src="https://github.com/user-attachments/assets/e3e0e22e-b46a-4328-950e-4dd410db523a" />

Wciśnięcie przycisku "Usuń" usuwa lekarza z listy, natomiast po wciśnięciu "Info" zostaną wyświetlone szczegółowe informacje dotyczące lekarza.

<img width="877" height="515" alt="Zrzut ekranu 2026-01-09 040359" src="https://github.com/user-attachments/assets/fb94f7d3-4d85-4416-a348-db836df43a76" />

Aby dodać nowego lekarza należy wcisnąć przycisk "Dodaj lekarza", zostanie wyświetlony formularz, który umożliwi na wpisanie nowego lekarza na listę po wypełnieniu wszystkich wymaganych pól i zapisaniu.

<img width="875" height="580" alt="Zrzut ekranu 2026-01-09 040928" src="https://github.com/user-attachments/assets/8bf8a360-b046-452e-a2d2-d78e1df1a940" />

Analogicznie sytuacja ma się z Pacjentami - aby dodać pacjenta należy wypełnić formularz pojawiający się po wciśnięciu przycisky 'Dodaj Pacjenta'.

<img width="880" height="515" alt="Zrzut ekranu 2026-01-09 040441" src="https://github.com/user-attachments/assets/56bf057b-8167-46ef-9ad9-9c2a8e5f49c6" />

Listę wszystkich pacjentów można ujrzeć po wciśnięciu przycisku 'Pacjenci'.
Każdego pacjenta można usunąć wciskając czerwony przycisk z napisem 'usuń'.

<img width="879" height="335" alt="image" src="https://github.com/user-attachments/assets/dd4c1e42-9225-421a-bf1b-ec46f77f224c" />

Również analogicznie sytuacja ma się z gabinetami - możemy je dodawać, usuwać, przeglądać wszystkie.

<img width="869" height="463" alt="Zrzut ekranu 2026-01-09 040554" src="https://github.com/user-attachments/assets/298eff0c-ff36-452a-804e-47a7a7025697" />

<img width="914" height="344" alt="Zrzut ekranu 2026-01-09 040634" src="https://github.com/user-attachments/assets/39fa8bac-f362-42dd-b584-fc751d4d4cdb" />

W dodatku przy każdym gabinecie wyświetla się przycisk 'dyżury' - po jego wciśnięciu wyświetlą nam się dokładne szczegóły na temat danego gabinetu oraz rozpiska wszystkich zaplanowanych w nim dyżurów.

<img width="864" height="554" alt="image" src="https://github.com/user-attachments/assets/393b2e44-9973-4e90-9fc0-1ab57a7b10b8" />

Przycisk 'Dodaj dyżur' przekierowuje nas do formularza, gdzie możemy dodać dyżur poprzez wybranie lekarza, gabinetu oraz daty początku i rozpoczacia dyżuru.

<img width="1202" height="889" alt="Zrzut ekranu 2026-01-09 040109" src="https://github.com/user-attachments/assets/228e50dc-2c9b-48bf-88de-7c7815ae4038" />

Przycisk 'Usuń wszystkie dyżury' usuwa wszystkie aktualnie zaplanowane dyżury (w razie nagłego bankructwa szpitala lub innych niespodziewanych sytuacji)

## Autorzy

- Antoni Łakomy
- Dawid Szłapa
- Szymon Tworek
- Filip Żołnierczyk
