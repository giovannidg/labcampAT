package it.openreply.labcampat2018.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Open Reply on 08/04/18.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class Results {

    @JsonProperty("epoch_time")
    private  Double epoch_time;

    @JsonProperty("processing_time_ms")
    private  Double processing_time_ms;

    @JsonProperty("results")
    private  List<Result> results;

    public Results(Double epoch_time, Double processing_time_ms, List<Result> results) {
        this.epoch_time = epoch_time;
        this.processing_time_ms = processing_time_ms;
        this.results = results;
    }

    public Results() {
    }

    public Double getEpochTime() {
        return epoch_time;
    }

    public Double getProcessingTimeMs() {
        return processing_time_ms;
    }

    public List<Result> getResults() {
        return results;
    }

}
