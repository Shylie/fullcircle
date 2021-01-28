package com.github.shylie.fullcircle.lang;

public class OpCode {
    /** Green, Green | ..., NumberValue -&gt; ..., NumberValue */
    public static final int NEGATE = 0;
    /** Green, Brown | ..., NumberValue (2) -&gt; ..., NumberValue */
    public static final int ADD = 1;
    /** Green, Blue | ..., NumberValue (2) -&gt; ..., NumberValue */
    public static final int SUBTRACT = 2;
    /** Green, Purple | ..., NumberValue (2) -&gt; ..., NumberValue */
    public static final int MULTIPLY = 3;
    /** Green, Cyan | ..., NumberValue (2) -&gt; ..., NumberValue */
    public static final int DIVIDE = 4;
    /** Green, Light Gray | ..., Value -&gt; ..., Value (2) */
    public static final int DUPLICATE = 5;

    /** Brown, Green | ..., NumberValue (3) -&gt; ..., NumberValue (3) */
    public static final int NEGATE_3 = 10;
    /** Brown, Brown | ..., NumberValue (6) -&gt; ..., NumberValue (3) */
    public static final int ADD_3 = 11;
    /** Brown, Blue | ..., NumberValue (6) -&gt; ..., NumberValue (3) */
    public static final int SUBTRACT_3 = 12;
    /** Brown, Purple | ..., NumberValue (6) -&gt; ..., NumberValue (3) */
    public static final int MULTIPLY_3 = 13;
    /** Brown, Cyan | ..., NumberValue (6) -&gt; ..., NumberValue (3) */
    public static final int DIVIDE_3 = 14;
    /** Brown, Light Gray | ..., Value (3) -&gt; ..., Value (6) */
    public static final int DUPLICATE_3 = 15;

    /** Blue, Green | ..., NumberValue (6) -&gt; ..., DoubleValue (3) */
    public static final int RAYCAST_BLOCKPOS = 20;
    /** Blue, Brown | ..., NumberValue (6) -&gt; ..., DoubleValue (3) */
    public static final int RAYCAST_BLOCKSIDE = 21;

    /** Purple, Green | ..., EntityValue -&gt; ..., DoubleValue (3) */
    public static final int ENTITY_POS = 30;
    /** Purple, Brown | ..., EntityValue -&gt; ..., DoubleValue (3) */
    public static final int ENTITY_EYE_POS = 31;
    /** Purple, Blue | ..., EntityValue -&gt; ..., DoubleValue (3) */
    public static final int ENTITY_LOOK = 32;
    /** Purple, Purple | ..., EntityValue -&gt; ..., EntityValue */
    public static final int ENTITY_LOOKED_AT = 33;

    /** Cyan, Green | ... -&gt; ..., EntityValue */
    public static final int CASTER = 40;

    /** Light Gray, Green | ... -&gt; ... */
    public static final int PAUSE = 50;
    /** Light Gray, Brown | ..., EntityValue, NumberValue (3) -&gt; ... */
    public static final int ADD_MOTION = 51;
    /** Light Gray, Blue | ..., StringValue (2), NumberValue (3) -&gt; ..., EntityValue */
    public static final int SPAWN_ENTITY = 52;
    /** Light Gray, Purple | ..., NumberValue (4) -&gt; ... */
    public static final int CREATE_EXPLOSION = 53;
    /** Light Gray, Cyan | ..., NumberValue (6) -&gt; ... */
    public static final int MOVE_BLOCK = 54;

    /** Orange, Green, Unknown, Unknown | ... -&gt; ..., NumberValue */
    public static final int CONSTANT = 90;
    /** Orange, Brown, Unknown, Unknown, Unknown, Unknown | ... -&gt; ..., NumberValue */
    public static final int CONSTANT_LONG = 91;
    /** Orange, Blue, Unknown, Unknown, Unknown, Unknown, ., Unknown, Unknown | ... -&gt; ..., NumberValue */
    public static final int CONSTANT_DOUBLE = 92;

    /** Lime */
    public static final int RETURN = 100;

    /** White, Unknown, Unknown | ... -&gt; ..., Value */
    public static final int LOAD = 101;
    /** White, White, Unknown, Unknown | ... -&gt; ..., Value (3) */
    public static final int LOAD_3 = 102;
    /** Black, Unknown, Unknown | ..., Value -&gt; ... */
    public static final int STORE = 103;
    /** Black, Black, Unknown, Unknown | ..., Value (3) -&gt; ... */
    public static final int STORE_3 = 104;

    public static final int JUMP_IF_NEGATIVE = 105;
    public static final int JUMP_IF_POSITIVE = 106;
    public static final int JUMP = 107;
    public static final int POP = 108;

    public static int parseColors(Object c1, Object c2) {
        return Integer.parseInt(c1.toString() + c2.toString());
    }

    public static int parseColors(Object c1, Object c2, Object c3, Object c4) {
        return Integer.parseInt(c1.toString() + c2.toString() + c3.toString() + c4.toString());
    }

    public static double parseColors(Object c1, Object c2, Object c3, Object c4, Object c5, Object c6) {
        return Double.parseDouble(c1.toString() + c2.toString() + c3.toString() + c4.toString() + "." + c5.toString() + c6.toString());
    }

    /** 0 */
    public static final char ID_GREEN = '0';
    /** 1 */
    public static final char ID_BROWN = '1';
    /** 2 */
    public static final char ID_BLUE = '2';
    /** 3 */
    public static final char ID_PURPLE = '3';
    /** 4 */
    public static final char ID_CYAN = '4';
    /** 5 */
    public static final char ID_LIGHT_GRAY = '5';
    /** 6 */
    public static final char ID_GRAY = '6';
    /** 7 */
    public static final char ID_YELLOW = '7';
    /** 8 */
    public static final char ID_LIGHT_BLUE = '8';
    /** 9 */
    public static final char ID_ORANGE = '9';
}