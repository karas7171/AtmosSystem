package com.karas7171.atmossystem.atmos;

public class GasCell {
    private float pressure;
    private float temperature;

    private float oxygen;

    public void setTemperature(float temp) { this.temperature = temp; }
    public void setPressure(float press) { this.pressure = press; }
    public void setOxygen(float oxy) { this.oxygen = oxy; }

    public float getPressure() { return pressure; }
    public float getTemperature() { return temperature; }
    public float getOxygen() { return oxygen; }
}
