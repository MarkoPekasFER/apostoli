import React, { useEffect } from "react";
import { GoogleMap, LoadScript, Marker } from "@react-google-maps/api";
import { useRouter } from 'next/compat/router';

export const containerStyle = {
  width: "100vw",
  height: "90vh",
};

export const origin = {
  lat: 45.803016,
  lng: 15.978817,
};

export const nightModeMapStyles = [
  { elementType: "geometry", stylers: [{ color: "#1c1f2a" }] },
  { elementType: "labels.text.stroke", stylers: [{ color: "#1c1f2a" }] },
  { elementType: "labels.text.fill", stylers: [{ color: "#5b6772" }] },
  {
    featureType: "administrative.locality",
    elementType: "labels.text.fill",
    stylers: [{ color: "#b98357" }],
  },
  {
    featureType: "poi",
    elementType: "labels.text.fill",
    stylers: [{ color: "#b98357" }],
  },
  {
    featureType: "poi.park",
    elementType: "geometry",
    stylers: [{ color: "#1d2e30" }],
  },
  {
    featureType: "poi.park",
    elementType: "labels.text.fill",
    stylers: [{ color: "#5e8c64" }],
  },
  {
    featureType: "road",
    elementType: "geometry",
    stylers: [{ color: "#2f3640" }],
  },
  {
    featureType: "road",
    elementType: "geometry.stroke",
    stylers: [{ color: "#1b1f26" }],
  },
  {
    featureType: "road",
    elementType: "labels.text.fill",
    stylers: [{ color: "#8a96a3" }],
  },
  {
    featureType: "road.highway",
    elementType: "geometry",
    stylers: [{ color: "#5b6772" }],
  },
  {
    featureType: "road.highway",
    elementType: "geometry.stroke",
    stylers: [{ color: "#191e29" }],
  },
  {
    featureType: "road.highway",
    elementType: "labels.text.fill",
    stylers: [{ color: "#e9be83" }],
  },
  {
    featureType: "transit",
    elementType: "geometry",
    stylers: [{ color: "#26333f" }],
  },
  {
    featureType: "transit.station",
    elementType: "labels.text.fill",
    stylers: [{ color: "#b98357" }],
  },
  {
    featureType: "water",
    elementType: "geometry",
    stylers: [{ color: "#14202f" }],
  },
  {
    featureType: "water",
    elementType: "labels.text.fill",
    stylers: [{ color: "#434c5f" }],
  },
  {
    featureType: "water",
    elementType: "labels.text.stroke",
    stylers: [{ color: "#14202f" }],
  },
];

export const defaultMapOptions = {
  fullscreenControl: false,
  mapTypeControl: false,
  streetViewControl: false,
  zoomControl: false,
  styles: [
    {
      featureType: "poi",
      elementType: "labels",
      stylers: [{ visibility: "off" }],
    },
    ...nightModeMapStyles,
  ],
};

const GoogleMapParent = () => {
  const [reports, setReports] = React.useState<any[]>([]);
  const router = useRouter();

  useEffect(() => {
    const fetchReports = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        // Redirect to login if not authenticated
        // router?.push('/login');
        // return;
      }
      try {
        const response = await fetch(process.env.NEXT_PUBLIC_API_URL+'/api/v1/report/all', {
          // headers: {
          //   'Authorization': `Bearer ${token}`,
          // },
        });
        if (response.ok) {
          const data = await response.json();
          setReports(data);
        } else if (response.status === 401) {
          // Token might have expired or is invalid
          localStorage.removeItem('token');
          // router?.push('/login');
        } else {
          console.error('Failed to fetch reports');
        }
      } catch (error) {
        console.error('An error occurred:', error);
      }
    };
    fetchReports();
  }, [router]);

  const handleMarkerClick = (report: any) => {
    // Handle marker click, e.g., show report details
    console.log('Clicked report:', report);
    alert(`Disaster Type: ${report.disasterType}\nDescription: ${report.description}`);
  };

 

  return (
    <LoadScript
      googleMapsApiKey={process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY || ""}
    >
      <GoogleMap
        mapContainerStyle={containerStyle}
        center={origin}
        zoom={10}
        options={defaultMapOptions}
      >
        {reports?.map((report, index) => (
        // .filter(report => report.status === 'PENDING') // change to 'ACTIVE' to show only active reports
          <Marker
            key={index}
            position={{
              lat: report.location.latitude,
              lng: report.location.longitude,
            }}
            onClick={() => handleMarkerClick(report)}
            icon={disasterIcons[report.disasterType] || disasterIcons.DEFAULT}
          />
        ))}
      </GoogleMap>
    </LoadScript>
  );
};

export default GoogleMapParent;

 // Define icons for different disaster types
 export const disasterIcons: any = {
  FIRE: {
    url: "/icons/fire.png",
    scaledSize: { width: 40, height: 40 },
  },
  EARTHQUAKE: {
    url: "/icons/mountain.png",
    scaledSize: { width: 40, height: 40 },
  },
  FLOOD: {
    url: "/icons/water.png",
    scaledSize: { width: 40, height: 40 },
  },
  // Add other disaster types with appropriate icons
  DEFAULT: {
    url: "/icons/warning.png",
    scaledSize: { width: 40, height: 40 },
  },
};