package com.example.rafaelmeyer.mycustomcamera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements SurfaceHolder.Callback {


    private static final int REQUEST_WRITE_READ = 1001;
    private static final int REQUEST_CAMERA = 1002;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean previewing = false;

    private Button buttonTakePicture;
    private ImageButton buttonConfirm;
    private ImageButton buttonCancel;
    private ImageButton buttonGallery;

    private MainActivity self;
    private String mCurrentPhotoPath;

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            inicializeSurfaceView();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
        buttonTakePicture.setVisibility(View.VISIBLE);
        buttonCancel.setVisibility(View.INVISIBLE);
        buttonConfirm.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTakePicture = (Button) findViewById(R.id.button_capture);

        buttonGallery = (ImageButton) findViewById(R.id.imageButtonGallery);
        buttonConfirm = (ImageButton) findViewById(R.id.buttonConfirm);
        buttonCancel  = (ImageButton) findViewById(R.id.buttonCancel);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        buttonGallery.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                        startActivity(intent);
                    }
                }
        );

        buttonTakePicture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        camera.takePicture(null, null, mPicture);
                        buttonTakePicture.setVisibility(View.INVISIBLE);
                        buttonCancel.setVisibility(View.VISIBLE);
                        buttonConfirm.setVisibility(View.VISIBLE);
                    }
                }
        );

        self = this;
    }

    private void inicializeSurfaceView() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        camera.setDisplayOrientation(90);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (previewing) {
            camera.stopPreview();
            previewing = false;
        }
        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] bytes, final Camera camera) {
            self.buttonCancel.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            camera.startPreview();
                            buttonCancel.setVisibility(View.INVISIBLE);
                            buttonConfirm.setVisibility(View.INVISIBLE);
                            buttonTakePicture.setVisibility(View.VISIBLE);
                        }
                    }
            );

            self.buttonConfirm.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            if (ContextCompat.checkSelfPermission(self, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(self, Manifest.permission.READ_EXTERNAL_STORAGE)
                                            == PackageManager.PERMISSION_GRANTED) {

                                savePhoto(bitmap);

                            } else {
                                ActivityCompat.requestPermissions(self,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_WRITE_READ);
                            }
                        }
                    }
            );
        }
    };

    private void savePhoto(Bitmap bitmap) {
        File photoFile = null;
        try {
            photoFile = createImageFile(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri photURI = FileProvider.getUriForFile(self,
                "com.example.android.fileprovider",
                photoFile);

        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        startActivity(intent);
    }

    private File createImageFile(Bitmap bitmap) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".png",
                storageDir);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_READ: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //savePhoto();
                } else {
                    Toast.makeText(this, "Don't have permission", Toast.LENGTH_SHORT).show();
                }
            }
            case REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    inicializeSurfaceView();
                } else {
                    Toast.makeText(this, "Dont't have permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
