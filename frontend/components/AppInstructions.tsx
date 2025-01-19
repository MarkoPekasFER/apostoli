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
        <CircleHelp className="text-blue-500 h-9 w-9 md:h-18 md:w-18 lg:h-16 lg:w-16"></CircleHelp>
      </DialogTrigger>
      <DialogContent className="bg-white dark:bg-slate-800">
        <DialogTitle className="text-black dark:text-white mt-2">
          Dobrodošli u aplikaciju za prijavu vremenskih nepogoda
        </DialogTitle>
        <hr className="border-black dark:border-white" />
        <DialogDescription className="mt-2 text-sm text-gray-800 dark:text-gray-100">
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
