# BankSystem 💸
Distributed banking system with clean architecture and REST API support.

---

## Features
- User and account management
- Transfers between accounts with varying commission (0%, 3%, 10%)
- Operation history storage
- Asynchronous communication via Kafka
- Password encryption (BCrypt), JWT-based authentication

---

## Technologies Used

### Backend
- **Java 21** (Maven)
- **Spring Boot**
- **Hibernate / JPA**
- **Spring MVC**
- **PostgreSQL**
- **Flyway** for DB migrations
- **Kafka** for event-driven architecture
- **JUnit 5** and **Mockito** for testing

---

## How It Works
The system allows creation and management of users and their bank accounts. All operations are recorded in history. External interaction is handled via REST API through the `ApiGateway`.

`Application` — core service containing business logic.

`StorageService` — separate Spring Boot service that subscribes to Kafka events and stores user and account changes in its own database.

---

## Getting Started

### Prerequisites
- Java 21
- Maven
- PostgreSQL
- Docker Desktop
- Git

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/lim0sha/BankSystem.git  
   cd BankSystem
   ```

2. Pull and run Kafka using Docker Compose:
   ```bash
   docker run -d --name kafka-container -p 9092:9092 bitnami/kafka
   docker-compose -f docker/docker-compose.yml up -d
   ```

3. Configure PostgreSQL:
  - Make sure your local PostgreSQL server is running.
  - Check configurations in `resources/application.yml` in each module for correct DB connection settings.

4. Build the project:
   ```bash
   mvn clean install
   ```

5. Run the main service:
   ```bash
   cd Application && mvn spring-boot:run
   ```

6. Run ApiGateway:
   ```bash
   cd ../ApiGateway/PresentationApiGateway && mvn spring-boot:run
   ```

7. Run StorageService:
   ```bash
   cd ../../StorageService && mvn spring-boot:run
   ```

8. Launch the client application (Presentation):
   ```bash
   cd ../Presentation && mvn exec:java -Dexec.mainClass="Presentation.Console.Main"
   ```

---

## License
This project is licensed under the MIT License – see the [LICENSE.md](docs/src/LICENSE.md) file for details.

## Contact
For questions or feedback, feel free to reach out at [limosha@inbox.ru](mailto:limosha@inbox.ru)

---
Feel free to customize this further to better fit your needs!