package hcmute.edu.vn.staticreceivers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    ExampleBoardCastReciever exampleBoardCastReciever = new ExampleBoardCastReciever();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(exampleBoardCastReciever, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}