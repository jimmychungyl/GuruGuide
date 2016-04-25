package protodev.test;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jimmy on 24/04/2016.
 */
public class GoogleDirection {

    public String directionsRequest(LatLng startPoint,
                                    LatLng endPoint,
                                    String travelMode) {
        /*
        {
            origin: LatLng | String | google.maps.Place,
                    destination: LatLng | String | google.maps.Place,
                travelMode: TravelMode,
                transitOptions: TransitOptions,
                drivingOptions: DrivingOptions,
                unitSystem: UnitSystem,
                waypoints[]: DirectionsWaypoint,
                optimizeWaypoints: Boolean,
                provideRouteAlternatives: Boolean,
                avoidHighways: Boolean,
                avoidTolls: Boolean,
                region: String
        }
        */
        StringBuilder sbDirectionRequest = new StringBuilder();
        sbDirectionRequest.append("https://maps.googleapis.com/maps/api/directions/json?");
        sbDirectionRequest.append("origin=" + startPoint.latitude + "," + startPoint.longitude);
        sbDirectionRequest.append("&destination=" + endPoint.latitude + "," + endPoint.longitude);
        sbDirectionRequest.append("&sensor=false");
/*        sbDirectionRequest.append("&mode=" + travelMode);
        sbDirectionRequest.append("&key=" + "AIzaSyAM0ySuzrQN4-GoBnHKkCple_vC6ypenGg");*/
        String sDirectionRequest = sbDirectionRequest.toString();
        return sDirectionRequest;
    }


}
