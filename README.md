# RedSlice Database

## 📚 Описание

**RedSlice Database** — это серверная часть для приложения RedSlice, реализованная с использованием Java Spring Boot. Она предоставляет REST API для управления чатами, ветками, сообщениями и пользователями.

## ⚙️ Основные функции

1. **Чаты:**
    - Создание, просмотр, обновление и удаление чатов.
    - Проверка доступа пользователя к чату.
    - Управление настройками чатов (температура, модель и контекст).

2. **Ветки:**
    - Создание веток внутри чатов.
    - Проверка доступа пользователя к ветке.
    - Просмотр дочерних веток.

3. **Сообщения:**
    - Создание сообщений и пар сообщений.
    - Получение всех сообщений в ветке.
    - Удаление сообщений.

4. **Пользователи:**
    - Регистрация пользователей через Firebase UID.
    - Получение данных пользователя по email или UID.

## 🚀 Технологии

- **Java 17**
- **Spring Boot 3**
- **PostgreSQL** — база данных.
- **Lombok** — для сокращения шаблонного кода.
- **Liquibase** — управление миграциями базы данных.

## 📂 Структура проекта

```
src
├── main
│   ├── java
│   │   └── redslicedatabase
│   │       ├── controller     // REST-контроллеры
│   │       ├── service        // Бизнес-логика
│   │       ├── model          // Сущности JPA
│   │       ├── dto            // Data Transfer Objects
│   │       ├── repository     // Репозитории JPA
│   │       ├── exception      // Обработка ошибок
│   │       └── logging        // Логирование
│   └── resources
│       ├── db                 // Миграции базы данных (Liquibase)
│       ├── application.yml    // Настройки приложения
```


### Настройка окружения

1. Создайте базу данных PostgreSQL.
2. Настройте `application.yml` или создайте файл `.env` для подключения к базе данных.

Пример `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/redslice
    username: your_db_user
    password: your_db_password
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

liquibase:
  change-log: classpath:db/changelog/db.changelog-master.xml
```

## 🌟 Особенности

- Полная валидация данных на уровне DTO.
- Логирование всех операций (входящие запросы, изменения в базе данных).
- Гибкая система ролей для будущего расширения.