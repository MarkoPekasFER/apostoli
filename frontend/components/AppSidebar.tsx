import {
  Calendar,
  Home,
  Inbox,
  Key,
  LifeBuoy,
  Search,
  Settings,
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

// Menu items.
const items = [
  {
    title: "Profil",
    url: "/settings/profile",
    icon: Home,
  },
  {
    title: "E-mail",
    url: "/settings/email",
    icon: Inbox,
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
