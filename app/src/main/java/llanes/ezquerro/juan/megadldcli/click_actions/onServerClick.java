package llanes.ezquerro.juan.megadldcli.click_actions;

import android.content.Context;

public abstract class onServerClick {
    public String URL;
    protected Integer COUNTER;

    public abstract void run(Integer id, String ip, Integer port, Context context);
}
