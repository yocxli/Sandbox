package net.yocxli.mediastorechecker.app;

import net.yocxli.mediastorechecker.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

public class PathInputDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private EditText mText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mText = new EditText(getActivity());
        mText.setHint(R.string.dialog_path_input_hint);
        builder.setView(mText);
        builder.setPositiveButton(R.string.dialog_path_input_ok, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String path = mText.getText().toString();
        Uri data = Uri.parse(path);
        Intent intent = new Intent(getActivity(), MediaStoreDetailActivity.class);
        intent.setData(data);
        startActivity(intent);
    }

}
