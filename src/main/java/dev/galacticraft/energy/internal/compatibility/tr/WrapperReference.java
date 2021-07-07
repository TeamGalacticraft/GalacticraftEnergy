/*
 * Copyright (c) 2021 Team Galacticraft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.galacticraft.energy.internal.compatibility.tr;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.misc.DestroyableRef;
import alexiil.mc.lib.attributes.misc.Reference;

public record WrapperReference<T>(Reference<T> wrapped) implements Reference<T>, CompatibilityLoopBreaker {

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
