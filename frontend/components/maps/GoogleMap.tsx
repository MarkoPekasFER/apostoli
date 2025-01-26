import React, { useEffect, useState } from "react";
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
  const [selectedReport, setSelectedReport] = useState<any>(null);
  const router = useRouter();
  const [reset, setReset] = React.useState(false);

useEffect(() => {
    setTimeout(() => {
      setReset(true);
    }, 2000);
  }, [reports]);

  console.log(reports)
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

  const handleMarkerClick = async (report: any) => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/report/get/${report.id}`
      );
      
      if (!response.ok) throw new Error('Failed to fetch report details');
      
      const data = await response.json();
      console.log(data);
      const photos = data.photoResponse.flatMap((photo: any) => 
        `data:image/jpeg;base64,${photo.data}`
      );

      setSelectedReport({
        ...data.reportDTO,
        photos: photos
      });
    } catch (error) {
      console.error('Error fetching report details:', error);
      alert('Failed to load report details');
    }
  };

  return (
    <>
      <LoadScript googleMapsApiKey={process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY || ""}>
        <GoogleMap
          mapContainerStyle={containerStyle}
          center={origin}
          zoom={10}
          options={defaultMapOptions}
        >
          {reports?.filter(report => report.status === 'ACTIVE').map((report, index) => (
            <Marker
              key={`${report.id}-${index}`}
              position={{
                lat: report?.locationName?.latitude,
                lng: report?.locationName?.longitude,
              }}
              onClick={() => handleMarkerClick(report)}
              icon={disasterIcons[report.disasterType] || disasterIcons.DEFAULT}
            />
          ))}
        </GoogleMap>
      </LoadScript>

      {selectedReport && (
        <div 
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            backgroundColor: 'rgba(0,0,0,0.5)',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            zIndex: 1000,
          }} 
          onClick={() => setSelectedReport(null)}
        >
          <div 
            style={{
              backgroundColor: 'white',
              padding: '20px',
              borderRadius: '8px',
              maxWidth: '500px',
              maxHeight: '80vh',
              overflowY: 'auto',
              position: 'relative',
            }} 
            onClick={(e) => e.stopPropagation()}
          >
            <h2 style={{ marginTop: 0 }}>{selectedReport.disasterType}</h2>
            <p><strong>Description:</strong> {selectedReport.description}</p>
            <p><strong>Date:</strong> {new Date(selectedReport.reportDateTime).toLocaleString()}</p>
            <p><strong>Status:</strong> {selectedReport.status}</p>
            <p><strong>Reported by:</strong> {selectedReport.username}</p>
            
            {selectedReport.photos?.length > 0 && (
              <div>
                <h4>Photos:</h4>
                {selectedReport.photos.map((photo: string, index: number) => (
                  <img 
                    key={index}
                    src={photo}
                    alt={`Report ${index + 1}`}
                    style={{ 
                      width: '100%', 
                      margin: '10px 0', 
                      borderRadius: '4px',
                      boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                    }}
                  />
                ))}
              </div>
            )}

            <button 
              onClick={() => setSelectedReport(null)}
              style={{
                position: 'absolute',
                top: '10px',
                right: '10px',
                background: 'none',
                border: 'none',
                fontSize: '1.2rem',
                cursor: 'pointer',
              }}
            >
              ×
            </button>
          </div>
        </div>
      )}
    </>
  );
};


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

export default GoogleMapParent;