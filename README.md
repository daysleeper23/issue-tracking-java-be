

# Teamwork

- Designing REST API endpoints
- Database schema
- Workable backend server with Spring Boot & Hibernate

---

# Members
Maksim Minenko, Thuong Le, Luu Dao

---

## Menu

- [Vision](#vision)
- [Business Requirements](#business-requirements)
- [Requirements](#requirements)
- [Mandatory Features](#mandatory-features)
- [Optional Features](#optional-features)

---

## Vision

You are required to build a fullstack project management system similar to Trello, Jira, or Monday.

The project can be single- or multi-tenant.

The main requirements are as follows:

- **User Management**
- **Projects and Workspaces**
- **Tasks and Issues**

To take the project to the next level, consider these additional requirements:

- **Collaboration**
- **Real-time Collaboration**
- **Integration with Other Platforms**
- **Reporting and Analytics**

### Business Requirements

- Brainstorm the backend design in terms of entity structure and how they will interact. 
- Discuss the implementation of architecture: CLEAN, DDD, TDD, and any possible pattern your team want to apply (repository pattern, CQRS, etc.).
- **Deadline Management**: Use any project management tool of your choice to ensure timely delivery.

---

## Requirements

_For team assignment, only 1 member should fork the repo, then the admin can invite other members to contribute in the same repo. The rest of the team members, must fork from the common repo(admin repo), making PRs against the common repo when changes are needed. Remember to have a develop branch before merging to main. Each feature/schema/bug/issue should have its own branch, and only one member should work on one branch/file at a time. Before making any new branch, make sure to sync the fork and run `git pull` to avoid conflicts with the common team repo._

1. **Create ERD Diagram** with entities, attributes, and relationships. Include the ERD as an image in the project.

2. **Design the API Endpoints** to follow REST API architecture. Document the endpoints in `.md` files, providing detailed explanations of queries, parameters, request bodies, authentication requirements (if applicable), sample responses, and status codes.

3. **Basic Entities** (Additional entities may be included as needed):
	Possible entities:
   - Tenant
   - User
   - Project
   - Workspace
   - Task
   - Role
   - Permission
   - Comment (optional)
   - Notification (optional)

4. **Develop Backend Server** using CLEAN Architecture:

   - Use exception handler to provide meaningful responses to users.
   - Unit testing is required primarily for the Domain and Service layers. It would be ideal to test also the controllers and data access.
   - The README file should clearly describe the project with sufficient detail and a readable structure.

---

## Mandatory Features

- **User Management**
   - User registration and login functionality
   - User authentication using email/password or other methods (e.g., Google, GitHub)
   - Custom roles and permissions (e.g., HR, Dev, PM, Guest)

- **Projects and Workspaces**
   - Ability to create and manage multiple projects/workspaces
   - Project details: name, description, start/end dates, status

- **Tasks and Issues**
   - Task/issue creation with title, description, priority, and deadline
   - Task/issue tracking: status updates (e.g., To-Do, In Progress, Done)
   - Assign tasks/issues to team members or specific users

- **Boards and Kanban (UI-related)**
   - Customizable boards for different projects/workspaces
   - Card-based representation of tasks/issues on the board
   - Drag-and-drop reordering of cards
   - Board filters and custom views (e.g., due dates, priority)

---

## Optional Features

- **Collaboration and Communication**
   - Notification system: email/text updates on task/issue changes
   - Tagging team members in comments
   - File attachments and commenting on tasks/issues

- **Real-Time Collaboration**
   - Real-time commenting with instant updates for team members
   - Auto-updates for task statuses

- **Integrations and APIs**
   - Integration with Google Drive, Trello, Slack, GitHub issues, calendar, and email clients

- **Gantt Charts and Timelines**
   - Gantt chart visualization for project timelines

- **Reporting and Analytics**
   - Customizable dashboards for project leaders and stakeholders
   - Task/issue analytics: time spent, effort required, conversion rates, etc.
