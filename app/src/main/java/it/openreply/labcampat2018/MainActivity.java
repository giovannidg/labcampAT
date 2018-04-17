package it.openreply.labcampat2018;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeripheralManager manager = PeripheralManager.getInstance();
        List<String> gpios = manager.getGpioList();
        Log.d("LAB", gpios.toString());

        try {
            ledGpio = manager.openGpio(LED_GPIO);
            ledGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            //Start blinking for test
            blinkLED();
        }catch (IOException e) {
            e.printStackTrace();
        }
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
        super.onDestroy();
    }

}
