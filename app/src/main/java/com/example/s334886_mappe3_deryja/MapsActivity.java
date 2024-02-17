package com.example.s334886_mappe3_deryja;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;


import com.example.s334886_mappe3_deryja.databinding.ActivityMapsBinding;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;



//Important to be able to click anywhere on the map
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, MinDialogInformasjon.MittInterfaceInfo, MinDialog.MittInterface {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;


    private double currentLatitude;
    private double currentLongitude;


    //Oppretter markør (globalt slik at jeg kan få tilgang til denne hvor som helst)
    Marker clickedMarkerGlobalVariabelForFjerning;

//Marker clickedMarkerNyttForsøk;


    private double midlertidigClickedLatitude;
    private double midlertidigClickedLongitude;




    private double globalLatitude;
    private double globalLongitude;




    private List<Sted> steder = new ArrayList<>();

    //Longitude og latitude man trykker på er ikke alltid samme, så man kan bruke en liten verdi som er tillatt å være annerledes
    private static final double LATITUDE_TOLERANCE = 1e-10;
    private static final double LONGITUDE_TOLERANCE = 1e-10;


    //Det som skal skje når knappen yes (Send) trykkes på: (skal aktivere en metode som tar inn de 4 variablene vi vil ha)
    @Override
    public void onYesClick(String adresse, String beskrivelse) {
        saveDataToWebService(currentLatitude, currentLongitude, adresse, beskrivelse);
    }


    public void visDialog() {
        DialogFragment dialog = new MinDialog();
        dialog.show(getSupportFragmentManager(), "Tittel");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






   /*     //For å bruke til metoden av å hente ut data:
        getJSON task = new getJSON();
        task.execute(new  String[]{"https://dave3600.cs.oslomet.no/~s334886/jsonout.php"}); */


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        mMap = googleMap;

        mMap.setOnMapClickListener(this); //Important to be able to click anywhere on the map

        getALLJSON task2 = new getALLJSON();
        task2.execute(new  String[]{"https://dave3600.cs.oslomet.no/~s334886/jsonout.php"});



       /* getALLJSON task2 = new getALLJSON();
        task2.execute(new  String[]{"https://dave3600.cs.oslomet.no/~s334886/jsonout.php"});


        Log.d("1", "onMapReady1");
        for (Sted sted: steder){
            LatLng marker = new LatLng(sted.getLatitude(), sted.getLongitude());

            Log.d("1", "onMapReady2");
            mMap.addMarker(new MarkerOptions().position(marker).title(sted.getAdresse()));


            Log.d("1", "onMapReady3");
        }


        //For å bruke til metoden av å hente ut data:
  /*   getJSON task = new getJSON();
        task.execute(new  String[]{"https://dave3600.cs.oslomet.no/~s334886/jsonout.php"});

*/


        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {


                Log.d("1", "onMapReady1");
                for (Sted sted: steder){
                    LatLng marker = new LatLng(sted.getLatitude(), sted.getLongitude());

                    Log.d("1", "onMapReady2");
                    mMap.addMarker(new MarkerOptions().position(marker).title(sted.getAdresse()));


                    Log.d("1", "onMapReady3");

                }
            }
        });




        //Innebygd metode for markør elementet --> hva som skjer når du trykker på et markør element

        //Dette gjør at popup boksen popper opp når jeg trykker på en allerede lagd markør
        //Kan bygge på mer logikk her
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                // Fetch the specific information for the clicked marker


//Denne linken gjør det mulig å få opp dialog boksen som er inni onPostExecute (istedenfor problemet jeg hadde tidligere som gjorde at
                //Det kun var en spesifikk inni denne metoden her (onMarkerClick) som kunne vises, og ikke en inni onPost
                LatLng markerPosition = marker.getPosition();
                double clickedLatitude = markerPosition.latitude;
                double clickedLongitude = markerPosition.longitude;


                //For å bruke til slette metoden
                midlertidigClickedLatitude = clickedLatitude;
                midlertidigClickedLongitude = clickedLongitude;

                // Bekreftet at begge blir oppdatert for hver markør jeg trykker på
                //   Log.d("MarkerClick", "Clicked Latitude: " + clickedLatitude + ", Clicked Longitude: " + clickedLongitude);
                //   Log.d("MarkerClick", "Updated midlertidigClickedLatitude: " + midlertidigClickedLatitude + ", Updated midlertidigClickedLongitude: " + midlertidigClickedLongitude);


                 Dialog dialog = new Dialog(MapsActivity.this);
                 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                 dialog.setContentView(R.layout.custom_dialoginformasjon_layout);

                for (Sted sted: steder){
                    if (clickedLatitude == sted.getLatitude() && clickedLongitude == sted.getLongitude()){
                        Log.d("Sted",sted.getBeskrivelse() + sted.getLatitude());
                        TextView info = dialog.findViewById(R.id.textViewMeldingen);
                        info.setText("Adresse: " + sted.getAdresse() + "\n" + "Beskrivelse: " + sted.getBeskrivelse());

                    }
                    dialog.show();
                }


                // Fetch the specific information for the clicked marker
               /* new getJSON().execute("https://dave3600.cs.oslomet.no/~s334886/jsonout.php",
                        String.valueOf(clickedLatitude), String.valueOf(clickedLongitude)); */

                return true;
            }
        });









    }



    //Innebygd metode for markør elementet --> hva som skjer når du trykker på et sted på kartet (lager markør)

    //Important to be able to click anywhere on the map
    @Override
    public void onMapClick(LatLng latLng) {




        //Selve markøren (clickedMarker)
        Marker clickedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Markøren")); //Legger til ny markør
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)); //Flytter markøren




        clickedMarkerGlobalVariabelForFjerning = clickedMarker; //Skal bruke denne senere som en global variabel for å fjerne markør


        // Får fram kordinatene til de trykkede markørene
        LatLng markerPosition = clickedMarker.getPosition();


        //For å finne ut hva latitude og longitude er for markøren man setter ut på kartet
        double latitude = markerPosition.latitude;
        double longitude = markerPosition.longitude;

        // Log the coordinates
        Log.d("MarkerCordinates", "Latitude: " + latitude + ", Longitude: " + longitude);


        //Setter de globale variablene lik latitude og longitude for å kunne bruke de
        currentLatitude = latitude;
        currentLongitude = longitude;





        visDialog();








    }


    private class getALLJSON extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... params) {

        Log.d("kjører den?", "do kjører");


        String retur = "";


        try {
            URL urlen = new URL(params[0]);
            HttpsURLConnection conn = (HttpsURLConnection) urlen.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            Log.d("kjører den?", "do kjører2");


            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code :" + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output = "";
            String s;

            while ((s = br.readLine()) != null) {
                output = output + s;

                //  alleSteder = alleSteder + s;
            }

            conn.disconnect();

            Log.d("kjører den?", "do kjører3");

            try {
                JSONArray mat = new JSONArray(output);

                for (int i = 0; i < mat.length(); i++) {


                    JSONObject jsonobject = mat.getJSONObject(i);


                    Sted nysted = new Sted(
                            jsonobject.getDouble("latitude"),
                            jsonobject.getDouble("longitude"),
                            jsonobject.getString("adresse"),
                            jsonobject.getString("beskrivelse"));

                    steder.add(nysted);

               /*     double latitude = jsonobject.getDouble("latitude");
                    double longitude = jsonobject.getDouble("longitude");

                    String adresse = jsonobject.getString("adresse");
                    String beskrivelse = jsonobject.getString("beskrivelse"); */





                //    LatLng marker = new LatLng(latitude, longitude);
                    Log.d("kjører den?", "do kjører4");

                   // mMap.addMarker(new MarkerOptions().position(marker).title("Markøren"));


                    Log.d("kjører den?", "do kjører5");

                    //  globalLatitude = latitude;
                    // globalLongitude = longitude;


                }


                //
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            return "Noe gikk feil" + e;
        }

        return retur;
    }

}































    //Hva som skjer når jeg trykker på yes knappen inni dialog boksen
    // (vil ikke at noe skal skje bortsett fra at dialogen forsvinner)


    @Override
    public void onYesClickInfo() {

    }

    @Override
    public boolean onNoClick() {


        if (Math.abs(globalLatitude - midlertidigClickedLatitude) < LATITUDE_TOLERANCE
                && Math.abs(globalLongitude - midlertidigClickedLongitude) < LONGITUDE_TOLERANCE) {

            clickedMarkerGlobalVariabelForFjerning.remove();}
        return false;
    }




    //FANT UT AV PROBLEMET: problemet var samme med jsonout at kun siste ble skrevet ut fordi jeg brukte currentlatitude, det
    //Samme gjorde jeg her ved å oppdatere midlertidiglatitude til currentlatitude istedenfor å oppdatere den til latitude og longitude
    //Med param innenfor her istedenfor

    //Metode for å fjerne markør og json objekt fra jsonout.php/databasen


    //Metode for sletting av objekt fra databasen







    //Metoden som er inni yes-knapp funksjonen(knappen for send) --> for å
    //legge til data, save det og sende inn til jsonin

    private void saveDataToWebService(double latitude, double longitude, String adresse, String beskrivelse) {

        Sted nyttSted = new Sted();
        nyttSted.setAdresse(adresse);
        nyttSted.setBeskrivelse(beskrivelse);
        nyttSted.setLatitude(latitude);
        nyttSted.setLongitude(longitude);

        steder.add(nyttSted);

        //Lager stedsobjekt -- legger til lokalt array
        new SaveDataAsyncTask().execute(latitude, longitude, adresse, beskrivelse);

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    private class SaveDataAsyncTask extends AsyncTask<Object, Void, Void> {


        @Override
        protected Void doInBackground(Object... params) {
            double latitude = (double) params[0];
            double longitude = (double) params[1];
            String adresse = (String) params[2];
            String beskrivelse = (String) params[3];




            String url = "https://dave3600.cs.oslomet.no/~s334886/jsonin.php";

            try {


                String postData = "latitude=" + latitude + "&longitude=" + longitude +
                        "&adresse=" + URLEncoder.encode(adresse, "UTF-8") +
                        "&beskrivelse=" + URLEncoder.encode(beskrivelse, "UTF-8");


                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(postData.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    //Data blir lagret i web service


                } else {
                    // Håndtere error
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
               //Exception kode

                Log.e("SaveDataAsyncTask", "Error sending data to server: " + e.getMessage());
            }

            return null;
        }

    }






    //Som forelesningen
    private class getJSON extends AsyncTask<String, Void, String> {
        JSONObject jsonObject;

        private double clickedLatitude;
        private double clickedLongitude;



        @Override
        protected String doInBackground(String... params) {
            String retur = "";


            // Tar ur latitude og longitude fra paramterne
            clickedLatitude = Double.parseDouble(params[1]);
            clickedLongitude = Double.parseDouble(params[2]);


            midlertidigClickedLatitude = clickedLatitude;
            midlertidigClickedLongitude = clickedLongitude;

            //Disse er for å kunne bruke variablene inni en annen metode (for sletting)


                try {
                    URL urlen = new URL(params[0]);
                    HttpsURLConnection conn = (HttpsURLConnection) urlen.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.connect();

                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code :" + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    String output = "";
                    String s;

                    while ((s = br.readLine()) != null) {
                        output = output + s;

                      //  alleSteder = alleSteder + s;
                    }

                    conn.disconnect();

                    try {
                        JSONArray mat = new JSONArray(output);

                        for (int i = 0; i < mat.length(); i++) {


                            JSONObject jsonobject = mat.getJSONObject(i);

                            double latitude = jsonobject.getDouble("latitude");
                            double longitude = jsonobject.getDouble("longitude");

                            String adresse = jsonobject.getString("adresse");
                            String beskrivelse = jsonobject.getString("beskrivelse");


                            LatLng marker = new LatLng(latitude, longitude);
                           mMap.addMarker(new MarkerOptions().position(marker).title("Markøren"));

                            globalLatitude = latitude;
                            globalLongitude = longitude;




                            //Laitutde og longitude kan være litt annerledes, så en liten differanse kan lages
                            if (Math.abs(latitude - clickedLatitude) < LATITUDE_TOLERANCE
                                    && Math.abs(longitude - clickedLongitude) < LONGITUDE_TOLERANCE) {

                                retur = retur + "Adresse: " + adresse + "\n" + "Beskrivelse: " + beskrivelse + "\n" + "Latitude: "+ latitude + "\n" +  "Longitude: "+ longitude;



                            }
                        }



                    //
                    } catch (JSONException e) {
                        e.printStackTrace();}

                } catch (Exception e) {
                    return "Noe gikk feil" + e;}

            return retur;
        }



       @Override
        protected void onPostExecute(String s) {

            if (s != null) {

                    visDialogInformasjon(getSupportFragmentManager(), "Info", s);
                }}
        }

  public void visDialogInformasjon(FragmentManager supportFragmentManager, String Info, String adresseLokalt){
      DialogFragment dialogInformation = new MinDialogInformasjon(adresseLokalt); //Henter ut data men kun fra det jeg skriver (fra jsonin)
      dialogInformation.show(getSupportFragmentManager(), "TittelInfo");


  }


       @Override
        public void onPointerCaptureChanged(boolean hasCapture, String SkrivUtjsonut) {
            super.onPointerCaptureChanged(hasCapture);


     }



}

