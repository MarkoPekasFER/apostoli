import {
  Calendar,
  Home,
  Inbox,
  Key,
  LifeBuoy,
  SquareArrowLeft,
  Settings,
  SettingsIcon,
  User,
} from "lucide-react";

import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar";
import Link from "next/link";

const items = [
  {
    title: "Podešavanje",
    url: "/settings/preferences",
    icon: SettingsIcon,
  },
  {
    title: "Profil",
    url: "/settings/profile",
    icon: User,
  },
  {
    title: "Lozinka",
    url: "/settings/password",
    icon: Key,
  },
  {
    title: "Podrška",
    url: "/settings/support",
    icon: LifeBuoy,
  },
  {
    title: "Povratak na početnu",
    url: "/",
    icon: SquareArrowLeft,
  },
];

export function AppSidebar() {
  return (
    <Sidebar className="flex">
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Opcije</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {items.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild>
                    <Link href={item.url}>
                      <item.icon />
                      <span>{item.title}</span>
                    </Link>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  );
}
