package me.jordancarlson.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by jcarlson on 3/23/15.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();

        Bundle bundle = getArguments();
        //add error handling
        String title = bundle.getString("title");
        String body = bundle.getString("body");
        String button = bundle.getString("button");


        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(body)
                .setPositiveButton(button, null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
