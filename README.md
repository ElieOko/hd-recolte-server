# HD Récolte

API de collecte des poubelles (clients, agents, adresses, vidage), réécrite en **Spring Boot Kotlin** sur le même découpage que [casa-spring-boot-backend-modern](https://github.com/ElieOko/casa-spring-boot-backend-modern).

## Structure

```
src/main/kotlin/server/web/recolte
├── RecolteApplication.kt
├── config/          sécurité HTTP, Jackson, OpenAPI, Flyway
├── exception/       erreurs JSON et accès refusé
├── route/           scopes public / protected / private
├── security/        JWT, hash, filtre, utilisateur courant
├── utils/
└── app/
    ├── address/     districts et communes
    ├── client/      clients et adresses
    ├── agent/
    ├── trash/       états et demandes de vidage
    ├── promotion/
    └── user/        comptes administrateurs
```

Chaque fonctionnalité suit les couches `domain` → `application/service` → `infrastructure` (`controller`, `persistence/entity`, `repository`, `mapper`). La persistance est en **R2DBC** (coroutines) et le schéma PostgreSQL est versionné par **Flyway**.

## Lancer

```bash
docker compose up -d
./gradlew bootRun
```

L'API écoute sur le port `8000` (le préfixe reste `/api`, comme l'application Laravel).

Profils : `dev` (défaut, PostgreSQL local), `prod` (variables d'environnement), `test` (H2 en mémoire).

| Variable | Rôle |
| --- | --- |
| `JDBC_URL` / `R2DBC_URL` | Connexions Flyway et R2DBC |
| `DB_USERNAME` / `DB_PASSWORD` | Identifiants PostgreSQL |
| `JWT_SECRET` | Secret HMAC en Base64 (32 octets minimum) |

Documentation : `http://localhost:8000/swagger-ui.html`

## Contrat conservé pour les applications mobiles

| Méthode | Chemin | Rôle |
| --- | --- | --- |
| `POST` | `/api/client` | Créer un client et son adresse |
| `GET` | `/api/client` | Lister les clients |
| `POST` | `/api/client/auth` | Connexion téléphone + avenue |
| `POST` | `/api/agent` | Créer un agent (`hda-1`, `hda-2`, …) |
| `GET` | `/api/agent` | Lister les agents |
| `POST` | `/api/agent/auth` | Connexion par code |
| `POST` | `/api/trash` | Demander un vidage |
| `GET` | `/api/trash` | Lister les poubelles |
| `GET` | `/api/trash/{clientId}` | Poubelle d'un client |
| `POST` | `/api/trash/clean/{id}` | Faire avancer le nettoyage |

Les mêmes opérations existent en versionné, sur le modèle Casa : `/api/v1/public/...`.

Les référentiels (communes de Kinshasa, états de poubelle `1` propre, `2` en cours, `3` à vider) sont insérés au démarrage. La création d'un district, d'une commune, d'un état ou d'une promotion passe par `/api/v1/protected/...` et demande un JWT.

Les comptes administrateurs :

- `POST /api/v1/public/auth/register`
- `POST /api/v1/public/auth/login`
- `POST /api/v1/public/auth/refresh`
- `GET /api/v1/protected/users`

Les codes client sont `HDC-1`, `HDC-2`, … et les codes agent `hda-1`, `hda-2`, … (préfixe agent en minuscules, comme le `strtolower` Laravel).

## Tests

```bash
./gradlew test
```
