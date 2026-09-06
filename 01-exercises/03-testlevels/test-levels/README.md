# Test Levels



## Übung 2)
#### Testing approach
Der Approach beschreibt, mit wie viel Wissen über das Innenleben getestet wird.
* White-Box: der Blick in den Quellcode ist erlaubt. Die Tests werden mit Kenntnis der inneren Funktionsweise entwickelt.
* Black-Box: Nur das von aussen sichtbare Verhalten fliesst in den Test ein. Die Tests werden nur anhand der Spezifikation bzw. der Anforderungen entwickelt. 
#### Testing levels
Die Levels beschreiben, auf welcher Stufe im Entwicklungsprozess und mit welcher Grösse des
Testobjekts getestet wird.
Unit → Component → Integration → System → Acceptance
| Level | Testobjekt | Wer testet | Üblicher Approach |
|---|---|---|---|
| Unit Testing | einzelne Klasse oder Methode in Isolation | Entwickler | White-Box |
| Component Testing | Zusammenspiel mehrerer Komponenten, Schnittstellen gemockt | Entwickler | White-Box |
| Integration Testing | echte Schnittstellen (DB, API, Message Queue), nicht mehr gemockt | Tester / QA-Team, teilweise Entwickler | Black-Box und White-Box |
| System Testing | die Software als Ganzes in produktionsnaher Umgebung | gleiches Team wie Integration | Black-Box |
| Acceptance Testing | das System gegen die Akzeptanzkriterien | Business / Kunde | Black-Box |
#### Testing types, techniques and tactics
Diese Gruppe beschreibt, was inhaltlich geprüft wird und mit welchem Vorgehen
* **Types** -> welche Art von Anforderung geprüft wird:
    * **Funktionale Tests**: Tut die Software das, was sie tun soll?
    * **Nichtfunktionale Tests**: Wie gut tut sie es?
        * Performance Testing
        * Usability Testing
        * Security Testing
        * Noch mehr
* **Techniques** -> basically Testing approach
* **Tactics** -> wie in der Praxis vorgegangen wird
    * Schnittstellen mocken oder echt verwenden
    * manuell testen oder automatisiert im Build ausführen
    * wann die Tests laufen (bei jedem Build, beim Deployment, nächtlich)
### Abhängigkeiten untereinander
Das Level ist die zentrale Grösse. Aus ihm ergeben sich Rolle, Approach, sinnvolle Types und die passenden Tactics.