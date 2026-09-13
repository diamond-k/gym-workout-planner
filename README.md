# Workout Planner

Workout Planner is a full-stack web application for creating, organising, and managing personalised workouts.

Users can create personalised workouts from a pre-populated exercise list, filter options by muscle group, and set target sets and reps for each exercise. Saved workouts can be edited or deleted, and each exercise includes an image and instructions on how to perform it. When a workout is saved, the selected exercise order, sets, and reps are retained.

The application is built with React and TypeScript on the frontend, a Spring Boot REST API on the backend, and MySQL for persistent data storage. The backend follows a layered Controller, Service, and Repository architecture using Spring Data JPA, DTOs, validation, and centralised exception handling. Validation is applied on both the frontend and backend, and the full application can be run together using Docker Compose.

---

## Features

- View saved workouts from the dashboard
- Create a workout from an exercise list
- Filter exercises by muscle group
- Set target sets and reps for each exercise
- Edit workout names, descriptions, exercises, sets, and reps
- Remove exercises from a workout
- View exercise images and instructions
- Delete workouts
- Show the most recently created or edited workouts first
- Warn before leaving the create/edit page with unsaved changes
- Validate workout data on both the frontend and backend
- Responsive layout for desktop and mobile
- Persist workout data in MySQL
- Test core workout creation, editing, and deletion logic with backend unit tests
- Run the frontend, API, and database together with Docker Compose

---

## Tech Stack

### Frontend

- React 19
- TypeScript
- Vite
- Mantine
- React Router
- Tabler Icons

### Backend

- Java 21
- Spring Boot
- Spring Data JPA
- Jakarta Validation
- Maven

### Database

- MySQL 8

### Containerisation

- Docker
- Docker Compose

---

## Frontend Architecture

The frontend is split into pages, reusable components, API services, TypeScript types, and styling.

- **Pages** represent the main screens of the application, including the dashboard, create/edit workout form, workout details, and exercise details.
- **Reusable components** are used for shared interface elements such as workout cards, exercise cards, exercise images, and the navigation bar.
- **React Router** handles navigation between pages. The main page routes are lazy loaded so they are only loaded when needed.
- **React hooks** such as `useState` and `useEffect` manage component state and load data from the API.
- A shared `RequestState` type is used to represent loading, success, and error states when data is requested.
- The `api.ts` service keeps API requests separate from the page components and provides the frontend with methods for creating, retrieving, updating, and deleting workouts.
- TypeScript types define the data exchanged between the frontend and backend.
- The workout form loads the exercise list once and applies the muscle group filter on the client side.
- Mantine components are used alongside custom CSS for the interface, with responsive layouts and controls for smaller screen sizes.

## Backend Architecture

The Spring Boot API follows a layered structure:

- **Controllers** receive HTTP requests and return API responses.
- **Services** contain the application and business logic.
- **Repositories** provide access to the MySQL database using Spring Data JPA.
- **Entities** represent the database tables and their relationships.
- **DTOs** define the request and response data used by the API.

Spring Data JPA maps the Java entities to the MySQL tables and manages the relationships between workouts, exercises, and workout exercises.

The repositories also use **derived query methods**, allowing Spring Data JPA to create queries from method names rather than requiring each query to be written manually. Examples used in the application include:

- `findByMuscleGroup()` to retrieve exercises belonging to a particular muscle group.
- `findAllByOrderByUpdatedAtDesc()` to return workouts with the most recently created or edited first.
- `findByWorkoutPlanIdOrderByPositionAsc()` to retrieve the exercises belonging to a workout in their saved order.

Workout creation and update timestamps use Java `Instant` values. Hibernate is configured to use UTC for JDBC timestamp handling, so API timestamps are unambiguous and the frontend can display them correctly in the viewer's local timezone.

## Project Structure

```text
gym-workout-planner/
├── api/                            # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/               # Controllers, services, repositories, entities and DTOs
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── schema.sql      # Database structure
│   │   │       └── data.sql        # Exercise seed data
│   │   └── test/
│   │       └── java/               # Backend tests
│   ├── local.properties.example    # Template for local database credentials
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/                       # React / Vite / TypeScript frontend
│   ├── public/
│   │   └── exercises/              # Exercise images
│   ├── src/
│   │   ├── components/
│   │   ├── data/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── styles/
│   │   ├── types/
│   │   └── utils/
│   ├── Dockerfile
│   ├── package.json
│   └── vite.config.ts
│
├── postman/                        # Postman API documentation and examples
├── docs/                           # Project documentation assets
├── .env.example                    # Template for Docker environment variables
├── API_PLAN.md                     # API endpoint plan
├── ASSESSMENT.md                   # Assessment brief
├── docker-compose.yml
└── README.md
```

---

## Prerequisites

To run the application locally:

- Java 21
- Node.js 22+
- MySQL 8
- Git

To run the full stack with Docker:

- Docker Desktop

---

## Running the Application Locally

### 1. Create the database

Connect to your local MySQL server and create an empty database.

For example, if you are using the terminal:

```bash
mysql -u your_mysql_username -p
```
Then create the database:

```sql
CREATE DATABASE gym_workout_planner;
```

### 2. Configure the API database connection

Local database credentials are stored in `api/local.properties`, which is ignored by Git.

From the `api` directory, copy the example file:

```bash
cd api
cp local.properties.example local.properties
```

Update `local.properties` with your local MySQL details:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gym_workout_planner
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

Do not place real database credentials in `application.properties`, because that file is committed to the repository.

### 3. Start the API

From the `api` directory:

```bash
./mvnw spring-boot:run
```

Wait for the application to finish starting. The API listens on `http://localhost:8080`, with application endpoints under `/api`. For example:

```text
http://localhost:8080/api/exercises
```

### 4. Start the frontend

In a second terminal, from the `frontend` directory:

```bash
cd frontend
npm ci
npm run dev
```

Open:

```text
http://localhost:5173
```

The frontend sends requests to relative `/api` paths. During local development, Vite proxies these requests to the Spring Boot API running on port `8080`.

---

## Running with Docker

Docker Compose runs the frontend, API, and MySQL database together.

### 1. Create the Docker environment file

From the project root:

```bash
cp .env.example .env
```

Set the Docker database values in `.env`:

```env
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_DATABASE=workout_planner
MYSQL_USER=workout_user
MYSQL_PASSWORD=your_password
```

The `.env` file is ignored by Git and should not be committed.

### 2. Start the full stack

Make sure Docker Desktop is running, then from the project root run:

```bash
docker compose up --build
```

Docker Compose will:

- start the MySQL database
- wait for the database healthcheck to pass
- start the Spring Boot API
- start the React frontend
- connect all three services through the `workout-network` Docker network
- persist MySQL data in the `db-data` named volume

### 3. Access the services

| Service | Address |
| --- | --- |
| Frontend | `http://localhost:5173` |
| API | `http://localhost:8080` |
| MySQL | Available to the containers as the `db` service |

Inside Docker, the API connects to MySQL using the `db` service name rather than `localhost`.

The database connection values are supplied through Docker environment variables:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

These override the local database settings when the API runs in Docker.

---

## Stopping and Resetting Docker

Stop the containers while keeping the database data:

```bash
docker compose down
```

Start them again with:

```bash
docker compose up --build
```

The named MySQL volume keeps the stored records between normal container restarts.

To remove the containers and the stored MySQL data:

```bash
docker compose down -v
```

The `-v` option removes the `db-data` volume, so the next startup uses a fresh database.

---

## Database

The application uses three main tables.

### `exercises`

Stores the exercise list, including:

- exercise name
- muscle group
- instructions

### `workout_plans`

Stores each workout, including:

- name
- optional description
- created timestamp
- updated timestamp

### `workout_plan_exercises`

Links exercises to workouts and stores the values that belong to that workout, including:

- target sets
- target reps
- exercise position, used to preserve the order exercises were added to the workout

A workout can contain multiple exercises. The `workout_plan_exercises` table stores the relationship between a workout and its selected exercises.

Each table has its own primary key. `workout_plan_exercises.workout_plan_id` and `workout_plan_exercises.exercise_id` are foreign keys to `workout_plans` and `exercises`, and the combination is unique so the same exercise cannot be stored twice in one workout.

---

## Database Seed Data

The application comes pre-populated with a core exercise list so users can begin building workouts straight away.

The list contains 24 exercises across eight muscle groups:

- Chest
- Back
- Shoulders
- Biceps
- Triceps
- Legs
- Glutes
- Core

`schema.sql` contains the full database structure and `data.sql` contains the initial exercise data.

The application uses `spring.sql.init.mode=always`, so the SQL initialisation scripts run whenever the API starts. Exercise names are unique in the schema and `data.sql` uses `INSERT IGNORE`, making repeated startup seeding idempotent instead of creating duplicate exercise records.

The workout tables are not populated with sample workouts because workouts are created by the user through the application.

---

## API Endpoints

The application code uses `workout-plans` in its API routes and backend model names, while the user interface refers to these records simply as **workouts**.

### Exercises

| Method | Endpoint | Description | Success | Possible errors |
| --- | --- | --- | --- | --- |
| `GET` | `/api/exercises` | Get all exercises | `200` | — |
| `GET` | `/api/exercises?muscleGroup=BACK` | Get exercises matching the selected muscle group | `200` | `400` invalid muscle group |

The `muscleGroup` query parameter accepts the application's supported muscle-group values: `CHEST`, `BACK`, `SHOULDERS`, `BICEPS`, `TRICEPS`, `LEGS`, `GLUTES`, and `CORE`. The workout form loads the full exercise list once and applies its filter on the client side; the filtered API endpoint remains available for direct API use.

### Workouts

| Method | Endpoint | Description | Success | Possible errors |
| --- | --- | --- | --- | --- |
| `GET` | `/api/workout-plans` | Get all workouts, most recently created or edited first | `200` | — |
| `GET` | `/api/workout-plans/{id}` | Get one workout by ID | `200` | `404` workout not found |
| `POST` | `/api/workout-plans` | Create a workout with selected exercises and target sets/reps | `200` | `400` validation, `404` exercise not found, `409` duplicate exercise |
| `PUT` | `/api/workout-plans/{id}` | Update a workout, including selected exercises and targets | `200` | `400` validation, `404` workout/exercise not found, `409` duplicate exercise |
| `DELETE` | `/api/workout-plans/{id}` | Delete a workout and its saved exercise relationships | `200` | `404` workout not found |

### Workout Exercises

| Method | Endpoint | Description | Success | Possible errors |
| --- | --- | --- | --- | --- |
| `GET` | `/api/workout-plans/{workoutPlanId}/workout-plan-exercises` | Get the exercises saved in a workout in their saved order | `200` | `404` workout not found |

`{id}` and `{workoutPlanId}` are numeric identifiers for saved workouts.

---

## Example Workout Request

The create and update endpoints send the workout details and selected exercises together.

```json
{
  "name": "Upper Body Workout",
  "description": "Chest, back and arms",
  "exercises": [
    {
      "exerciseId": 1,
      "targetSets": 3,
      "targetReps": 10
    },
    {
      "exerciseId": 2,
      "targetSets": 3,
      "targetReps": 12
    }
  ]
}
```

A workout response contains the saved workout metadata. Timestamps are returned as UTC instants with a `Z` suffix:

```json
{
  "id": 4,
  "name": "Upper Body Workout",
  "description": "Chest, back and arms",
  "createdAt": "2026-09-13T08:15:32.545786Z",
  "updatedAt": "2026-09-13T08:15:32.545786Z"
}
```

---

## Validation and Error Handling

Workout data is validated on both the frontend and backend.

Validation includes:

- workout name is required
- workout name must be 255 characters or less
- workout name must contain at least one letter or number
- description must be 1000 characters or less
- at least one exercise must be selected
- exercise IDs must be valid positive values
- target sets and target reps must be positive values

The frontend displays validation messages close to the relevant form fields where possible.

The backend uses centralised exception handling with `@RestControllerAdvice` to return appropriate HTTP responses for validation failures, missing resources, invalid muscle groups, and duplicate workout exercises.

---

## Backend Testing

The backend includes JUnit 5 and Mockito tests for `WorkoutPlanService`. The service tests use mocked repositories, so they exercise business logic without requiring a real database. Coverage includes:

- creating workouts and preserving exercise order
- rejecting duplicate or missing exercises
- adding, removing, reordering, and updating exercises during workout edits
- preserving existing workout-exercise relationships when only targets change
- handling missing workouts
- deleting existing workouts and rejecting deletes for missing workouts

An additional Spring Boot context test verifies that the application context loads successfully. Run the full backend test suite from the `api` directory with:

```bash
./mvnw test
```

---

## Using the Application

### Dashboard

The dashboard is the starting point of the application. It displays saved workouts as cards, with the most recently created or edited workouts shown first.

Each card shows the workout name, optional description, number of exercises, and when the workout was created or last edited.

If no workouts have been created yet, an empty state is shown with an option to create the first workout. Once workouts exist, the **Create** option remains available from the dashboard so more workouts can be added. Selecting a workout card opens its details page.

### Creating a Workout

Selecting **Create** opens the workout form. A **Back** link at the top of the page allows the user to return to the dashboard at any time.

The user can enter a workout name and an optional description, then choose exercises from the exercise list. The full exercise list is loaded when the form opens and can be filtered by muscle group to make individual exercises easier to find.

When an exercise is added, it moves into the **In This Workout** section with default target sets and reps. These values can be changed to suit the workout, and exercises can also be removed before saving.

Exercises are kept in the order they were added so the same order can be shown when the workout is viewed later.

The form validates the workout before it is submitted. A name and at least one exercise are required, and validation messages are shown alongside the relevant fields.

If the user tries to leave the page — either by selecting **Back** or navigating away another way — after making changes without saving them, a confirmation prompt allows them to either stay on the form or discard the changes.

When **Save** is selected, the workout and its selected exercises are sent to the API. Once the workout has been successfully created, the user is taken directly to the details page for the new workout.

### Viewing a Workout

The workout details page shows the workout name, optional description, and the list of exercises included in that workout.

Each exercise displays its image, muscle group, and the target sets and reps saved for that workout. The exercises are returned in the same order in which they were originally added.

The page also provides **Edit** and **Delete** options, as well as a **Back to workouts** link to return to the dashboard.

Each exercise in the workout can be selected to open its own details page.

### Viewing Exercise Details

Selecting an exercise opens a detailed view of that exercise.

The page shows the exercise image, name, muscle group, the target sets and reps for the current workout, and written instructions explaining how to perform the movement.

The image gives the user a quick visual reference for the exercise, while the written instructions provide further guidance.

Selecting **Back to workout** returns the user to the workout they were previously viewing.

### Editing a Workout

Selecting **Edit** from the workout details page reopens the workout form with the existing name, description, exercises, sets, and reps already filled in.

The user can then:

- change the workout name or description
- add new exercises
- remove existing exercises
- change target sets or reps

When the edited workout is saved, the existing workout is updated rather than creating a new one. Exercises that remain in the workout are updated, newly selected exercises are added, and exercises that were removed from the form are removed from that workout.

The updated workout is then shown again on its details page.

As with creating a workout, the user is warned if they attempt to leave the edit form with unsaved changes.

### Deleting a Workout

Selecting **Delete** from the workout details page opens a confirmation pop-up before anything is removed.

The user can cancel the action and remain on the workout, or confirm the deletion. Once confirmed, the workout is deleted and the user is returned to the dashboard.

---

## API Documentation

A summary of the available endpoints is provided in the [API endpoint plan](API_PLAN.md).

Detailed API documentation is provided through the exported Postman collection in the [`postman`](postman/) folder.

The collection contains the current API requests for:

- exercise retrieval and filtering
- workout CRUD operations
- exercises belonging to a workout

The saved requests include the expected HTTP methods, request parameters, request bodies, response examples, and status codes for testing the API.

When running locally:

- The frontend application opens at [http://localhost:5173](http://localhost:5173), where `/` is the dashboard.
- The API runs on `http://localhost:8080`.
- API endpoints are accessed through routes such as [http://localhost:8080/api/exercises](http://localhost:8080/api/exercises).

---

## Useful Commands

### Frontend

From the `frontend` directory, start the app locally with:

```bash
npm run dev
```

Create a production build with:

```bash
npm run build
```

### Backend

From the `api` directory, start the Spring Boot API locally with:

```bash
./mvnw spring-boot:run
```

Build the backend and create the JAR file with:

```bash
./mvnw clean package
```

Run all backend tests with:

```bash
./mvnw test
```

Run only the workout service tests with:

```bash
./mvnw -Dtest=WorkoutPlanServiceTest test
```

### Docker

All Docker commands below should be run from the project root.

Start the full stack and keep the logs visible in the terminal:

```bash
docker compose up --build
```

Start the full stack in the background:

```bash
docker compose up --build -d
```

#### Rebuild the frontend

If frontend code has changed:

```bash
docker compose up --build -d frontend
```

If only the frontend needs rebuilding and the API and database should be left running as they are:

```bash
docker compose up --build -d --no-deps frontend
```

#### Rebuild the API

If backend code has changed:

```bash
docker compose up --build -d api
```

#### Start only the API and database

This is useful when the frontend is not needed, for example when testing API endpoints directly:

```bash
docker compose up -d db api
```

If backend code has changed and the API also needs rebuilding:

```bash
docker compose up --build -d db api
```

#### Check container status

```bash
docker compose ps
```

#### Check recent API logs

```bash
docker compose logs api --tail=50
```

#### Stop the API only

This leaves the frontend and database running:

```bash
docker compose stop api
```

Start it again with:

```bash
docker compose start api
```

#### Restart the API

```bash
docker compose restart api
```

#### Stop the full Docker stack but keep the database data

```bash
docker compose down
```

The MySQL named volume is kept, so saved workouts remain available the next time the stack starts.

#### Reset the Docker database

```bash
docker compose down -v
```

The `-v` option also removes the MySQL volume, so any workouts stored in that Docker database will be deleted. Use this only when a fresh database is needed.

#### Connect to the Docker MySQL database

```bash
docker compose exec db mysql -uworkout_user -p workout_planner
```

Enter the password from the `.env` file when prompted.

## Troubleshooting

### Port already in use

If Docker reports that port `8080` or `5173` is already in use, another local process is already using that port. Stop any locally running Spring Boot API or Vite frontend before starting the Docker stack.

### Docker changes are not appearing

If frontend or backend code has changed since the images were last built, rebuild before starting the stack:

```bash
docker compose up --build
```

For API-only testing with the database:

```bash
docker compose up --build -d db api
```

### Resetting the Docker database

To completely reset the Docker database and recreate it from the current schema and seed data:

```bash
docker compose down -v
docker compose up --build
```

The `-v` option deletes the existing MySQL volume, including any workouts created in that Docker database. Use this only when a fresh database is needed.
