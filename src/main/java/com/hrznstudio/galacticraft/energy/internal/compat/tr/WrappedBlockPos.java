package com.hrznstudio.galacticraft.energy.internal.compat.tr;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class WrappedBlockPos extends BlockPos implements CompatBreaker {
    public WrappedBlockPos(int i, int j, int k) {
        super(i, j, k);
    }

    public WrappedBlockPos(double d, double e, double f) {
        super(d, e, f);
    }

    public WrappedBlockPos(Vec3d pos) {
        super(pos);
    }

    public WrappedBlockPos(Position pos) {
        super(pos);
    }

    public WrappedBlockPos(Vec3i pos) {
        super(pos);
    }

    public WrappedBlockPos(BlockPos pos) {
        super(pos);
    }
}
