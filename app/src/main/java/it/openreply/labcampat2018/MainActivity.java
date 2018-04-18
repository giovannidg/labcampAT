package it.openreply.labcampat2018;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.openalpr.OpenALPR;

import java.io.File;
import java.io.IOException;
import java.util.List;

import it.openreply.labcampat2018.camera.CameraHandler;
import it.openreply.labcampat2018.camera.ImageHelper;
import it.openreply.labcampat2018.camera.ImagePreprocessor;
import it.openreply.labcampat2018.model.Candidate;
import it.openreply.labcampat2018.model.DatabaseFields;
import it.openreply.labcampat2018.model.ParkingSpot;
import it.openreply.labcampat2018.model.Results;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    private Gpio ledGpio;
    private static String LED_GPIO = "BCM26";
    private Gpio avoidanceSensor;
    private static String AVOIDANCE_GPIO = "BCM27";
    private static final String LOG_TAG = "LABCAMP";

    //Camera
    private ImagePreprocessor mImagePreprocessor;
    private CameraHandler mCameraHandler;
    private boolean isProcessing;
    //Plate recognition
    static final String ANDROID_DATA_DIR = "/data/data/it.openreply.labcampat2018";
    final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";
    //Plate number  filter
    private String regexTarga = "[a-zA-Z]{2}[0-9]{3,4}[a-zA-Z]{2}";

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference mParkingSpotReference;
    private ValueEventListener mEventValueListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeripheralManager manager = PeripheralManager.getInstance();
        List<String> gpios = manager.getGpioList();
        Log.d(LOG_TAG, gpios.toString());

        try {
            ledGpio = manager.openGpio(LED_GPIO);
            ledGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            //Start blinking for test
//            blinkLED();

            avoidanceSensor = manager.openGpio(AVOIDANCE_GPIO);
            avoidanceSensor.setDirection(Gpio.DIRECTION_IN);
            avoidanceSensor.setActiveType(Gpio.ACTIVE_HIGH);
            avoidanceSensor.setEdgeTriggerType(Gpio.EDGE_BOTH);
            avoidanceSensor.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    try {
                        if (gpio.getValue()) {
                            Log.d(LOG_TAG, "far far away");
                            if (ledGpio != null)
                                ledGpio.setValue(false);
                        } else {
                            Log.d(LOG_TAG, "is near");
                            if (ledGpio != null)
                                ledGpio.setValue(true);
                            loadPhoto();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Continue listening for more interrupts
                    return true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        initCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        database = FirebaseDatabase.getInstance();
        mParkingSpotReference = database.getReference(getParkingSpotID());
        mEventValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ParkingSpot value = dataSnapshot.getValue(ParkingSpot.class);
                Log.d(LOG_TAG, value.isFree() + " - " + value.getPlateNumber() + " - " + value.getTimestamp());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG, "uh uh, DB error");
            }
        };
        mParkingSpotReference.addValueEventListener(mEventValueListener);
    }

    private @DatabaseFields.ParkingSpotID
    String getParkingSpotID() {
        return DatabaseFields.SPOT1;
    }

    /**
     * blink the led 5 times
     */
    private void blinkLED() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        if (ledGpio != null)
                            ledGpio.setValue(i % 2 == 0);
                        Thread.sleep(500);
                    } catch (Exception e) { /* uh! uh!*/ }
                }
            }
        }).start();
    }

    /**
     * Initialize the camera that will be used to capture images.
     */
    private void initCamera() {
        // ADD CAMERA SUPPORT
        mImagePreprocessor = new ImagePreprocessor();
        mCameraHandler = CameraHandler.getInstance();
        Handler threadLooper = new Handler(getMainLooper());

        mCameraHandler.initializeCamera(this, threadLooper,
                new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader imageReader) {
                        isProcessing = false;
                        Bitmap bitmap = mImagePreprocessor.preprocessImage(imageReader.acquireNextImage());
                        onPhotoReady(bitmap);
                    }
                });
//        CameraHandler.dumpFormatInfo(this);
    }

    private void loadPhoto() {
        // ADD CAMERA SUPPORT
        if (!isProcessing) {
            isProcessing = true;
            mCameraHandler.takePicture();
        }
    }

    private void onPhotoReady(Bitmap bitmap) {
        // SHOW BITMAP WITH A SCREEN
        //RECOGNIZE NUMBER PLATE
        File file = new File(ImageHelper.IMAGE_PATH, ImageHelper.IMAGE_NAME);
        if (file != null) {
            OpenALPR alpr = OpenALPR.Factory.create(MainActivity.this, ANDROID_DATA_DIR);
            String result = alpr.recognizeWithCountryRegionNConfig("eu", "it", file.getAbsolutePath(), openAlprConfFile, 10);
            Log.e("RESULT", "result: " + result);
            String plate = "";
            if (!TextUtils.isEmpty(result)) {
                plate = parseResultAndGetPlate(result);
            }

            if (!TextUtils.isEmpty(plate)) {//a number plate was recognized
                ParkingSpot spot = new ParkingSpot(false, plate, System.currentTimeMillis());
                mParkingSpotReference.setValue(spot);
            } else {
                ParkingSpot spot = new ParkingSpot(true, "NO PLATE", System.currentTimeMillis());
                mParkingSpotReference.setValue(spot);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mParkingSpotReference.removeEventListener(mEventValueListener);
    }

    @NonNull
    private String parseResultAndGetPlate(String result) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Results results = mapper.readValue(result, Results.class);
            for (Candidate candidate : results.getResults().get(0).getCandidates()) {
                if (candidate.getPlate().matches(regexTarga)) {
                    Log.d(LOG_TAG, "plate: " + candidate.getPlate());
                    return candidate.getPlate();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onDestroy() {
        //Close Led GPIO
        if (ledGpio != null) {
            try {
                ledGpio.close();
                ledGpio = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Close the obstacle avoidance GPIO
        if (avoidanceSensor != null) {
            try {
                avoidanceSensor.close();
                avoidanceSensor = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

}
