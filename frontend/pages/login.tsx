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

function Login() {
  return (
    <Card className="w-[350px]">
      <CardHeader>
        <CardTitle>Prijava</CardTitle>
        <CardDescription>Prijavi se na svoj korisnicki racun ili se registriraj ovdje.</CardDescription>
      </CardHeader>
      <CardContent>
        <form>
          <div className="grid w-full items-center gap-4">
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="name">Email</Label>
              <Input id="name" placeholder="Vas email" />
            </div>
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="name">Sifra</Label>
              <Input id="name" type="password" placeholder="Vasa sifra" />
            </div>
            
          </div>
        </form>
      </CardContent>
      <CardFooter className="flex justify-between">
        <Button variant="outline">Cancel</Button>
        <Button>Deploy</Button>
      </CardFooter>
    </Card>
  );
}

export default Login;
