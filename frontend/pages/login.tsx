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
                <Input id="email" type="email" placeholder="Email" />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Input id="password" type="password" placeholder="Sifra" />
              </div>
            </div>
          </form>
          <div className="w-full pt-4">
            <Button className="w-full">Prijava</Button>
          </div>
          <div className="pt-4">
            <p className="text-sm text-center">
              Nemate korisnicki racun?{" "}
              <Link href="/register" className="text-blue-500">
                Registriraj se
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
