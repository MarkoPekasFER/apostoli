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
import Link from "next/link";
import { useRouter } from 'next/compat/router';

function Register() {
  const [username, setUsername] = React.useState('');
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [repeatPassword, setRepeatPassword] = React.useState('');
  const router = useRouter();
  
  const handleSubmit = async (e: { preventDefault: () => void; }) => {
    e.preventDefault();
    if (password !== repeatPassword) {
      alert('Lozinke se ne podudaraju');
      return;
    }
    try {
      const response = await fetch(process.env.NEXT_PUBLIC_API_URL+'/api/v1/user/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password, email }),
      });
      if (response.ok) {
        // Optionally log in the user automatically
        // Redirect to login page
        router?.push('/login');
      } else {
        // Handle errors
        const errorData = await response.json();
        console.error('Registration failed:', errorData);
        alert('Registracija nije uspjela: ' + (errorData.message || 'Nepoznata greška'));
      }
    } catch (error) {
      console.error('An error occurred:', error);
      alert('Došlo je do pogreške prilikom registracije.');
    }
  };

  return (
    <div className="w-full h-screen flex items-center justify-center p-4">
      <Card className="max-w-[350px] w-full">
        <CardHeader>
          <CardTitle className="text-3xl">Registracija</CardTitle>
          <CardDescription>
            Registriraj se za novi korisnički račun.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit}>
            <div className="grid w-full items-center gap-4">
              <div className="flex flex-col space-y-1.5">
                <Input
                  id="username"
                  placeholder="Korisničko ime"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Input
                  id="email"
                  type="email"
                  placeholder="Vaš email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Input
                  id="password"
                  type="password"
                  placeholder="Vaša šifra"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Input
                  id="repeat_password"
                  type="password"
                  placeholder="Ponovite šifru"
                  value={repeatPassword}
                  onChange={(e) => setRepeatPassword(e.target.value)}
                />
              </div>
            </div>
            <div className="w-full pt-4">
              <Button className="w-full" type="submit">
                Registracija
              </Button>
            </div>
          </form>
          <div className="pt-4">
            <p className="text-sm text-center">
              Već imate račun?{" "}
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
              Registracija s Googleom
            </Button>
          </Link>
        </CardFooter>
      </Card>
    </div>
  );
}

export default Register;