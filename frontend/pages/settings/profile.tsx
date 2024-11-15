import sidebarLayout from "@/components/SidebarLayout";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
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
import { UserIcon } from "lucide-react";

import React from "react";

const Email = () => {
  return (
    <Card className="min-h-fit sm:w-3/4 md:w-1/2 lg:w-1/2 bg-white shadow-lg rounded-lg p-6">
      <CardHeader>
        <CardTitle>Postavke - Profil</CardTitle>
        <CardDescription>Vaš korisnički profil.</CardDescription>
      </CardHeader>

      <CardContent className="w-full flex flex-col items-center h-4/5">
        <Avatar className="my-3 ml-5 h-32 w-32">
          <AvatarImage src="https://img.freepik.com/premium-vector/anime-girl-face_24640-79435.jpg" />
          <AvatarFallback>AP</AvatarFallback>
        </Avatar>

        <Button className="mt-2 mb-7">Promijeni ikonu</Button>

        <form className="my-3">
          <div className="grid w-full gap-4">
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="email">Novi E-mail za obavijesti</Label>
              <Input id="newEmail" placeholder="Unesite novi e-mail" />
            </div>
          </div>
        </form>

        <h1 className="font-extrabold text-lg mt-2">Povijest</h1>
        <div className="mt-1 bg-slate-500 h-1/2 w-3/4 rounded p-3">
          <h2 className="text-white">Vaše prijavljene nesreće</h2>
          <hr />
        </div>
      </CardContent>

      <CardFooter className="flex justify-between"></CardFooter>
    </Card>
  );
};

export default Email;
Email.getLayout = sidebarLayout;
