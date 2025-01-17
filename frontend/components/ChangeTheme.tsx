import sidebarLayout from "@/components/SidebarLayout";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { Switch } from "@/components/ui/switch";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useTheme } from "next-themes";

const ChangeTheme = () => {
  const { setTheme } = useTheme();

  return (
    <>
      <div className="flex flex-col space-y-1.5">
        <Label htmlFor="tema">Tema</Label>
        <Select onValueChange={(value) => setTheme(value)}>
          <SelectTrigger id="tema">
            <SelectValue placeholder="Izaberi" />
          </SelectTrigger>
          <SelectContent position="popper">
            <SelectItem value="dark">Tamna</SelectItem>
            <SelectItem value="light">Svijetla</SelectItem>
          </SelectContent>
        </Select>
      </div>
    </>
  );
};

export default ChangeTheme;
