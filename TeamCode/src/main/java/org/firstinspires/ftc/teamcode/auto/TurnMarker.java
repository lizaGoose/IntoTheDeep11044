package org.firstinspires.ftc.teamcode.auto;

public class TurnMarker {
    public int curve;
    private VariableProducer producer;

    TurnMarker(int curve, VariableProducer producer) {
        this.curve = curve;
        this.producer = producer;
    }

    public VariableProducer getProducer() {
        return producer;
    }

    public void setProducer(VariableProducer producer) {
        this.producer = producer;
    }
}
