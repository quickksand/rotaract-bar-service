# Todo

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

### Should
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



# Rotaract Ansbach Altstadtfest Bar App


![[Pasted image 20250803195002.png]]


- Drinks mit Preisen zusammenklicken
- Dann ob aufs haus oder net
- Wird im Backend gerechnet
- Wird gespeichert als Bestellung

- Was bringts?
    - [x] Rechner
    - Soll-Ist Becher abgleich
    - Auswertungen wie Beliebtheitsmesser für Nachbewertung und Live-Ausruf Rabatt
        - Was war der erfolgreichste Drink?
        - Wann haben wir am besten verkauft?
        - Wie haben die Live-Rabatte funktioniert? Hat sich das in den Verkaufszahlen niedergeschlagen?
        - Welche Drinks gingen nach Temperatur wie gut?
    - Tracking Verbrauch und Prognose (z.B. Secco könnte über den Abend knapp werden, vielleicht nochmal nachkaufen)
    - Anzeige welche Flaschen benötigt für Bestellung (Rezept Zutatenliste, dann ist bei großen Bestellungen einfach zu sammeln und wenn ich mal nicht weiß, dann hab ich referenz)
    - wann wird gesamtmenge knapp (mit schätzung an alkohol)

Ablauf
1. becher rein = 2€ guthaben
2. Dann bestellung (und drink wird immer mit pfand ergänzt)
3. Bei shots darf keine verrechnung eig stattfinden, andererseits wenn der becher weg ust bringt die

# ERM
- **Product** (id, name, base_price, product_type, deposit_required)
    - product_type: ENUM('drink', 'shot', 'deposit_return')
    - deposit_required
    - ...
- **OrderItem** (id, quantity, product_id, unit_price, deposit_per_item, line_total)
- **Order** (id, timestamp, total_amount, deposit_amount, payment_method)

```
Order #123:
- OrderItem 1: quantity=1, product="Becher Rückgabe", unit_price=-2.00€, deposit=0€
- OrderItem 2: quantity=1, product="Cuba Libre", unit_price=6.50€, deposit=2.00€  
- OrderItem 3: quantity=1, product="Shot", unit_price=2.00€, deposit=0€

Berechnung:
Item 1: 1 × (-2.00€ + 0€) = -2.00€
Item 2: 1 × (6.50€ + 2.00€) = 8.50€  
Item 3: 1 × (2.00€ + 0€) = 2.00€
Gesamt: 8.50€
```



# Archiv

### Zubereitungsscreen

Sprung zwischen Bestellung ↔ Zubereitung  
✅ **Multi-Order-Übersicht** - Wenn's hektisch wird, siehst du alles auf einen Blick  
✅ **Klare Priorisierung** - Neue Bestellungen "poppen" heraus, Status-Farben sind eindeutig

Wichtiger Constraint:
Die Zubereitungsübersicht muss im Frontend ablaufen. Wir werden wahrscheinlich mehrere POS-Systeme verwenden weil ich will nicht Links an der Bar sehen, was rechts an offenen Bestellungen ist. Außerdem könnte ich dann nicht mehr auseinanderhalten, was an meiner Bar gefordert ist und was am anderen Teil der Bar. Achtung: was passiert, wenn Verbindung getrennt wird? (Stand lokal speichern? und im Zweifel Rechner nicht von Service abhängig machen?)

- [x] OpenAPI erweitern für ingredients array bei products
- [x] Backend: Entität Products (nur Drinks) um Liste ingredients erweitern (basal zB Cuba Libre hat ingredients: Rum/Havana, Cola, Limette, Limettensaft)
- [x] Frontend
    - [x] neue Route einführen für Zubereitungsscreen
    - [x] Neuer Component für Zubereitungsscreen
    - [x] Buttons im Header zum wechseln zwischen Zubereitungsscreen und Bestellungsscreen
    - [x] Anzeigen, welcher Button gerade aktiv ist (pseudo code if route = order order sonst zubereitung? gerne besser)
    - [x] Nach Bezahlen soll eine offene order erstellt werden mit items
    - [x] Darstellung der offenen orders jeweils als karte in Zubereitungsscreen
    - [x] Funktionalität Starten / einzelne Position abhaken / Fertig implementieren
        - [x] einzelne Position abhaken macht automatisch start
    - [x] Zuordnung Rezept zu Drinks als aufklappbarer Bereich
    - [x] ...?


![[Pasted image 20250803121159.png]]

## Tag 13 - 15
- [x] Caroussel für Items
- [x] Pfand-System
- [x] Stempelkarten-System

## Tag 12 - iPad-optimiertes Rotaract POS Design

Block 1: Tailwind + Split-Layout Setup (60 min)
4. [x] Tailwind installieren & Rotaract-Farben konfigurieren (20 min)
5. [x] App-Layout auf Split-View umstellen (2 Spalten) (25 min)
6. [x] Responsive iPad-Breakpoints testen (15 min)


Block 3: Touch-Optimierung & Kontraste (45 min)
7. [x] Button-Größen für iPad optimieren (min-h-11) (15 min)
8. [x] Hohe Kontraste für Bar-Beleuchtung (15 min)
9. [x] Final-Test: Alle Farben gemäß Schema anwenden (15 min)



## Tag 11

- [x] removeFromOrder() (15 min)
- [x] Summary-Tabelle mit Produktnamen + Preisen (30 min)
- [x] ~~Tailwind Setup (45 min)~~
- [x] ~~Erste Tailwind-Styles in Summary (15 min) _Total: ~2h_~~


## Tag 10 Order Summary

1. [x] Gesamtpreis-Berechnung implementieren
- [x] RxJS-Preisberechnung implementiert (45 min)
    - Constructor-basierte Subscription mit `combineLatestWith`
    - `currentOrderSum$` als derived observable
- [x] Service-Kapselung verbessert (15 min)
    - Private BehaviorSubject mit public getter
    - `asObservable()` für saubere Interfaces
- [x] addToOrder() mit immutable Pattern (20 min)
    - Array-Mutation-Problem erkannt und gelöst
    - `.map()` vs `.filter()` Pattern verstanden

## Tag 9

### Block 1: OpenAPI Generator Setup (45 min)

1. [x] npm package @openapitools/openapi-generator-cli installieren (5 min)
2. [x] Script in package.json hinzufügen (10 min)
3. [x] Erste Generierung testen (15 min)
4. [x] Generated Services anschauen & verstehen (15 min)

### Block 2: User Experience (45 min)

1. [x] Order Summary Komponente erstellen (10 min)
2. ~~Gesamtpreis-Berechnung implementieren (10 min)~~
3. [x] Order nach erfolgreichem Senden resetten (10 min)
4. ~~Erfolg/Fehler Feedback hinzufügen (10 min)~~
5. ~~Styling für Summary-Komponente (5 min)~~

- Mal deployen mit vercel oder netlify? Oder eu alternative?

## Core MVP (Tag 8)

### Block 1: Schema refactoring (20 min)

1. [x] OpenAPI Schema anpassen (10 min) - keine zirkulären Referenzen mehr sondern nur per Id
2. [x] Mapping anpassen, sodass toDto aus Referenz auf product source = target product.id wird
3. [x] Models neu generieren (10 min)
4. [x] PurchaseOrderService anpassen, sodass nach Ids gegangen wird

### Block 2 Frontend Integration

1. PurchaseOrderDto im Frontend erstellen (15 min)
    - Import das generierte `PurchaseOrderDto` Interface
    - Funktion schreiben: `currentOrder[]` → `PurchaseOrderDto`

    - [x] PurchaseOrderDto - was fehlt? Converter-Function

      ```ts
      convertToPurchaseOrderDto(): PurchaseOrderDto {  
        return {  
          items: this.currentOrder  
        };  
      }
      ```

2. HTTP POST implementieren (20 min)
    - [x] Service-Methode für POST `/api/orders`
    - [x] Button/Aktion "Bestellung abschicken" hinzufügen

- Tag 7 - Touch-optimierte Frontend-Bestellung
- Tag 6 - API First Grundlagen & [[Mapstruct - Mapping from Entity to Dto]]
- Tag 5 - Product Refactoring mit festen Produkten

## Tag 4 - Repository Magic & Testing

1. [x] Spring Data Magic: OrderRepository Query (20 min)
    - `findByCustomerName()` implementieren und testen
2. [x] Spring Data Magic: OrderedItem Queries (25 min)
    - `findByOrderId()` und `findByDrinkName()` implementieren
3. [x] Testen: Alle GET-Endpoints durchprobieren (15 min)
    - Frontend oder Postman, alle deine neuen Endpoints checken
4. [x] RestController: Query-Parameter nutzen (20 min)
    - GET `/api/orders?customerName=xyz` Endpoint hinzufügen
5. [x] Optional: Bidirektionales Problem nochmal anschauen (20 min)
    - Falls Zeit und Lust da ist

## Tag 3 - RestController erweitern & Repository Magic

1. [x] Bidirektionales Problem debuggen (20 min)
    - Warum wird OrderId-Referenz nicht belegt? Cascade-Einstellungen prüfen
2. [x] RestController: GET Order by ID (15 min)
    - `/api/orders/{id}` Endpoint hinzufügen
3. [x] RestController: GET alle Orders (15 min)
    - `/api/orders` für Liste aller Orders erweitern
4. [x] Spring Data Magic: Erste Custom Query (20 min)
    - Im OrderRepository: `findByCustomerName()` implementieren
5. [x] Spring Data Magic: OrderedItem Queries (20 min)
    - Im OrderedItemRepository: `findByOrderId()` und `findByDrinkName()`
6. [x] Testen: Neue Endpoints im Frontend/Postman (15 min)
    - Alle neuen Endpoints einmal durchprobieren

## Tag 1Frontend anbinden

1. [x] Frontend aufsetzen init
2. [x] Hello World
3. [x] Service starten
4. [x] Ersten Request vorbereiten (GET)
5. [x] CORS im Backend
6. [x] Ergebnis anzeigen von GET
7. [x] Get Response objekt verstehen, response type üblicherweise JSON. hierbei text
8. [x] POST
9. [x] [[CORS#Lösung 1 in Angular via Proxy]]

berechnete felder kommen nicht in die datenbank
preis aktuell bei ordereditems notieren, weil preis sich ändern kann.
