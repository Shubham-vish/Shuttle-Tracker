
package com.iitism.devism.shuttletracker;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by shubham on 26/11/17.
 */
public class MapsActivityWithMarkers extends MapsActivity implements ClusterManager.OnClusterClickListener<MapItem>, ClusterManager.OnClusterInfoWindowClickListener<MapItem>, ClusterManager.OnClusterItemClickListener<MapItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MapItem> {
    private ClusterManager<MapItem> mClusterManager;

    private class RestMapItemRenderer extends DefaultClusterRenderer<MapItem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final TextView mTextView;
        private final int mDimension;
        private final Drawable TRANSPARENT_DRAWABLE = new ColorDrawable(Color.TRANSPARENT);

        Firebase mFireBase;

        public RestMapItemRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);

            View multiRest = getLayoutInflater().inflate(R.layout.multi_rest_map, null);
            mClusterIconGenerator.setContentView(multiRest);
            mClusterIconGenerator.setBackground(TRANSPARENT_DRAWABLE);

            mClusterImageView = (ImageView) multiRest.findViewById(R.id.image);

            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            View itemRest = getLayoutInflater().inflate(R.layout.rest_map,null);
            mIconGenerator.setContentView(itemRest);
            mIconGenerator.setBackground(TRANSPARENT_DRAWABLE);
            mImageView=(ImageView)itemRest.findViewById(R.id.image_rest);
            mTextView = (TextView) itemRest.findViewById(R.id.restaurant_name_view_map);
        }

        @Override
        protected void onBeforeClusterItemRendered(MapItem restMapItem, MarkerOptions markerOptions) {
            // Draw a single restMapItem.
            // Set the info window to show their name.
            mImageView.setImageResource(restMapItem.icon);
            mTextView.setText(restMapItem.getName());
           // mTextRatingView.setText((int) restMapItem.getRating()+" Star");
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(restMapItem.name);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MapItem> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (MapItem p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.icon);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

          //  mClusterImageView.setImageDrawable();
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<MapItem> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().name;
        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MapItem> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(MapItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MapItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }


    @Override
    protected void startDemo() {
       // getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 9.5f));

        mClusterManager = new ClusterManager<MapItem>(this, getMap());
        mClusterManager.setRenderer(new RestMapItemRenderer());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems();
        mClusterManager.cluster();
    }

    private void addItems() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = firebaseDatabase.getReference("Positions");
        mRef = mRef.child("category");
        //Toast.makeText(getApplicationContext(),mRef.getKey(),Toast.LENGTH_SHORT).show();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()){

                    int drawable;
                    if(snapshotChild.getKey().equals("Hostel")){
                        drawable = R.drawable.hostel;
                    }else if(snapshotChild.getKey().equals("Cafeteria")){
                        drawable = R.drawable.icon;
                    }else if(snapshotChild.getKey().equals("Academic")){
                        drawable = R.drawable.academic;
                    }else if(snapshotChild.getKey().equals("Bus Stop")){
                        drawable = R.drawable.bus_stop;
                    }else{
                        drawable = R.drawable.random;
                    }
                    //Toast.makeText(getApplicationContext(),snapshotChild.getKey(),Toast.LENGTH_SHORT).show();

                    for(DataSnapshot positions : snapshotChild.getChildren()){
                        String mName = positions.getKey();

                        DataSnapshot latitude = positions.child("latitude");
                        DataSnapshot longitude = positions.child("longitude");
                        Double latitudeDouble = (Double) latitude.getValue();
                        Double longitudeDouble = (Double) longitude.getValue();

                        LatLng mLatLng = new LatLng(latitudeDouble,longitudeDouble);

                        mClusterManager.addItem(new MapItem(mLatLng, mName, drawable));
                        //Toast.makeText(getApplicationContext(),positions.getKey(),Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        LatLng Ruby = new LatLng(23.812497320586754,86.44351423044122);

    }



}








