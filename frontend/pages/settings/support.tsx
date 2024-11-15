import sidebarLayout from "@/components/SidebarLayout";
import React from "react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

const Support = () => {
  return (
    <Card className="h-4/5 sm:w-3/4 md:w-1/2 lg:w-1/2 bg-white shadow-lg rounded-lg p-6">
      <CardHeader>
        <CardTitle>Postavke - podrška</CardTitle>
        <CardDescription>Korisnička podrška.</CardDescription>
      </CardHeader>

      <CardContent>
        <form>
          <div className="flex flex-col space-y-1.5 my-2">
            <Label>Kontakt na mail:</Label>
            <div className="text-sm">
              <a
                className="text-blue-600 hover:underline"
                href="mailto:apostoli7@gmail.com"
              >
                apostoli7@gmail.com
              </a>
            </div>
          </div>
          <div className="grid w-full gap-4">
            <div className="flex flex-col space-y-1.5 mt-8">
              <Label htmlFor="oldPassword">
                Komentar/prijedlog za poboljšanje aplikacije
              </Label>
              <Input
                id="comment"
                placeholder="Unesite komentar/prijedlog/upit"
              />
            </div>
          </div>
        </form>
      </CardContent>

      <CardFooter className="flex justify-between">
        <Button>Pošalji upit</Button>
      </CardFooter>
    </Card>
  );
};

export default Support;

Support.getLayout = sidebarLayout;
