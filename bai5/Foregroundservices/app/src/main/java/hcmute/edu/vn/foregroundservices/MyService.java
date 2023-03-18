package hcmute.edu.vn.foregroundservices;

import static hcmute.edu.vn.foregroundservices.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyService extends Service {
    public  static  final  int ACTION_PAUSE = 1;
    public static final  int ACTION_RESUME = 2;
    public static final  int ACTION_CLEAR = 3;

    public static final  int ACTION_START = 4;
    private boolean isPlayer;
    private Song mSong;

    public MyService() {
    }
    private MediaPlayer mediaPlayer;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if(bundle !=null){
            Song song = (Song) bundle.get("object_song");
            if(song !=null){
                mSong = song;
                startMusic(song);
                senNotification(song);
            }
        }

        int actionMusic = intent.getIntExtra("action_music_service",0);
        handleActionMusic(actionMusic);

        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), song.getResource());
        }
        mediaPlayer.start();
        isPlayer = true;
        sendActionToActivity(ACTION_START);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer !=null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void handleActionMusic(int action){
        switch (action){
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_CLEAR:
                stopSelf();
                sendActionToActivity(ACTION_CLEAR);

                break;

        }
    }

    private void resumeMusic() {
        Log.e("actoion", "resume");

        if(mediaPlayer != null && !isPlayer){
            mediaPlayer.start();
            isPlayer= true;
            senNotification(mSong);
            sendActionToActivity(ACTION_PAUSE);
        }
    }

    private void pauseMusic() {
        Log.e("actoion", "pause");
        if(mediaPlayer != null && isPlayer){
            mediaPlayer.pause();
            isPlayer = false;
            senNotification(mSong);
            sendActionToActivity(ACTION_RESUME);

        }
    }


    private void senNotification(Song song){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), song.getImage());

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);
        remoteViews.setTextViewText(R.id.tv_title_song, song.getTitle());
        remoteViews.setTextViewText(R.id.tv_single_song, song.getSingle());
        remoteViews.setImageViewBitmap(R.id.img_song, bitmap);

        remoteViews.setImageViewResource(R.id.btn_stop, R.drawable.stop_circle);

        if(isPlayer){
            Log.e("actoion", "chay");
            remoteViews.setOnClickPendingIntent(R.id.btn_stop, getPendingIntent(this, ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.btn_stop, R.drawable.stop_circle);
        }
        else{
            Log.e("actoion", "dung");
            remoteViews.setOnClickPendingIntent(R.id.btn_stop, getPendingIntent(this, ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.btn_stop, R.drawable.play_circle);
        }
        remoteViews.setOnClickPendingIntent(R.id.btn_clear, getPendingIntent(this, ACTION_CLEAR));

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)

                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();

        startForeground(1, notification);
    }

    private PendingIntent getPendingIntent(Context context, int action) {

        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("action_music",action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void sendActionToActivity(int action){
        Intent intent = new Intent("send_data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", mSong);
        bundle.putBoolean("status_player", isPlayer);
        bundle.putInt("action_music", action);

        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
