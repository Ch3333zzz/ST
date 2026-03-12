package org.example.task3;

import org.example.task3.exceptions.InteractionWithDamagedDoorException;

public abstract class Door {
    private boolean isClosed;
    private float HP = 100;

    public void open() throws InteractionWithDamagedDoorException {
        if (HP <= 25)
            throw new InteractionWithDamagedDoorException();
        isClosed = false;
    }

    public void close() throws InteractionWithDamagedDoorException {
        if (HP <= 25)
            throw new InteractionWithDamagedDoorException();
        isClosed = true;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void damage() {
        if (this.HP <= 0)
            return;

        this.HP -= 25;
    }

    public void repare() {
        if (this.HP >= 100)
            return;
        this.HP += 25;
    }

    public float getHP() {
        return HP;
    }
}
