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
import Link from "next/link";

function Login() {
  return (
    <div className="w-full h-screen flex items-center justify-center p-4">
      <Card className="max-w-[350px] w-full">
        <CardHeader>
          <CardTitle className="text-3xl">Prijava</CardTitle>
          <CardDescription>
            Prijavi se na svoj korisnicki racun ili se registriraj ovdje.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form>
            <div className="grid w-full items-center gap-4">
              <div className="flex flex-col space-y-1.5">
                <Input id="name" placeholder="Ime" />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Input id="last_name" placeholder="Prezime" />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Input id="email" type="email" placeholder="Vas email" />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Input id="password" type="password" placeholder="Vasa sifra" />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Input
                  id="repeat_password"
                  type="password"
                  placeholder="Ponovite sifru"
                />
              </div>
            </div>
          </form>
          <div className="w-full pt-4">
            <Button className="w-full">Prijava</Button>
          </div>
          <div className="pt-4">
            <p className="text-sm text-center">
              Vec imate racun?{" "}
              <Link href="/login" className="text-blue-500">
                Prijavite se
              </Link>
            </p>
          </div>
        </CardContent>
        <CardFooter className="flex justify-between">
          <Link href="/" className="w-full">
            <Button className="w-full" variant={"outline"}>
              <img
                src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Google_%22G%22_logo.svg/768px-Google_%22G%22_logo.svg.png"
                className="w-6 h-6 mr-2"
                alt="google logo"
              />
              Login With Google
            </Button>
          </Link>
        </CardFooter>
      </Card>
    </div>
  );
}

export default Login;
