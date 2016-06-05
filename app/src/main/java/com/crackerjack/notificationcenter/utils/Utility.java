package com.crackerjack.notificationcenter.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crackerjack.notificationcenter.R;
import com.crackerjack.notificationcenter.webService.models.AccessToken;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by pratik on 05/06/16.
 */
public class Utility {


    private final static String TAG = "Utility";

    private TextView tvDuration, tvPlayback;
    private ImageButton ibPlayback;
    private CountDownTimer countUpTimer;
    private MediaPlayer mp;

    private static Utility instance;

    private ProgressDialog progressDialog;

    private ProgressBar progressBar;
    private Dialog loadingDialog;
    public Dialog dialogConfirmation;

    //  private static ApiService apiService;

    private static SharedPreferences mPrefs;
    private boolean isPlaying, isDownloaded;
    private int duration;
    private Dialog imageDialog, audioDialog;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE

    };


    public static Utility getInstance() {

        if (instance == null) {
            instance = new Utility();
        }

        return instance;
    }


    public String getCurrentDateTime() {

        SimpleDateFormat strToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return strToDate.format(new Date());
    }

    public void showProgressBar(String message, Context context) {

        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            this.progressDialog = null;
        }
        progressDialog = new ProgressDialog(context, R.style.MyProgressBarTheme);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_DeviceDefault_ProgressBar_Large);
        progressDialog.show();

    }

    public void dismissProgressBar() {
        try {
            if ((this.progressDialog != null) && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            this.progressDialog = null;
        }

    }

    public static void markPresent(Context context) {

        mPrefs = context.getSharedPreferences("myData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putLong(Constants.PRESENCE_TIME, Calendar.getInstance().getTimeInMillis());
        editor.apply();

        long lToday = mPrefs.getLong(Constants.PRESENCE_TIME, 0);

        Logger.v("mark present " + lToday);

    }


    public boolean isPresent(Context context) {


        mPrefs = context.getSharedPreferences("myData", Context.MODE_PRIVATE);

        long lToday = mPrefs.getLong(Constants.PRESENCE_TIME, 0);

        Calendar storedDate = Calendar.getInstance();
        storedDate.setTimeInMillis(lToday);
        storedDate.set(Calendar.HOUR_OF_DAY, 0);
        storedDate.set(Calendar.MINUTE, 0);
        storedDate.set(Calendar.SECOND, 0);
        storedDate.set(Calendar.MILLISECOND, 0);


        Calendar todaysDate = Calendar.getInstance();
        todaysDate.set(Calendar.HOUR_OF_DAY, 0);
        todaysDate.set(Calendar.MINUTE, 0);
        todaysDate.set(Calendar.SECOND, 0);
        todaysDate.set(Calendar.MILLISECOND, 0);

        if (todaysDate.getTimeInMillis() == storedDate.getTimeInMillis()) {
            return true;
        }

        return false;
    }


    public String getHelpDescription(String[] items) {

        String result = "";

        for (String item : items) {

            result = result + "\n" + item + "\n";
        }

        return result;
    }


    public static void shortSnack(View view, String message) {

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackView = snackbar.getView();
        snackbar.show();

        TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
    }


    public static String getApplicationName(Context ctx) throws PackageManager.NameNotFoundException {

        if (ctx == null) {
            throw new NullPointerException("Context cannot be null");
        }

        final PackageManager packageMgr = ctx.getPackageManager();
        ApplicationInfo appInfo = null;

        try {
            appInfo = packageMgr.getApplicationInfo(ctx.getPackageName(), PackageManager.SIGNATURE_MATCH);
        } catch (final PackageManager.NameNotFoundException e) {
            throw new PackageManager.NameNotFoundException(e.getMessage());
        }

        return (String) (appInfo != null ? packageMgr.getApplicationLabel(appInfo) : "UNKNOWN");
    }


    public static boolean isOnline(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public String getAccessToken() {

        String accessToken = "";

        AccessToken accessTokenObj = Hawk.get(Constants.ACCESS_TOKEN);
        if (accessTokenObj != null) {
            accessToken = accessTokenObj.getId();
        }

        return accessToken;

    }
    /**
     * @param context
     * @param imageDrawable   pass 0 to hide image
     * @param onClickListener
     * @param stringContent   To Change the title ,content & button name pass String parameters accordingly
     * @see //In In the 4th parameter(
     * <br>
     * -To Change the title ,content & button name pass String parameters accordingly<br>
     * {1}param 1 -Title<br>
     * {2}param 2 -Content<br>
     * {3}param 3 -Button 1<br>
     * {4}param 4 -Button 2 )
     */
    public void showServifyDialog(final Context context, int imageDrawable, Boolean isCancelable, DialogClick onClickListener, String... stringContent) {


        dialogConfirmation = new Dialog(context, R.style.WhiteBGDialog);
        dialogConfirmation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirmation.setContentView(R.layout.dialog_confirmation_item);
        TextView tvDialogContent = (TextView) dialogConfirmation.findViewById(R.id.tvDialogContent);
        TextView tvDialogTitle = (TextView) dialogConfirmation.findViewById(R.id.tvDialogTitle);
        ImageView ivDialogIcon = (ImageView) dialogConfirmation.findViewById(R.id.ivDialogIcon);
        Button btnCommon = (Button) dialogConfirmation.findViewById(R.id.btnAllow);
        Button btnSkip = (Button) dialogConfirmation.findViewById(R.id.btnSkip);

        if (imageDrawable == 0) {
            ivDialogIcon.setVisibility(View.GONE);
        } else {
            ivDialogIcon.setImageResource(imageDrawable);
        }
        btnSkip.setVisibility(View.VISIBLE);

        dialogConfirmation.setCancelable(isCancelable);

        try {
            tvDialogTitle.setText(Html.fromHtml(stringContent[0]));
        } catch (ArrayIndexOutOfBoundsException e) {
            btnCommon.setOnClickListener(onClickListener);
            btnSkip.setOnClickListener(onClickListener);
            dialogConfirmation.show();

            return;
        }
        try {
            tvDialogContent.setText(Html.fromHtml(stringContent[1]));
        } catch (ArrayIndexOutOfBoundsException e) {
            btnCommon.setOnClickListener(onClickListener);
            btnSkip.setOnClickListener(onClickListener);
            dialogConfirmation.show();

            return;

        }
        try {
            btnCommon.setText(stringContent[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            btnCommon.setOnClickListener(onClickListener);
            btnSkip.setOnClickListener(onClickListener);
            dialogConfirmation.show();

            return;
        }
        try {
            btnSkip.setText(stringContent[3]);
            btnSkip.setVisibility(View.VISIBLE);
        } catch (ArrayIndexOutOfBoundsException e) {
            btnSkip.setVisibility(View.GONE);

        }


        btnCommon.setOnClickListener(onClickListener);
        btnSkip.setOnClickListener(onClickListener);
        dialogConfirmation.show();

    }


    public void dismissServifyDialog() {

        if (dialogConfirmation != null && dialogConfirmation.isShowing()) {
            dialogConfirmation.dismiss();
        }
    }

 /*   public void setupAudioDialog(Context context) {

        audioDialog = new Dialog(context);
        audioDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        audioDialog.setContentView(R.layout.audio_dialog);
        audioDialog.setCancelable(false);

        ImageView ivAudioClose = (ImageView) audioDialog.findViewById(R.id.ivAudioClose);
        ivAudioClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopPlayingAudio();
                    audioDialog.dismiss();
                } else {
                    audioDialog.dismiss();
                }
            }
        });
    }

    public void setupImageDialog(Context context) {

        imageDialog = new Dialog(context);
        imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageDialog.setContentView(R.layout.image_dialog);

        ImageView ivClose = (ImageView) imageDialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog.dismiss();
            }
        });
    }


    public void openAudioDialog(Context context, final Files file) {

        if (audioDialog == null) {
            setupAudioDialog(context);
        }

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Servify");
        final File f = new File(myDir, file.getFileName());

        file.setLocalFilePath(f.getPath());

        if (audioDialog.isShowing()) {
            if (isPlaying) {
                stopPlayingAudio();
                audioDialog.dismiss();
            } else {
                audioDialog.dismiss();
            }
        }

        downloadFile(context, file);
    }

    public void stopPlayingAudio() {

        countUpTimer.cancel();
        tvDuration.setText(String.valueOf(Math.round(mp.getDuration() / 1000)));

        mp.release();
        mp = null;
        isPlaying = false;

        tvPlayback.setText("Start");
        //TODO: Add image
        ibPlayback.setImageResource(R.drawable.play);
    }

    public void downloadFile(Context context, final Files file) {

        Utility.getInstance().showProgressBar("Downloading Audio", context);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Servify");
        final File f = new File(myDir, file.getFileName());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL u = new URL(file.getFilePath());
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(f));
            fos.write(buffer);
            fos.flush();
            fos.close();

            openAudioDialogAfterDownload(file);

        } catch (FileNotFoundException e) {
            Utility.getInstance().dismissProgressBar();

            return; // swallow a 404
        } catch (IOException e) {
            Utility.getInstance().dismissProgressBar();

            return; // swallow a 404
        }

    }


    public void openImageDialog(Context context, Files file) {

        if (imageDialog == null) {
            setupImageDialog(context);
        }

        if (imageDialog.isShowing()) {
            imageDialog.dismiss();
        } else {
            TextView tvImageTitle = (TextView) imageDialog.findViewById(R.id.tvImageTitle);
            ImageView ivAttachedImage = (ImageView) imageDialog.findViewById(R.id.ivAttachedImage);

            tvImageTitle.setText("Image");

            if(!TextUtils.isEmpty(file.getFilePath())) {
                Picasso.with(context).load(file.getFilePath()).fit().centerInside().into(ivAttachedImage);
            }
            imageDialog.show();
        }
    }
*/
    public String getFormattedTime(String strDate) {

        final SimpleDateFormat inputSDF = new SimpleDateFormat("HH:mm:ss");
        inputSDF.setTimeZone(TimeZone.getDefault());

        try {

            Date date = inputSDF.parse(strDate);

            System.out.println("Input time: " + date);

            final SimpleDateFormat outputSDF = new SimpleDateFormat("HH:mm:ss");
            outputSDF.setTimeZone(TimeZone.getTimeZone("UTC"));

            String output = outputSDF.format(date);

            System.out.println("UTC time: " + output);

            return output;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }


    public String getScheduleDateTime(String strDate, String time) {

        final SimpleDateFormat inputSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputSDF.setTimeZone(TimeZone.getDefault());

        try {

            Date date = inputSDF.parse(strDate);

            System.out.println("Input Date Time: " + date);

            final SimpleDateFormat outputSDF = new SimpleDateFormat("yyyy-MM-dd");
            outputSDF.setTimeZone(TimeZone.getTimeZone("UTC"));

            String output = outputSDF.format(date);

            System.out.println("UTC Date Time: " + output);

            return output + " " + time;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }


    public String getCurrentTimeHHmmss() {

        SimpleDateFormat strToDate = new SimpleDateFormat("HH:mm:ss");

        return strToDate.format(new Date());
    }

    public String getCurrentDate() {

        SimpleDateFormat strToDate = new SimpleDateFormat("yyyy-MM-dd");

        return strToDate.format(new Date());
    }

   /* public void setupCountUpTimer(final int AudioDuration) {

        long durationMilli = AudioDuration;

        countUpTimer = new CountDownTimer(durationMilli, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvDuration.setText(String.valueOf(Math.round(AudioDuration / 1000) - (millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                stopPlayingAudio();
            }
        };
    }

    public void playAudio(Files file) {
        mp = new MediaPlayer();
        try {
            mp.setDataSource(file.getLocalFilePath());
            mp.prepare();

            //TODO: show file duration

            duration = mp.getDuration();
            setupCountUpTimer(duration);

            countUpTimer.start();
            mp.start();

            isPlaying = true;

            tvPlayback.setText("Stop");
            //TODO: Add image
            ibPlayback.setImageResource(R.drawable.stop);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openAudioDialogAfterDownload(final Files file) {

        TextView tvAudioTitle = (TextView) audioDialog.findViewById(R.id.tvAudioTitle);

        tvAudioTitle.setText("Audio");

        Utility.getInstance().dismissProgressBar();

        audioDialog.show();

        ImageView ivAudioClose = (ImageView) audioDialog.findViewById(R.id.ivAudioClose);
        ivAudioClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopPlayingAudio();
                    audioDialog.dismiss();
                } else {
                    audioDialog.dismiss();
                }
            }
        });

        tvDuration = (TextView) audioDialog.findViewById(R.id.tvDuration);
        tvPlayback = (TextView) audioDialog.findViewById(R.id.tvPlayback);
        ibPlayback = (ImageButton) audioDialog.findViewById(R.id.ibPlayback);


        MediaPlayer tempMP = new MediaPlayer();
        try {
            tempMP.setDataSource(file.getLocalFilePath());
            tempMP.prepare();
            duration = tempMP.getDuration();
            tvDuration.setText(String.valueOf(Math.round(tempMP.getDuration() / 1000)));

            tempMP.release();
            tempMP = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvPlayback.setText("Start");
        setupCountUpTimer(duration);

        playAudio(file);

        ibPlayback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopPlayingAudio();
                } else {
                    playAudio(file);
                }
            }
        });
    }
*/

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionPhoneState = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);

        if (permission != PackageManager.PERMISSION_GRANTED &&
                permissionPhoneState != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}
