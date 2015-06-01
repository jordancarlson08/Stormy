package me.jordancarlson.stormy.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import me.jordancarlson.stormy.utils.Constants;

/**
 * Created by jcarlson on 3/23/15.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();

        Bundle bundle = getArguments();

        String title = bundle.getString(Constants.ALERT_TITLE);
        String body = bundle.getString(Constants.ALERT_BODY);
        String button = bundle.getString(Constants.ALERT_BUTTON);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(body)
                .setPositiveButton(button, null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
