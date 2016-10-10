package llanes.ezquerro.juan.megadldcli.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import llanes.ezquerro.juan.megadldcli.R;
import llanes.ezquerro.juan.megadldcli.providers.ServersContentProvider;

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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView name;
        public TextView ip;
        public Integer port;
        public Integer id;
        private Integer delete_counter = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.serverItemName);
            ip = (TextView) itemView.findViewById(R.id.serverItemIp);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (delete_counter <= 0) {
                delete_counter++;
                Toast.makeText(mContext, R.string.confirm_delete, Toast.LENGTH_SHORT).show();
                return false;
            }

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
