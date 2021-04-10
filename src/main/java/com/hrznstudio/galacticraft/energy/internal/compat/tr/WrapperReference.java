package com.hrznstudio.galacticraft.energy.internal.compat.tr;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.DestroyableRef;
import alexiil.mc.lib.attributes.misc.Reference;

public class WrapperReference<T> implements Reference<T>, CompatBreaker {
    private final Reference<T> wrapped;

    public WrapperReference(Reference<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public T get() {
        return this.wrapped.get();
    }

    @Override
    public boolean set(T value) {
        return this.wrapped.set(value);
    }

    @Override
    public boolean isValid(T value) {
        return this.wrapped.isValid(value);
    }

    @Override
    public boolean set(T value, Simulation simulation) {
        return this.wrapped.set(value, simulation);
    }

    @Override
    public DestroyableRef<T> asDestroyable() {
        return this.wrapped.asDestroyable();
    }
}
