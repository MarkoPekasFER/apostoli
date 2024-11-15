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
      <div className="absolute bottom-0 left-0 w-full">
        <div className="p-4 pb-8 lg:p-16 lg:px-28 w-full flex justify-end">
          <Link href={'/report'}>
          <Button size={'icon'}>
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
      <div className="absolute bottom-0 right-0 w-full mb-5 ml-5 p-5">
        <AppInstructions></AppInstructions>
      </div>
    </div>
  );
}