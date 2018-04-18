package it.openreply.labcampat2018.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by Open Reply on 09/04/18.
 */
public class ParkingSpot {

    private boolean isFree;

    private String plateNumber;

    private Long timestamp;

    public ParkingSpot(boolean isFree, String plateNumber, Long timestamp) {
        this.isFree = isFree;
        this.plateNumber = plateNumber;
        this.timestamp = timestamp;
    }

    public ParkingSpot() {
    }

    @PropertyName(DatabaseFields.FIELD_IS_FREE)
    public boolean isFree() {
        return isFree;
    }

    @PropertyName(DatabaseFields.FIELD_PLATE_NUMBER)
    public String getPlateNumber() {
        return plateNumber;
    }

    @PropertyName(DatabaseFields.FIELD_TIMESTAMP)
    public Long getTimestamp() {
        return timestamp;
    }

    @PropertyName(DatabaseFields.FIELD_IS_FREE)
    public void setFree(boolean free) {
        isFree = free;
    }

    @PropertyName(DatabaseFields.FIELD_PLATE_NUMBER)
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @PropertyName(DatabaseFields.FIELD_TIMESTAMP)
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
