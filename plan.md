# Todo

- [ ] Stempelkarte ist aktuell immer drin, vielleicht bei Bedarf an aus. Wollen wir jemandem, der keine Stempelkarte
  hat, auch einfach den 5. Drink umsonst geben? Eher nicht.
- [ ] Badge an Zubereitungstabe, wie viele Bestellungen noch offen sind/noch nicht geschlossen sind.

**US-1 (Must)**

> _Als Barkeeper:in möchte ich Shots in beliebiger Menge – einschließlich ganzer Flaschen – in einer Bestellung erfassen
können, um auch unter Stress große Bestellungen schnell und fehlerfrei abzuwickeln._

_Akzeptanzkriterium: Einzelne Positionen können vor dem Abschicken korrigiert oder entfernt werden._

| #   | Task                                                                                                 | Aufwand |
|-----|------------------------------------------------------------------------------------------------------|---------|
| 1.1 | Long-Press auf Produkt-Button öffnet Mengen-Dialog (z. B. Angular Material Bottom Sheet oder Dialog) | M       |
| 1.2 | Zahlenpad im Dialog: 1–99 + Schnellwahl-Buttons (z. B. 6, 12, 24 für Shot-Flaschen)                  | M       |
| 1.3 | In `OrderSummary`: Minus-Button je Position zum Dekrementieren der Menge                             | S       |
| 1.4 | In `OrderSummary`: Löschen-Button (Mülleimer) je Position für vollständiges Entfernen                | S       |
| 1.5 | Gesamtpreis-Update im `OrderService` reagiert korrekt auf Mengenänderungen (RxJS-seitig prüfen)      | S       |

Erste Ansätze für die longPress-Logik

```ts
@ViewChild('myButton', { static: false })  
button!: ElementRef<HTMLButtonElement>;  
  
ngAfterViewInit() {  
  console.log(this.button.nativeElement);  
  
  const pointerDown$ = fromEvent(this.button.nativeElement, 'pointerdown');  
  const pointerUp$ = fromEvent(this.button.nativeElement, 'pointerup');  
  const pointerLeave$ = fromEvent(this.button.nativeElement, 'pointerleave');  
  const pointerCancel$ = fromEvent(this.button.nativeElement, 'pointercancel');  
  
  const cancel$ = merge(pointerUp$, pointerLeave$, pointerCancel$);  
  
  pointerDown$  
    .pipe(  
      switchMap(() =>  
        timer(500).pipe(  
          takeUntil(cancel$)  
        )  
      ),  
      takeUntilDestroyed()  
    )  
    .subscribe(() => {  
      console.log("pressed!");  
    });  
}
```

**US-5 (Should)**

> _Als Barkeeper:in möchte ich das System auch dann nutzen können, wenn die Verbindung zum Server kurzzeitig
unterbrochen ist, um den Betrieb ohne Unterbrechung fortzuführen._

**US-2 (Should)**

> _Als Organisator:in möchte ich Produktpreise während des laufenden Betriebs über eine separate Admin-Route (
PIN-geschützt) anpassen können, um den Verkauf gezielt anzuregen – mit der Erwartung, dass alle Terminals nach einem
Nachladen den neuen Preis verwenden._

**US-3 (Should)**

> _Als Organisator:in möchte ich nach dem Event eine Auswertung einsehen und exportieren können, um Umsätze
nachzuvollziehen, die Barkasse abzugleichen (Soll vs. Ist) und den Becherbestand zu verifizieren._

**US-4 (Could)**

> _Als Organisator:in möchte ich während des Events den Verbrauch je Produkt und Zutat im Blick haben, um drohende
Engpässe frühzeitig zu erkennen und gegenzusteuern._

## Backlog (nach MoSCoW)

### Must

- [ ] Deployment
  - Via Render? **[Render.com](https://www.google.com/url?sa=i&source=web&rct=j&url=https://render.com/&ved=2ahUKEwiN-YyF_fKSAxWz_7sIHZN0L2wQy_kOegQIBBAB&opi=89978449&cd&psig=AOvVaw3zgtR1lHnwu7AhuiKGJ2y6&ust=1772051672767000):** Der derzeit beliebteste Anbieter für Full-Stack Anwendungen. Es bietet eine einfache Anbindung an GitHub, kostenloses Web-Service-Hosting und Managed PostgreSQL. _Hinweis: Kostenlose Postgres-Instanzen werden nach 90 Tagen gelöscht (Daten müssen gesichert werden)._
    - deployment mit render ist nicht standardweg => weil keine container
    - Auth schwierig => eigentlich cooler wenn es wirklich IPAD App ist dann keine WebAPP die abgesichert werden muss.
    - Miet kubernetes cluster, deploy app in docker containern
    - Kosten vielleicht über RAC abdecken ODER einfach einen Rechner starten und die app da starten und dann per internetfreigabe zugriff, 0hoster
        - dyndns eintrag via router => damit ip auf namen verweist. oder flexdns
        - fritz hat eine feste adresse, die immer gemeldet wird mit ständigr myfritz adresse
    - cors frontend klären, wie ich dynamisch post anfragen an backend schicke (lokal geht reverse proxy)
  - Lokal hosten auf server rac bar ? Und wie demo? Eigener server
  - Oder kurz passwortgeschützt hosten
  - Alternative: Lokale App und am ende werden 3 csvs zusammengefügt

- Technisch
  - Ausfallsicherheit, wenn Netzprobleme?
  - Und was machen bei Reload?
  - was ist wenn backend nicht erreichbar (kein wlan?) -> nicht in order tap erst auf erfolgreichen post machen

- [ ] Trinkgeld eintragen
- [ ] Eigenbedarf erfassen

### Should
- [ ] Wann geht was aus?
  - [ ] Annahme Eis
- [ ] Auswertung
- [ ] Auswertung Export
- [ ] Kreditkartenzahlung / SumUP / Paypal anbinden - Stand

- [ ] Long-Press für remove und large quantity
- [ ] Drinkberechnung - wann laufen wir estimatedly leer?
- [ ] Auswertungen unter /auswertung
    - [ ] Soll-Ist Becher Abgleich
        - [ ] Backend Berechnung
        - [ ] Netto-Becher Bewegung je Bestellung
    - [ ] Beliebtheitsmesser -> Order by Drink ID/Name
    - [ ] Charts
    - [ ] ...

### Could
- [ ] Mapping von DTO zu ENTITY (`OrderedItemDto): OrderedItem`) in Frontend
    - [ ] Technische Last Orders im Frontend halten
        - auch abgeschlossene könnte performance killen - mal schauen. Eventuell löschen