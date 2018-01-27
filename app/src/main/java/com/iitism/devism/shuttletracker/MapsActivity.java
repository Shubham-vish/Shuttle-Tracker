package com.iitism.devism.shuttletracker;

import android.*;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import static com.iitism.devism.shuttletracker.R.id.map;


/**
 * Created by shubham on 26/11/17.
 */
public abstract class MapsActivity extends AppCompatActivity implements com.google.android.gms.maps.OnMapReadyCallback {

    private static final LatLng CSE =new LatLng(23.812448243039064,86.441491844844) ;
    int key=1;
    LatLng prev,next;
    Marker marker;
    private GoogleMap mGoogleMap;
    Bitmap icon;

    private static final LatLng l= new LatLng(23.81976387811088,86.43551119930567);
    private static final LatLng ISM = new LatLng(23.814200,86.441122);
    private static final LatLng Maingate = new LatLng(23.809979945223105,86.44245486556781);
    private static final LatLng Ruby = new LatLng(23.812497320586754,86.44351423044122);

    public IconGenerator iconGenerator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        prev=l;
        next=l;

        Toast.makeText(this,"Searching Shuttle, Plase Wait :)", Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;


        iconGenerator = new IconGenerator(getApplicationContext());
        View itemRest = getLayoutInflater().inflate(R.layout.bus_icon,null);
        iconGenerator.setContentView(itemRest);
        Drawable TRANSPARENT_DRAWABLE = new ColorDrawable(Color.TRANSPARENT);
        iconGenerator.setBackground(TRANSPARENT_DRAWABLE);
        icon = iconGenerator.makeIcon();

        mGoogleMap.addMarker(new MarkerOptions().position(l).icon(BitmapDescriptorFactory.fromBitmap(icon)).title("Dhaiya Gate: Source"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(l));
        mGoogleMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(23.81976387811088,86.43551119930567))
                .add(new LatLng(23.819052291688454,86.43638023502649))
                .add(new LatLng(23.818546817583943,86.43708297378839))
                .add(new LatLng(23.81810513957039,86.43775889046015))
                .add(new LatLng(23.81775670363251,86.43825241691889))
                .add(new LatLng(23.817226686609523,86.43901952869714))
                .add(new LatLng(23.816883155901625,86.43951841957391))
                .add(new LatLng(23.81656578476923,86.43981278200067))
                .add(new LatLng(23.81605539336223,86.43985569734491))
                .add(new LatLng(23.815652967953298,86.43987715501703))
                .add(new LatLng(23.815172018876925,86.43980205316461))
                .add(new LatLng(23.814602729912234,86.43946945924677))
                .add(new LatLng(23.814480037998035,86.43958211202539))
                .add(new LatLng(23.81423465382171,86.43953383226312))
                .add(new LatLng(23.814087423093287,86.4394211794845))
                .add(new LatLng(23.813896022896703,86.43951773900903))
                .add(new LatLng(23.813724253249262,86.439512374591))
                .add(new LatLng(23.81348622921928,86.43968135375894))
                .add(new LatLng(23.813078888114276,86.44000321884073))
                .add(new LatLng(23.812708353830363,86.44031703729547))
                .add(new LatLng(23.81251695160062,86.44053161401666))
                .add(new LatLng(23.812448243039064,86.44101977605737))
                .add(new LatLng(23.812472781815224,86.441491844844))
                .add(new LatLng(23.812620014374783,86.4419424559585))
                .add(new LatLng(23.810716120485505,86.44241463243259))
                .add(new LatLng(23.810325948116294,86.44245486556781))
                .add(new LatLng(23.809979945223105,86.4424709588219))
                .add(new LatLng(23.80935664690468,86.44256751834644))
                .add(new LatLng(23.811451964810406,86.44270956773676))
                .add(new LatLng(23.81185440324184,86.44328892488397))
                .add(new LatLng(23.811957466541937,86.44349813718713))
                .add(new LatLng(23.812497320586754,86.44351423044122))
                .add(new LatLng(23.81316477339333,86.44356787462152))
                .add(new LatLng(23.813606468216786,86.44351423044122))
                .add(new LatLng(23.81447022263987,86.4434927727691))
                .add(new LatLng(23.814715606370907,86.44282222051538))
                .add(new LatLng(23.81474996005623,86.44290268678583))
                .add(new LatLng(23.814681252676476,86.44208193082727))
                .add(new LatLng(23.81508368109776,86.4419961001388))
                .add(new LatLng(23.815324156022342,86.44195854921259))
                .add(new LatLng(23.815289802488962,86.4411860730163))
                .add(new LatLng(23.815255448946484,86.44045651216425))
                .add(new LatLng(23.815172018876925,86.43980205316461))
        );
        CameraPosition camPos = new CameraPosition.Builder()
                .target(new LatLng(l.latitude, l.longitude))
                .zoom(17)
                .bearing(1)
                .tilt(1)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mGoogleMap.animateCamera(camUpd3);
        //MapStyleOptions styleOptions=MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setUpMap();
        startDemo();
    }


    public void setUpMap() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        } else {
            mGoogleMap.setMyLocationEnabled(true);
           // mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    protected void onPause() {
        key=0;
        super.onPause();
    }

    @Override
    protected void onResume() {
        key=1;
      //  doit();
        super.onResume();
    }




    private void doit() {
        if(key==1) {

            final Handler hd = new Handler();
            hd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    volleyStringRequst("http://www.saddacampus.com/app/firebase/get.php");
                    doit();
                }
            }, 4000);
        }
    }

    public void onLocationChanged(final LatLng prev, final LatLng currentpos)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;
        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;
            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                if (marker != null) {
                    marker.remove();
                }
                LatLng currentPosition = new LatLng(
                        prev.latitude * (1 - t) + currentpos.latitude * t,
                        prev.longitude * (1 - t) + currentpos.longitude * t);
               // BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.unnamed);
                marker = mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .position(
                                new LatLng(currentPosition.latitude,
                                        currentPosition.longitude))
                        .draggable(true).visible(true).title("Shuttle"));

                CameraPosition camPos = new CameraPosition.Builder()
                        .target(new LatLng(currentpos.latitude, currentpos.longitude))
                        .zoom(18)
                        .bearing(1)
                        .tilt(1)
                        .build();
                CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                mGoogleMap.animateCamera(camUpd3);
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
//                } else {
//                    //Toast.makeText(getApplicationContext(),"Shuttle is on it's way :)",Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }





    public void volleyStringRequst(String url){

        String  REQUEST_TAG = "map";
        //Toast.makeText(getApplicationContext(),"Sahi 1 hai",Toast.LENGTH_SHORT).show();
        StringRequest strReq = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response maps", response.toString());
                // Toast.makeText(getApplicationContext(),"Sahi fyyvy response hai",Toast.LENGTH_SHORT).show();

                try {
                    JSONObject responseObj = new JSONObject(response);

                    String longi = responseObj.getString("lattitude");
                    String latti = responseObj.getString("longitude");
                    Log.e(""+longi,""+latti);
//                    Toast.makeText(MapsActivity.this, response
//
//                            ,Toast.LENGTH_SHORT).show();
                    Double d1=Double.parseDouble(longi);
                    Double d2=Double.parseDouble(latti);

                    LatLng location = new LatLng(d1,d2);
                    prev=next;
                    next=location;
                    onLocationChanged(prev,next);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"JSON Exception: Contact DevISM/Report Bug!",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Check your INTERNET Connection.",Toast.LENGTH_SHORT).show();
                VolleyLog.d("MAPS Activity", "Error: " + error.getMessage());

            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }

    protected abstract void startDemo();




    protected GoogleMap getMap() {
        return mGoogleMap;
    }





}