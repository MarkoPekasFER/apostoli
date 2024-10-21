# Projektni zadatak: Aplikacija za prijavu i upravljanje prirodnim nepogodama

**Tim:** Apostoli  
**Mentor:** izv. prof. dr. sc. Vlado Sruk
**Last updated:** 20/10/2024

## 1. Uvod
S obzirom na sve veću učestalost i ozbiljnost prirodnih nepogoda, poput poplava, potresa, požara i oluja, potrebno je razviti aplikaciju koja će omogućiti bolju koordinaciju između građana, vlasti i humanitarnih organizacija u kriznim situacijama. Cilj ovog projekta je stvoriti platformu koja omogućava brzu prijavu nesreća, učinkovitu distribuciju informacija i pravovremenu reakciju hitnih službi.

## 2. Funkcionalnosti aplikacije
Aplikacija će omogućiti sljedeće funkcionalnosti:

1. **Prijava nesreće:**
    - Građani mogu prijaviti nesreću putem aplikacije.
    - Pri prijavi je potrebno unijeti lokaciju (preko GPS-a ili ručno), odabrati kategoriju nesreće (npr. potres, poplava, požar itd.), dodati opis, i (opcionalno) priložiti sliku.
    - Detalji prijave: Korisnici mogu zaokružiti područje nesreće na karti, a aplikacija automatski izračunava prosječnu veličinu područja u slučaju višestrukih prijava sličnih incidenata.

2. **Kategorije nesreća:**
    - Aplikacija podržava različite vrste nesreća, uključujući:
        - Poplava
        - Potres
        - Požar
        - Tornado
        - Oluja
        - Jaka kiša/tuca
        - Vulkan
        - Snijeg
        - Teroristički napad (?)
        - Ostalo

3. **Reakcija hitnih službi:**
    - Nakon prijave nesreće, hitne službe mogu reagirati ažuriranjem statusa incidenta.
    - Hitne službe definiraju i preporučuju sigurne evakuacijske rute koje se ažuriraju u stvarnom vremenu.
    - Građani mogu putem vidjeti preporučene evakuacijske rute, uključujući najbrži put do skloništa ili siguran izlaz iz grada.

4. **Obavijesti u stvarnom vremenu:**
    - Koristeći WebSockets, aplikacija šalje obavijesti svim korisnicima kada dođe do novih prijava ili ažuriranja postojećih prijava.
    - Obavijesti uključuju promjene evakuacijskih ruta, dolazak pomoći, ili nove prijave nesreća.
    - Ne anonimnim korisnicima dolazi upozorenje na mail da se dogodila nesreća u blizini, te su obavješteni za sve promjene i sigurnosne mjere putem maila.

5. **Prikaz nesreća na interaktivnoj karti:**
    - Nesreće su prikazane na karti putem integracije s Mapbox servisom.
    - Korisnici mogu vidjeti pregled svih aktivnih incidenata u njihovoj blizini i pretraživati nesreće po gradu, državi ili vrsti.

6. **Autentifikacija korisnika:**
    - Autentifikacija će se obavljati putem OAuth 2.0, omogućujući korisnicima da se prijave putem Googlea ili drugih servisa.
    - Prijava incidenata može biti i anonimna, no korisnici moraju imati osnovne funkcionalnosti pristupa kroz korisnički račun za većinu akcija.

7. **Anonimne vs. neanonimne prijave:**
    - Iako anonimne prijave mogu biti korisne u nekim slučajevima, korisnici se potiču na stvaranje računa radi dodatnih funkcionalnosti kao što su praćenje prijava i instantan dolazak obavijesti na mail.

8. **Tražilica i feed nesreća:**
    - Na početnoj stranici aplikacije bit će traka s popisom najnovijih aktivnih nesreća.
    - Korisnici će moći pretraživati nesreće po državi, gradu i vrsti nesreće kako bi brzo pronašli relevantne informacije.

9. **Statistička analiza:**
    - Korisnici mogu pregledati statistike o vrstama nesreća, učestalosti i ozbiljnosti incidenata po regijama.
    - Grafički prikaz nesreća kroz vrijeme (grafovi, dijagrami).
    - Filtriranje statistika po vrstama nesreća (poplave, potresi itd.), vremenskim razdobljima, regijama (gradovima, državama).

## 3. Tehničke specifikacije
- **Backend:** Spring Boot
    - **REST API:**
        - Backend koristi Spring Boot za izradu REST API-ja. Ovi API endpointi omogućavaju prijavu nesreća, dohvaćanje popisa nesreća, te komunikaciju s bazom podataka.
        - Klijent šalje POST zahtjev za prijavu nesreće i GET zahtjev za dohvaćanje podataka o nesrećama.
        - Kreirat će se dodatni API endpointi koji će dohvaćati podatke o nesrećama i generirati statistike. Statistike će se generirati dinamički iz baze podataka i ažurirati u stvarnom vremenu.
    - **WebSockets:**
        - Koristit će se WebSockets za obavijesti u stvarnom vremenu. Na primjer, kada hitne službe ažuriraju evakuacijske rute, korisnici će automatski dobiti obavijest bez potrebe za ponovnim zahtjevima.

- **Frontend:** React i React Native
    - **Web aplikacija (React):**
        - Web sučelje će omogućiti građanima pregled nesreća, pretraživanje incidenata, i praćenje u stvarnom vremenu.

- **Karte:** Mapbox
    - Mapbox će se koristiti za prikaz svih nesreća i evakuacijskih ruta na interaktivnoj karti.
    - Aplikacija će koristiti geolokacijske podatke za prikaz najbližih skloništa ili ruta.

- **Cloud Hosting i Baza podataka:**
    - Još uvijek razmatramo najbolju cloud platformu za hostanje aplikacije i baze podataka. Moguće opcije uključuju Azure, AWS ili Google Cloud, ovisno o potrebama projekta i performansama svake platforme.
    - Za pohranu prijava nesreća i korisničkih podataka, koristit će se skalabilna baza podataka, s mogućnostima kao što su Azure Cosmos DB, Amazon RDS (MySQL) ili Google Cloud SQL. Odluka o specifičnom rješenju bit će donesena na temelju testiranja performansi i troškova.

- **OAuth 2.0 Autentifikacija:**
    - Prijava korisnika bit će omogućena putem OAuth 2.0, gdje korisnici mogu koristiti postojeće račune na Googleu, Facebooku i drugim platformama.

## 4. Dizajn korisničkog sučelja:
- **Preglednost i funkcionalnost:** Naglasak na jednostavnom, intuitivnom sučelju s brzim pristupom informacijama. Prioritet su pregled trenutnih nesreća i interakcija s kartom.
- **Prikaz nesreća na karti:**
    - Interaktivna karta kao centralni element, s prikazanim markerima koji omogućuju brzo pregledavanje nesreća.
    - Zoom in funkcionalnost otkriva precizniji prikaz nesreća na lokalnoj razini.
    - Klikom na marker prikazuju se osnovni podaci o nesreći, uključujući vrstu nesreće, vrijeme i opis.
    - Detaljni prikaz nesreće u popup prozoru ili sidebaru s priloženim slikama i zahvaćenim područjem prikazanim na karti.

- **Sidebar s detaljima:** Lista nesreća s osnovnim informacijama. Klikom na nesreću otvara se detaljniji prikaz s opisom, statusom i priloženim materijalima.

- **Geografska oznaka zahvaćenog područja:**
    - Prikaz zahvaćenog područja na karti, ovisno o vrsti nesreće.
    - U slučaju više prijava za isti incident, prikazat će se najveće ili prosječno zahvaćeno područje.

- **Traka s obavijestima (news ticker):**
    - U stvarnom vremenu prikazuje najnovije nesreće s osnovnim podacima (vrsta, lokacija, vrijeme).
    - Klikom na ticker korisnik se prebacuje na prikaz te nesreće na karti.

## 5. Zaključak
Ovaj projekt će omogućiti bolju komunikaciju između građana i hitnih službi te poboljšati brzinu reakcije u kriznim situacijama. Aplikacija će osigurati pravovremene informacije i koordinaciju akcija u stvarnom vremenu