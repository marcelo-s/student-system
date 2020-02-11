# Problem 1
Create a REST API for a system that assigns students to classes.  API can be used by both a UI and programmatically by other systems.

Deliverables:

1.  (required) java code via a GitHub repository.
2.  (required) a short write-up  around what technologies/frameworks you are/would use in implementing various parts/tiers of this system.
3.  (optional) deployable/runnable war/jar.
4.  (optional) API documentation

# Technologies
- Java 11
- Spring Boot
- Lombok
- H2
- JPA

# Framework
Spring Framework

# Architecture

### Entities
- Student: Entity that represents Students. A student can be attend many classes

- ClassOfStudents: Entity that represents a class. A class can have many students.

- ClassAttendance: Join Entity, resulting of the many to many association between students and classes.
This is an independent Entity with its own Id and two foreign keys for Student and ClassOfStudents.
This was made in order to add more attributes in the future for this relationship, for example: "grades",
or in the case that a same student has attended the same class multiple times.
 

### API Layer - Controllers :
- StudentController: Has the root mapping : "/students"
This controller handles the different endpoints for the CRUD operations on Students.
Also this controller handles adding a student to a specific class.

- ClassOfStudentsController: Has the root mapping : "/classes"
This controller handles the different endpoints for the CRUD operations on ClassOfStudents.

### Service Layer - Services
- Interfaces: There are 2 interfaces for Students and ClassOfStudents that define the service functionalities.
- Implementations: There are 2 implementations, one for each interface. These implementations use the repositories to
respond to the different API operations. 

### Persistence Layer -  Repository, Model

- Models: 3 main models : Student, ClassOfStudents and ClassAttendance.

Student has a one to many relationship with ClassAttendance.

ClassOfStudents has a one to many relationship with ClassAttendance.

ClassAttendance has a many to one relationship with Student and ClassOfStudents

- Repositories: 
LoadDB : Class with preloaded data to test
StudentRepository, ClassOfStudentsRepository and ClassAttendanceRepository have different methods to interact
with the Database

## Exception Handling
Used the @ControllerAdvice annotation to have control over the different exceptions across the whole application

Specific errors handled:
 ```
EntityNotFoundException
```
 This exception is thrown whenever an invalid Id (non-existent) is passed as a parameter

##Build
The whole project is available as a .jar file on the target/ folder



