package com.example.fragment2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceLocationFragment extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    TextView text1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeviceLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceLocationFragment newInstance(String param1, String param2) {
        DeviceLocationFragment fragment = new DeviceLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
     /*   supportMapFragment =(SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.google_map);*/
        text1 = (TextView) getActivity().findViewById(android.R.id.text1);
     supportMapFragment  = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        client = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getcurrentlocation();
            getLocation1();

        }
        else {
            ActivityCompat.requestPermissions(getActivity(),
                    new  String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }
    @SuppressLint("MissingPermission")
    private void getLocation1() {
        client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                Log.d("loaction", String.valueOf(location));
                if (location != null) {


                    try {
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresss = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                      text1.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Latitude :</b></br> </font>"
                                        + addresss.get(0).getLatitude()
                        ));
                        /*text2.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Longitude :</b></br> </font>"
                                        + addresss.get(0).getLongitude()
                        ));
                        text3.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Country Name :</b></br> </font>"
                                        + addresss.get(0).getCountryName()
                        ));
                        text4.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Locality :</b></br> </font>"
                                        + addresss.get(0).getLocality()
                        ));
                        text5.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Address :</b></br> </font>"
                                        + addresss.get(0).getAddressLine(0)
                        ));*/
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }else{
                    Toast.makeText(getContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getcurrentlocation() {
        @SuppressLint("MissingPermission")
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latLng =  new LatLng(location.getLatitude()
                                    ,location.getLongitude());

                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("Your here");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getcurrentlocation();
            }
        }
    }


}