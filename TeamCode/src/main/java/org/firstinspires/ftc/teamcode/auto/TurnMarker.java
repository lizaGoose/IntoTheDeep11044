package org.firstinspires.ftc.teamcode.auto;

import androidx.annotation.NonNull;

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

    @NonNull
    @Override
    public TurnMarker clone() {
        return new TurnMarker(this.curve, this.producer);
    }
}
