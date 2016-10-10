package llanes.ezquerro.juan.megadldcli.dialogs;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;

import llanes.ezquerro.juan.megadldcli.R;
import llanes.ezquerro.juan.megadldcli.providers.ServersContentProvider;

public class ServerDataDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the layout
        final View dialog_view = getActivity().getLayoutInflater().inflate(R.layout.dialog_server_data, null);

        // Use the Builder class for convenient dialog construction
        final AlertDialog serverDataDialog = new AlertDialog.Builder(getActivity())
                .setView(dialog_view)
                .setTitle(R.string.server_dialog_title)
                .create();

        // Buttons action
        Button save = (Button) dialog_view.findViewById(R.id.serverDialogSave);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String serverName = ((EditText) dialog_view.findViewById(R.id.serverName)).getText().toString();
                String serverIp = ((EditText) dialog_view.findViewById(R.id.serverIp)).getText().toString();
                Integer serverPort = Integer.parseInt(
                        ((EditText) dialog_view.findViewById(R.id.serverPort)).getText().toString()
                );

                if (checkInput(serverName, serverIp, serverPort)) {
                    saveData(serverName, serverIp, serverPort);
                    serverDataDialog.dismiss();
                }
            }
        });

        Button cancel = (Button) dialog_view.findViewById(R.id.serverDialogCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                serverDataDialog.cancel();
            }
        });

        return serverDataDialog;
    }

    private boolean checkInput(String name, String ip, Integer port) {
        boolean is_ok = true;
        Integer error_msg = 0;

        if (name.isEmpty() || ip.isEmpty()) {
            error_msg = R.string.error_empty_field;
            is_ok = false;
        }

        Matcher match_ip = Patterns.IP_ADDRESS.matcher(ip);
        if (!match_ip.matches()) {
            error_msg = R.string.error_invalid_ip;
            is_ok = false;
        }

        if (port <= 1 || port > 65535) {
            error_msg = R.string.error_invalid_port;
            is_ok = false;
        }

        if (!is_ok) {
            Toast.makeText(getContext(), error_msg, Toast.LENGTH_SHORT).show();
        }

        return is_ok;
    }

    private void saveData(String name, String ip, Integer port) {
        ContentValues fields = new ContentValues();
        fields.put("name", name);
        fields.put("ip", ip);
        fields.put("port", port);

        ContentResolver cr =  getContext().getContentResolver();

        cr.insert(ServersContentProvider.CONTENT_URI, fields);
    }
}
