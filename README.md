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
│   │       ├── config         // Конфигурационные файлы
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
2. Настройте `application.properties`.

Пример `application.properties`:

```properties
server.port=8083
spring.application.name=RedSliceDatabase

# Connecting to data source
spring.datasource.url=jdbc:postgresql://localhost:5432/red_juice_db
spring.datasource.username=username
spring.datasource.password=password

# Liquibase settings
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml

# Disabling the HibernateDDL for using Liquibase
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

```

Также надо создать файд по директории src/main/java/redslicedatabase/redslicedatabase/config/AppConfig.java
```java
package redslicedatabase.redslicedatabase.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class AppConfig {

   private final String apiDatabaseKey = "someApiKey";
}
```
Это нужно для доступа к базе данных только от бэкенда
## 🌟 Особенности

- Полная валидация данных на уровне DTO.
- Логирование всех операций (входящие запросы, изменения в базе данных).
- Гибкая система ролей для будущего расширения.