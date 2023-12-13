CO2103 University Module Management System

This repository contains the source code for a simple University Module Management System developed using the Spring Boot framework. The system allows for the management of modules, convenors, and sessions.

Features

Convenor Management:
  View a list of all convenors.
  Create a new convenor with a name and position.
  View details of a specific convenor by ID.
  Update convenor details by ID.
  Delete a convenor by ID.

Module Management:
  View a list of all modules.
  Create a new module with a code, title, level, and optional status.
  Update module details by code.
  Delete a module by code.
  View all sessions associated with a module.
  Add a new session to a module.

Session Management:
  View a list of all sessions.
  Delete all sessions.
  Filter sessions based on module code, session ID, or both.
  View details of a specific session by ID.

Getting Started

Clone the repository:

  Copy code
  git clone https://github.com/your-username/CO2103-Module-Management.git

  Open the project in your preferred Java development environment (e.g., IntelliJ, Eclipse).
  Run the Part1Application class to start the Spring Boot application.
  Access the application through your web browser at http://localhost:8080.

Usage

Convenor Management
  To view all convenors: http://localhost:8080/convenors
  To create a new convenor: http://localhost:8080/convenors?name=ConvenorName&position=PROFESSOR
  To view a specific convenor: http://localhost:8080/convenors/1
  To update a convenor: http://localhost:8080/convenors/1?name=NewName&position=LECTURER
  To delete a convenor: http://localhost:8080/convenors/1

Module Management
  To view all modules: http://localhost:8080/modules
  To create a new module: http://localhost:8080/modules?code=CO1234&title=ModuleTitle&level=3&optional=false
  To update a module: http://localhost:8080/modules/CO1234?title=NewTitle&level=4&optional=true
  To delete a module: http://localhost:8080/modules/CO1234
  To view sessions of a module: http://localhost:8080/modules/CO1234/sessions
  To add a session to a module: http://localhost:8080/modules/CO1234/sessions?topic=NewSession&time=2022-01-01T12:00:00&duration=2

Session Management
  To view all sessions: http://localhost:8080/sessions
  To delete all sessions: http://localhost:8080/sessions
  To filter sessions by module and/or session ID: http://localhost:8080/sessions?id=1&code=CO1234
  To view details of a specific session: http://localhost:8080/sessions/1


License
  This project is licensed under the MIT License - see the LICENSE file for details.

Peter Nicholl (@pn98)

