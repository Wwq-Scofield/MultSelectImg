package com.zhs.imgselect;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import com.zhs.app.imgselect.R;
public class MainActivity extends AppCompatActivity {
    private NotificationManager manager;
    private Notification.Builder mBuilder;
    private RemoteViews mRemoteView;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ImgSelecter.init().maxCount(9).setChooseMode(true).showCamera(true).startMultChooseActivity(this,1);
    }

    public void showNotif(View view){
        showBigPictureNotificationWithMZ(this);

    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showBigPictureNotificationWithMZ(Context context) {
        RemoteViews  mRemoteView = new RemoteViews(this.getPackageName(), R.layout.layout_notification);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Notification notification = generateNotification(builder);
        notification.bigContentView = mRemoteView;
        setClick(mRemoteView);
        notificationManager.notify(111, notification);
    }

    private void setClick(RemoteViews mRemoteView) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendhomeIntent = PendingIntent.getActivity(this, 0, homeIntent, 0);
        mRemoteView.setOnClickPendingIntent(R.id.img, pendhomeIntent);
    }

    private Notification generateNotification(Notification.Builder builder) {
        Notification.Builder builder1 = builder.setSmallIcon(R.mipmap.icon).setContentTitle("title")
                .setContentText("Content text");
        return builder1.getNotification();

    }

}
