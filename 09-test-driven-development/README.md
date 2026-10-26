# Test Driven Development



## Aufgabe 1 & 2
- [Projekt ist hier](./FancyApp)
- ```vendor\bin\phpunit --testdox Testcases\Test.php```
#### Ablauf
- [Lösung](./FancyApp/Application/MyFancyClass.php)
1. Red: alle 3 Tests rot
2. `shortString` mit `strlen`: immer noch rot, weil `strlen` Bytes zählt und "ä" 2 Bytes hat
3. Green: mit `mb_strlen` / `mb_substr` grün, danach `calcAverage` und `getOpposite`
4. Refactor: nicht nötig, Code ist schon kurz



## Aufgabe 3
TODO