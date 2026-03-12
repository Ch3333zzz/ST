package org.example.task3;

public class ParsecOfSpace {
    private short hydrogenLvl;

    public ParsecOfSpace(short hydrogenLvl) {
        if (hydrogenLvl > 100)
            this.hydrogenLvl = 100;
        else if (hydrogenLvl < 0)
            this.hydrogenLvl = 0;
        else
            this.hydrogenLvl = hydrogenLvl;
    }

    public short getHydrogenLvl() {
        return hydrogenLvl;
    }
}
