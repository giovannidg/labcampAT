package it.openreply.labcampat2018.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Open Reply on 08/04/18.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class Result {

    @JsonProperty("plate")
    private String plate;

    @JsonProperty("confidence")
    private Double confidence;

    @JsonProperty("matches_template")
    private Double matches_template;

    @JsonProperty("region")
    private String region;

    @JsonProperty("region_confidence")
    private Double region_confidence;

    @JsonProperty("processing_time_ms")
    private Double processing_time_ms;

    @JsonProperty("coordinates")
    private List<Coordinate> coordinates;

    @JsonProperty("candidates")
    private List<Candidate> candidates;

    public Result(String plate, Double confidence, Double matches_template, String region, Double region_confidence, Double processing_time_ms, List<Coordinate> coordinates, List<Candidate> candidates) {
        this.plate = plate;
        this.confidence = confidence;
        this.matches_template = matches_template;
        this.region = region;
        this.region_confidence = region_confidence;
        this.processing_time_ms = processing_time_ms;
        this.coordinates = coordinates;
        this.candidates = candidates;
    }

    public Result() {
    }

    /**
     * Gets the plate.
     *
     * @return the plate.
     */
    public String getPlate() {
        return plate;
    }

    /**
     * Gets the confidence.
     *
     * @return The confidence.
     */
    public Double getConfidence() {
        return confidence;
    }

    /**
     * Gets the matches template.
     *
     * @return The matches template.
     */
    public Double getMatchesTemplate() {
        return matches_template;
    }

    /**
     * Gets the region.
     *
     * @return The region.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Gets the region confidence.
     *
     * @return the region confidence.
     */
    public Double getRegionConfidence() {
        return region_confidence;
    }

    /**
     * Gets the processing time.
     *
     * @return The processing time.
     */
    public Double getProcessingTimeMs() {
        return processing_time_ms;
    }

    /**
     * Gets the coordinates.
     *
     * @return The coordinates.
     */
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    /**
     * Gets the candidates.
     *
     * @return The candidates.
     */
    public List<Candidate> getCandidates() {
        return candidates;
    }

}
