import { ReactElement } from "react";
import { AppSidebar } from "./AppSidebar";
import { SidebarProvider, SidebarTrigger } from "./ui/sidebar";

export default function sidebarLayout(page: ReactElement) {
  return (
    <SidebarProvider>
      <div className="flex h-screen">
        <AppSidebar />
        <main className="flex-1 p-4 bg-gray-100 w-screen">
          <SidebarTrigger />
          {page}
        </main>
      </div>
    </SidebarProvider>
  );
}
