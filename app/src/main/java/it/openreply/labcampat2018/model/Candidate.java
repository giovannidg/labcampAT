package it.openreply.labcampat2018.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Open Reply on 08/04/18.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class Candidate {

    @JsonProperty("plate")
    private String plate;

    @JsonProperty("confidence")
    private Double confidence;

    @JsonProperty("matches_template")
    private Integer matches_template;

    public Candidate(String plate, Double confidence, Integer matches_template) {
        this.plate = plate;
        this.confidence = confidence;
        this.matches_template = matches_template;
    }

    public Candidate() {
    }

    /**
     * Gets the plate.
     *
     * @return The plate.
     */
    public String getPlate() {
        return plate;
    }

    /**
     * Gets the confidence level.
     *
     * @return the confidence level.
     */
    public Double getConfidence() {
        return confidence;
    }

    /**
     * Gets the matches template.
     *
     * @return the matches template.
     */
    public Integer getMatchesTemplate() {
        return matches_template;
    }

}
