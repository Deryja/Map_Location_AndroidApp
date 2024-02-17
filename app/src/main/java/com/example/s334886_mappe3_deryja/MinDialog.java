package com.example.s334886_mappe3_deryja;





import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;



//Klasse for å lage dialog bokser med tekst inni

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class MinDialog extends DialogFragment {
    private MittInterface callback;

    public interface MittInterface {
        void onYesClick(String adresse, String beskrivelse);

        void onPointerCaptureChanged(boolean hasCapture, String ss);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (MittInterface) getActivity();}

        catch (ClassCastException e) {
            throw new ClassCastException("Kallende klasse må implementere interfacet!");}



    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_layout, null);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.tittel)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                        EditText editTextAdresse = view.findViewById(R.id.editTextAdresse);
                        EditText editTextBeskrivelse = view.findViewById(R.id.editTextBeskrivelse);


                        //For å få ut teksten man har skrevet inn
                        String inputTextAdresse = editTextAdresse.getText().toString();
                        String inputTextBeskrivelse = editTextBeskrivelse.getText().toString();

                        callback.onYesClick(inputTextAdresse, inputTextBeskrivelse);


                    }
                })

                .setView(view)
                .create();
    }
}











