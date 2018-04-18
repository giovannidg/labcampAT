package it.openreply.labcampat2018.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by Open Reply on 09/04/18.
 */
public interface DatabaseFields {

    public String FIELD_IS_FREE = "is_free";
    public String FIELD_PLATE_NUMBER = "plate_number";
    public String FIELD_TIMESTAMP = "timestamp";

    @Retention(SOURCE)
    @StringDef({
            SPOT1,
            SPOT2,
            SPOT3,
            SPOT4,
            SPOT5,
            SPOT6
    })
    public @interface ParkingSpotID { }
    public static final String SPOT1 = "parking_spot_1";
    public static final String SPOT2 = "parking_spot_2";
    public static final String SPOT3 = "parking_spot_3";
    public static final String SPOT4 = "parking_spot_4";
    public static final String SPOT5 = "parking_spot_5";
    public static final String SPOT6 = "parking_spot_6";

}
