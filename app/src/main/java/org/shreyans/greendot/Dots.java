package org.shreyans.greendot;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Dots extends Activity {

    ListView mainListView;
    int numWeeks = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_dots);

        // construct data source
        int currentWeek = getCurrentWeek();
        ArrayList<Week> weeksArray = new ArrayList<Week>();
        for (int i = 0; i < numWeeks; i++) {
            int weekNumber = currentWeek + i;
            ArrayList<Integer> dots = WeeksStorage.getDotsForWeek(this, weekNumber);
            WeeksStorage.getDotsForWeek(this, weekNumber);
            Week w = new Week(currentWeek + i, dots, currentWeek);
            weeksArray.add(i, w);
        }

        // create adapter to convert the array to views
        WeeksAdapter adapter = new WeeksAdapter(this, weeksArray);

        // attach the adapter to a ListView
        mainListView = (ListView) findViewById(R.id.mainListView);
        mainListView.setAdapter(adapter);
        mainListView.setDivider(null);

        Button button = (Button) findViewById(R.id.screenshotButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndShareScreenshot();
            }
        });
    }

    private void saveAndShareScreenshot() {

        // take screenshot
        View screen = getWindow().getDecorView().getRootView();
        screen.setDrawingCacheEnabled(true);
        Bitmap bitmap = screen.getDrawingCache();
        String filename = getScreenshotName();
        String filePath = Environment.getExternalStorageDirectory().getPath()
                + File.separator + filename;

        Log.v("filename => ", filename);
        Log.v("filePath => ", filePath);

        File imageFile = new File(filePath);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapData = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
            /*fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            */
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }

        // share
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/png");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
        startActivity(Intent.createChooser(share, "Share Image"));

    }

    private int getCurrentWeek() {
        Calendar cal = new GregorianCalendar();
        Date date = new Date();
        cal.setTime(date);
        int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        return currentWeek;
    }

    private String getScreenshotName() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        return formattedDate + ".png";
    }
}