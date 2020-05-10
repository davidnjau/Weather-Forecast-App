package com.centafrique.weather.pojo;

public class ForecastDataPojo {

    private String humidity;
    private String wind_speed;
    private String pressure;

    public ForecastDataPojo(String humidity, String wind_speed, String pressure) {
        this.humidity = humidity;
        this.wind_speed = wind_speed;
        this.pressure = pressure;
    }

    public ForecastDataPojo() {
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
}
