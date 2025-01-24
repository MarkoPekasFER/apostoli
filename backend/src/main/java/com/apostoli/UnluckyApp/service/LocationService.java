package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.City;
import com.apostoli.UnluckyApp.model.entity.Location;

public interface LocationService {
    Location findOrCreateLocation(Location location);

    City findNearestCity(Location location);
}
