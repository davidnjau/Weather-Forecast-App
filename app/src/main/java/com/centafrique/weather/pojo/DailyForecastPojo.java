package com.centafrique.weather.pojo;

public class DailyForecastPojo {

    private Double minTemp;
    private String dt;
    private Double maxTemp;
    private int id;
    private String description;

    public DailyForecastPojo(Double minTemp, String dt, Double maxTemp, int id, String description) {
        this.minTemp = minTemp;
        this.dt = dt;
        this.maxTemp = maxTemp;
        this.id = id;
        this.description = description;
    }

    public DailyForecastPojo() {
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
