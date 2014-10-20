package com.arly;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

@SuppressLint("NewApi")
public class InfoDialog extends DialogFragment implements OnClickListener {


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(AR_activity.nowObj.getName())
                .setMessage("Distance: "+(int)AR_activity.nowObj.getDistance()+" m")
                .setPositiveButton("Show at map", this)
                .setNegativeButton("Break", this);
        		
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
            {
            	String uri = "geo:"+ AR_activity.nowObj.getLocation().getLatitude() + "," + AR_activity.nowObj.getLocation().getLongitude();
            	Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            	startActivity(mapIntent);
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