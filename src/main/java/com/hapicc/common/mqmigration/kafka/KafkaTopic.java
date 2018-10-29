package com.hapicc.common.mqmigration.kafka;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class KafkaTopic {

    @NotBlank
    private String name;

    @Min(1)
    private int numPartitions;

    private Short replicationFactor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumPartitions() {
        return numPartitions;
    }

    public void setNumPartitions(int numPartitions) {
        this.numPartitions = numPartitions;
    }

    public Short getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(Short replicationFactor) {
        this.replicationFactor = replicationFactor;
    }
}
