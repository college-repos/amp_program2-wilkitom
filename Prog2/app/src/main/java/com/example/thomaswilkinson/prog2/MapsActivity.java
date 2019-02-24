package com.example.thomaswilkinson.prog2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private int markerCount=0;
    ArrayList<Bitmap> BitMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
        BitMaps = new ArrayList<Bitmap>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng prexies = new LatLng(41.313112, -105.580927);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18) );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(prexies));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                AlertDialog.Builder alertadd = new AlertDialog.Builder(MapsActivity.this);
                LayoutInflater factory = LayoutInflater.from(MapsActivity.this);
                final View view = factory.inflate(R.layout.alertdialogue, null);
                ImageView img = view.findViewById(R.id.dialog_imageview);
                int temp = (int) marker.getTag();
                if(BitMaps.get(temp) != null) {
                    img.setImageBitmap(BitMaps.get(temp));
                    alertadd.setView(view);
                    alertadd.setNeutralButton("Done!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {

                        }
                    });

                    alertadd.show();

                    return false;
                }
                else
                {
                    img.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
                    return false;
                }
            }
        });
    }

    public void getLocation()
    {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

        }
        else{

            client.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                dispatchTakePictureIntent();
                                double lat = location.getLatitude();
                                double lon = location.getLongitude();
                                LatLng loca= new LatLng(lat,lon);
                                Marker mTag = mMap.addMarker(new MarkerOptions().position(loca));
                                mTag.setTag(markerCount);
                                markerCount++;
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(loca));
                            }
                        }

                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }
    Context getContext(){
        Context mContext = this;
        return mContext;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            BitMaps.add(Bitmap.createScaledBitmap(imageBitmap, 1000, 1000, false));
        }
        else
        {
            BitMaps.add(Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888));
        }
    }
}
