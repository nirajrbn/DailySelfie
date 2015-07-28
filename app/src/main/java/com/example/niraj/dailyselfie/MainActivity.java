package com.example.niraj.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    static public final String MYTAG = "Lab-Selfie";
    SampleAlarmReceiver alarm = new SampleAlarmReceiver();

    //private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    //private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";

    private String mCurrentPhotoPath= null;
   /* private Uri mCapturedImageURI = null;*/
    private static final int REQUEST_TAKE_PHOTO =1;
    private ImageView mImageView = null;
    /*private ImageView mThumbnailImageView;
    private Uri contentUri = null;  */

    //declare variables required for ListView
    private String[] FilePathStrings; //holds file path of the images captured.
    private String[] FileNameStrings;  //holds name of the images captured.
    private File[] listImage;    //holds the images.
    ListView list;
    ListViewAdapter adapter;
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Populate the list array with image and file name
         populateListArray();

        //create ListAdapter and bind it to ListView
         createListAdapter();

        //fire alarm
        //alarm is an object of class 'SampleAlarmReceiver'.
        // In tis app, Alarm is set without any intervention of a user.
        // As of now, there is no provision of turning the alarm off and no user
        //interaction provision. The user interaction can be placed in menu bar --> for future work.

        alarm.setAlarm(this);

    }//end onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);

    }//end onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_camera:
                dispatchTakePictureIntent();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }//end onOptionsItemSelected



    void populateListArray(){

        Log.i(MYTAG, "Entered populateListArray");
        // Check whether SD Card exists
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                    .show();
        } else {
            // Locate the image folder in your SD Card
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Pictures");  //'Pictures' is the folder where google cam
                                                      // stores pictures.
            Log.i(MYTAG, "file = " +file);
            Log.i(MYTAG, "file.isDirectory = " +file.isDirectory());
        }

        //'Pictures' is a folder under SdCard where stock Google Camera store pictures.
        //If 'Pictures' directory exists then populate the corresponding arrays

        if (file.isDirectory())
        {
            listImage = file.listFiles();
            Log.i(MYTAG, "listImage = " +listImage);
            // Create a String array for FilePathStrings
            //listImage.length is the total number of image in the folder
            FilePathStrings = new String[listImage.length];
            Log.i(MYTAG, "listImage.length = " +listImage.length);

            // Create a String array for FileNameStrings
            FileNameStrings = new String[listImage.length];

            for (int i = 0; i < listImage.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listImage[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listImage[i].getName();
            }
        } //endif

    }


    void createListAdapter(){

        Log.i(MYTAG, "Entered createListAdapter");
        // Locate the ListView in activity_main.xml
        list = (ListView) findViewById(R.id.listview);
        // Pass String arrays to ListViewAdapter Class
        adapter = new ListViewAdapter(this, FilePathStrings, FileNameStrings);
        // Set the ListViewAdapter to the ListView
        list.setAdapter(adapter);




        // Capture Listview item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //ViewImage activity display large image if the list image is pressed.
                Intent i = new Intent(MainActivity.this, ViewImage.class);
                // Pass String arrays FilePathStrings to ViewImage
                i.putExtra("filepath", FilePathStrings);
                // Pass String arrays FileNameStrings to ViewImage
                i.putExtra("filename", FileNameStrings);
                //Pass click position
                i.putExtra("position", position);
                startActivity(i);
            }

        });

    }

    private void dispatchTakePictureIntent() {
        Log.i(MYTAG, "Entered DispatchTakePicture Intent method");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
           if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "There was a problem saving the photo...", Toast.LENGTH_SHORT)
                        .show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri fileUri = Uri.fromFile(photoFile);

                //Original code below
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        fileUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }  //end dispathTakePictureIntent()

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        //original code
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.i(MYTAG, "the mCurrentPhotoPath in createImageFile()=" +mCurrentPhotoPath);
        Log.i(MYTAG, "the var 'image' in createImageFile()=" +image);
        return image;   //'image' is a file type variable

    }  //       end of createImageFile()

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(MYTAG, "Entered onActivityResult");
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            /*Uri selectedImage = data.getData();
            ImageView photo = (ImageView) findViewById(R.id.imageView);
            Bitmap mBitmap = null;
            try
            {
                Log.i(MYTAG, "Entered try method");
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }   */


            //add captured picture to gallery
            addPicToGallery();

            //Repopulate the List Array with new image
            populateListArray();
            //Create List array and add new image
            createListAdapter();

            /*
            //Original code (3 lines) below
            //Note: The original code does not work when Uri is attached to Camera Intent using
            //putExtra method in 'dispatchTakePicture() method above.
            Log.i(MYTAG, "Entered Bundle Extras");
            Bundle extras = data.getExtras();
           Bitmap imageBitmap = (Bitmap) extras.get("data");
          mImageView.setImageBitmap(imageBitmap);   //end of original code (3 lines)

          */

            // Show the full sized image.
            //setFullImageFromFilePath();
            //setFullImageFromFilePath(this.getCurrentPhotoPath(), mThumbnailImageView);


        } else {
            Toast.makeText(this, "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }




    }  //end of onActivityResult()


    private void addPicToGallery() {
        Log.i(MYTAG, "Entered addPicToGallery method");

        //removed "file:" tag in mCurrentPhotoPath that was inserted in "createImage" method
        //this "file:" tag was causing java.io.FileNotFoundException, ENOENT (No such file or directory)
        //error.
        mCurrentPhotoPath = mCurrentPhotoPath.replace("file:","");

        File f = new File(mCurrentPhotoPath);
        Log.i(MYTAG, "value of mCurrentPhotoPath in addPicToGallery: " +mCurrentPhotoPath);
        Log.i(MYTAG, "value of f in addPicToGallery:  " +f);
        Uri contentUri = Uri.fromFile(f);

        Log.i(MYTAG, "value of contentUri in addPicToGallery: " + contentUri);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

        Log.i(MYTAG, "Returned from addPicToGallery method");
    } //end of addPicToGallery()


    /*
    //This method is to show the image just captured by the camera. As the main layout consits of
    //layout view, it is better to put this method to some other activity where this method is the
    //main method and which will show the image captured and when user press back button, it will
    //reach the main list layout.
    private void setFullImageFromFilePath() {

        Log.i(MYTAG, "Entered setFullImageFromFilePath method");

        // Get the dimensions of the View
        int targetW = 50;
        int targetH = 50;

        Log.i(MYTAG,"var targetW in setFullImageFromFilePath is:"  +targetW);
        Log.i(MYTAG,"the targetH in setFullImageFromFilePath is:" + targetH);


        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //bmOptions.inSampleSize = 3;
        bmOptions.inJustDecodeBounds = true;
        Log.i(MYTAG, "the mCurrentPhotoPath in setFullImageFromFilePath is:" + mCurrentPhotoPath);

        //original code
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
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

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap) ;

        Log.i(MYTAG, "Exited from setFullImageFromFilePath method");
    }
  */


}//end MainActivity
