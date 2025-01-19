import UserAddReport from "@/components/maps/UserAddReport";
import UserIcon from "@/components/user/UserIcon";


import AppInstructions from "@/components/AppInstructions";

import { Inter } from 'next/font/google'
import dynamic from 'next/dynamic';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
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

  // useEffect(() => {
  //   const fetchReports = async () => {
  //     const token = localStorage.getItem('token');
  //     if (!token) {
  //       // Redirect to login if not authenticated
  //       router.push('/login');
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
  //         router.push('/login');
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
          <Link href={'/report'}>
          <Button className="bg-blue-500" size={'icon'}>
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
      <div className="h-screen bg-white">
          <h1 className="font-bold text-2xl text-center pt-10">
            Nesreće HR
          </h1>
          <p className="text-center text-lg pt-5 max-w-xl mx-auto">
            Prijavite nesreću i vidite sve nesrece u blizini pomocu 
          naše aplikacije. Vaša sigurnost nam je na prvom mjestu, stoga budite oprezni i odgovorni.
          </p>
          <div className="flex justify-center pt-10">
            <Link href="/report">
              <Button>
                Prijavi nesreću
              </Button>
            </Link>
          </div>

          <div className="grid xl:grid-cols-2 gap-2 xl:gap-8 p-4">
            <div>

            </div>
            <div className="w-full relative object-cover pt-[100%] rounded-xl overflow-hidden">
              <Image src="/hero.jpg"  alt="hero" fill className="object-cover" />
            </div>
          </div>
      </div>
    </div>
  );
}