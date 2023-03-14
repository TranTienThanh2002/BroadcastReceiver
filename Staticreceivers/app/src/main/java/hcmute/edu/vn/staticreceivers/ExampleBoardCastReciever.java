package hcmute.edu.vn.staticreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class ExampleBoardCastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Toast.makeText(context, "Boot completed", Toast.LENGTH_LONG).show();
        }
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            Log.e("activity",context.toString());
            Toast.makeText(context, "Connectivity change", Toast.LENGTH_LONG).show();

        }
    }
}
