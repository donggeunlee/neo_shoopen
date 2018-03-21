package com.jdplus.neo_shoopen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jdplus.neo_shoopen.util.LayoutSize;

public class Activity_Intropage extends AppCompatActivity {

    private static final String TAG = "Activity_Intropage";
    public static Activity_Intropage m_Ref;
    LayoutSize m_LayoutSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intropage);

        m_Ref = Activity_Intropage.this;
        m_LayoutSize = new LayoutSize(m_Ref);

        new Intro_Loading().execute();
    }

    private class Intro_Loading extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Loadingpage_Load();
        }

        @Override
        protected Boolean doInBackground(Void... arg) {
            boolean rtn = false;
            try {
                Thread.sleep(2000);
                rtn = true;
            }
            catch (Exception e) {
                e.printStackTrace();
                return rtn;
            }
            return rtn;
        }

        @Override
        protected void onPostExecute(Boolean arg) {
            super.onPostExecute(arg);
            if(arg) {
                Intent intent = new Intent(m_Ref, Activity_Main.class);
                startActivity(intent);

                finish();
            }
        }
    }

    private void Loadingpage_Load() {
        final ImageView loadview = (ImageView)findViewById(R.id.intropage_view);
        loadview.post(new Runnable() {
            @Override
            public void run() {
                try {
                    loadview.setAdjustViewBounds(true);
                    loadview.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    loadview.setImageResource(R.drawable.intro);
                    loadview.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                catch (Exception e) {e.printStackTrace();}
            }
        });
    }
}
