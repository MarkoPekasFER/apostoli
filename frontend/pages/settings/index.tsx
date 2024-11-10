"use client";
import * as React from "react";
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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

import sidebarLayout from "@/components/SidebarLayout";
import { NextPageWithLayout } from "../_app";

const Settings: NextPageWithLayout = () => {
  return (
    <div className="flex items-center justify-center bg-gray-100 w-screen h-full">
      <Card className="w-[400px] bg-white shadow-lg rounded-lg p-6">
        <CardHeader>
          <CardTitle>Postavke</CardTitle>
          <CardDescription>
            Upravljajte postavkama kako biste prilagodili aplikaciju svojim
            potrebama.
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form>
            <div className="grid w-full gap-4">
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="name">Ime</Label>
                <Input id="name" placeholder="Unesite ime" />
              </div>

              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="email">Novi E-mail</Label>
                <Input id="email" placeholder="Unesite novi e-mail" />
              </div>

              <div className="flex flex-col space-y-1.5">
                <Label className="">Promijena lozinke</Label>
                <Label htmlFor="password">Stara lozinka</Label>
                <Input
                  id="password"
                  type="password"
                  placeholder="Unesite staru lozinku"
                />
                <Label htmlFor="password">Nova lozinka</Label>
                <Input
                  id="password"
                  type="password"
                  placeholder="Unesite novu lozinku"
                />
              </div>

              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="tema">Tema</Label>
                <Select>
                  <SelectTrigger id="tema">
                    <SelectValue placeholder="Izaberi" />
                  </SelectTrigger>
                  <SelectContent position="popper">
                    <SelectItem value="Tamna">Tamna</SelectItem>
                    <SelectItem value="Svijetla">Svijetla</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="flex items-center space-x-2">
                <Label htmlFor="notifications">Primaj obavijesti</Label>
              </div>
            </div>
          </form>
        </CardContent>

        <CardFooter className="flex justify-between">
          <Button variant="outline">Poništi</Button>
          <Button>Spremi promjene</Button>
        </CardFooter>
      </Card>
    </div>
  );
};
Settings.getLayout = sidebarLayout;
export default Settings;
