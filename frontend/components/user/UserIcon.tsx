import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import {
  Cloud,
  CreditCard,
  Github,
  Keyboard,
  LifeBuoy,
  LogOut,
  Mail,
  MessageSquare,
  Plus,
  PlusCircle,
  Settings,
  User,
  UserPlus,
  Users,
} from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuPortal,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuSub,
  DropdownMenuSubContent,
  DropdownMenuSubTrigger,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

function UserIcon() {
  const router = useRouter();
  const [token, setToken] = useState("");

  useEffect(() => {
    // fetch token from local storage
    const token = localStorage.getItem("token");
    if (!token) {
      // Redirect to login if not authenticated
      // router.push("/login");
      return;
    }
    setToken(token);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    router.push("/login");
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Avatar>
          <AvatarImage src="/user.png" className="bg-white" />
          <AvatarFallback>AP</AvatarFallback>
        </Avatar>
      </DropdownMenuTrigger>
      {
token?
        <DropdownMenuContent className="w-56 ml-4">
        <DropdownMenuLabel>Moj Racun</DropdownMenuLabel>
        <DropdownMenuSeparator />
        <DropdownMenuGroup>
          <DropdownMenuItem>
            <User />
            <Link href="/settings/profile">Profil</Link>
            <DropdownMenuShortcut>⇧⌘P</DropdownMenuShortcut>
          </DropdownMenuItem>
          <DropdownMenuItem>
            <Settings />
            <Link href="/settings/preferences/">Postavke</Link>
          </DropdownMenuItem>
        </DropdownMenuGroup>
        <DropdownMenuSeparator />
        <DropdownMenuItem>
          <LifeBuoy />
          <Link href="/settings/support/">Podrška</Link>
        </DropdownMenuItem>
        <DropdownMenuItem disabled>
          <Cloud />
          <span>API</span>
        </DropdownMenuItem>
        <DropdownMenuSeparator />
          <DropdownMenuItem onClick={handleLogout}>
            <LogOut />
            <span>Log out</span>
            <DropdownMenuShortcut>⇧⌘Q</DropdownMenuShortcut>
          </DropdownMenuItem>
      </DropdownMenuContent>
    :
    <DropdownMenuContent className="w-56 ml-4">
      <DropdownMenuLabel>Moj Racun</DropdownMenuLabel>
      <DropdownMenuSeparator />
      <DropdownMenuItem>
        <UserPlus />
        <Link href="/register">Registriraj se</Link>
      </DropdownMenuItem>
      <DropdownMenuItem>
        <LogOut />
        <Link href="/login">Prijavi se</Link>
      </DropdownMenuItem>  
    </DropdownMenuContent>
    }
    </DropdownMenu>
  );
}

export default UserIcon;
