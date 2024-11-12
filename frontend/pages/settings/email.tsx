import sidebarLayout from "@/components/SidebarLayout";
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

import React from "react";

const Email = () => {
  return (
    <Card className="h-4/5 sm:w-3/4 md:w-1/2 lg:w-1/2 bg-white shadow-lg rounded-lg p-6">
      <CardHeader>
        <CardTitle>Postavke</CardTitle>
        <CardDescription>
          Dodajte novi E-mail za slanje obavijesti.
        </CardDescription>
      </CardHeader>

      <CardContent>
        <form>
          <div className="grid w-full gap-4">
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="email">Novi E-mail</Label>
              <Input id="newEmail" placeholder="Unesite novi e-mail" />
            </div>
          </div>
        </form>
      </CardContent>

      <CardFooter className="flex justify-between">
        <Button>Spremi promjene</Button>
      </CardFooter>
    </Card>
  );
};

export default Email;
Email.getLayout = sidebarLayout;
