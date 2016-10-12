package llanes.ezquerro.juan.megadldcli;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import llanes.ezquerro.juan.megadldcli.adapters.ServersAdapter;
import llanes.ezquerro.juan.megadldcli.click_actions.onServerClick;
import llanes.ezquerro.juan.megadldcli.dialogs.ServerDataDialog;
import llanes.ezquerro.juan.megadldcli.providers.ServersContentProvider;

public class MegadldCLIActivity extends AppCompatActivity {
    private Cursor serverTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_megadld_cli);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // Setup app for share or manage
        Intent intent = getIntent();
        onServerClick mOnServerClick;

        if (intent.getAction() == Intent.ACTION_SEND) { // Send url to server
            mOnServerClick = new onServerClick() {
                @Override
                public void run(Integer id, String ip, Integer port, Context context) {
                    Toast.makeText(context, URL, Toast.LENGTH_SHORT).show();
                }
            };

            mOnServerClick.URL = intent.getStringExtra(Intent.EXTRA_TEXT);

            fab.hide();
        } else { // Manage servers
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ServerDataDialog dialog = new ServerDataDialog();
                    dialog.show(getSupportFragmentManager(), "ServerDataDialog");
                }
            });

            mOnServerClick = new onServerClick() {
                @Override
                public void run(Integer id, String ip, Integer port, Context context) {
                    if (COUNTER <= 0) {
                        COUNTER++;
                        Toast.makeText(context, R.string.confirm_delete, Toast.LENGTH_SHORT).show();
                    } else {
                        ContentResolver cr = context.getContentResolver();
                        cr.delete(ServersContentProvider.CONTENT_URI, "_ID=" + id.toString(), null);
                    }
                }
            };
        }

        // Fill view
        String[] projection = new String[]{
                ServersContentProvider.Server._ID,
                ServersContentProvider.Server.NAME,
                ServersContentProvider.Server.IP,
                ServersContentProvider.Server.PORT};

        serverTable = getContentResolver().query(
                ServersContentProvider.CONTENT_URI, projection, null, null, null
        );

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.servers_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ServersAdapter(this, serverTable, mOnServerClick));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_megadld_cli, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverTable.close();
    }
}
