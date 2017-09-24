package decodertech.com.saviour;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static decodertech.com.saviour.ContactDb.Contact_person.TABLE_NAME;

/**
 * Created by Dhruve on 9/18/2017.
 */

public class HomeActivity extends Fragment{
    LocationManager locationManager;
    LocationListener locationListener;
    String address;
    MapView mMapView;
    private GoogleMap googleMap;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED) {
                sendMessage(getView());
                }}
        }
        if(requestCode==2){
        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
        startListening();
        }}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Button saveMe=rootView.findViewById(R.id.saveMeButton);
        mMapView = (MapView) rootView.findViewById(R.id.mView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getLocation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //sendMessage(view);
            }
        });


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                try {
                    Location location=getLocation();
                    LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });



        return rootView;
    }
    public void sendMessage(View view){
    ArrayList<contact> list= (ArrayList<contact>) readFromDb();
        ArrayList<String> list1=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            String num=list.get(i).getNumber();
            list1.add(num);
        }
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},1);
        }
        else{
            try {
                for (int j=0;j<list1.size();j++) {
                    SmsManager.getDefault().sendTextMessage(list1.get(j),null,"Please Contact this number as the person requires medical attention asap",null,null);
                }
            }catch (Exception e){
                AlertDialog.Builder alertDialogBuilder = new
                        AlertDialog.Builder(getContext());
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.setMessage(e.getMessage());
                dialog.show();
            }

   /* for (int j=0;j<list1.size();j++) {
        Uri uri = Uri.parse("smsto:" + list1.get(j));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", "Please Contact this number as the person requires medical attention asap");
        startActivity(intent);

    }*/
    }
    }
    public List<contact> readFromDb(){

        SQLiteDatabase database=new SqliteHelper(getContext()).getReadableDatabase();
        String selectQuery="SELECT  * FROM " + TABLE_NAME;
        ArrayList<contact> arrayList=new ArrayList<>();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                contact lang = new contact();
                lang.setPerson_name(cursor.getString(1));
                lang.setNumber(cursor.getString(2));
                lang.setLoc(cursor.getString(3));
                arrayList.add(lang);
            } while (cursor.moveToNext());
        }

        return arrayList;
    }
    public void startListening(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        }
    }
    public Location getLocation() throws IOException {

        locationManager= (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    updateLocationInfo(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        Location lastKnown=null;
        if(Build.VERSION.SDK_INT<23){
            startListening();
        }
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            lastKnown=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(lastKnown!=null){

                updateLocationInfo(lastKnown);
            }
        }
        return lastKnown;
    }
    public void updateLocationInfo(Location location) throws IOException {
        //Toast.makeText(getContext(),location.toString(),Toast.LENGTH_SHORT).show();


        Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses=null;
            String errorMessage=null;
            address ="";
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                Log.i("PlaceInfo", addresses.get(0).toString());
               // Toast.makeText(getContext(),addresses.get(0).toString(),Toast.LENGTH_LONG).show();

            }

            catch (IOException ioException) {
                // Catch network or other I/O problems.
                errorMessage = getString(R.string.service_not_available);
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                errorMessage = getString(R.string.invalid_lat_long_used);
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " +
                        location.getLongitude(), illegalArgumentException);
            }
        try{
        if(addresses.get(0).getLatitude()!=0 &&addresses.get(0).getLongitude()!=0 ){
            address+="Latitude:"+addresses.get(0).getLatitude()+"\n";
            address+="Longitude:"+addresses.get(0).getLongitude()+"\n";
        }

                if(addresses.get(0).getMaxAddressLineIndex()==0){
                    address+=addresses.get(0).getAddressLine(0)+"\n";
                }

                else {for(int i=0;i<=addresses.get(0).getMaxAddressLineIndex();i++)
                {
                    if(addresses.get(0).getAddressLine(i)!=null){
                        address+=addresses.get(0).getAddressLine(i) + "\n";
                    }}
                }
        if(addresses.get(0).getSubAdminArea()!=null){
            address+="District:"+addresses.get(0).getSubAdminArea()+"\n";
        }
                //Toast.makeText(getContext(),address,Toast.LENGTH_SHORT).show();
    }catch (Exception e){
    }

}

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
