package com.hapicc.common.mqmigration.rabbit;

import javax.validation.constraints.NotBlank;

public class RabbitExchange {

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    private boolean durable = true;

    private boolean autoDelete;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }
}
