package com.example.s334886_mappe3_deryja;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class MinDialogInformasjon extends DialogFragment {


 TextView TextViewMeldingen;





    private String melding;


   // private String adresse;
   // private String beskrivelse;

    private MittInterfaceInfo callback;

    public interface MittInterfaceInfo {

        //Hva som skjer når jeg trykker på yes knappen inni dialog boksen
        // (vil ikke at noe skal skje bortsett fra at dialogen forsvinner)

        void onYesClickInfo();
        boolean onNoClick();



        void onPointerCaptureChanged(boolean hasCapture);
    }



    public MinDialogInformasjon(String melding) {
        this.melding = melding;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (MittInterfaceInfo) getActivity();}

        catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");}



    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialoginformasjon_layout, null);


        TextView textViewMeldingen = view.findViewById(R.id.textViewMeldingen);
    //    TextView textViewBeskrivelse = view.findViewById(R.id.TextViewBeskrivelse);

        textViewMeldingen.setText(melding);
       // textViewBeskrivelse.setText("Beskrivelse: " + beskrivelse);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.tittelinfo)


                .setPositiveButton(R.string.tittelOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        callback.onYesClickInfo();
                    }
                })

                .setNegativeButton(R.string.slett, new
                        DialogInterface.OnClickListener() {
                            public void onClick (DialogInterface dialog,int whichButton){

                                callback.onNoClick();



                            }


                        })


                .setView(view)
                .create();
    }
}
