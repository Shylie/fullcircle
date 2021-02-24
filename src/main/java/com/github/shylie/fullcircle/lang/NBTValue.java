package com.github.shylie.fullcircle.lang;

import net.minecraft.nbt.CompoundNBT;

public class NBTValue implements Value {
	public final CompoundNBT value;

	public NBTValue(CompoundNBT value) {
		// init with copy to prevent duplicate references.
		// unsure if this should be in dup() instead.
		this.value = value.copy();
	}

	@Override
	public Value add(Value other) {
		return null;
	}

	@Override
	public Value sub(Value other) {
		return null;
	}

	@Override
	public Value mul(Value other) {
		return null;
	}

	@Override
	public Value div(Value other) {
		return null;
	}

	@Override
	public Value neg() {
		return null;
	}

	@Override
	public Value dup() {
		return new NBTValue(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) { return true; }
		if (obj instanceof NBTValue) {
			return value.equals(((NBTValue)obj).value);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
