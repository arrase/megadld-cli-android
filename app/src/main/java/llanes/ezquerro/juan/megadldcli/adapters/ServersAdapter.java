package llanes.ezquerro.juan.megadldcli.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import llanes.ezquerro.juan.megadldcli.R;
import llanes.ezquerro.juan.megadldcli.click_actions.onServerClick;
import llanes.ezquerro.juan.megadldcli.providers.ServersContentProvider;

public class ServersAdapter extends CursorRecyclerViewAdapter<ServersAdapter.ViewHolder> {
    private Context mContext;
    private onServerClick mOnServerClick;

    public ServersAdapter(Context context, Cursor cursor, onServerClick onServerClick) {
        super(cursor);
        mContext = context;
        mOnServerClick = onServerClick;
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
        viewHolder.onServerClick = mOnServerClick;

        viewHolder.name.setText(
                cursor.getString(cursor.getColumnIndex(ServersContentProvider.Server.NAME))
        );

        viewHolder.ip.setText(
                cursor.getString(cursor.getColumnIndex(ServersContentProvider.Server.IP))
        );
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView name;
        TextView ip;
        Integer port;
        Integer id;
        onServerClick onServerClick;

        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.serverItemName);
            ip = (TextView) itemView.findViewById(R.id.serverItemIp);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            onServerClick.run(id, ip.getText().toString(), port, mContext);
            return true;
        }
    }
}
