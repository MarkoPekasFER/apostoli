import sidebarLayout from "@/components/SidebarLayout";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import ChangeTheme from "../../components/ChangeTheme";

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
            <ChangeTheme></ChangeTheme>
            <div className="flex items-center space-x-2">
              <Label htmlFor="notifications">Primaj obavijesti</Label>
              <Switch id="notifications" />
            </div>
          </div>
        </form>
      </CardContent>

      <CardFooter className="flex justify-between"></CardFooter>
    </Card>
  );
};

export default Profile;
Profile.getLayout = sidebarLayout;
