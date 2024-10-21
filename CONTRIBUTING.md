# Contributing to Apostoli

## Prerequisites

Prije nego što počnete, molimo vas da se osigurate da imate instalirane sljedeće alate:

- [Node.js](https://nodejs.org/) (verzija 12.x ili novija)
- [pnpm](https://pnpm.io/) (preporučeno)

## Postavljanje okruženja

1. Forkajte ovaj repozitorij i klonirajte vaš fork na lokalno računalo:

    ```sh
    git clone https://github.com/MarkoPekasFER/apostoli.git
    cd apostoli
    ```

2. Pređite u `frontend` direktorij:

    ```sh
    cd frontend
    ```

3. Instalirajte potrebne pakete:

    ```sh
    pnpm i
    ```

## Development

Za pokretanje Next.js lokalnog razvojnog servera koristite:

```sh
pnpm run dev
```

Otvorite [http://localhost:3000](http://localhost:3000) u pregledniku kako biste vidjeli vašu aplikaciju.

## Kreiranje grana za nove featurse

Kada započinjete rad na novoj značajci ili popravku, stvorite novu granu za svoj rad:

```sh
git checkout -b ime-vaše-grane
```

Molimo koristite opisne nazive grana, kao što su `feature/naziv-značajke` ili `fix/opis-popravka`.

## Commit poruke

Koristite jasne i opisne commit poruke. Commit poruka bi trebala slijediti konvenciju:

```
Kratki (50 znakova ili manje) sažetak promjena

Dulji opis promjena (ako je potreban). Ovaj dio može
imati više paragrafa i dodati dodatne detalje o
promjeni. Ako postoji povezani GitHub issue ili PR,
referencirajte ga ovdje.
```

## Pull Requestovi

Kada ste spremni napraviti pull request (PR), obavezno uključite sljedeće:

1. Detaljan opis što vaš PR radi.
2. Oznaku za povezani issue (ako postoji).
3. Testove (ako je primjenjivo).

Nakon što kreirate PR, jedan od naših recenzenata će ga pregledati čim bude moguće.

## Stil koda

Molimo slijedite style guide i formatiranje koda koji se već koristi u projektu. Prije nego što commitate, pokrenite `lint` da provjerite kod za stilne pogreške:

```sh
pnpm run lint
```

ili ako koristite npm:

```sh
npm run lint
```