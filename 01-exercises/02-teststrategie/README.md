# Test Strategie



## Übung 1)
### Abstrakte Testfälle
| ID | Input condition | Expected discount |
|---|---|---|---|
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
**Testobjekt:** Europcar Switzerland, https://www.europcar.ch
**Testart:** functional Black-Box tests
**Testumgebung:** Windows 11, Chrome
| ID | Description | Expected Result | Acctual Resultat |
|---|---|---|---|---|---|
| 1 | Search for available cars with valid input: pickup "Zürich Flughafen", pickup 15.09.2026 10:00, return 18.09.2026 10:00 | A list of available vehicle categories is shown, each with a price for the 3-day period. Chosen location and dates are displayed unchanged. | |
| 2 | Search with an invalid date range: return date (15.09.2026) is before the pickup date (18.09.2026) | The search is not executed. A clear error message points to the invalid date range and the user stays on the form with the entered values kept. | |
| 3 | Complete a booking end to end: select a vehicle, enter driver data, confirm the reservation | The booking is accepted, a confirmation page with a unique booking reference is shown and a confirmation email is sent to the address entered. | |
| 4 | Verify the price calculation: compare the daily rate shown in the result list against the total in the final booking summary for a 3-day rental | Total = daily rate × 3 plus the listed fees/taxes. The breakdown in the summary adds up exactly to the total charged, no hidden difference. | |
| 5 | Cancel an existing booking via "Manage your booking" using the booking reference from TC3 | The booking is found, cancellation is confirmed on screen, the status changes to "cancelled" and a cancellation email is sent. The booking can no longer be used. | |

---



## Übung 3)
Wir wollen ganz grob herausfinden, was für Testfälle es in dieser Software gibt.

* Die Applikation läuft bei Ihnen und Sie können diese testen.
* Identifizieren Sie mögliche Black-Box Testfälle, welche Sie als Benutzer testen können.
* Welche Methoden im Code könnten für White-Box Testfälle verwendet werden?
* Was würden Sie am Code generell verbessern, welche Best Practices fallen Ihnen ein?

Listen Sie Ihre Testfälle tabellarisch auf in einem Markdown-Dokument und stellen Sie Ihre Lösung in Ihr Repository.