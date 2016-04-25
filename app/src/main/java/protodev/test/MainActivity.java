package protodev.test;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {

    Button btnShowLocation;
    GPSTracker gps;

    GoogleMap mMap;
    locationData userLocationData = new locationData();
    ArrayList<locationData> locationCarPark = new ArrayList<locationData>();
    ArrayList<locationData> locationAirport = new ArrayList<locationData>();
    private LocationManager locationManager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowLocation = (Button) findViewById(R.id.show_Location);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(MainActivity.this);
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(),
                            "Your Location is -\nLat: " + latitude + "\nLong: " + longitude,
                            Toast.LENGTH_LONG)
                            .show();

                } else {
                    gps.showSettingsAlert();
                }

            }
        });

        //For HttpURLConnection
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());

        //For Marker
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();

        StrictMode.setThreadPolicy(policy);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Get airport car park availability
        String airportData = getJSONData("http://aahkcarpark.blob.core.windows.net/production/data.json");
        airportData = airportData.replace("window.jsonpCallback(",
                "");
        airportData = airportData.replace(");",
                "");/*
        JSONObject jsonObjectAirport1 = null;
        try {
            jsonObjectAirport1 = new JSONObject(airportData);
            JSONObject jsonArrayAirportCarpark1 = jsonObjectAirport1.getJSONObject("carpark1");
            String sAirportC1H = jsonArrayAirportCarpark1.getString("hourly");
            String sAirportC1D = jsonArrayAirportCarpark1.getString("daily");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        JSONObject jsonObjectAirport2 = null;
        try {
            jsonObjectAirport2 = new JSONObject(airportData);
            JSONObject jsonArrayAirportCarpark2 = jsonObjectAirport2.getJSONObject("carpark2");
            String sAirportC2H = jsonArrayAirportCarpark2.getString("hourly");
            String sAirportC2D = jsonArrayAirportCarpark2.getString("daily");

            JSONObject jsonArrayAirportCarpark4 = jsonObjectAirport2.getJSONObject("carpark4");
            JSONObject jsonArrayAirportCarpark4In = jsonArrayAirportCarpark4.getJSONObject("indoor");
            String sAirportC4IH = jsonArrayAirportCarpark4In.getString("hourly");
            String sAirportC4ID = jsonArrayAirportCarpark4In.getString("daily");
            JSONObject jsonArrayAirportCarpark4Out = jsonArrayAirportCarpark4.getJSONObject("outdoor");
            String sAirportC4OH = jsonArrayAirportCarpark4Out.getString("hourly");
            String sAirportC4OD = jsonArrayAirportCarpark4Out.getString("daily");

            JSONObject jsonArrayAirportCarpark1 = jsonObjectAirport2.getJSONObject("carpark1");
            String sAirportC1H = jsonArrayAirportCarpark1.getString("hourly");
            String sAirportC1D = jsonArrayAirportCarpark1.getString("daily");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Initializes Bluetooth adapter.
/*        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();*/

        // Initializes Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);


        // Initializes Floating Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,
                        "Replace with your own action",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action",
                                null)
                        .show();
            }
        });

        FloatingActionButton buttonCarPark = (FloatingActionButton) findViewById(R.id.buttonCarPark);
        buttonCarPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = new String();
                try {
                    string =
                            getDirection(userLocationData.Marker.getPosition(),
                                    locationCarPark.get(1).Marker.getPosition(),
                                    "DRIVING");
                    Log.v("MSG", "Get direction success");
                    Log.v("MSG", string);
                } catch (Exception e) {
                    Log.v("ERR", "Get direction fail");
                }

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API)
                .build();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,
                menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            locationData locData = new locationData();
            locData.Marker = mMap.addMarker(new MarkerOptions().title("My Location"));
            userLocationData.Marker = locData.Marker;
            //
            // mMap.addMarker(userLocationData.Marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocationData.Marker.getPosition()));
        } catch (Exception e) {

        }


        //Get data from JSON
        //
        String jsonData = getJSONData("https://api.myjson.com/bins/10a78");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("address");

            for (int i = 0;
                 i < jsonArray.length();
                 i++) {
                locationData locData = new locationData();
                locData.Marker = mMap.addMarker(new MarkerOptions().title(jsonArray.getJSONObject(i)
                        .getString("name"))
                        .position(new LatLng(jsonArray.getJSONObject(i)
                                .getDouble("lat"),
                                jsonArray.getJSONObject(i)
                                        .getDouble("lng"))));
                locationCarPark.add(locData);
            }
            ;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this,
                    "Set my location fail",
                    Toast.LENGTH_SHORT)
                    .show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else {


            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings()
                    .setMyLocationButtonEnabled(true);
            Toast.makeText(MainActivity.this,
                    "Set my location success",
                    Toast.LENGTH_SHORT)
                    .show();
        }

/*        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.v("Check","Marker " + marker + " Click successes");
            }
        });*/

        //My location button
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                /*Toast.makeText(
                        MainActivity.this,
                        "My location",
                        Toast.LENGTH_SHORT).show();*/
                LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                //criteria.setAccuracy(Criteria.ACCURACY_FINE);

                //Todo:
                //Modify the best provider to provider list to prevent null information feedback

                String provider = "network";
                /*String provider = service.getBestProvider(criteria,
                                                          true);*/
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                Location location = service.getLastKnownLocation(provider);
                if (location != null) {
                    LatLng userLatLng = new LatLng(location.getLatitude(),
                            location.getLongitude());

                    Toast.makeText(MainActivity.this,
                            "My location is available",
                            Toast.LENGTH_SHORT)
                            .show();
                    try {
                        locationData locData = new locationData();
                        locData.Marker = mMap.addMarker(new MarkerOptions().title("My Location")
                                .position(userLatLng));
                        userLocationData.Marker = locData.Marker;
                        userLocationData.Marker.setVisible(false);
                        //
                        //mMap.addMarker(userLocationData.Marker);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocationData.Marker.getPosition()));
                    } catch (Exception e) {

                    }

                } else {
                    Toast.makeText(MainActivity.this,
                            "My location is not available",
                            Toast.LENGTH_SHORT)
                            .show();
                }

                return false;
            }

        });

        // Add a marker in Sydney and move the camera
/*
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/

        for (int i = 0;
             i < locationCarPark.size();
             i++) {

            makeMapMarker(locationCarPark.get(i));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 1: {
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private String getJSONData(String urlString) {
        HttpURLConnection conn = null;
        try {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            // Make a connection
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            // Read data from url
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                    "UTF-8"));
            String jsonString = reader.readLine();
            reader.close();
            // Get json
            //String jsonString = jsonString1;
            return jsonString;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    public void makeMapMarker(locationData locData) {
        LatLng location = locData.Marker.getPosition();
        mMap.addMarker(new MarkerOptions().position(location)
                .title(locData.Marker.getTitle()));
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float accuracy = location.getAccuracy();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(Action.TYPE_VIEW,
                // TODO: choose an action type.
                "Main Page",
                // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://protodev.test/http/host/path"));
        AppIndex.AppIndexApi.start(client,
                viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(Action.TYPE_VIEW,
                // TODO: choose an action type.
                "Main Page",
                // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://protodev.test/http/host/path"));
        AppIndex.AppIndexApi.end(client,
                viewAction);
        client.disconnect();
    }

    //http://kidheart-android.blogspot.hk/2011/10/locationlocationlistener.html
    private void locate() {
        TextView tv = (TextView) findViewById(R.id.location);
        StringBuilder builder = new StringBuilder("Existing provider");
        //呼叫getProviders(boolean EnableOnly)取得一組可用的location provider名稱，true表示為可用的；false表示停用的。
        List<String> providers = locationManager.getProviders(true);
        //定義位置監聽器，接收來自LocationManager的通知。
        android.location.LocationListener locationlistener = new android.location.LocationListener() {
            //位置管理服務利用reuqestLocationUpdates(String , long , float , LocationListener)方法註冊位置監聽器後，這些方法就會被呼叫。
            //Provider狀態改變時呼叫此方法。
            public void onStatusChanged(String provider,
                                        int status,
                                        Bundle extras) {

            }

            //位置改變時呼叫此方法。
            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                System.out.println("onLcationChanged");
            }

            //用戶關閉provider時呼叫此方法。
            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                System.out.println("onProviderDisabled");

            }

            //用戶啟動provider時呼叫此方法。
            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                System.out.println("onProviderEnabled");
            }
        };

        for (String provider : providers) {
            //註冊目前的activity定期由provider通知位置更新。
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(provider,
                    0,
                    1000,
                    locationlistener);

            builder.append("\n")
                    .append(provider)
                    .append(":");
            //回傳從provider最後所知的位置。
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                builder.append("(");
                builder.append(lat);
                builder.append(",");
                builder.append(lng);
                builder.append(")");
                //userLocationData.Marker.setPosition(new LatLng(lat,lng));
            } else {
                builder.append("No location information");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = simpleDateFormat.format(new java.util.Date());
            builder.append(date);
        }
        tv.setText(builder);
    }

    private String getDirection(LatLng startPoint,
                                LatLng endPoint,
                                String travalMode) {

        GoogleDirection googleDirection = new GoogleDirection();
        JsonUtil JsonUtil = new JsonUtil();

        JSONObject jsonObject = JsonUtil.getJSONObject(googleDirection.directionsRequest(startPoint,
                endPoint,
                travalMode));

        JSONArray routes = new JSONArray();
        try {
            //jsonObject = new JSONObject(jsonData);
            routes = jsonObject.getJSONArray("routes");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("MSG", "Routes : " + routes);

        // Decode polyline


        String string = JsonUtil.getJSONData(googleDirection.directionsRequest(startPoint,
                endPoint,
                travalMode));

        return string;
    }

    private String getClosedLocation() {

        //TODO
        String closedLocation = new String();
        Log.v("Check",
                "User Location is : " + userLocationData.Marker.getPosition());
        for (int i = 0;
             i < locationCarPark.size();
             i++) {
            locationCarPark.get(i).Marker.getPosition();
        }
        Log.v("Check",
                "Closed Location is : " + closedLocation);

        return closedLocation;
    }

    public class locationData {
        int ID;
        Marker Marker;
        String HourlyAvailability;
        String HourlyPrice;
        String DailyAvailability;
        String DailyPrice;
    }
}
