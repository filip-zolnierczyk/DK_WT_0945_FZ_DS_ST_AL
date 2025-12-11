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

## Autorzy

- Antoni Łakomy
- Dawid Szłapa
- Szymon Tworek
- Filip Żołnierczyk
