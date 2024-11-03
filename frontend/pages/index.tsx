import GoogleMapParent from "@/components/maps/GoogleMap";
import UserAddReport from "@/components/maps/UserAddReport";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import UserIcon from "@/components/user/UserIcon";

export default function Home() {
  return (
    <div>
      <GoogleMapParent />
      <div className="absolute top-0 left-0 w-full">
        <div className="p-4 w-full flex">
          <UserIcon />
        </div>
      </div>
      <div className="absolute bottom-0 left-0 w-full">
        <div className="p-4 pb-8 lg:p-16 lg:px-28 w-full flex justify-end">
          <UserAddReport />
        </div>
        {/* <Link href="/about">
        <Button>
          About
        </Button>
      </Link> */}
      </div>
    </div>
  );
}
