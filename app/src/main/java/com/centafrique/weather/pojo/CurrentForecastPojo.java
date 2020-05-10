package com.centafrique.weather.pojo;

public class CurrentForecastPojo {

    private String timezone;
    private String dt;
    private String temp;
    private String id;
    private String description;

    public CurrentForecastPojo(String timezone, String dt, String temp, String id, String description) {
        this.timezone = timezone;
        this.dt = dt;
        this.temp = temp;
        this.id = id;
        this.description = description;
    }

    public CurrentForecastPojo() {
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
