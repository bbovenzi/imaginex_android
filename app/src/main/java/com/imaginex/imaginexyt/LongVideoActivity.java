package com.imaginex.imaginexyt;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LongVideoActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    VideoView longvidView;
    VideoView shortvidView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_video);

        int mUIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);

        longvidView = (VideoView)findViewById(R.id.longvideoView);
        String vidAddress = "android.resource://"+getPackageName()+"/"+R.raw.lion;
        Uri vidUri = Uri.parse(vidAddress);
        longvidView.setVideoURI(vidUri);
        playVideo();

        shortvidView = (VideoView)findViewById(R.id.shortvideoView);
        vidAddress = "android.resource://"+getPackageName()+"/"+R.raw.garlic;
        vidUri = Uri.parse(vidAddress);
        shortvidView.setVideoURI(vidUri);
        shortvidView.setZOrderOnTop(true);

       RelativeLayout layout = (RelativeLayout) findViewById(R.id.videoOverlay);

        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            Log.d("MyTag", "Touch detected, playing next video and launching app");
                            longvidView.stopPlayback();
                            shortvidView.start();
                            shortvidView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    startService(decorView);
                                    String url = "http://imaginex.co";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });
                        } else {
                            Log.d("MyTag", "Other thing detected");
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });
/*
        layout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("MyTag", "Touch detected, playing next video and launching app");
                longvidView.stopPlayback();
                shortvidView.start();
                shortvidView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        String url = "http://imaginex.co";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                return false;
            }
        }):
*/



    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        playVideo();
    }

    public void startService(View view) {
        Log.d("MyTag", "launching service");
        startService(new Intent(getBaseContext(), OverlayService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), OverlayService.class));
    }

    public void playVideo(){

        longvidView.start();

        longvidView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

                mp.start();
            }
        });

    }

    public void stopVideo(){

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        final View decorView = getWindow().getDecorView();
        stopService(decorView);
    }


        // Set up the user interaction to manually show or hide the system UI.
        /*mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

}
