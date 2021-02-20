package com.hrznstudio.galacticraft.energy.internal.compat.tr;

import alexiil.mc.lib.attributes.Simulation;
import com.hrznstudio.galacticraft.energy.api.Capacitor;
import com.hrznstudio.galacticraft.energy.api.EnergyTransferable;
import com.hrznstudio.galacticraft.energy.api.EnergyType;
import com.hrznstudio.galacticraft.energy.compat.tr.TREnergyType;
import team.reborn.energy.EnergyHandler;

/**
 * TechReborn Energy
 * Reference Values:
 * 1 Coal -> 4000.0
 * 1 Plank -> 750.0
 */
public class TREnergyWrapper implements Capacitor, EnergyTransferable {
    private final EnergyHandler handler;

    public TREnergyWrapper(EnergyHandler handler) {
        this.handler = handler;
    }

    @Override
    public void setEnergy(int amount) {
        this.handler.set(amount);
    }

    @Override
    public EnergyType getEnergyType() {
        return TREnergyType.INSTANCE;
    }

    @Override
    public int getEnergy() {
        return ((int) this.handler.getEnergy());
    }

    @Override
    public int getMaxCapacity() {
        return ((int) this.handler.getMaxStored());
    }

    @Override
    public int tryExtract(EnergyType type, int amount, Simulation simulation) {
        return ((int) this.handler.extract(amount));
    }

    @Override
    public int tryInsert(EnergyType type, int amount, Simulation simulation) {
        return amount - ((int) this.handler.insert(amount));
    }
}
