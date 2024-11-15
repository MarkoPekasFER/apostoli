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
import AppInstructions from "@/components/AppInstructions";

const Profile = () => {
  return (
    <Card className="h-4/5 sm:w-3/4 md:w-1/2 lg:w-1/2 bg-white shadow-lg rounded-lg p-6">
      <CardHeader>
        <CardTitle>Postavke - podešavanje</CardTitle>
        <CardDescription>
          Upravljajte postavkama kako biste prilagodili aplikaciju svojim
          potrebama.
        </CardDescription>
      </CardHeader>

      <CardContent>
        <form>
          <div className="grid w-full gap-4">
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="tema">Tema</Label>
              <Select>
                <SelectTrigger id="tema">
                  <SelectValue placeholder="Izaberi" />
                </SelectTrigger>
                <SelectContent position="popper">
                  <SelectItem value="Tamna">Tamna</SelectItem>
                  <SelectItem value="Svijetla">Svijetla</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="flex items-center space-x-2">
              <Label htmlFor="notifications">Primaj obavijesti</Label>
              <Switch id="notifications" />
            </div>
          </div>
        </form>
      </CardContent>

      <CardFooter className="flex justify-between">
        <Button>Spremi promjene</Button>
      </CardFooter>
    </Card>
  );
};

export default Profile;
Profile.getLayout = sidebarLayout;
