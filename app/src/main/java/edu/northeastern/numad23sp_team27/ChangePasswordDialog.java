package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ChangePasswordDialog extends DialogFragment {

    public interface ChangePasswordDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String newPassword);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    ChangePasswordDialog.ChangePasswordDialogListener listener;

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ChangePasswordDialogListener) context;
        } catch (ClassCastException e) {
            Log.d("error", e.getMessage());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.change_password, null);
        EditText newPassword = v.findViewById(R.id.passwordET);

        builder.setView(v)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(ChangePasswordDialog.this, newPassword.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(ChangePasswordDialog.this);
                    }
                });
        return builder.create();
    }
}
