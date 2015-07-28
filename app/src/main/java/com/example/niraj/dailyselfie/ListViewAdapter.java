package com.example.niraj.dailyselfie;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by niraj on 7/24/2015.
 */
public class ListViewAdapter extends BaseAdapter {

    static public final String MYTAG = "ListViewAdapter";

    private ImageView image;
    int position;
    // Declare variables
    private Activity activity;
    private String[] filepath;
    private String[] filename;

    private static LayoutInflater inflater = null;
    View vi;

    public ListViewAdapter(Activity a, String[] fpath, String[] fname) {
        activity = a;
        filepath = fpath;
        filename = fname;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return filepath.length;

    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.listview_item, null);
        // Locate the TextView in listview_item.xml
        TextView text = (TextView) vi.findViewById(R.id.text);
        // Locate the ImageView in listview_item.xml
       image = (ImageView) vi.findViewById(R.id.image);


        // Set file name to the TextView followed by the position
        text.setText(filename[position]);

        /******************************
         * The following code is for downsizing the image which are in Pictures directory. Images
         * are individually downscaled in different 'scaleFactor' inorder that the thumbview image
         * displayed is of same size.
         */
        // Get the dimensions of the View
        //ToDO --> targetW and targetH always is zero returned by img.getWidth and img.getHeight()
        //function. I don't understand why imageView object that are supposed to be created in
        //View getView() is not created till this point the consequence of which is targetW and targetH
        //are zero.
        // int targetW = img.getWidth();
        //int targetH = img.getHeight();

        //targetW and targetH had to be set to 100 so that the image can be downscaled.
        int targetW = 100;
        int targetH= 100;

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
        Log.i(MYTAG,"scaleFactor is:" +scaleFactor);

        //Decode the image file into a Bitmap sized to fill the view
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;  //the original image shall be reduced by 7
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filepath[position], bmOptions);

        image.setImageBitmap(bitmap) ;


        //call setImageFromFilePath to display scaled image
        //setImageFromFilePath(filepath, image);

        /*
         //original code
        // Decode the filepath with BitmapFactory followed by the position
        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);

        // Set the decoded bitmap into ImageView
        image.setImageBitmap(bmp);
        */

        Log.i(MYTAG, "Exited from setFullImageFromFilePath method");
        return vi;

    }//end getView

// Seperating the downscaling code in following method did not work
  //ToDO --> Find out why seprating the code did not work
    private void setImageFromFilePath(String fpath[], ImageView img) {

        Log.i(MYTAG, "Entered setImageFromFilePath method");

        // Get the dimensions of the View
        //ToDO --> targetW and targetH always is zero returned by img.getWidth and img.getHeight()
        //function. I don't understand why imageView object that are supposed to be created in
        //View getView() is not created till this point the consequence of which is targetW and targetH
        //are zero.
       // int targetW = img.getWidth();
        //int targetH = img.getHeight();

        //targetW and targetH had to be set to 100 so that the image can be downscaled.
        int targetW = 100;
        int targetH= 100;

        Log.i(MYTAG,"var targetW in setFullImageFromFilePath is:"  +targetW);
        Log.i(MYTAG,"the targetH in setFullImageFromFilePath is:" + targetH);


        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        Log.i(MYTAG, "the mCurrentPhotoPath in setFullImageFromFilePath is:" + fpath[position]);

        //original code
        BitmapFactory.decodeFile(fpath[position], bmOptions);
       int photoW= bmOptions.outWidth;
       int photoH = bmOptions.outHeight;

        Log.i(MYTAG,"var photoW in setFullImageFromFilePath is:"  +photoW);
        Log.i(MYTAG,"the photoH in setFullImageFromFilePath is:" +photoH);

        //Determine how much to scale down the image
        int scaleFactor = Math.min((int)(photoW/targetW), (int)(photoH/targetH));
        Log.i(MYTAG,"scaleFactor is:" +scaleFactor);

        //Decode the image file into a Bitmap sized to fill the view
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;  //the original image shall be reduced by 7
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fpath[position], bmOptions);

       image.setImageBitmap(bitmap) ;

        Log.i(MYTAG, "Exited from setFullImageFromFilePath method");
    }


}