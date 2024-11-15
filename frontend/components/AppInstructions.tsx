import React from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";

import { CircleHelp } from "lucide-react";

const AppInstructions = () => {
  return (
    <Dialog>
      <DialogTrigger>
        <CircleHelp className=" h-9 w-9 md:h-18 md:w-18 lg:h-16 lg:w-16  w-auto"></CircleHelp>
      </DialogTrigger>
      <DialogContent className="bg-white">
        <DialogTitle className="text-black">
          Dobrodošli u aplikaciju za prijavu vremenskih nepogoda
        </DialogTitle>
        <hr className="border-black" />
        <DialogDescription className="mt-2 text-sm text-gray-600">
          Dobrodošli u verziju 1.0 aplikacije za prijavu vremenskih nepogoda.
          Ova aplikacija omogućava brzo i jednostavno prijavljivanje različitih
          vremenskih nepogoda putem intuitivnog korisničkog sučelja.
          <ul className="mt-4 list-disc list-inside">
            <li>
              Kliknite na ikonu <b>“+”</b> u glavnom izborniku.
            </li>
            <li>Odaberite odgovarajuću kategoriju vremenske nepogode.</li>
            <li>Po želji možete priložiti fotografiju, unijeti opis.</li>
            <li>
              Obavezno <b>morate</b> odabrati kategoriju i lokaciju za prijavu.
            </li>
            <li>
              Kliknite <b>“Prijavi”</b> kako biste poslali prijavu.
            </li>
          </ul>
          <p className="mt-4">
            Hvala što doprinosite zajednici prijavom vremenskih uvjeta!
          </p>
        </DialogDescription>
      </DialogContent>
    </Dialog>
  );
};

export default AppInstructions;
