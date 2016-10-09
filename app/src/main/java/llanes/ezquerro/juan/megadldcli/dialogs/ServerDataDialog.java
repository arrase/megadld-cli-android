package llanes.ezquerro.juan.megadldcli.dialogs;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;

import llanes.ezquerro.juan.megadldcli.R;
import llanes.ezquerro.juan.megadldcli.database.ServerDB;

public class ServerDataDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the layout
        final View dialog_view = getActivity().getLayoutInflater().inflate(R.layout.dialog_server_data, null);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set layout from xml
        builder.setView(dialog_view);
        builder.setTitle(R.string.server_dialog_title);

        // Set action buttons
        builder.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String serverName = ((EditText) dialog_view.findViewById(R.id.serverName)).getText().toString();
                String serverIp = ((EditText) dialog_view.findViewById(R.id.serverIp)).getText().toString();
                Integer serverPort = Integer.parseInt(
                        ((EditText) dialog_view.findViewById(R.id.serverPort)).getText().toString()
                );

                if (checkInput(serverName, serverIp, serverPort)) {
                    saveData(serverName, serverIp, serverPort);
                }
            }
        }).setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return builder.create();
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

        ServerDB db_conn = new ServerDB(getContext());
        SQLiteDatabase db_writer = db_conn.getWritableDatabase();
        db_writer.insert(ServerDB.SERVERS_TABLE_NAME, null, fields);
        db_writer.close();
        db_conn.close();
    }
}
