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

            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if(noConnectivity){
                Toast.makeText(context, "Disconnect", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context, "Connect", Toast.LENGTH_LONG).show();
            }

        }
    }
}
