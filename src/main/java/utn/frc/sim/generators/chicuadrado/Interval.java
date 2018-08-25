package utn.frc.sim.generators.chicuadrado;

import org.apache.commons.lang3.StringUtils;
import utn.frc.sim.util.MathUtils;

public class Interval {
    private double from;
    private double to;
    private int observedFrequency;
    private int expectedFrequency;


    public Interval(double from, double to, int expectedFrequency) {
        this.from = from;
        this.to = to;
        this.expectedFrequency = expectedFrequency;
        this.observedFrequency = 0;
    }

    public double getFrom() {
        return from;
    }

    public void setFrom(double from) {
        this.from = from;
    }

    public double getTo() {
        return to;
    }

    public void setTo(double to) {
        this.to = to;
    }

    public int getObservedFrequency() {
        return observedFrequency;
    }

    public void setObservedFrequency(int observedFrequency) {
        this.observedFrequency = observedFrequency;
    }

    public int getExpectedFrequency() {
        return expectedFrequency;
    }

    public void setExpectedFrequency(int expectedFrequency) {
        this.expectedFrequency = expectedFrequency;
    }

    public boolean includes(double number) {
        return from <= number && number < to;
    }

    public void addOccurrence() {
        observedFrequency++;
    }

    public String displayName() {
        return getDisplay(from) + "-" + getDisplay(to);
    }

    @Override
    public String toString() {
        return "Interval{" +
                "from=" + getDisplay(from) +
                ", to=" + getDisplay(to) +
                ", observedFrequency=" + observedFrequency +
                ", expectedFrequency=" + expectedFrequency +
                '}';
    }

    public String getDisplay(double number) {
        return StringUtils
                .rightPad(getDoubleString(number), 6, '0');

    }

    private String getDoubleString(double number) {
        return Double.toString((MathUtils.round(number, 4)));
    }

    public double getResult() {
        return Math.pow(observedFrequency - expectedFrequency, 2) / expectedFrequency;
    }

    public String getDisplayableResult() {
        return getDisplay(getResult());
    }

    public String getPlottableInterval() {
        return getDisplay((from + to) / 2);
    }
}
