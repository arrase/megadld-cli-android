package llanes.ezquerro.juan.megadldcli.adapters;

import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;

import llanes.ezquerro.juan.megadldcli.R;
import llanes.ezquerro.juan.megadldcli.providers.ServersContentProvider;
import llanes.ezquerro.juan.megadldcli.tcp.Client;

public class ServersAdapter extends CursorRecyclerViewAdapter<ServersAdapter.ViewHolder> {
    private Context mContext;

    public ServersAdapter(Context context, Cursor cursor) {
        super(cursor);
        mContext = context;
    }

    @Override
    public ServersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.server_item, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        viewHolder.port = cursor.getInt(cursor.getColumnIndex(ServersContentProvider.Server.PORT));
        viewHolder.id = cursor.getInt(cursor.getColumnIndex(ServersContentProvider.Server._ID));

        viewHolder.name.setText(
                cursor.getString(cursor.getColumnIndex(ServersContentProvider.Server.NAME))
        );

        viewHolder.ip.setText(
                cursor.getString(cursor.getColumnIndex(ServersContentProvider.Server.IP))
        );
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView name;
        public TextView ip;
        public Integer port;
        public Integer id;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.serverItemName);
            ip = (TextView) itemView.findViewById(R.id.serverItemIp);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            String url = clipboard.getPrimaryClip().getItemAt(0).getText().toString();

            Matcher match_url = Patterns.WEB_URL.matcher(url);
            if (!match_url.matches()) {
                Toast.makeText(mContext, R.string.invalid_url, Toast.LENGTH_SHORT).show();
                return;
            }
            Client client = new Client(ip.getText().toString(), port, url, mContext);
            client.execute();
        }

        @Override
        public boolean onLongClick(View v) {
            ContentResolver cr = mContext.getContentResolver();
            Integer rows = cr.delete(ServersContentProvider.CONTENT_URI, "_ID=" + id.toString(), null);
            if (rows < 0) {
                return false;
            }
            Toast.makeText(mContext, name.getText() + " deleted!", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
