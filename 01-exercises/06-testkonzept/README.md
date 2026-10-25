# Testkonzept TBZ Students



## 1. Introduction
TBZ Students ist eine Webapplikation zur Verwaltung von Studenten. Man kann alle Studenten anzeigen und neue mit Name und E-Mail erfassen. Frontend: Angular, Backend: Spring Boot, Datenbank: H2 (in-memory). Das Konzept orientiert sich an IEEE 829.



## 2. Test Items
```text
+---------------------+   HTTP / JSON   +-----------------------+   JPA   +-------------+
|  Angular Frontend   |  ===========>   |  Spring Boot Backend  |  ====>  |   H2 DB     |
|  localhost:4200     |                 |  REST API :8081       |         |  in-memory  |
|  [1] StudentList    |                 |  [3] StudentController|         |             |
|  [2] StudentForm    |                 |  [4] StudentRepository|         |             |
+---------------------+                 +-----------------------+         +-------------+
```

| Nr | Test Item | Beschreibung |
|---|---|---|
| 1 | StudentListComponent | Tabelle mit allen Studenten |
| 2 | StudentFormComponent | Formular zum Erfassen |
| 3 | StudentController | REST API `GET` und `POST /students` |
| 4 | StudentRepository | Speichert und lädt Studenten via JPA |



## 3. Features to be tested
| ID | Feature | Tool |
|---|---|---|
| F1 | Studenten werden geladen und angezeigt | JUnit, Postman, Selenium |
| F2 | Neuer Student wird erfasst und erscheint in der Liste | JUnit, Postman, Selenium |
| F3 | Submit ist erst aktiv, wenn Name und E-Mail ausgefüllt sind | Selenium |
| F4 | Fehlerhafte Requests werden abgelehnt (400, 405, 415) | Postman |
| F5 | Eingabevalidierung im Backend (neu, mit TDD) | JUnit, Postman |
| F6 | Antwortzeiten unter Last | JMeter |



## 4. Features not to be tested
* Login und Berechtigungen (gibt es nicht)
* Bearbeiten und Löschen (gibt es nicht)
* Frameworks wie Spring, Angular und H2 selbst
* Design, Usability und mobile Geräte
* Security Tests wie Penetration Tests



## 5. Approach
Neue Features werden mit **TDD** umgesetzt:

1. **Red**: Test schreiben, der fehlschlägt
2. **Green**: So wenig Code wie möglich, bis der Test grün ist
3. **Refactor**: Code aufräumen, Tests bleiben grün

Beispiel F5: Der Test «POST mit leerem Namen liefert 400» ist zuerst rot. Danach ergänzen wir `@Valid` und `@NotBlank`, bis er grün ist.

Teststufen: Unit Tests (laufend), Integrationstests (wenn Controller und Repository zusammenspielen), danach API Tests, E2E Tests und am Schluss ein Lasttest.



## 6. Item pass / fail criteria
Ein Test ist erfolgreich, wenn alle Assertions erfüllt sind. Ein Feature ist fertig, wenn alle Tests dazu grün sind. Lasttest: Bei 50 Usern liegt p95 unter 2 Sekunden und die Fehlerrate unter 1 %.

| Fehlerklasse | Beispiel |
|---|---|
| Gering | Hinweis «Name is required» erscheint nach dem Leeren nicht mehr |
| Mittel | Backend speichert leeren Studenten |
| Schwer | Liste lädt nicht, Backend liefert 500 |



## 7. Environmental Needs
| Bereich | Tools |
|---|---|
| Entwicklung | JDK 17, Maven, Node.js 18, IntelliJ |
| Unit Tests | JUnit 5, Mockito, Jasmine |
| API Tests | Postman, Newman |
| E2E Tests | Selenium 4, Chrome |
| Lasttest | JMeter 5.6.3 |



## 8. Schedule
| Woche | Aufgabe |
|---|---|
| 1 | Testkonzept, Umgebung aufsetzen |
| 2 | Unit Tests, F5 mit TDD |
| 3 | API Tests und E2E Tests |
| 4 | Lasttest, Fehler beheben |