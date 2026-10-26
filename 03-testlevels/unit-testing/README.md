# Unit Testing



## Aufgabe 1)
[Schau hier ins Projekt](./SimpleCalculator/)



---



## Aufgabe 2)

### 1 Annotationen
#### 1.1 Lifecycle Annotations
| Annotation | Wann wird es ausgeführt | Typischer Anwendungsfall |
|---|---|---|
| `@Test` | markiert eine Methode als Testfall | der eigentliche Testfall |
| `@BeforeEach` | vor jedem Test | Testobjekt frisch aufsetzen |
| `@AfterEach` | nach jedem Test | aufräumen, z.Bsp. temporäre Daten löschen |
| `@BeforeAll` | einmal vor allen Tests (muss static sein) | teure Ressource einmalig starten, z.Bsp. DB-Verbindung |
| `@AfterAll` | einmal nach allen Tests (muss static sein) | diese Ressource wieder schliessen |
#### 1.2 @DisplayName
Gibt dem Test einen lesbaren Namen im Report, statt des Methodennamens. Leerzeichen und
Sonderzeichen sind erlaubt.
```java
@Test
@DisplayName("10 / 0 should throw an ArithmeticException")
void tenDividedByZeroShouldThrowArithmeticException() { ... }
```
#### 1.3 @ParameterizedTest
Führt denselben Test mit verschiedenen Eingabewerten aus. Spart viele fast identische Methoden.
```java
@ParameterizedTest
@CsvSource({
        "2.0,  3.0,  5.0",
        "-2.0, 3.0,  1.0",
        "1.5,  2.25, 3.75"
})
void addParameterized(double summand1, double summand2, double expected) {
    assertEquals(expected, calculator.add(summand1, summand2), DELTA);
}
```
- Hinzufügliche Annotations für @ParameterizedTest
| Annotation | Liefert |
|---|---|
| `@ValueSource` | eine Liste einzelner Werte (`ints`, `strings`, `doubles`) |
| `@CsvSource` | mehrere Parameter pro Durchlauf, als CSV-Zeilen |
| `@CsvFileSource` | dasselbe aus einer CSV-Datei |
| `@EnumSource` | alle Werte eines Enums |
| `@MethodSource` | Werte aus einer eigenen Methode, für komplexe Objekte |
| `@NullSource` / `@EmptySource` | null bzw. leerer Wert, für Validierungstests |
### 1.4 Dokumentation, Struktur und Steuer Annotations
| Annotation | Zweck | Anwendungsfall |
|---|---|---|
| `@Nested` | innere Testklassen zur Gruppierung | alle `divide`-Tests in einer Gruppe zusammenfassen |
| `@Tag("slow")` | Tests kategorisieren | in der Pipeline nur die schnellen Tests laufen lassen |
| `@Disabled("Grund")` | Test vorübergehend ausschalten | Test zu einem bekannten, noch offenen Bug |
| `@RepeatedTest(10)` | Test mehrfach ausführen | Verhalten prüfen, das mit Zufall arbeitet |
| `@Order(1)` mit `@TestMethodOrder` | Reihenfolge festlegen | nur im Ausnahmefall, Tests sollten unabhängig sein |
| `@TestInstance(PER_CLASS)` | eine Instanz für alle Tests | wenn `@BeforeAll` nicht `static` sein soll |
| `@TempDir` | temporäres Verzeichnis | Tests, die Dateien schreiben |
```java
@Nested
@DisplayName("divide")
class DivideTests {
    @Test
    void tenDividedByFourShouldEqualTwoPointFive() { ... }
}
```
### 2 Assertion Methods
#### 2.1 General Assertion Methods
Alle Assertions sind statische Methoden.
Die Reihenfolge der Parameter ist immer **(expected, actual)**.
| Assertion | Prüft | Beispiel |
|---|---|---|
| `assertEquals(exp, act)` | Werte sind gleich | `assertEquals(5.0, calculator.add(2.0, 3.0))` |
| `assertEquals(exp, act, delta)` | gleich innerhalb einer Toleranz | `assertEquals(0.3, calculator.add(0.1, 0.2), 0.0001)` |
| `assertNotEquals(exp, act)` | Werte sind nicht gleich | `assertNotEquals(0.0, kontostand)` |
| `assertTrue(cond)` / `assertFalse(cond)` | Bedingung ist wahr / falsch | `assertTrue(account.withdraw(100.0))` |
| `assertNull(obj)` / `assertNotNull(obj)` | Referenz ist null / nicht null | `assertNull(bank.getAccount(99))` |
| `assertSame(exp, act)` | dieselbe Objekt-Referenz | `assertSame(konto, bank.getAccount(1))` |
| `assertArrayEquals(exp, act)` | Arrays inhaltlich gleich | `assertArrayEquals(new int[]{1,2}, result)` |
| `assertThrows(Typ.class, executable)` | Exception wird geworfen | siehe unten |
| `assertAll(...)` | mehrere Assertions, alle werden ausgeführt | siehe unten |
| `assertTimeout(dauer, executable)` | Code ist schnell genug | `assertTimeout(Duration.ofSeconds(1), () -> service.load())` |
#### 2.2 Delta
Bei `double` darf nie exakt verglichen werden. `0.1 + 0.2` ergibt `0.30000000000000004`, weil
Kommazahlen binär nicht exakt darstellbar sind. Der dritte Parameter ist die erlaubte Abweichung.
```java
assertEquals(0.3, calculator.add(0.1, 0.2), 0.0001);
```
#### 2.3 assertThrows Method
Prüft, dass eine Exception geworfen wird. Der Rückgabewert ist die Exception selbst, dadurch lässt
sich zusätzlich die Fehlermeldung prüfen.
```java
ArithmeticException exception = assertThrows(
        ArithmeticException.class,
        () -> calculator.divide(10.0, 0.0));

assertEquals("Division by zero is not allowed.", exception.getMessage());
```
#### 2.4 assertAll Method
Normalerweise bricht ein Test bei der ersten fehlgeschlagenen Assertion ab. `assertAll` führt alle
aus und zeigt am Schluss sämtliche Fehler.
```java
assertAll("Konto",
        () -> assertEquals("Meier", account.getUserLastName()),
        () -> assertEquals(500.0, account.getBalance(), 0.0001),
        () -> assertEquals(Currency.CHF, account.getCurrency()));
```
#### 2.5 Fehlermeldung
Jede Assertion nimmt als letzten Parameter optional eine Meldung, die bei Fehlschlag erscheint.
```java
assertEquals(5.0, calculator.add(2.0, 3.0), DELTA, "Addition liefert falsches Resultat");
```
### 3 Assumptions
Bricht einen Test ab, statt ihn fehlschlagen zu lassen, wenn eine Vorbedingung nicht erfüllt ist.
Der Test erscheint dann als "skipped".
```java
assumeTrue("CI".equals(System.getenv("ENV")));
```
### 4 Grundsätze
* Ein Test prüft genau eine Sache und ist unabhängig von den anderen Tests.
* Der Methodenname sagt, was geprüft wird, z.Bsp. `twoPlusThreeShouldEqualFive`.
* `double` immer mit `delta` vergleichen, nie exakt.
* Zu jeder Methode gehören auch Negativtests und Grenzwerte (Edgcases), nicht nur der Success-Path.


---



## Aufgabe 3)
* The Bank software is basically a simple bank software.
* THe Main creates a instance of the class Bank. Bank contains all accounts and methods to do interactions and visualisations. 
* Account is a abstract class wich provoides the the important methods for everything to work. HTe implementations then change some of these methods slightly, like the savings account adding a spendings limit.
* The account also hase the history of every Booking. This is a nother class that contains important information like when the Booking was made and how much was transvered.
* BankingUtils, AccountBalanceComparator and AccountBalanceInvertComparator are all utillity classes.



---



## Aufgabe 4)
[Schau hier ins Projekt](./Bank/src/test/java/ch/schule/bank/junit5/)