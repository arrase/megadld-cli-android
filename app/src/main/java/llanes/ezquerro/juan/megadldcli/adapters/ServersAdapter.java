package llanes.ezquerro.juan.megadldcli.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import llanes.ezquerro.juan.megadldcli.R;
import llanes.ezquerro.juan.megadldcli.providers.ServersContentProvider;

public class ServersAdapter extends CursorRecyclerViewAdapter<ServersAdapter.ViewHolder> {

    public ServersAdapter(Cursor cursor) {
        super(cursor);
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
        viewHolder.name.setText(
                cursor.getString(cursor.getColumnIndex(ServersContentProvider.Server.NAME))
        );

        viewHolder.ip.setText(
                cursor.getString(cursor.getColumnIndex(ServersContentProvider.Server.IP))
        );
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView ip;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.serverItemName);
            ip = (TextView) itemView.findViewById(R.id.serverItemIp);
        }

    }
}
