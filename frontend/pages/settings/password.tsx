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

const Password = () => {
  return (
    <Card className="h-4/5 sm:w-3/4 md:w-1/2 lg:w-1/2 bg-white shadow-lg rounded-lg p-6">
      <CardHeader>
        <CardTitle>Postavke - lozinka</CardTitle>
        <CardDescription>Promijenite lozinku profila.</CardDescription>
      </CardHeader>

      <CardContent>
        <form>
          <div className="grid w-full gap-4">
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="oldPassword">Stara lozinka</Label>
              <Input
                id="oldPassword"
                placeholder="Unesite staru lozinku profila"
              />
            </div>

            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="newPassword">Nova lozinka</Label>
              <Input
                id="newPassword"
                placeholder="Unesite novu lozinku profila"
              />
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

export default Password;
Password.getLayout = sidebarLayout;
