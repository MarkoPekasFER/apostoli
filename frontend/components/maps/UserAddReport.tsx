// UserAddReport.tsx
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "../ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../ui/select";
import { Label } from "../ui/label";
import { Button } from "../ui/button";
import { useEffect, useState } from "react";
import { Plus } from "lucide-react";
import { useRouter } from "next/router";

// Import Google Maps components
import { GoogleMap, LoadScript, Marker } from "@react-google-maps/api";

function UserAddReport() {
  const [form, setForm] = useState({
    description: "",
    disasterType: "",
    location: {
      latitude: 0,
      longitude: 0,
    },
  });
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const router = useRouter();

  // Map settings
  const mapContainerStyle = {
    width: "100%",
    height: "256px",
  };

  const [mapCenter, setMapCenter] = useState({
    lat: 45.803016,
    lng: 15.978817,
  });

  const [markerPosition, setMarkerPosition] =
    useState<google.maps.LatLngLiteral | null>(null);

  // Function to get user's current location
  const getCurrentLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const userLocation = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          };
          setMapCenter(userLocation);
          setMarkerPosition(userLocation);
          setForm((prevForm) => ({
            ...prevForm,
            location: {
              latitude: position.coords.latitude,
              longitude: position.coords.longitude,
            },
          }));
        },
        (error) => {
          console.error("Error getting location:", error);
          alert(
            "Ne možemo dohvatiti vašu lokaciju. Molimo omogućite pristup lokaciji."
          );
        }
      );
    } else {
      alert("Vaš preglednik ne podržava geolokaciju.");
    }
  };

  useEffect(() => {
    if (isDialogOpen) {
      getCurrentLocation();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isDialogOpen]);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    if (!token) {
      // Redirect to login if not authenticated
      router.push("/login");
      return;
    }
    try {
      const response = await fetch(
        process.env.NEXT_PUBLIC_API_URL + "/api/v1/report/submit",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(form),
        }
      );
      if (response.ok) {
        alert("Nesreća je uspješno prijavljena");
        setIsDialogOpen(false);
        // Optionally, refresh the page to show the new report
        router.push("/");
      } else if (response.status === 401) {
        // Token might have expired or is invalid
        localStorage.removeItem("token");
        router.push("/login");
      } else {
        const errorData = await response.json();
        console.error("Failed to submit report:", errorData);
        alert(
          "Greška prilikom prijave nesreće: " +
            (errorData.message || "Nepoznata greška")
        );
      }
    } catch (error) {
      console.error("An error occurred:", error);
      alert("Došlo je do pogreške prilikom prijave nesreće.");
    }
  };

  // Handle map click to select location
  const handleMapClick = (event: google.maps.MapMouseEvent) => {
    if (event.latLng) {
      const lat = event.latLng.lat();
      const lng = event.latLng.lng();

      setMarkerPosition({ lat, lng });
      setForm((prevForm) => ({
        ...prevForm,
        location: {
          latitude: lat,
          longitude: lng,
        },
      }));
    }
  };

  return (
    <Dialog open={true} onOpenChange={() => {
      router.back();
    }}>
      <DialogTrigger className="p-4 bg-white text-neutral-900 shadow-2xl rounded-full">
        {/* <Plus /> */}
      </DialogTrigger>
      <DialogContent className="bg-white text-neutral-900 dark:bg-gray-900 dark:text-white dark:border-white">
        <form onSubmit={handleSubmit}>
          <DialogHeader>
            <DialogTitle>Prijavi Nesreću</DialogTitle>
            <DialogDescription>Dodaj podatke o nesreći</DialogDescription>
          </DialogHeader>
          <div className="flex flex-col gap-2 pt-4">
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="description">Kratki opis</Label>
              <Input
                id="description"
                placeholder="Kratko opišite nesreću"
                value={form.description}
                onChange={(e) =>
                  setForm({ ...form, description: e.target.value })
                }
              />
            </div>
            <div className="flex flex-col space-y-1.5">
              <Label htmlFor="location">Odaberite lokaciju</Label>
              <div className="w-full h-64 bg-neutral-100 rounded-md">
                <LoadScript
                  googleMapsApiKey={
                    process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY || ""
                  }
                >
                  <GoogleMap
                    mapContainerStyle={mapContainerStyle}
                    center={mapCenter}
                    zoom={10}
                    onClick={handleMapClick}
                  >
                    {markerPosition && <Marker position={markerPosition} />}
                  </GoogleMap>
                </LoadScript>
              </div>
            </div>
            <div className="grid w-full items-center gap-4 pb-4">
              <div className="flex flex-col space-y-1.5">
                <Label htmlFor="disasterType">Tip nesreće</Label>
                <Select
                  onValueChange={(value) =>
                    setForm({ ...form, disasterType: value })
                  }
                  value={form.disasterType}
                >
                  <SelectTrigger id="disasterType">
                    <SelectValue placeholder="Odaberite tip nesreće" />
                  </SelectTrigger>
                  <SelectContent position="popper">
                    <SelectItem value="EARTHQUAKE">Potres</SelectItem>
                    <SelectItem value="FIRE">Požar</SelectItem>
                    <SelectItem value="FLOOD">Poplava</SelectItem>
                    <SelectItem value="HURRICANE">Uragan</SelectItem>
                    <SelectItem value="TORNADO">Tornado</SelectItem>
                    <SelectItem value="TSUNAMI">Tsunami</SelectItem>
                    <SelectItem value="VOLCANO">Vulkan</SelectItem>
                    <SelectItem value="OTHER">Drugo</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>
          </div>
          <DialogFooter>
            <Button className="dark:bg-black dark:text-white" type="submit">
              Prijavi
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

export default UserAddReport;
