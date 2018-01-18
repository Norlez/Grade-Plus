## Anmerkungen

### Project-Stage

In der Datei `web.xml` wurde der Parameter `javax.faces.PROJECT_STAGE` auf
`Development` gesetzt. Diese Einstellung verhindert unter anderem das Caching
von Ressourcen durch JSF und vereinfacht somit die Entwicklungsphase. Für den
Produktiveinsatz sollte der Parameter jedoch auf `Production` gesetzt, oder der
Eintrag gänzlich entfernt werden, was den selben Effekt hat.

### Erstellen des Archivs

Der folgende Befehl erstellt das war-Archiv, welches anschließend im Verzeichnis
`target` zu finden ist.

```bash
mvn package
```

Das Projektverzeichnis lässt sich jederzeit wieder mit:

```bash
mvn clean
```

aufräumen.

### Starten der Applikation

Nachdem das Archiv erstellt wurde, kann der Server (Embedded Payara) mit
folgendem Befehl gestartet werden:

```bash
mvn embedded-payara:run
```

Anschließend kann die Applikation unter `http://localhost:8080/gradelog/`
aufgerufen werden. Um die Anwendung nutzen zu können, muss sich der User
zunächst mit einem bestehenden Benutzerkonto einloggen. Es ist ein
Standardbenutzer eingerichtet (s.u.). Weitere Benutzer können unter
`http://localhost:8080/gradelog/admin/users.xhtml` angelegt werden. Um den Server
zu beenden, muss der Buchstabe `x` in das Terminal eingegeben werden. Ein
Redeployment wird durch ein einfaches `ENTER` durchgeführt. Damit Änderungen im
Quellcode auch übernommen werden, muss das Archiv wie oben bereits beschrieben
vor dem Redeployment erneut erstellt werden.

### Standard-Benutzer

Beim Start der Applikation wird durch die Methode `init` der Klasse
`DBInit` im Paket `de.unibremen.st.gradelog.persistence` ein Standard-Benutzer
angelegt mit den Nutzerdaten, die als Konstanten in derselben Klasse
definiert sind.

### Debugging der Applikation

Im Folgenden gehen wir davon aus, dass das Projekt erfolgreich in eines der drei
IDEs importiert wurde: Netbeans, IntelliJ, Eclipse.

#### Netbeans
In den Projekteigenschaften (`File` -> `Project Properties (GradeLog)`) die Kategorie
`Actions` auswählen. In der `default-configuration` im Listenfeld `Actions` die Aktion
`Debug project` auswählen. Im Feld `Execute Goals` `embedded-payara:run` eintragen.
Dann die Schaltfläche `Add>` unterhalb von `Set Properties:` anklicken und aus
der sich öffnenden Liste `Debug Maven build` auswählen. In dem zugehörigen 
Textfeld sollten sich dann die folgenden zwei Zeilen befinden:
```
netbeans.deploy.debugmode=true
jpda.listen=maven
```
Über die Schaltfläche `Debug Project` in der Toolbar oder die Taste `F11` kann
jetzt eine Debug-Session für das Projekt gestartet werden und diese sollte
an gesetzten Breakpoints anhalten.

Ein evtl. bereits laufender embedded Payara-Server muss zuvor natürlich
beendet werden.

#### IntelliJ
Zunächst sollte sichergestellt sein, dass die `Tool Buttons` (üblicherweise
rechts an der Seite) eingeblendet sind. Dazu muss der Haken bei `View` ->
`Tool Buttons` gesetzt sein. Anschließend muss eine neue `Run Configuration`
erstellt werden, die den Payara-Server aus IntelliJ heraus startet. Dazu
sind folgende Schritte notwendig: `Maven Projects` (üblicherweise ganz rechts
an der seite) -> `GradeLog` -> `Plugins` -> `embedded-payara` -> Rechtsklick
auf `embedded-payara:run` -> `Create 'gradelog [embedded-p...'...`. Es
öffnet sich ein Dialog, der mit einem Klick auf `OK` bestätigt werden muss. Nun
kann der Server über den üblichen Mechanismus im Debugmodus gestartet werden
und sollte an gesetzten Breakpoints anhalten.

#### Eclipse
Zunächst wird eine `Run Configuration` zum Starten des Payara-Servers 
erstellt. Dies ist z. B. möglich durch Rechtsklick auf das Projekt und Auswahl 
von `Run As` -> `Maven build...`. Dann öffnet sich ein Dialog, in dem oben ein
passender Name und als `Goals` `embedded-payara:run` eingegeben wird.
Der Start des Debug-Laufs kann nun aus dem Menü gewählt werden: 
`Run` -> `Debug Configurations` und im erscheinenden Dialog aus der Liste den 
vorher vergebenen Namen auswählen und auf den `Debug`-Button klicken.

### Test Report
In der POM ist das Plugin `maven-surefire-report-plugin` eingebunden. Dieses
erlaubt die Generierung von Reports bzgl. der Testausführung als Sammlung von
HTML-Dateien. Wird `mvn site` ausgeführt, wird eine Sammlung von HTML-Dateien
erzeugt, die generelle Projektinformationen und eben auch die Test-Reports
enthält. Die Hauptseite ist zu finden unter `target/site/index.html`.
Wenn nur ein Testreport gewünscht wird, kann dieser mittels
`mvn surefire-report:report` erzeugt werden. Die Hauptseite des Reports
findet sich dann unter `target/site/surefire-report.html`.

### UI-Tests
Es gibt einen Test für den Anwendungsfall *Benutzer registrieren*. Er ist
zu finden in in den Test-Paketen unter 
`de.unibremen.st.gradelog.uitest.RegisterUserIT`. 
Ausgeführt wird er als Integrationstest mit dem 
`failsafe`-Plugin von Maven. Ausgeführt werden kann er also durch
```
mvn failsafe:integration-test
```

Der UI-Test ist so konfiguriert, dass er einen eigenen Container startet
(`Payara-embedded`) und ein eigens für den Test erstelltes Archiv auf
diesen Container deployt. Als Browser wird `PhantomJS` verwendet, der
keine GUI anzeigt (Stichwort: *headless*), sondern im Hintergrund arbeitet.
Dieser Browser soll weitgehend kompatibel zu `Firefox` und `Chrome` sein.
Da er headless arbeitet, kann er optimal im Rahmen der Continous Integration
eingesetzt werden.

Falls beim Testen Unsicherheit bzgl. der aktuell angezeigten Seite besteht,
kann ein Screenshot explizit im Testcode angefordert werden:
```
File scrFile = ((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE);
try {
   FileUtils.copyFile(scrFile, new File("screenshot.png"));
} catch (IOException ex) { 
// do what you want in this case. 
}
```
Hier wird der erstellte Screenshot in das Arbeitsverzeichnis (während der
Test läuft, typischerweise das Projektverzeichnis) gespeichert unter dem
Namen `screenshot.png`.

### Architektur

#### Persistenz
Zum Persistieren der Daten wird JPA verwendet. Die zu verwendene Datenquelle
kann in der Datei `resources/META-INF/persistence.xml` konfiguriert und in der
Klasse `JPADAO` mit der Annotation `@PersistenceContext` ausgewählt werden.

#### CDI vs. EJB
Wir verwenden CDI statt der JSF-eigenen Dependency Injection. Seit JSF
2.2 kennt CDI auch den ViewScope, so dass es keinen echten Grund mehr
gibt, in einer Webapplikation auf die veraltete @ManagedBean Dependency
Injection zu setzen. Die ManagedBean-Annotation wird durch @Named ersetzt
und die Injektion selbst erfolgt mittels @Inject. Durch den Umstieg auf
CDI wird die Existenz einer leeren `beans.xml` (in `src/main/webapp/WEB-INF`)
nötig. Diese signalisiert dem Container, dass er nach Klassen mit der
Annotation @named scannen muss um Abhängigkeiten auflösen zu können.
Ohne die `beans.xml` werden die Klassen mit `@named` nicht gefunden.

#### Constructor-Injection
Statt die Attribute direkt zu injizieren, verwenden wir Constructor-Injection.
Es gibt dann einen Konstruktor für die Beans, die die zu injizierenden
Objekte als Argumente erhalten. Dadurch können die Attribute erstens final
gesetzt werden und zweitens die Argumente im Konstruktor auf Plausibilität
(z. B. nicht null) geprüft werden. Damit ist im Fehlerfall schneller
ersichtlich, dass die Dependency Injection nicht funktioniert hat.

#### Beans und Ressourcenallokation
Einige der in dem Package `controller` enthaltenen Beans (z.B. `UserBean`)
ermöglichen den Zugriff auf eine Liste von Objekten (vgl.
`UserBean#getAllUsers()`). Anders als sonst üblich werden diese Listen nicht
als Kopie, sondern direkt zurückgeben. Der Grund dafür ist, dass im Rahmen
des JSF-Lebenszyklus Methoden durchaus häufiger aufgerufen werden können.
Dies macht sich insbesondere bei `h:dataTable` Elementen bemerkbar, wenn ein
neuer Eintrag hinzugefügt und/oder gelöscht wird (es empfiehlt sich
[diesen StackOverflow-Beitrag](http://stackoverflow.com/questions/2090033/why-jsf-calls-getters-multiple-times)
zu lesen). Um ein mehrfaches Kopieren von Listen in den jeweiligen
Zugriffsmethoden zu vermeiden (bzw. zu reduzieren) wird:

1. In jeder Klasse eine spezielle Methoden bereitgestellt die mit
`@PostConstruct` annotiert ist (vgl. `UserBean#init()`), sodass benötigten
Ressourcen (hier Listen) nicht durch Zugriffsmethoden, sondern einmal nach
dem Injizieren eines Objektes allokiert werden.
2. Keine Kopie der Ressource erzeugt, sondern direkt zurückgegeben.

Dieses Konzept eignet sich für jede Art von Ressource, deren Allokation
sehr teuer ist.

#### View Scope für Backing Beans
Der View Scope enthält Beans für eine bestimmte Webseite,
unabhängig von der Anzahl der Requests während der Anzeige der Seite. Dies ist
insbesondere bei dynamischen Webseiten mit Formularen und Tabellen nützlich, wo
zahlreiche Requests in einer Anzeige stattfinden. Damit schließt der View Scope eine
der größten Lücken zwischen dem Request und Session Scope. In der JSF Spezifikation 2.0
war der View Scope nur für JSF verfügbar, mit der JSF Spezifikation 2.2 lässt er sich
jedoch auch für CDI Beans nutzen. Dadurch gibt es nun jedoch zwei Annotationen
viewscoped, die ältere ist unter javax.faces.bean.ViewScoped zu finden,
während die neuere unter javax.faces.view.ViewScoped liegt. [Quelle](http://blog.triona. de/development/jee/ein-kurzer-uberblick-uber-scopes-in-java-ee7.html)
