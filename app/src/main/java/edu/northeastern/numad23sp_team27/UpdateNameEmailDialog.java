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

public class UpdateNameEmailDialog extends DialogFragment {

    public interface NameEmailDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String displayName, String email);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NameEmailDialogListener listener;

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NameEmailDialogListener) context;
        } catch (ClassCastException e) {
            Log.d("error", e.getMessage());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.update_name_email, null);
        EditText name = v.findViewById(R.id.displayNameET);
        EditText email = v.findViewById(R.id.emailET);

        builder.setView(v)
                .setPositiveButton(R.string.updateDiag, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(UpdateNameEmailDialog.this, name.getText().toString(), email.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(UpdateNameEmailDialog.this);
                    }
                });
        return builder.create();
    }
}