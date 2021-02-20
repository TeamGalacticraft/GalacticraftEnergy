package com.hrznstudio.galacticraft.energy.compat.tr;

import com.hrznstudio.galacticraft.energy.api.EnergyType;

public enum TREnergyType implements EnergyType { //todo: conversion
    INSTANCE;

    @Override
    public int convertToDefault(int amount) {
        return 0;
    }

    @Override
    public int convertFromDefault(int amount) {
        return 0;
    }
}
