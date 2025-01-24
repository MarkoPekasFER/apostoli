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

function Login() {
  const [username, setUsername] = React.useState('');
  const [password, setPassword] = React.useState('');
  const router = useRouter();

  const handleSubmit = async (e: { preventDefault: () => void; }) => {
    e.preventDefault();
    try {
      const response = await fetch(process.env.NEXT_PUBLIC_API_URL+'/api/v1/user/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });
      if (response.ok) {
        // response body is readable stream with token as text
        const data = await response.text();
        console.log(data)
        // Assume data contains the JWT token
        const token = data

        // Store the token in localStorage
        localStorage.setItem('token', token);

        // Redirect to the main page
        router?.push('/');
      } else {
        // Handle errors
        const errorData = await response.json();
        console.error('Login failed:', errorData);
        alert('Prijava nije uspjela: ' + (errorData.message || 'Nepoznata greška'));
      }
    } catch (error) {
      console.error('An error occurred:', error);
      alert('Došlo je do pogreške prilikom prijave.');
    }
  };

  return (
    <div className="w-full h-screen flex items-center justify-center p-4">
      <Card className="max-w-[350px] w-full">
        <CardHeader>
          <CardTitle className="text-3xl">Prijava :)</CardTitle>
          <CardDescription>
            Prijavi se na svoj korisnički račun ili se registriraj ovdje.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit}>
            <div className="grid w-full items-center gap-4">
              <div className="flex flex-col space-y-1.5">
                <Input
                  id="username"
                  type="text"
                  placeholder="Korisničko ime"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Input
                  id="password"
                  type="password"
                  placeholder="Šifra"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>
            </div>
            <div className="w-full pt-4">
              <Button className="w-full" type="submit">
                Prijava
              </Button>
            </div>
          </form>
          <div className="pt-4">
            <p className="text-sm text-center">
              Nemate korisnički račun?{" "}
              <Link href="/register" className="text-blue-500">
                Registriraj se
              </Link>
            </p>
          </div>
        </CardContent>
        <CardFooter className="flex justify-between">
          <Link href="/" className="w-full">
            <Button className="w-full" id="google" variant={"outline"}>
              <img
                src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Google_%22G%22_logo.svg/768px-Google_%22G%22_logo.svg.png"
                className="w-6 h-6 mr-2"
                alt="google logo"
              />
              Prijava s Googleom
            </Button>
          </Link>
        </CardFooter>
      </Card>
    </div>
  );
}
// DRUGI JEZICI
// return (
//     <div className="w-full h-screen flex items-center justify-center p-4">
//       <Card className="max-w-[350px] w-full">
//         <CardHeader>
//           <CardTitle className="text-3xl">Login :)</CardTitle>
//           <CardDescription>
//             Log in to your account or register here.
//           </CardDescription>
//         </CardHeader>
//         <CardContent>
//           <form onSubmit={handleSubmit}>
//             <div className="grid w-full items-center gap-4">
//               <div className="flex flex-col space-y-1.5">
//                 <Input
//                   id="username"
//                   type="text"
//                   placeholder="Username"
//                   value={username}
//                   onChange={(e) => setUsername(e.target.value)}
//                 />
//               </div>
//               <div className="flex flex-col space-y-1.5">
//                 <Input
//                   id="password"
//                   type="password"
//                   placeholder="Password"
//                   value={password}
//                   onChange={(e) => setPassword(e.target.value)}
//                 />
//               </div>
//             </div>
//             <div className="w-full pt-4">
//               <Button className="w-full" type="submit">
//                 Login
//               </Button>
//             </div>
//           </form>
//           <div className="pt-4">
//             <p className="text-sm text-center">
//               Don't have an account? {" "}
//               <Link href="/register" className="text-blue-500">
//                 Register here
//               </Link>
//             </p>
//           </div>
//         </CardContent>
//         <CardFooter className="flex justify-between">
//           <Link href="/" className="w-full">
//             <Button className="w-full" id="google" variant={"outline"}>
//               <img
//                 src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Google_%22G%22_logo.svg/768px-Google_%22G%22_logo.svg.png"
//                 className="w-6 h-6 mr-2"
//                 alt="google logo"
//               />
//               Sign in with Google
//             </Button>
//           </Link>
//         </CardFooter>
//       </Card>
//       <Card className="max-w-[350px] w-full mt-8">
//         <CardHeader>
//           <CardTitle className="text-3xl">Iniciar sesión :)</CardTitle>
//           <CardDescription>
//             Inicia sesión en tu cuenta o regístrate aquí.
//           </CardDescription>
//         </CardHeader>
//         <CardContent>
//           <form onSubmit={handleSubmit}>
//             <div className="grid w-full items-center gap-4">
//               <div className="flex flex-col space-y-1.5">
//                 <Input
//                   id="username"
//                   type="text"
//                   placeholder="Nombre de usuario"
//                   value={username}
//                   onChange={(e) => setUsername(e.target.value)}
//                 />
//               </div>
//               <div className="flex flex-col space-y-1.5">
//                 <Input
//                   id="password"
//                   type="password"
//                   placeholder="Contraseña"
//                   value={password}
//                   onChange={(e) => setPassword(e.target.value)}
//                 />
//               </div>
//             </div>
//             <div className="w-full pt-4">
//               <Button className="w-full" type="submit">
//                 Iniciar sesión
//               </Button>
//             </div>
//           </form>
//         </CardContent>
//       </Card>
//       <Card className="max-w-[350px] w-full mt-8">
//         <CardHeader>
//           <CardTitle className="text-3xl">Accedi :)</CardTitle>
//           <CardDescription>
//             Accedi al tuo account o registrati qui.
//           </CardDescription>
//         </CardHeader>
//         <CardContent>
//           <form onSubmit={handleSubmit}>
//             <div className="grid w-full items-center gap-4">
//               <div className="flex flex-col space-y-1.5">
//                 <Input
//                   id="username"
//                   type="text"
//                   placeholder="Nome utente"
//                   value={username}
//                   onChange={(e) => setUsername(e.target.value)}
//                 />
//               </div>
//               <div className="flex flex-col space-y-1.5">
//                 <Input
//                   id="password"
//                   type="password"
//                   placeholder="Password"
//                   value={password}
//                   onChange={(e) => setPassword(e.target.value)}
//                 />
//               </div>
//             </div>
//             <div className="w-full pt-4">
//               <Button className="w-full" type="submit">
//                 Accedi
//               </Button>
//             </div>
//           </form>
//         </CardContent>
//       </Card>
//     </div>
//   );
// }
// 
// export default Login;


export default Login;
