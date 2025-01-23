package com.apostoli.UnluckyApp.service.impl;


import com.apostoli.UnluckyApp.model.entity.City;
import com.apostoli.UnluckyApp.model.entity.Location;
import com.apostoli.UnluckyApp.repository.LocationRepository;
import com.apostoli.UnluckyApp.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final CityServiceImpl cityService;


    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, CityServiceImpl cityService) {
        this.locationRepository = locationRepository;
        this.cityService = cityService;
    }

    public Location findOrCreateLocation(Location location) {
        Location existingLocation = locationRepository.findByLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
        if (existingLocation != null) {
            return existingLocation;
        } else {
            City nearestCity = findNearestCity(location);
            location.setCity(nearestCity);
            return locationRepository.save(location);
        }
    }


    public City findNearestCity(Location location){
        City nearestCity = null;
        double minDistance = Double.MAX_VALUE;

        for(City city : cityService.getAllCities()){
            double distance = calculateDistance(location.getLatitude(),location.getLongitude(),city.getLatitudeCityCenter(),city.getLongitudeCityCenter());
            if(distance < minDistance){
                minDistance = distance;
                nearestCity = city;
            }
        }

        // Provjera je li najbliži grad udaljen više od 30 km
        if (minDistance > 35) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is outside of Croatia");
        }

        return nearestCity;
    }

    /**
    Haversine formula:
    The haversine formula can be used to find the distance between two points on a sphere given their latitude and longitude.
     **/
    private double calculateDistance(Double locLat, Double locLng, double cityLat, double cityLng) {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(cityLat - locLat);
        double lonDistance = Math.toRadians(cityLng - locLng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(locLat)) * Math.cos(Math.toRadians(cityLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


}
