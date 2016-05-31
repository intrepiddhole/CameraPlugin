package org.apache.cordova.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Camera Activity Class. Configures Android camera to take picture and show it.
 */
public class CameraActivity extends Activity {

	private static final String TAG = "CameraActivity";
	
	private CameraPreview mPreview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getResources().getIdentifier("cameraplugin", "layout", getPackageName()));

		// Create a Preview and set it as the content of activity.
	    mPreview = new CameraPreview(this, 0, CameraPreview.LayoutMode.FitToParent);
		
		LinearLayout preview = (LinearLayout)findViewById(getResources().getIdentifier("camera_preview", "id", getPackageName()));
		preview.addView(mPreview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// Add a listener to the Capture button
		ImageView captureButton = (ImageView) findViewById(getResources().getIdentifier("button_capture", "id", getPackageName()));
		captureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mPreview.takePhoto(CameraActivity.this);
			}
		});

		ImageView cancelButton = (ImageView) findViewById(getResources().getIdentifier("button_cancel", "id", getPackageName()));
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	public void proceedWithBitmap(byte[] data, Camera camera){
		Uri fileUri = (Uri) getIntent().getExtras().get(MediaStore.EXTRA_OUTPUT);

		File pictureFile = new File(fileUri.getPath());

		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			fos.write(data);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Error accessing file: " + e.getMessage());
		}
		setResult(RESULT_OK);
		finish();
	}
}