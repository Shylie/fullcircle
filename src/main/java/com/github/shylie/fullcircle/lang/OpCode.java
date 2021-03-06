package com.github.shylie.fullcircle.lang;

public class OpCode {
	/** Green, Green | ..., NumberValue -&gt; ..., NumberValue */
	public static final int NEGATE = 0;
	/** Green, Brown | ..., NumberValue (3) -&gt; ..., NumberValue (3) */
	public static final int NEGATE_3 = 1;
	/** Green, Blue | ..., NumberValue (2) -&gt; ..., NumberValue */
	public static final int ADD = 2;
	/** Green, Purple | ..., NumberValue (6) -&gt; ..., NumberValue (3) */
	public static final int ADD_3 = 3;
	/** Green, Cyan | ..., NumberValue (2) -&gt; ..., NumberValue */
	public static final int SUBTRACT = 4;
	/** Green, Light Gray | ..., NumberValue (6) -&gt; ..., NumberValue (3) */
	public static final int SUBTRACT_3 = 5;
	/** Green, Gray | ..., NumberValue (2) -&gt; ..., NumberValue */
	public static final int MULTIPLY = 6;
	/** Green, Yellow | ..., NumberValue (6) -&gt; ..., NumberValue (3) */
	public static final int MULTIPLY_3 = 7;
	/** Green, Light Blue | ..., NumberValue (2) -&gt; ..., NumberValue */
	public static final int DIVIDE = 8;
	/** Green, Orange | ..., NumberValue (6) -&gt; ..., NumberValue (3) */
	public static final int DIVIDE_3 = 9;
	/** Brown, Green | ..., Value [Any] -&gt; ..., Value [Any] (2) */
	public static final int DUPLICATE = 10;
	/** Brown, Brown | ..., Value [Any] (3) -&gt; ..., Value [Any] (6) */
	public static final int DUPLICATE_3 = 11;
	/** Brown, Blue | ..., Value [Any] (2) -&gt; ..., NumberValue */
	public static final int COMPARE = 12;
	/** Brown, Purple | ..., NumberValue (2) -&gt; ..., NumberValue */
	public static final int POW = 13;
	/** Brown, Cyan | ..., NumberValue -&gt; ..., NumberValue */
	public static final int SIN = 14;
	/** Brown, Light Gray | ..., NumberValue -&gt; ..., NumberValue */
	public static final int COS = 15;
	/** Brown, Gray | ..., NumberValue -&gt; ..., NumberValue */
	public static final int TAN = 16;
	/** Brown, Yellow | ..., NumberValue (2) -&gt; ..., NumberValue */
	public static final int MOD = 17;
	/** Brown, Light Blue | ..., NumberValue -&gt; ..., NumberValue */
	public static final int TRUNCATE = 18;
	/** Brown, Orange | ..., Value [Any] -&gt; ... */
	public static final int POP = 19;

	/** Blue, Green | ..., NumberValue (6) -&gt; ..., NumberValue (4) */
	public static final int RAYCAST_BLOCKPOS = 20;
	/** Blue, Brown | ..., NumberValue (6) -&gt; ..., NumberValue (4) */
	public static final int RAYCAST_BLOCKSIDE = 21;

	/** Purple, Green | ..., EntityValue -&gt; ..., NumberValue (3) */
	public static final int ENTITY_POS = 30;
	/** Purple, Brown | ..., EntityValue -&gt; ..., NumberValue (3) */
	public static final int ENTITY_EYE_POS = 31;
	/** Purple, Blue | ..., EntityValue -&gt; ..., NumberValue (3) */
	public static final int ENTITY_LOOK = 32;
	/** Purple, Purple | ..., EntityValue -&gt; ..., EntityValue, NumberValue */
	public static final int ENTITY_LOOKED_AT = 33;
	/** Purple, Cyan | ..., EntityValue -&gt; ..., NBTValue */
	public static final int ENTITY_NBT = 34;

	/** Cyan, Green | ... -&gt; ..., EntityValue */
	public static final int CASTER = 40;

	/** Light Gray, Green | ..., NumberValue -&gt; ... */
	public static final int PAUSE = 50;
	/** Light Gray, Brown | ..., EntityValue, NumberValue (3) -&gt; ... */
	public static final int ADD_MOTION = 51;
	/** Light Gray, Blue | ..., NumberValue (3), StringValue (2) -&gt; ..., EntityValue */
	public static final int SPAWN_ENTITY = 52;
	/** Light Gray, Purple | ..., NumberValue (4) -&gt; ... */
	public static final int CREATE_EXPLOSION = 53;
	/** Light Gray, Cyan | ..., EntityValue, NBTValue -&gt; ... */
	public static final int MODIFY_ENTITY_NBT = 54;
	/** Light Gray, Light Gray | ..., NumberValue (3) -&gt; ..., BlockStateValue */
	public static final int GET_BLOCK = 55;
	/** Light Gray, Gray | ..., NumberValue (3), BlockStateValue -&gt; ..., NumberValue */
	public static final int SET_BLOCK = 56;

	/** Gray, Green | ... -&gt; ..., NBTValue */
	public static final int NEW_NBT = 60;
	/** Gray, Brown | ..., NBTValue, StringValue, StringValue -&gt; ..., Value [String|Long|Double|NBT] */
	public static final int NBT_GET = 61;
	/** Gray, Blue | ..., NBTValue, StringValue, StringValue, Value [String|Long|Double|NBT|Entity] -&gt; ..., NBTValue */
	public static final int NBT_SET = 62;

	/** Yellow, Green | ..., StringValue, StringValue -&gt; ..., BlockStateValue */
	public static final int DEFAULT_BLOCK_STATE = 70;
	/** Yellow, Brown | ..., BlockStateValue, StringValue -&gt; ..., Value [String|Long], NumberValue */
	public static final int BLOCK_STATE_GET = 71;
	/** Yellow, Blue | ..., BlockStateValue, StringValue, Value [String|Long] -&gt; ..., BlockStateValue, NumberValue */
	public static final int BLOCK_STATE_SET = 72;

	/** Orange, Green, Unknown, Unknown | ... -&gt; ..., NumberValue */
	public static final int CONSTANT = 90;
	/** Orange, Brown, Unknown, Unknown, Unknown, Unknown | ... -&gt; ..., NumberValue */
	public static final int CONSTANT_LONG = 91;
	/** Orange, Blue, Unknown, Unknown, Unknown, Unknown, ., Unknown, Unknown | ... -&gt; ..., NumberValue */
	public static final int CONSTANT_DOUBLE = 92;

	/** Lime */
	public static final int RETURN = 100;

	/** White | ..., StringValue -&gt; ..., Value [Any] */
	public static final int LOAD = 101;
	/** White, White | ..., StringValue -&gt; ..., Value [Any] (3) */
	public static final int LOAD_3 = 102;
	/** White, White, White | ..., StringValue -&gt; ... */
	public static final int CALL = 103;
	/** Black | ..., StringValue, Value [Any] -&gt; ... */
	public static final int STORE = 104;
	/** Black, Black | ..., StringValue, Value [Any] (3) -&gt; ... */
	public static final int STORE_3 = 105;
	/** Black, Black, Black, Red | ... -&gt; ... */
	public static final int DEFINE = 106;

	public static final int JUMP_IF_NEGATIVE = 107;
	public static final int JUMP_IF_POSITIVE = 108;
	public static final int JUMP = 109;
	public static final int JUMP_TO_CALLEE = 110;

	public static int parseColors(Object c1, Object c2) {
		return Integer.parseInt(c1.toString() + c2.toString());
	}

	public static int parseColors(Object c1, Object c2, Object c3, Object c4) {
		return Integer.parseInt(c1.toString() + c2.toString() + c3.toString() + c4.toString());
	}

	public static double parseColors(Object c1, Object c2, Object c3, Object c4, Object c5, Object c6) {
		return Double.parseDouble(c1.toString() + c2.toString() + c3.toString() + c4.toString() + "." + c5.toString() + c6.toString());
	}

	/** {@value} */
	public static final char ID_GREEN = '0';
	/** {@value} */
	public static final char ID_BROWN = '1';
	/** {@value} */
	public static final char ID_BLUE = '2';
	/** {@value} */
	public static final char ID_PURPLE = '3';
	/** {@value} */
	public static final char ID_CYAN = '4';
	/** {@value} */
	public static final char ID_LIGHT_GRAY = '5';
	/** {@value} */
	public static final char ID_GRAY = '6';
	/** {@value} */
	public static final char ID_YELLOW = '7';
	/** {@value} */
	public static final char ID_LIGHT_BLUE = '8';
	/** {@value} */
	public static final char ID_ORANGE = '9';
}