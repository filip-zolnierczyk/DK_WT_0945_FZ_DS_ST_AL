# Szpital

Projekt obejmuje podstawowe operacje związane z dodawaniem, usuwaniem oraz odczytywaniem lekarzy.

## Struktura projektu

Projekt został podzielony na dwie części - frontend oraz backend, a komunikacja między nimi odbywa się poprzez zapytania Rest API. Backend tworzy bazę danych w pliku `database.mv.db` znajdującego się w katalogu `backend`.

### Schemat bazy danych

![Schemat bazy danych](backend/db_schema.png)

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

Na stronie głównej znajduje się lista wszystkich lekarzy wraz z przyciskami które umożliwiają wykonanie określonej akcji na danym lekarzu.

<img width="500" alt="Zrzut ekranu 2025-12-15 213442" src="https://github.com/user-attachments/assets/5b28d7b8-704c-4497-8af7-27442745cf3c" />

Wciśnięcie przycisku "Usuń" usuwa lekarza z listy, natomiast po wciśnięciu "Info" zostaną wyświetlone szczegółowe informacje dotyczące lekarza.

<img width="500" alt="Zrzut ekranu 2025-12-15 213838" src="https://github.com/user-attachments/assets/035fcb31-6509-43c6-86f0-2aad18acb888" />

Aby dodać nowego lekarza należy wcisnąć przycisk "Dodaj lekarza", zostanie wyświetlony formularz, który umożliwi na wpisanie nowego lekarza na listę po wypełnieniu wszystkich wymaganych pól i zapisaniu.

<img width="500" alt="Zrzut ekranu 2025-12-15 213604" src="https://github.com/user-attachments/assets/b425ece9-9e85-44e3-9e0e-a7faa446fa2a" />




## Autorzy

- Antoni Łakomy
- Dawid Szłapa
- Szymon Tworek
- Filip Żołnierczyk
