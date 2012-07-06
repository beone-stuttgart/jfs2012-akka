Beispiele zum Vortrag "Baby Steps mit Akka"
===========================================

Dies sind die vollständigen Beispielprogramme des Vortrags
[Baby Steps mit Akka](http://www.java-forum-stuttgart.de/abstracts.html#A7).


Übersetzen und Generieren von Projektdateien
--------------------------------------------

Die Sourcen können mit [Gradle](http://gradle.org/) übersetzt werden. Es
ist nicht zwingend erforderlich, hierfür Gradle zu installieren. Der
Gradle-Wrapper `gradlew` kümmert sich um alle benötigten Abhängigkeiten.
Mittels

```bash
gradlew idea
gradlew eclipse
```

werden die abhängigen Bibliothekten heruntergeladen
und die Projektdateien für IntelliJ Idea bzw. Eclipse erzeugt.

