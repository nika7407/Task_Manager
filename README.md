### Hexlet tests and linter status:

[![Actions Status](https://github.com/nika7407/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/nika7407/java-project-99/actions)
[![Maintainability](https://qlty.sh/badges/2e1ed84f-c22c-4015-b1f2-4c97910b6eb8/maintainability.svg)](https://qlty.sh/gh/nika7407/projects/java-project-99)
[![Code Coverage](https://qlty.sh/badges/2e1ed84f-c22c-4015-b1f2-4c97910b6eb8/test_coverage.svg)](https://qlty.sh/gh/nika7407/projects/java-project-99)

Task Manager — REST API приложение для управления задачами, метками, пользователями и статусами. Позволяет создавать,
фильтровать и редактировать задачи, назначать исполнителей и добавлять метки.

Приложение Сделано при помощи фреймворка `Spring-Boot`

### Демо

Развёрнутая версия доступна по ссылке:  
**[spring-manager.onrender.com](https://spring-manager.onrender.com)**

---

## Возможности

- JWT-аутентификация и авторизация
- CRUD-операции:
    - задачи (`/api/tasks`)
    - статусы (`/api/task_statuses`)
    - метки (`/api/labels`)
    - Пользователи (`/api/users`)
- Связи:
    - задача ↔ исполнитель
    - задача ↔ статус
    - задача ↔ метки

- Фильтрация задач по параметрам:
    - `titleCont` — по части названия
    - `assigneeId` — по id исполнителя
    - `status` — по slug статуса
    - `labelId` — по id метки
