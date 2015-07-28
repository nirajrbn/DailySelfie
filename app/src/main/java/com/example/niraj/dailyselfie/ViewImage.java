package com.example.niraj.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by niraj on 7/24/2015.
 */
public class ViewImage extends Activity {

    static public final String MYTAG = "ViewImage";
    // Declare Variable
    TextView text;
    ImageView mImageView;

    //int position =0;
    String[] filepath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(MYTAG, "Entered onCreate of ViewImage class");
        super.onCreate(savedInstanceState);
        // Get the view from view_image.xml
        setContentView(R.layout.view_image);

        // Retrieve data from MainActivity on GridView item click
        Intent i = getIntent();

        // Get the position
        int position = i.getExtras().getInt("position");

        // Get String arrays FilePathStrings which stores file path
        String[] filepath = i.getStringArrayExtra("filepath");

        // Get String arrays FileNameStrings which stores file name
        String[] filename = i.getStringArrayExtra("filename");

        // Locate the TextView in view_image.xml
        text = (TextView) findViewById(R.id.imagetext);

        // Load the text into the TextView followed by the position
        text.setText(filename[position]);

        //Locate the ImageView in view_image.xml
        mImageView = (ImageView)findViewById(R.id.full_image_view);

        // Decode the filepath with BitmapFactory followed by the position
        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);

        // Set the decoded bitmap into ImageView
        mImageView.setImageBitmap(bmp);

       // setFullImageFromFilePath();

    } //end onCreate
    /*
    //This method is to show the image just captured by the camera. As the main layout consits of
    //layout view, it is better to put this method to some other activity where this method is the
    //main method and which will show the image captured and when user press back button, it will
    //reach the main list layout.

    //ToDO: Find out why 'position' variable is not recognized in the following metho?
    private void setFullImageFromFilePath() {

        Log.i(MYTAG, "Entered setFullImageFromFilePath method");
        mImageview = (ImageView) findViewById(R.id.full_image_view);
        // Get the dimensions of the View
        int targetW = mImageview.getWidth();
        int targetH = mImageview.getHeight();

        Log.i(MYTAG,"var targetW in setFullImageFromFilePath is:"  +targetW);
        Log.i(MYTAG,"the targetH in setFullImageFromFilePath is:" + targetH);


        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //bmOptions.inSampleSize = 3;
        bmOptions.inJustDecodeBounds = true;
        Log.i(MYTAG, "the mCurrentPhotoPath in setFullImageFromFilePath is:" + filepath[position]);

        //original code
        BitmapFactory.decodeFile(filepath[position], bmOptions);
        int photoW= bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        Log.i(MYTAG,"var photoW in setFullImageFromFilePath is:"  +photoW);
        Log.i(MYTAG,"the photoH in setFullImageFromFilePath is:" +photoH);

        //Determine how much to scale down the image
        int scaleFactor = Math.min((int)(photoW/targetW), (int)(photoH/targetH));

        //Decode the image file into a Bitmap sized to fill the view
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filepath[position], bmOptions);


        mImageview.setImageBitmap(bitmap) ;

        Log.i(MYTAG, "Exited from setFullImageFromFilePath method");
    }
*/



}//End ViewImage