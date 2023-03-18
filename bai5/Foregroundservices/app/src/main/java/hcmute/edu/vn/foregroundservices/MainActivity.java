package hcmute.edu.vn.foregroundservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btnStartService;
    private Button btnStopService;

    private RelativeLayout layoutBottom;

    private ImageView imgSong, imgPlayOrPause, imgClear;

    private TextView tvTitleSong, tvSingleSong;

    private Song mSong;
    private boolean isPlaying;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle ==null){
                return;
            }

            mSong = (Song) bundle.get("object_song");
            isPlaying = (boolean) bundle.get("status_player");
            int actionMusic = bundle.getInt("action_music");

            handleLayoutMusic(actionMusic);
        }
    };

    private void handleLayoutMusic(int actionMusic) {
        switch (actionMusic){
            case MyService.ACTION_START:
                layoutBottom.setVisibility(View.VISIBLE);
                showInforSong();
                setStatusButtonPlayorPause();
                break;
            case MyService.ACTION_PAUSE:
                setStatusButtonPlayorPause();
                break;
            case MyService.ACTION_RESUME:
                setStatusButtonPlayorPause();
                break;
            case MyService.ACTION_CLEAR:
                layoutBottom.setVisibility(View.GONE);
                break;

        }
    }

    private void  showInforSong(){
        if(mSong == null){
            return;
        }
        imgSong.setImageResource(mSong.getImage());
        tvTitleSong.setText(mSong.getTitle());
        tvSingleSong.setText(mSong.getSingle());

        imgPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    sendActivityToService(MyService.ACTION_PAUSE);
                }
                else{
                    sendActivityToService(MyService.ACTION_RESUME);
                }

            }
        });

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendActivityToService(MyService.ACTION_CLEAR);
            }
        });
    }
    private void sendActivityToService(int action){
        Intent intent = new Intent(this,MyService.class);
        intent.putExtra("action_music_service", action);
        startService(intent);

    }
    private void setStatusButtonPlayorPause(){
        if(isPlaying){
            imgPlayOrPause.setImageResource(R.drawable.stop_circle);
        }
        else{
            imgPlayOrPause.setImageResource(R.drawable.play_circle);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));

        btnStartService = findViewById(R.id.btn_start_service);
        btnStopService = findViewById(R.id.btn_stop_service);

        layoutBottom = findViewById(R.id.layout_bottom);
        imgSong = findViewById(R.id.img_song );
        imgPlayOrPause = findViewById(R.id.btn_stop);
        imgClear = findViewById(R.id.btn_clear);
        tvTitleSong = findViewById(R.id.tv_title_song);
        tvSingleSong = findViewById(R.id.tv_single_song);

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStartService();
            }

            
        });
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                clickStopService();
            }
        });
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void clickStopService() {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    private void clickStartService() {
        Song song = new Song("big city boy", "Binz",  R.raw.nhac1, R.drawable.ava_2);
        Intent intent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", song);
        intent.putExtras(bundle);
//        intent.putExtra("key_data_intent", edtDataintent.getText().toString().trim());
        startService(intent);
    }
}