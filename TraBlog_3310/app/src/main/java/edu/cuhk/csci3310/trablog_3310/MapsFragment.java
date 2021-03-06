package edu.cuhk.csci3310.trablog_3310;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import edu.cuhk.csci3310.trablog_3310.R;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

public class MapsFragment extends Fragment {
    Marker marker;
    GoogleMap gm;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
        gm = googleMap;
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

            /*if (! (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }*/



            LocationManager locationManager;
            Location loc;
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));

                LatLng curLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 15));
                marker = googleMap.addMarker(new MarkerOptions().position(curLoc).title("Here!"));
                ((TextView)getActivity().findViewById(R.id.latlng)).setText( Double.toString(loc.getLatitude()) +';'+ Double.toString(loc.getLongitude()));

            }else{
                LatLng hk = new LatLng(22.302711, 114.177216);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hk, 12));
            }

            googleMap.setOnMapClickListener(pointOfInterest -> {

                Log.d("map","here" + pointOfInterest);
                if(marker != null) marker.remove();
                marker = googleMap.addMarker(new MarkerOptions().position(pointOfInterest).title("Here!"));
                ((TextView)getActivity().findViewById(R.id.latlng)).setText( Double.toString( pointOfInterest.latitude) + ';' + Double.toString( pointOfInterest.longitude));

            });
        }

    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void moveCamera() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager;
            Location loc;

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));

            LatLng curLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
            gm.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 10));
            marker = gm.addMarker(new MarkerOptions().position(curLoc).title("Here!"));
            ((TextView)getActivity().findViewById(R.id.latlng)).setText( Double.toString(curLoc.latitude) +';'+ Double.toString(curLoc.longitude));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                moveCamera();
                break;
        }
    }
}