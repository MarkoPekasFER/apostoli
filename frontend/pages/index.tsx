import UserAddReport from "@/components/maps/UserAddReport";
import UserIcon from "@/components/user/UserIcon";

import AppInstructions from "@/components/AppInstructions";

import { Inter } from "next/font/google";
import dynamic from "next/dynamic";
import { useEffect, useState } from "react";
import { useRouter } from "next/compat/router";
import { Plus } from "lucide-react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import Image from "next/image";

const GoogleMapParent = dynamic(() => import("@/components/maps/GoogleMap"), {
  ssr: false,
});
const inter = Inter({ subsets: ["latin"] });

export default function Home() {
  const [reports, setReports] = useState([]);
  const router = useRouter();
  const [token, setToken] = useState("");

  useEffect(() => {
    // fetch token from local storage
    const token = localStorage.getItem("token");
    if (!token) {
      // Redirect to login if not authenticated
      return;
    }
    setToken(token);
  }, []);

  // useEffect(() => {
  //   const fetchReports = async () => {
  //     const token = localStorage.getItem('token');
  //     if (!token) {
  //       // Redirect to login if not authenticated
  //       router?.push('/login');
  //       return;
  //     }
  //     try {
  //       const response = await fetch(process.env.NEXT_PUBLIC_API_URL+'/api/v1/report/all', {
  //         headers: {
  //           'Authorization': `Bearer ${token}`,
  //         },
  //       });
  //       if (response.ok) {
  //         const data = await response.json();
  //         setReports(data);
  //       } else if (response.status === 401) {
  //         // Token might have expired or is invalid
  //         localStorage.removeItem('token');
  //         router?.push('/login');
  //       } else {
  //         console.error('Failed to fetch reports');
  //       }
  //     } catch (error) {
  //       console.error('An error occurred:', error);
  //     }
  //   };
  //   fetchReports();
  // }, [router]);

  return (
    <div>
      <style jsx global>{`
        html {
          font-family: ${inter.style.fontFamily};
        }
      `}</style>
      <GoogleMapParent
      // reports={reports}
      />
      <div className="absolute top-0 left-0 w-full">
        <div className="p-4 w-full flex">
          <UserIcon />
        </div>
      </div>
      <div className="absolute bottom-24 left-0 w-full">
        <div className="p-4 pb-16 lg:p-16 lg:px-28 w-full flex justify-between">
          <AppInstructions></AppInstructions>
          <Link href={token ? "/report" : "/login"}>
            <Button className="bg-blue-500" size={"icon"}>
              <Plus />
            </Button>
          </Link>
        </div>

        {/* <Link href="/about">
        <Button>
          About
        </Button>
      </Link> */}
      </div>
      <div className="bg-white dark:bg-slate-800">
        <h1 className="font-bold text-2xl text-center pt-10">Nesreće HR</h1>
        <p className="text-center text-lg pt-5 max-w-xl mx-auto">
          Prijavite nesreću i vidite sve nesrece u blizini pomocu naše
          aplikacije. Vaša sigurnost nam je na prvom mjestu, stoga budite
          oprezni i odgovorni.
        </p>
        <div className="flex justify-center pt-10">
          <Link href={token ? "/report" : "/login"}>
            <Button>
              {token ? "Prijavi nesreću" : "Ulogiraj se za prijavu nesreće"}
            </Button>
          </Link>
        </div>

        <div className="grid xl:grid-cols-2 gap-2 xl:gap-8 p-4 mt-8">
          <div className="max-w-2xl mx-auto w-full">
            <div className="w-full relative object-cover pt-[100%] rounded-xl overflow-hidden">
              <Image src="/hero.jpg" alt="hero" fill className="object-cover" />
            </div>
          </div>
          <div className="flex flex-col items-center justify-center">
            <h2 className="font-bold text-xl pt-5 text-center">
              Za sigurnu i informiranu Hrvatsku
            </h2>
            <p className="text-center text-lg pt-5 max-w-xl mx-auto">
              Ostanite sigurni i informirani o svim nesrećama u Hrvatskoj.
              Prijavite nesreću i pomozite drugima.
            </p>
          </div>
        </div>

        <div className="py-24">
          <h2 className="font-bold text-2xl text-center pt-10">
            Kako koristiti aplikaciju
          </h2>
          <p className="text-center text-lg pt-5 max-w-xl mx-auto">
            Prijavite nesreću i vidite sve nesreće u blizini pomoću naše
            aplikacije. Vaša sigurnost nam je na prvom mjestu, stoga budite
            oprezni i odgovorni.
          </p>
        </div>

        <div className=" mb-12">
          <h2 className="font-bold text-2xl text-center pt-10">
            Želite pomoći?
          </h2>
          <p className="text-center text-lg pt-5 max-w-xl mx-auto">
            Sve državne i privatne institucije koje žele pomoći ljudima u
            nesreći se mogu javiti na email: slu@unlucky.hr
          </p>
          <p className="text-center text-lg pt-5 max-w-xl mx-auto">
            Povratno ćemo se javiti ako vjerujemo da možemo zajedno pomoći.
          </p>
        </div>

        <footer>
          <div className="bg-gray-800 text-white dark:bg-black">
            <div className="grid md:grid-cols-2">
              <div className="p-4">
                {/* contact details */}
                <p className="font-bold">Unlucky d.o.o.</p>
                <p>Ulica grada Vukovara 269D</p>
                <p>10000 Zagreb</p>
                <p>OIB: 12345678901</p>
              </div>
              <div className="p-4">
                <p className="font-bold">Kontakt</p>
                <p>Email: info@unlucky.hr</p>
                <p>Telefon: 01 123 4567</p>
              </div>
            </div>
            <div className="container mx-auto py-4">
              <p className="text-center">&copy; 2021 Nesreće HR</p>
            </div>
          </div>
        </footer>
      </div>
    </div>
  );
}
