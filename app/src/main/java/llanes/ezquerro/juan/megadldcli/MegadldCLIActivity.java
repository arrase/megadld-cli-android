package llanes.ezquerro.juan.megadldcli;

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

import llanes.ezquerro.juan.megadldcli.adapters.ServersAdapter;
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
        mRecyclerView.setAdapter(new ServersAdapter(serverTable));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerDataDialog dialog = new ServerDataDialog();
                dialog.show(getSupportFragmentManager(), "ServerDataDialog");
            }
        });
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
