import React, { useEffect } from "react";
import { GoogleMap, LoadScript, Marker } from "@react-google-maps/api";

const containerStyle = {
  width: "100vw",
  height: "100vh",
};

const origin = {
  lat: 45.803016,
  lng: 15.978817,
};

const nightModeMapStyles = [
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

const defaultMapOptions = {
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
  const [locations, setLocations] = React.useState<any[]>([]);

  useEffect(() => {
    const fetchLocations = async () => {
      const res = await fetch("/api/get/locations");
      const locations = await res.json();
      return locations;
    };
    setTimeout(
      () => fetchLocations().then((locations) => setLocations(locations)),
      1000
    );
  }, []);

  const handleMarkerClick = (e: any) => {
    const location = locations.find(
      (loc) => loc.lat === e.latLng.lat() && loc.lng === e.latLng.lng()
    );
    const name = location?.name;

    console.log(name);
  };
  
  const fireIcon: any = {
    url: "https://cdn-icons-png.freepik.com/256/1066/1066232.png?semt=ais_hybrid", // URL to the custom fire image
    scaledSize: {
      "width": 40,
      "height": 40,
    },
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
        {locations?.map((location, index) => (
          <Marker
            key={index}
            position={location}
            onClick={(e) => handleMarkerClick(e)}
            icon={fireIcon}
          />
        ))}
      </GoogleMap>
    </LoadScript>
  );
};

export default GoogleMapParent;
