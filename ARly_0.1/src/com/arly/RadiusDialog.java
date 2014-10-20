package com.arly;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

@SuppressLint("NewApi")
public class RadiusDialog extends DialogFragment implements OnClickListener {

    final EditText editText = new EditText(AR_activity.context);
    
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Radius")
                .setMessage("Input radius (in meters)")
                .setPositiveButton("OK", this)
                .setNegativeButton("Cancel", this)
                .setView(editText);
        		
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
            {
            	String value = editText.getText().toString();
            	AR_activity.radius = Double.parseDouble(value)/1000.0;
            }
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
        }
    }

	public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}