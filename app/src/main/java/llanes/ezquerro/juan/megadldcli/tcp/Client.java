package llanes.ezquerro.juan.megadldcli.tcp;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import llanes.ezquerro.juan.megadldcli.R;

public class Client extends AsyncTask<Void, Void, JSONObject> {
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    private String ip;
    private Integer port;
    private String url;
    private Context mContext;

    public Client(String ip, Integer port, String url, Context context) {
        this.ip = ip;
        this.port = port;
        this.url = url;
        this.mContext = context;
    }

    private void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        try {
            InetAddress serverAddr = InetAddress.getByName(ip);
            Socket socket = new Socket(serverAddr, port);

            try {
                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                sendMessage("{\"url\":\"" + url + "\"}");

                String mServerMessage = mBufferIn.readLine();

                return new JSONObject(mServerMessage);

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (socket.isConnected()) {
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        Integer userFeedback = R.string.error;
        try {
            if (response.getBoolean("status")) {
                userFeedback = R.string.url_accepted;
            }
            Toast.makeText(mContext, userFeedback, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
