\# StudySync - Smart Study Group Finder and Planner



Software Construction and Development (SCD) project, BS Software Engineering, Semester 6.

Submitted to: Ma'am Muniba



| Member | SAP ID | Main work |

|---|---|---|

| Aliza Rizvi (team lead) | 58782 | Project setup, database, entities, repositories (Repository), ConfigurationManager (Singleton), student services |

| Anum Rehman | 60196 | Matching (Strategy), SessionFactory (Factory), group and session services |

| Mahtab Mazhar | 61307 | Notifications and reminders (Observer), controllers, Thymeleaf pages, CSS/JS |



\## What it does

Students register, add their courses, study style and free time. StudySync suggests study partners and groups,

lets students create and join groups, schedule Online / Physical / Exam Revision sessions, share notes links,

mark attendance, rate each other, see session history and get notifications and reminders.



\## Technologies

Java 21, Spring Boot 3.5 (MVC), Thymeleaf, MySQL 8.4, HTML, CSS, Bootstrap 5, JavaScript, JUnit 5, Mockito, Maven, Git/GitHub.



\## Design patterns

| Pattern | Where |

|---|---|

| MVC | `controller/` (Controller), `templates/` (View), `model/` + `service/` (Model) |

| Repository | `repository/` - StudentRepository, GroupRepository, SessionRepository and 7 more |

| Factory | `factory/SessionFactory` creates OnlineSession, PhysicalSession, ExamRevisionSession |

| Singleton | `config/ConfigurationManager.getInstance()` |

| Observer | `observer/SessionNotifier` (subject), NotificationObserver and ActivityLogObserver |

| Strategy | `strategy/` - AvailabilityMatchingStrategy, GradeMatchingStrategy, StudyStyleMatchingStrategy |



\## How to run

1\. Install JDK 21, MySQL 8.4 (root password `studysync123`) and IntelliJ IDEA.

2\. Open the project in IntelliJ and wait for Maven to download the libraries.

3\. Make sure the MySQL service is running. The database `studysync\_db` is created automatically.

4\. Run `StudysyncApplication` and open http://localhost:8080

5\. Demo login: `ali@demo.com` / `demo123` (also sara@, hamza@, fatima@demo.com).



\## Tests

Right-click `src/test/java` > Run 'All Tests', or run `mvnw.cmd test`. 24 unit tests (JUnit 5 + Mockito), all passing.



\## Documentation

UML diagrams (class, ER, deployment) are in `docs/uml/`. The database export is in `database/studysync\_db.sql`.

