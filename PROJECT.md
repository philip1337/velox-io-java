# Mini Projekt - Antrag

Ein Virtuelles File System was es erlaubt Dateien in einem Archiv zu haben und die wieder abzurufen. 
Ziel ist es für eine Software ein Archiv zu liefern was die Dateien nicht direkt von der Festplatte lädt. 
Hierbei werden mehrere Dateien in eine Datei “gepackt”.

Hierbei sollten folgende Vorteile entstehen:
* Schutz vor Diebstahl bzw. Kopien «Piracy»
* Das verändern der Daten soll für einen Normalen Nutzer nicht möglich sein.
* Die Dateien können komprimiert werden.

Das Archiv sollte ungefähr so aufgebaut sein:
Archive Header
* Magic  (Erlaubt es eine Datei zu auf Ihre richtigkeit zu überprüfen).
* Version (Falls sich die Archive ändern sollten brauchen wir eine Version).
* Count (Anzahl Dateien in diesem Archive).

Count * Archive Entry
* Filename (Dateiname)
* Harddisk size (Grösse der Datei in Bytes).
* Compressed size (Grösse der Datei komprimiert).
* Hash (evtl.) (Validierung der Datei, ein CRC Hash).
* Offset (Die Position der Datei in RAW Data)

RAW Data
* Alle Dateien “gepackt”.


Für das Komprimieren der Dateien wird vermutlich Lz4 genutzt.

# Anwendung
Um ein Archiv zu erstellen wird es eine Konsolenapplikation geben wo man eine Liste an Pfaden angeben kann. 
Danach wird ein Archiv erstellt. Die Quelldateien werden nur “Read-Only” geöffnet und werden nicht verändert und auch nicht gelöscht.
