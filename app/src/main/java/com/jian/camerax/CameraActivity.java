package com.jian.camerax;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraX;
import androidx.camera.core.CaptureConfig;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

import android.Manifest;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jian.deepintoandroid.R;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CameraActivity extends AppCompatActivity {

    private final static String TAG = CameraActivity.class.getSimpleName();
    private static final int RC_CAMERA = 0x01;

    private TextureView mTextureView = null;
    private Button mBtnCapture = null;
    private Executor mCaptureExecutor = null;

    public Preview mPreview = null;
    public ImageCapture mImageCapture = null;

    private ImageAnalysis mImageAnalysis = null;
    private Executor mImageAnalyzerExecutor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mTextureView = findViewById(R.id.view_finder);
        mBtnCapture = findViewById(R.id.btnCapture);
        mBtnCapture.setOnClickListener(new MyClickListener());
        methodRequiresTwoPermission();
    }

    @AfterPermissionGranted(RC_CAMERA)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "got permissions", Toast.LENGTH_LONG).show();
            mTextureView.post(new Runnable() {
                @Override
                public void run() {
                    startCamera();
                }
            });
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.request_cameraPermission),
                    RC_CAMERA, perms);
        }
    }


    private void startCamera() {
        //1. init preview config
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .build();

        mPreview = new Preview(previewConfig);
        mPreview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(@NonNull Preview.PreviewOutput output) {
                //set the surface of textureView
                ViewGroup parent = (ViewGroup) mTextureView.getParent();
                parent.removeView(mTextureView);
                parent.addView(mTextureView, 0);
                Log.d("Jian", "remove and add back the UIs");
                mTextureView.setSurfaceTexture(output.getSurfaceTexture());
            }
        });

        //2. add the capture config
        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
                .build();
        mImageCapture = new ImageCapture(imageCaptureConfig);
        mCaptureExecutor = Executors.newSingleThreadExecutor();


        //3. image analyzer
        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder()
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .setTargetResolution(new Size(480, 640))
                .setLensFacing(CameraX.LensFacing.FRONT)
                .build();
        mImageAnalyzerExecutor = Executors.newFixedThreadPool(2);
        mImageAnalysis = new ImageAnalysis(imageAnalysisConfig);

        mImageAnalysis.setAnalyzer(mImageAnalyzerExecutor, new MyImageAnalyzer());


        //bind to lifecycle
        CameraX.bindToLifecycle(this, mPreview, mImageCapture, mImageAnalysis);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



    private class MyImageAnalyzer implements ImageAnalysis.Analyzer {
        @Override
        public void analyze(ImageProxy imageProxy, int rotationDegrees) {
            final Image image = imageProxy.getImage();
            Log.d(TAG, "dim : " + image.getWidth() + "," + image.getHeight() + " @ "
            + image.getTimestamp());
        }
    }



    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnCapture:
                    Log.d(TAG, "capture button clicked");
                    //take one picture
                    String path = getExternalCacheDir() + "/" + System.currentTimeMillis() + ".png";
                    File photo = new File(path);
                    mImageCapture.takePicture(photo, mCaptureExecutor, new ImageCapture.OnImageSavedListener(){

                        @Override
                        public void onImageSaved(@NonNull File file) {
                            Log.d(TAG, "image saved >>> " + path);
                        }

                        @Override
                        public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
                            Log.e(TAG, "image failed to save!!");
                        }
                    });

                    break;
            }
        }
    }

}
