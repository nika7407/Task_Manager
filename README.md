### Hexlet tests and linter status:

[![Actions Status](https://github.com/nika7407/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/nika7407/java-project-99/actions)  
[![Maintainability](https://qlty.sh/badges/2e1ed84f-c22c-4015-b1f2-4c97910b6eb8/maintainability.svg)](https://qlty.sh/gh/nika7407/projects/java-project-99)  
[![Code Coverage](https://qlty.sh/badges/2e1ed84f-c22c-4015-b1f2-4c97910b6eb8/test_coverage.svg)](https://qlty.sh/gh/nika7407/projects/java-project-99)

**Task Manager** — A REST API application for managing tasks, labels, users, and statuses. It allows creating, filtering, and editing tasks, assigning executors, and adding labels.

The application is built using the **Spring Boot** framework.

### Demo

A deployed version is available at:  
**[spring-manager.onrender.com](https://spring-manager.onrender.com)**
to login use `hexlet@example.com` and `qwerty` as password

---  

## Features

- **JWT authentication and authorization**
- **CRUD operations**:
  - Tasks (`/api/tasks`)
  - Statuses (`/api/task_statuses`)
  - Labels (`/api/labels`)
  - Users (`/api/users`)
- **Relationships**:
  - Task <  > Assignee
  - Task <  > Status
  - Task <  > Labels
- **Task filtering by parameters**:
  - `titleCont` - by partial title match
  - `assigneeId` - by assignee ID
  - `status` - by status slug
  - `labelId` - by label ID