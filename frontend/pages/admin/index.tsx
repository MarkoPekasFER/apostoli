import DisasterInstructions from "@/components/DisasterAdmin";
import {
  containerStyle,
  defaultMapOptions,
  disasterIcons,
  origin,
} from "@/components/maps/GoogleMap";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { GoogleMap, LoadScript, Marker } from "@react-google-maps/api";
import { useEffect, useState } from "react";

export type DisasterReport = {
  id: number;
  disasterType: "TORNADO" | "FLOOD" | "EARTHQUAKE" | "FIRE" | string; // Extend as needed
  locationName: {
    id: number;
    address: string | null;
    latitude: number;
    longitude: number;
    city: {
      id: number;
      name: string;
      zipCode: string | null;
      latitudeCityCenter: number;
      longitudeCityCenter: number;
    };
  };
  reportDateTime: string; // ISO 8601 format
  description: string;
  status: "PENDING" | "RESOLVED" | "ACTIVE" | string; // Extend as needed
  user: {
    id: number;
    username: string;
    password: string; // Consider using a safer type if passwords are sensitive
    email: string;
    verified: boolean;
    roles: {
      id: number;
      name: "USER" | "ADMIN" | string; // Extend as needed
    }[];
  };
  photos: string[]; // Assuming photos are URLs or file paths
};

export default function Admin() {
  const [loading, setLoading] = useState(true);
  const [reports, setReports] = useState<DisasterReport[]>([]);
  setTimeout(() => {
    setLoading(false);
  }, 1000);

  const fetchReports = async () => {
    fetch(process.env.NEXT_PUBLIC_API_URL + "/api/v1/report/all", {
      headers: {
        // Authorization: `Bearer ${localStorage.getItem("token")}`,
        // "Content-Type": "application/json",
      },
    })
      .then((res) => res.json())
      .then((data) => {
        setReports(data);
      });
  };
  useEffect(() => {
    // /api/v1/report/all
    fetchReports();
  }, []);

  console.log(reports);

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold">Admin</h1>
      <div className="w-full">
        <Tabs defaultValue="reports" className="w-full">
          <TabsList className="grid w-full grid-cols-2">
            <TabsTrigger value="reports">Reports</TabsTrigger>
            <TabsTrigger value="other">Instructions</TabsTrigger>
          </TabsList>
          <TabsContent value="reports" className="grid gap-2">
            {reports.sort((a, b) => {
              if (a.status === "PENDING" && b.status !== "PENDING") {
                return -1;
              } else if (a.status !== "PENDING" && b.status === "PENDING") {
                return 1;
              }
              return 0;
            }).sort((a, b) => b.reportDateTime.localeCompare(a.reportDateTime)).
            map((report, i) => (
              <div
                key={i}
                className={`p-4 rounded-md border ${
                  report.status === "PENDING"
                    ? "border-yellow-500 border-2"
                    : report.status === "ACTIVE"
                    ? "border-green-500 border-2"
                    : report.status === "FALSE"
                    ? "border-red-500 border-2 opacity-50"
                    : ""
                }`}
              >
                <p className="font-semibold text-lg mb-2">
                  {report.description}
                </p>
                {/* map with lat/long marker */}
                <div className="w-full h-96">
                  <LoadScript
                    googleMapsApiKey={
                      process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY || ""
                    }
                  >
                    <GoogleMap
                      mapContainerStyle={{
                        width: "100%",
                        height: "100%",
                      }}
                      center={{
                        lat: report?.locationName?.latitude,
                        lng: report?.locationName?.longitude,
                      }}
                      zoom={10}
                      options={defaultMapOptions}
                    >
                      <Marker
                        key={loading ? "loading" : "loaded"}
                        position={{
                          lat: report?.locationName?.latitude,
                          lng: report?.locationName?.longitude,
                        }}
                        icon={
                          disasterIcons[report.disasterType] ||
                          disasterIcons.DEFAULT
                        }
                      />
                    </GoogleMap>
                  </LoadScript>
                </div>
                <div className="flex justify-between">
                  <p>{report.status}</p>
                  <p>{report.reportDateTime}</p>
                </div>
                <div className={`flex gap-2
                  ${report.status === "ACTIVE" ? "flex" : "hidden"}
                  `}>
                    <Button 
                    onClick={() => {
                      fetch(
                        process.env.NEXT_PUBLIC_API_URL +
                          `/api/v1/report/resolve/${report.id}`,
                        {
                          method: "POST",
                          headers: {
                            // Authorization: `Bearer ${localStorage.getItem("token")}`,
                            // "Content-Type": "application/json",
                          },
                        }
                      )
                        .then((res) => res.json())
                        .then((data) => {
                          console.log(data);
                          fetchReports();
                        });
                    }}
                    >Resolve</Button>
                </div>
                <div
                  className={` gap-2 ${
                    report.status === "PENDING" ? "flex" : "hidden"
                  }`}
                >
                  <Button
                    onClick={() => {
                      fetch(
                        process.env.NEXT_PUBLIC_API_URL +
                          `/api/v1/report/approve/${report.id}`,
                        {
                          method: "POST",
                          headers: {
                            // Authorization: `Bearer ${localStorage.getItem("token")}`,
                            // "Content-Type": "application/json",
                          },
                        }
                      )
                        .then((res) => res.json())
                        .then((data) => {
                          console.log(data);
                          fetchReports();
                        });
                    }}
                  >
                    Approve
                  </Button>
                  <Button
                    onClick={() => {
                      fetch(
                        process.env.NEXT_PUBLIC_API_URL +
                          `/api/v1/report/reject/${report.id}`,
                        {
                          method: "POST",
                          headers: {
                            // Authorization: `Bearer ${localStorage.getItem("token")}`,
                            // "Content-Type": "application/json",
                          },
                        }
                      )
                        .then((res) => res.json())
                        .then((data) => {
                          console.log(data);
                          fetchReports();
                        });
                    }}
                    variant={"destructive"}
                  >
                    Reject
                  </Button>
                </div>
              </div>
            ))}
          </TabsContent>
          <TabsContent value="other">
            <DisasterInstructions />
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
}
