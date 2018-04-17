package it.openreply.labcampat2018.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Open Reply on 08/04/18.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class Coordinate {

    @JsonProperty("x")
    private  Integer x;

    @JsonProperty("y")
    private  Integer y;

    public Coordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate() {
    }

    /**
     * Gets the X coordinate.
     *
     * @return The X coordinate.
     */
    public Integer getX() {
        return x;
    }

    /**
     * Gets the Y coordinate.
     *
     * @return the Y coordinate.
     */
    public Integer getY() {
        return y;
    }

}
