# vet-clinic-api

Projekt zaliczeniowy: REST API dla przychodni weterynaryjnej w Spring Boot. Nie ma frontu, wszystko testujemy przez requesty HTTP.

## Technologie

- Java 21+
- Spring Boot 4 (Web MVC, Data JPA, Validation)
- PostgreSQL (lokalnie w Dockerze)
- H2 (tylko do testów)
- JUnit 5 + Mockito
- Lombok

## Jak odpalić

Potrzebne: JDK 21 lub nowszy i Docker.

1. Baza danych:

   ```bash
   docker compose up -d
   ```

2. Aplikacja:

   ```bash
   ./mvnw spring-boot:run
   ```

   Na Windowsie `.\mvnw.cmd spring-boot:run` albo Run na `VetClinicApiApplication` w IntelliJ.

3. API działa na `http://localhost:8080`.

## Testy

```bash
./mvnw test
```

Testy lecą na profilu `test` z bazą H2 w pamięci, więc Docker nie jest do nich potrzebny.

## Profile

| Profil | Baza | Do czego |
| --- | --- | --- |
| `dev` (domyślny) | PostgreSQL z docker-compose | normalne uruchamianie |
| `test` | H2 w pamięci | testy |

## Struktura projektu

Każdy moduł ma swój pakiet, a w nim warstwy:

```
pl.vetclinic.vet_clinic_api
├── common        # wyjątki i obsługa błędów wspólna dla wszystkich
├── pet           # zwierzęta
├── vet           # weterynarze
└── appointment   # wizyty
    ├── controller
    ├── service
    ├── repository
    ├── model
    ├── dto
    └── mapper
```

## Obsługa błędów

Błędy zwracamy zawsze w tym samym formacie (`ErrorResponse`):

| Kod | Kiedy |
| --- | --- |
| 400 | błędna walidacja albo zły JSON |
| 404 | nie ma rekordu o takim id |
| 409 | konflikt, np. zajęty termin wizyty |

## Moduły i autorzy

| Moduł | Autor | Endpointy |
| --- | --- | --- |
| Szkielet + `common` | Bartosz | - |
| Pets | Bartosz | `/api/pets` |
| Vets | _do uzupełnienia_ | `/api/vets` |
| Appointments | _do uzupełnienia_ | `/api/appointments` |

## Jak pracujemy z gitem

- Nie pushujemy na `main`. Każdy moduł robimy na swoim branchu (`feature/pets`, `feature/vets`, `feature/appointments`) i wrzucamy przez Pull Request.
- PR musi przejrzeć i zaakceptować ktoś inny z zespołu.
- Mergujemy zwykłym merge commitem, bez squasha, żeby została historia.

Commity piszemy po angielsku, krótko i konkretnie:

| Prefiks | Kiedy |
| --- | --- |
| `feat(modul):` | nowa funkcjonalność |
| `test(modul):` | testy |
| `fix(modul):` | poprawka błędu |
| `refactor(modul):` | zmiana kodu bez zmiany działania |
| `chore:` | konfiguracja, zależności |
| `docs:` | README i dokumentacja |

Przykład: `feat(pets): add search by species`
