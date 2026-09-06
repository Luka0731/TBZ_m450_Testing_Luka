# Test Strategie



## Übung 1)
### Abstrakte Testfälle
| ID | Input condition | Expected discount |
|---|---|---|
| A1 | price < 15'000 | 0 % |
| A2 | price >= 15'000 AND price <= 20'000 | 5 % |
| A3 | price > 20'000 AND price < 25'000 | 7 % |
| A4 | price >= 25'000 | 8.5 % |
### Konkrete Testfälle
| ID | Input price CHF | Expected discount | Discount amount CHF | Final price CHF | Purpose |
|---|---|---|---|---|---|
| K1 | 0.00 | 0 % | 0.00 | 0.00 | lower edge, trivial case |
| K2 | 14'999.00 | 0 % | 0.00 | 14'999.00 | just below threshold 15'000 |
| K3 | 15'000.00 | 5 % | 750.00 | 14'250.00 | exactly on threshold 15'000 |
| K4 | 17'500.00 | 5 % | 875.00 | 16'625.00 | representative value inside A2 |
| K5 | 20'000.00 | 5 % | 1'000.00 | 19'000.00 | exactly on threshold 20'000 |
| K6 | 20'001.00 | 7 % | 1'400.07 | 18'600.93 | just above threshold 20'000 |
| K7 | 22'500.00 | 7 % | 1'575.00 | 20'925.00 | representative value inside A3 |
| K8 | 24'999.00 | 7 % | 1'749.93 | 23'249.07 | just below threshold 25'000 |
| K9 | 25'000.00 | 8.5 % | 2'125.00 | 22'875.00 | exactly on threshold 25'000 |
| K10 | 30'000.00 | 8.5 % | 2'550.00 | 27'450.00 | clearly inside A4 |



---



## Übung 2)
* **Testobjekt:** Europcar Switzerland, https://www.europcar.ch
* **Testart:** functional Black-Box tests
* **Testumgebung:** Windows 11, Chrome
| ID | Description | Expected Result | Acctual Resultat |
|---|---|---|---|
| 1 | Search for available cars with valid input: pickup "Zürich Flughafen", pickup 15.09.2026 10:00, return 18.09.2026 10:00 | A list of available vehicle categories is shown, each with a price for the 3-day period. Chosen location and dates are displayed unchanged. | |
| 2 | Search with an invalid date range: return date (15.09.2026) is before the pickup date (18.09.2026) | The search is not executed. A clear error message points to the invalid date range and the user stays on the form with the entered values kept. | |
| 3 | Complete a booking end to end: select a vehicle, enter driver data, confirm the reservation | The booking is accepted, a confirmation page with a unique booking reference is shown and a confirmation email is sent to the address entered. | |
| 4 | Verify the price calculation: compare the daily rate shown in the result list against the total in the final booking summary for a 3-day rental | Total = daily rate × 3 plus the listed fees/taxes. The breakdown in the summary adds up exactly to the total charged, no hidden difference. | |
| 5 | Cancel an existing booking via "Manage your booking" using the booking reference from TC3 | The booking is found, cancellation is confirmed on screen, the status changes to "cancelled" and a cancellation email is sent. The booking can no longer be used. | |



---



## Übung 3)
### White-Box Testfälle
Diese Testfälle basieren auf dem Quellcode (Klassen und Methoden), nicht auf der Konsolen-Oberfläche.
Aufgerufen werden die Methoden direkt, nicht über das Menü.
| ID | Target Object or Class:Method/s | Description | Input | Expected Output | Actual Output |
|---|---|---|---|---|---|
| W1 | Account:withdraw | Betrag kleiner als Kontostand abheben (Normalfall, else-Zweig) | Konto mit balance = 1000.00, withdraw(400.00) | return `true`, getBalance() = 600.00 | |
| W2 | Account:withdraw | Grenzwert: Betrag exakt gleich Kontostand (Bedingung `amount > balance` ist false) | balance = 1000.00, withdraw(1000.00) | return `true`, getBalance() = 0.00 | |
| W3 | Account:withdraw | Grenzwert: Betrag knapp grösser als Kontostand (if-Zweig) | balance = 1000.00, withdraw(1000.01) | return `false`, getBalance() unverändert 1000.00 | |
| W4 | Account:withdraw | Betrag 0 abheben | balance = 1000.00, withdraw(0.00) | return `true`, getBalance() = 1000.00 | |
| W5 | Account:withdraw | Negativen Betrag abheben (keine Validierung im Code vorhanden) | balance = 1000.00, withdraw(-100.00) | Laut Fachlogik müsste die Eingabe abgelehnt werden. Der Code liefert `true` und getBalance() = 1100.00 | |
| W6 | Account:deposit | Normale Einzahlung | balance = 100.00, deposit(50.00) | getBalance() = 150.00 | |
| W7 | Account:deposit | Negative Einzahlung (keine Validierung im Code vorhanden) | balance = 100.00, deposit(-50.00) | Laut Fachlogik müsste die Eingabe abgelehnt werden. Der Code liefert getBalance() = 50.00 | |
| W8 | Account:getId / Account.counter | Statischer Zähler: zwei neue Konten hintereinander erstellen | zwei mal `new Account(...)` | Die IDs sind fortlaufend und unterschiedlich | |
| W9 | Account:getId / Account.counter | Statischer Zähler über zwei Bank-Instanzen hinweg | `new Bank()` + Konto, danach nochmals `new Bank()` + Konto | Erwartet wäre pro Bank eine eigene Nummerierung ab 1. Der Code zählt global weiter, weil `counter` static ist | |
| W10 | Account:pseudoDeleteAccount | Alle Felder werden zurückgesetzt | Konto mit Daten, pseudoDeleteAccount() | getId() = 0, getBalance() = 0.00, getCurrency() = null, getUserLastName() = null | |
| W11 | Bank:createAccount | Konto wird erstellt und der Liste hinzugefügt | neue Bank, createAccount("Meier", CHF, 500.00) | Rückgabe ist ein Account mit Nachname "Meier", Währung CHF, Kontostand 500.00; getNumberOfAccounts() = 1 | |
| W12 | Bank:getAccount | Vorhandene Kontonummer suchen (Treffer in der Schleife) | Bank mit 5 Konten, getAccount(3) | Account mit getId() = 3 | |
| W13 | Bank:getAccount | Nicht vorhandene Kontonummer suchen (Schleife läuft durch) | Bank mit 5 Konten, getAccount(99) | return `null` | |
| W14 | Bank:getAccount | Suche in leerer Bank | neue Bank ohne Konten, getAccount(1) | return `null` | |
| W15 | Bank:deleteAccount | Konto wird aus der Liste entfernt | Bank mit 5 Konten, deleteAccount(Konto 3) | getNumberOfAccounts() = 4, getAccount(3) = null | |
| W16 | Bank:getNumberOfAccounts | Anzahl nach mehreren Erstellungen | neue Bank, 3 mal createAccount(...) | return 3 | |
| W17 | Counter:convertCurrency | Zweig 1: USD nach CHF (Kurs 1.11) | convertCurrency(100.00, USD, CHF) | 111.00 | |
| W18 | Counter:convertCurrency | Zweig 2: USD nach EUR (Kurs 0.91) | convertCurrency(100.00, USD, EUR) | 91.00 | |
| W19 | Counter:convertCurrency | Zweig 3: CHF nach USD (Kurs 0.9) | convertCurrency(100.00, CHF, USD) | 90.00 | |
| W20 | Counter:convertCurrency | Default-Zweig: Kombination ist nicht implementiert | convertCurrency(100.00, EUR, CHF) | Betrag unverändert 100.00 plus Meldung "! Es wurde keine Umrechnung vorgenommen." | |
| W21 | Counter:convertCurrency | Default-Zweig: gleiche Währung | convertCurrency(100.00, CHF, CHF) | Betrag unverändert 100.00 plus Meldung | |
| W22 | Counter:convertCurrency | Hin- und Rückumrechnung ergibt nicht den Ausgangsbetrag | 100.00 CHF nach USD, Resultat zurück nach CHF | Erwartet wären wieder 100.00. Der Code liefert 99.90, weil 0.9 und 1.11 nicht zueinander passen | |
| W23 | Counter:transferAmount | Überweisung zwischen Konten gleicher Währung | Konto A (CHF, 1000.00) nach Konto B (CHF, 0.00), Betrag 200.00 | A = 800.00, B = 200.00 | |
| W24 | Counter:transferAmount | Überweisung mit Währungsumrechnung | Konto A (USD, 1000.00) nach Konto B (CHF, 0.00), Betrag 100.00 | A = 900.00 USD, B = 111.00 CHF | |
| W25 | ExchangeRateOkhttp:getExchangeRate | Fehlerfall der externen Schnittstelle (catch-Zweig) | Aufruf ohne Netzwerkverbindung | return 0.00 plus Fehlermeldung auf der Konsole | |
### Black-Box Testfälle
#### Result Abkürzungen
Viele Resultate der Testefälle wiederhollen sich. Also habe ich diese Tabelle erstellt um Redudanzen zu sparen.
| ID | Description | Result |
|---|---|---|
| R1 | Wenn eine ungültige eingabe passiert | Die Konsole sagt dir warum dieser Input nicht akzeptiert wird und du kriegst einen weiteren Eingabeversuch, bis du es richtig hinbekommst |
| R2 | Wenn eine eingabe passiert die korigierbar vom system ist. Beispiel: "A" anstadt "a", "a " andtadt "a" | Es geht weiter wie der standart success-path dieses Testfalles |
#### Testfälle
| ID | Area:Input-Interface | Description | Expected Result | Acctual Resultat |
|---|---|---|---|---|
| T1 | Akktionsauswahl: | q eingeben | SUCESS PATH: Eine Tschüss-Nachricht erscheint und das Programm schliesst sich | |
| T2 | Akktionsauswahl: | a eingeben | SUCESS PATH: Es zeigt alle erstellten Konten an und dessen gebrauchten Währung  | |
| T3 | Akktionsauswahl: | w eingeben | SUCESS PATH: Es leitet dich zur Wechselkursabfragung weiter | |
| T4 | Akktionsauswahl: | e eingeben | SUCESS PATH: Es leitet dich zur Kontokreation weiter | |
| T5 | Akktionsauswahl: | "a " eingeben | #R1 | |
| T5 | Akktionsauswahl: | "W" eingeben | #R2 | |
| T5 | Akktionsauswahl: | "Wasser" eingeben | #R1 | |
| T6 | Akktionsauswahl: | ungültige Opption eingeben | #R1 | |
| T7 | Akktionsauswahl: | enter drücken ohne etwas einzugeben | #R1 | |
| T8 | Kontokreation:Nachnamen | "Günter" eingeben | SUCESS PATH: es geht weiter zur Währungskürzel abfrage | |
| T9 | Kontokreation:Nachnamen | "Günter " eingeben | #R2 | |
| T10 | Kontokreation:Nachnamen | bereits existierenden namen eingeben | duplikate sind okay, es geht den normalen success-path weiter | |
| T11 | Kontokreation:Nachnamen | enter drücken ohne etwas einzugeben | #R1 | |
| T12 | Kontokreation:Nachnamen | " " eingeben | #R1 | |
| T13 | Kontokreation:Nachnamen | speziele zeichen eingeben die das Sysytem nicht speichern kann, wie zum Beispiel ü, é, ç, [] | #R1 | |
| T14 | Kontokreation:Nachnamen | zeichenkombinationen eingeben die etwas auslösen könnten wie zum Beispiel "Bern\tHard" eingeben | #R2 | |
| T15 | Kontokreation:Währungskürzel | alle gültigen Währungen eingeben | SUCESS PATH: Die generierte Kontonummer, der Name wie du es eingegeben hast und einen 0.00 der Währung die du eingegebn hast sollten angezeig werden. Danach kommst du wieder zur Akktionsauswahl. Das konnto sollte nun bei der der eingabe von "a" in der Akktionswahl ebenfalls angezeigt werden | |
| T16 | Kontokreation:Währungskürzel | "eur" eingeben | #R2 | |
| T17 | Kontokreation:Währungskürzel | "eUr" eingeben | #R2 | |
| T18 | Kontokreation:Währungskürzel | "EUR " eingeben | #R2 | |
| T19 | Kontokreation:Währungskürzel | " EUR" eingeben | #R2 | |
| T20 | Kontokreation:Währungskürzel | "EUR CHF" eingeben | #R1 | |
| T21 | Kontokreation:Währungskürzel | "USDAMERICA" eingeben | #R1 | |
| T22 | Kontokreation:Währungskürzel | ungültige Opption eingeben | #R1 | |
| T23 | Kontokreation:Währungskürzel | enter drücken ohne etwas einzugeben | #R1 | |
| T24 | Kontokreation | Nach dem Erforlreicher Kontoerstellung |  | |
| T25 | Wechselkursabfragung: | alle gültigen kombinationen eingeben | SUCESS PATH: Der Wechselkurse wird korrekt angezeigt und die Weschelkursabragung ist abgeschlossen. Es bringt dich zurück zur Akktionsauswahl | |
| T26 | Wechselkursabfragung: | "EURCHF" eingeben | #R2 | |
| T27 | Wechselkursabfragung: | "eur chf" eingeben | #R2 | |
| T28 | Wechselkursabfragung: | "eUr cHF" eingeben | #R2 | |
| T29 | Wechselkursabfragung: | "EUR CHF " eingeben | #R2 | |
| T30 | Wechselkursabfragung: | " EUR CHF" eingeben | #R2 | |
| T31 | Wechselkursabfragung: | "EUR CHF USD" eingeben | #R1 | |
| T32 | Wechselkursabfragung: | "USD" eingeben | #R1 | |
| T33 | Wechselkursabfragung: | "USD   CHF" eingeben | #R2 | |
| T34 | Wechselkursabfragung: | ungültige Opption eingeben | #R1 | |
| T35 | Wechselkursabfragung: | enter drücken ohne etwas einzugeben | #R1 | |

### Code Verbesserungsvorschläge (Best Practices)
* Ein Enum ist im Main.java File drinnen. Jede Klasse hat ein eigenes File, also separieren
* Offensichtlich ist ja der Bank Code nicht komplett fert und es ist auch nur ein Beispielprojekt, also wäre es verkehrt alles zu beurteilen. Deswegen zäghle ich nicht alle Best PRactices auf die mir so einfallen, wie z. B. dass man nicht umbedingt einen public leeren constructor auschreiben muss.
* Wenn man String inputs bekommt, immer noch trim machen. Sonst wird ein Lehrzeichen am ende der Eingabe als andere Eingabe angesehen. Das führt zu fehlern "EUR " ist dann nicht das gleiche wie "EUR".
* Farben verwenden, z.B. error messages stechen mehr raus wenn sie rot gefärbt sind.







### Ergänzungen zu den Best Practices (aus der Code-Sicht)

* `Account.counter` ist `static`. Die Kontonummern laufen dadurch global über alle Bank-Instanzen
  weiter und werden nie zurückgesetzt. Für Tests ist das schlecht, weil die Testfälle dann
  voneinander abhängen: das Resultat hängt davon ab, wie viele Konten vorher erstellt wurden.
* `deposit()` und `withdraw()` prüfen den Betrag nicht auf negative Werte. Ein negativer
  Abhebungsbetrag erhöht den Kontostand.
* `convertCurrency()` deckt nur 3 von 6 möglichen Währungskombinationen ab. Die restlichen
  laufen still in den Default-Fall und geben den Betrag unverändert zurück.
* Die Kurse sind fest im Code als Konstanten hinterlegt und nicht konsistent
  (CHF nach USD und zurück ergibt nicht wieder den Ausgangsbetrag).
* `convertCurrency()` ist `private` und lässt sich daher von aussen nicht direkt testen.
  Für White-Box-Tests müsste die Sichtbarkeit mindestens auf package-private gesetzt werden.
* Der API-Key von apilayer.com steht im Klartext im Quellcode. Solche Secrets gehören in eine
  Konfigurationsdatei oder Umgebungsvariable, nicht ins Repository.
* `catch (Exception e)` fängt alles ab und unterscheidet danach mit `instanceof`. Besser wäre,
  die konkreten Exception-Typen direkt in eigenen catch-Blöcken zu behandeln.
* Tippfehler im Klassennamen `AccountExeption` (richtig: `AccountException`). Zudem ist sie eine
  innere Klasse von `Counter` und gehört in ein eigenes File.
* `ExchangeRateOkhttp` ruft direkt eine externe API auf. Für Unit-Tests müsste diese
  Schnittstelle weggemockt werden, sonst hängen die Tests von Netzwerk und Fremdsystem ab.