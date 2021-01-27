package com.github.shylie.fullcircle.lang;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.shylie.fullcircle.FCPacketHandler;
import com.github.shylie.fullcircle.FullCircle;
import com.github.shylie.fullcircle.net.MessageAdditiveMotion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Explosion.Mode;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class VM {
    private static final Logger LOGGER = LogManager.getLogger(FullCircle.MOD_ID + ".lang.VM");
    private static final int STACK_MAX = 512;
    /** 2 blocks of info */
    private static final int REGISTER_MAX = 10 * 10;
    
    private final boolean compiled;

    private int ip;
    private int stackTop;
    private Value[] stack;
    private Value[] registers;
    private Value[] registers_3;
    private Chunk chunk;
    private PlayerEntity caster;

    public final DimensionType dimension;

    public VM(String[] source, Map<String, String> strings, int cx, int cy, Direction startDirection, PlayerInteractEvent event, DimensionType dimension) {
        chunk = new Chunk();
        ip = 0;
        stack = new Value[STACK_MAX];
        registers = new Value[REGISTER_MAX];
        registers_3 = new Value[REGISTER_MAX * 3];
        this.dimension = dimension;
        caster = event.getPlayer();

        compiled = Compiler.COMPILER.Compile(source, strings, cx, cy, startDirection, event, chunk);
    }

    public InterpretResult run(WorldTickEvent event) {
        if (!compiled) {
            LOGGER.debug("Compilation error");
            return InterpretResult.COMPILE_ERROR;
        }

        // maybe limit amount of instructions run to prevent infinite loops?
        for (int i = 0; i < stackTop; i++) {
            LOGGER.debug(String.format("stack[%d] = %s", i, stack[i]));
        }
        chunk.dissasembleInstruction(ip);

        int instruction = readInt();
        switch (instruction) {
            case OpCode.RETURN:
                return InterpretResult.OK;

            case OpCode.CONSTANT:
                push(chunk.readConstant(readInt()));
                return InterpretResult.CONTINUE;

            case OpCode.NEGATE:
            {
                Value a = pop();
                if (a == null) {
                    LOGGER.debug(String.format("Null on stack at OP_NEGATE at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(a.neg());
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.NEGATE_3:
            {
                Value z = pop();
                Value y = pop();
                Value x = pop();
                if (x == null || y == null || z == null) {
                    LOGGER.debug(String.format("Null on stack at OP_NEGATE_3 at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(x.neg());
                    push(y.neg());
                    push(z.neg());
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.ADD:
            {
                Value b = pop();
                Value a = pop();
                if (a == null) {
                    LOGGER.debug(String.format("Null on stack at OP_ADD at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(a.add(b));
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.ADD_3:
            {
                Value z2 = pop();
                Value y2 = pop();
                Value x2 = pop();
                Value z1 = pop();
                Value y1 = pop();
                Value x1 = pop();
                if (x1 == null || y1 == null || z1 == null) {
                    LOGGER.debug(String.format("Null on stack at OP_ADD_3 at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(x1.add(x2));
                    push(y1.add(y2));
                    push(z1.add(z2));
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.SUBTRACT:
            {
                Value b = pop();
                Value a = pop();
                if (a == null) {
                    LOGGER.debug(String.format("Null on stack at OP_SUBTRACT at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(a.sub(b));
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.SUBTRACT_3:
            {
                Value z2 = pop();
                Value y2 = pop();
                Value x2 = pop();
                Value z1 = pop();
                Value y1 = pop();
                Value x1 = pop();
                if (x1 == null || y1 == null || z1 == null) {
                    LOGGER.debug(String.format("Null on stack at OP_SUBTRACT_3 at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(x1.sub(x2));
                    push(y1.sub(y2));
                    push(z1.sub(z2));
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.MULTIPLY:
            {
                Value b = pop();
                Value a = pop();
                if (a == null) {
                    LOGGER.debug(String.format("Null on stack at OP_MULTIPLY at instruction address %04d", ip));
                }
                else {
                    push(a.mul(b));
                }
                return InterpretResult.CONTINUE;
            }

            case OpCode.MULTIPLY_3:
            {
                Value z2 = pop();
                Value y2 = pop();
                Value x2 = pop();
                Value z1 = pop();
                Value y1 = pop();
                Value x1 = pop();
                if (x1 == null || y1 == null || z1 == null) {
                    LOGGER.debug(String.format("Null on stack at OP_MULTIPLY_3 at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(x1.mul(x2));
                    push(y1.mul(y2));
                    push(z1.mul(z2));
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.DIVIDE:
            {
                Value b = pop();
                Value a = pop();
                if (a == null) {
                    LOGGER.debug(String.format("Null on stack at OP_DIVIDE at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(a.div(b));
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.DIVIDE_3:
            {
                Value z2 = pop();
                Value y2 = pop();
                Value x2 = pop();
                Value z1 = pop();
                Value y1 = pop();
                Value x1 = pop();
                if (x1 == null || y1 == null || z1 == null) {
                    LOGGER.debug(String.format("Null on stack at OP_DIVIDE_3 at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(x1.div(x2));
                    push(y1.div(y2));
                    push(z1.div(z2));
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.DUPLICATE:
            {
                Value a = pop();
                if (a == null) {
                    LOGGER.debug(String.format("Null on stack at OP_DUPLICATE at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(a);
                    push(a.dup());
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.DUPLICATE_3:
            {
                Value z = pop();
                Value y = pop();
                Value x = pop();
                if (x == null || y == null || z == null) {
                    LOGGER.debug(String.format("Null on stack at OP_DUPLICATE_3 at instruction address %04d", ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    push(x);
                    push(y);
                    push(z);
                    push(x.dup());
                    push(y.dup());
                    push(z.dup());
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.LOAD:
            {
                int loc = readInt();
                if (loc >= 0 && loc < REGISTER_MAX) {
                    push(registers[loc]);
                    return InterpretResult.CONTINUE;
                }
                else {
                    LOGGER.debug(String.format("Invalid register address %03d at instruction address %04d", loc, ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.LOAD_3:
            {
                int loc = readInt() * 3;
                if (loc >= 0 && loc + 2 < REGISTER_MAX * 3) {
                    push(registers_3[loc]);
                    push(registers_3[loc + 1]);
                    push(registers_3[loc + 2]);
                    return InterpretResult.CONTINUE;
                }
                else {
                    LOGGER.debug(String.format("Invalid register address %03d at instruction address %04d", loc, ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.STORE:
            {
                int loc = readInt();
                if (loc >= 0 && loc < REGISTER_MAX) {
                    registers[loc] = pop();
                    return InterpretResult.CONTINUE;
                }
                else {
                    LOGGER.debug(String.format("Invalid register address %03d at instruction address %04d", loc, ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.STORE_3:
            {
                int loc = readInt() * 3;
                if (loc >= 0 && loc + 2 < REGISTER_MAX * 3) {
                    registers_3[loc + 2] = pop();
                    registers_3[loc + 1] = pop();
                    registers_3[loc] = pop();
                    return InterpretResult.CONTINUE;
                }
                else {
                    LOGGER.debug(String.format("Invalid register address %03d at instruction address %04d", loc, ip));
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.JUMP_IF_NEGATIVE:
            {
                int jmpto = readInt();
                if (asDouble(peek(0)).isPresent() && asDouble(peek(0)).get() < 0) {
                    ip = jmpto;
                }
                return InterpretResult.CONTINUE;
            }

            case OpCode.JUMP_IF_POSITIVE:
            {
                int jmpto = readInt();
                if (asDouble(peek(0)).isPresent() && asDouble(peek(0)).get() > 0) {
                    ip = jmpto;
                }
                return InterpretResult.CONTINUE;
            }

            case OpCode.JUMP:
            {
                ip = readInt();
                return InterpretResult.CONTINUE;
            }

            case OpCode.POP:
            {
                pop();
                return InterpretResult.CONTINUE;
            }

            case OpCode.RAYCAST_BLOCKPOS:
            {
                Optional<Double> lz = asDouble(pop());
                Optional<Double> ly = asDouble(pop());
                Optional<Double> lx = asDouble(pop());
                Optional<Double> sz = asDouble(pop());
                Optional<Double> sy = asDouble(pop());
                Optional<Double> sx = asDouble(pop());

                if (lz.isPresent() && ly.isPresent() && lx.isPresent() && lx.isPresent() && sz.isPresent() && sy.isPresent() && sx.isPresent()) {
                    BlockRayTraceResult result = event.world.rayTraceBlocks(
                        new RayTraceContext(
                            new Vector3d(sx.get(), sy.get(), sz.get()),
                            new Vector3d(sx.get() + lx.get() * 512.0, sy.get() + ly.get() * 512.0, sz.get() + lz.get() * 512.0),
                            BlockMode.OUTLINE,
                            FluidMode.NONE,
                            null
                        )
                    );
                    push(new DoubleValue(result.getPos().getX()));
                    push(new DoubleValue(result.getPos().getY()));
                    push(new DoubleValue(result.getPos().getZ()));
                    return InterpretResult.CONTINUE;
                }
                else {
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.RAYCAST_BLOCKSIDE:
            {
                Optional<Double> lz = asDouble(pop());
                Optional<Double> ly = asDouble(pop());
                Optional<Double> lx = asDouble(pop());
                Optional<Double> sz = asDouble(pop());
                Optional<Double> sy = asDouble(pop());
                Optional<Double> sx = asDouble(pop());

                if (lz.isPresent() && ly.isPresent() && lx.isPresent() && lx.isPresent() && sz.isPresent() && sy.isPresent() && sx.isPresent()) {
                    BlockRayTraceResult result = event.world.rayTraceBlocks(
                        new RayTraceContext(
                            new Vector3d(sx.get(), sy.get(), sz.get()),
                            new Vector3d(sx.get() + lx.get() * 512.0, sy.get() + ly.get() * 512.0, sz.get() + lz.get() * 512.0),
                            BlockMode.OUTLINE,
                            FluidMode.NONE,
                            null
                        )
                    );
                    push(new DoubleValue(result.getFace().getXOffset()));
                    push(new DoubleValue(result.getFace().getYOffset()));
                    push(new DoubleValue(result.getFace().getZOffset()));
                    return InterpretResult.CONTINUE;
                }
                else {
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.ENTITY_POS:
            {
                Value ev = pop();
                if (ev instanceof EntityValue) {
                    Entity entity = event.world.getEntityByID(((EntityValue)ev).entityID);
                    push(new DoubleValue(entity.getPosX()));
                    push(new DoubleValue(entity.getPosY()));
                    push(new DoubleValue(entity.getPosZ()));
                    return InterpretResult.CONTINUE;
                }
                else {
                    LOGGER.debug(String.format("Invalid entity type '%s'", ev != null ? ev.getClass().getSimpleName() : null));
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.ENTITY_EYE_POS:
            {
                Value ev = pop();
                if (ev instanceof EntityValue) {
                    Entity entity = event.world.getEntityByID(((EntityValue)ev).entityID);
                    push(new DoubleValue(entity.getPosX()));
                    push(new DoubleValue(entity.getPosYEye()));
                    push(new DoubleValue(entity.getPosZ()));
                    return InterpretResult.CONTINUE;
                }
                else {
                    LOGGER.debug(String.format("Invalid entity type '%s'", ev != null ? ev.getClass().getSimpleName() : null));
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.ENTITY_LOOK:
            {
                Value ev = pop();
                if (ev instanceof EntityValue) {
                    Entity entity = event.world.getEntityByID(((EntityValue)ev).entityID);
                    push(new DoubleValue(entity.getLookVec().x));
                    push(new DoubleValue(entity.getLookVec().y));
                    push(new DoubleValue(entity.getLookVec().z));
                    return InterpretResult.CONTINUE;
                }
                else {
                    LOGGER.debug(String.format("Invalid entity type '%s'", ev != null ? ev.getClass().getSimpleName() : null));
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.ENTITY_LOOKED_AT:
            {
                Value ev = pop();

                if (ev instanceof EntityValue) {
                    Entity entity = event.world.getEntityByID(((EntityValue)ev).entityID);

                    Vector3d start = new Vector3d(entity.getPosX(), entity.getPosYEye(), entity.getPosZ());
                    Vector3d end = start.add(entity.getLookVec().normalize().scale(512.0));

                    BlockRayTraceResult result = event.world.rayTraceBlocks(
                        new RayTraceContext(
                            start,
                            end,
                            BlockMode.VISUAL,
                            FluidMode.NONE,
                            null
                        )
                    );

                    Entity entityLooked = null;
                    Entity entityFound = null;

                    List<Entity> entitiesInBoundingBox = event.world.getEntitiesWithinAABBExcludingEntity(entity, entity.getBoundingBox().grow(
                            entity.getLookVec().x * 512.0,
                            entity.getLookVec().y * 512.0,
                            entity.getLookVec().z * 512.0).
                        grow(1.0)
                    );

                    double minDistance = 512.0;

                    for (Entity e : entitiesInBoundingBox) {
                        if (e.canBeCollidedWith()) {
                            float collisionBorderSize = entity.getCollisionBorderSize();
                            AxisAlignedBB hitbox = e.getBoundingBox().grow(collisionBorderSize);
                            Optional<Vector3d> interceptPosition = hitbox.rayTrace(start, end);

                            if (interceptPosition.isPresent()) {
                                double distanceToEntity = start.distanceTo(interceptPosition.get());

                                if (distanceToEntity < minDistance || distanceToEntity == 0.0) {
                                    entityLooked = e;
                                    minDistance = distanceToEntity;
                                }
                            }

                            if (entityLooked != null && (minDistance < 512.0 || result == null)) {
                                entityFound = entityLooked;
                            }
                        }
                    }

                    if (entityFound != null) {
                        push(new EntityValue(entityFound.getEntityId()));
                    }
                    else {
                        LOGGER.debug(String.format("No entity found at instruction address %04d", ip));
                        push(null);
                    }
                    return InterpretResult.CONTINUE;
                }
                else {
                    LOGGER.debug(String.format("Invalid entity type '%s'", ev != null ? ev.getClass().getSimpleName() : null));
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.ADD_MOTION:
            {
                Optional<Double> z = asDouble(pop());
                Optional<Double> y = asDouble(pop());
                Optional<Double> x = asDouble(pop());
                Value ev = pop();

                if (!x.isPresent() || !y.isPresent() || !z.isPresent()) {
                    return InterpretResult.RUNTIME_ERROR;
                }
                else if (ev instanceof EntityValue) {
                    Entity entity = event.world.getEntityByID(((EntityValue)ev).entityID);

                    if (entity instanceof ServerPlayerEntity) {
                        MessageAdditiveMotion motion = new MessageAdditiveMotion(entity.getEntityId(), x.get(), y.get(), z.get());
                        FCPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)entity), motion);
                    }
                    else {
                        entity.addVelocity(x.get(), y.get(), z.get());
                    }

                    return InterpretResult.CONTINUE;
                }
                else {
                    LOGGER.debug(String.format("Invalid entity type '%s'", ev != null ? ev.getClass().getSimpleName() : null));
                    return InterpretResult.RUNTIME_ERROR;
                }
            }

            case OpCode.SPAWN_ENTITY:
            {
                Optional<Double> z = asDouble(pop());
                Optional<Double> y = asDouble(pop());
                Optional<Double> x = asDouble(pop());

                Value pathValue = pop();
                Value namespaceValue = pop();

                if (!(pathValue instanceof StringValue) || !(namespaceValue instanceof StringValue)) {
                    return InterpretResult.RUNTIME_ERROR;
                }

                if (!x.isPresent() || !y.isPresent() || !z.isPresent()) {
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    Entity e = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(((StringValue)namespaceValue).value, ((StringValue)pathValue).value)).create(event.world);
                    e.moveForced(x.get(), y.get(), z.get());
                    event.world.addEntity(e);
                    push(new EntityValue(e.getEntityId()));
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.CREATE_EXPLOSION:
            {
                Optional<Double> radius = asDouble(pop());
                Optional<Double> z = asDouble(pop());
                Optional<Double> y = asDouble(pop());
                Optional<Double> x = asDouble(pop());

                if (!x.isPresent() || !y.isPresent() || !z.isPresent() || !radius.isPresent()) {
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    event.world.createExplosion(null, x.get(), y.get(), z.get(), radius.get().floatValue(), Mode.NONE);
                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.MOVE_BLOCK:
            {
                Optional<Double> dz = asDouble(pop());
                Optional<Double> dy = asDouble(pop());
                Optional<Double> dx = asDouble(pop());
                Optional<Double> oz = asDouble(pop());
                Optional<Double> oy = asDouble(pop());
                Optional<Double> ox = asDouble(pop());

                if (!ox.isPresent() || !oy.isPresent() || !oz.isPresent() || !dx.isPresent() || !dy.isPresent() || !dz.isPresent()) {
                    return InterpretResult.RUNTIME_ERROR;
                }
                else {
                    BlockPos pos = new BlockPos(ox.get(), oy.get(), oz.get());
                    BlockState state = event.world.getBlockState(pos);
                    if (event.world.getTileEntity(pos) != null || state.getPushReaction() != PushReaction.NORMAL || state.getBlockHardness(event.world, pos) == -1) {
                        return InterpretResult.CONTINUE;
                    }

                    BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(event.world, pos, state, caster);
                    if (MinecraftForge.EVENT_BUS.post(breakEvent)) {
                        return InterpretResult.CONTINUE;
                    }

                    BlockPos nPos = new BlockPos(ox.get() + dx.get(), oy.get() + dy.get(), oz.get() + dz.get());
                    BlockState nState = event.world.getBlockState(nPos);

                    if (!event.world.isBlockModifiable(caster, pos) || !event.world.isBlockModifiable(caster, nPos)) {
                        return InterpretResult.CONTINUE;
                    }

                    if (nState.isAir(event.world, nPos) || nState.getMaterial().isReplaceable()) {
                        event.world.setBlockState(nPos, state, 1 | 2);
                        event.world.removeBlock(pos, false);
                        event.world.playEvent(2001, pos, Block.getStateId(state));
                    }

                    return InterpretResult.CONTINUE;
                }
            }

            case OpCode.PAUSE:
            {
                return InterpretResult.PAUSE;
            }

            default:
                LOGGER.debug(String.format("Unknown OpCode '%04d' at instruction address %04d", instruction, ip));
                return InterpretResult.RUNTIME_ERROR;
        }
    }

    public PlayerEntity getCaster() {
        return caster;
    }

    private int readInt() {
        return chunk.read(ip++);
    }

    private void resetStack() {
        stackTop = 0;
        for (int i = 0; i < STACK_MAX; i++) {
            stack[i] = null;
        }
    }

    private void push(Value value) {
        if (stackTop >= STACK_MAX) {
            LOGGER.debug(String.format("Stack overflow at instruction address %04d", ip));
        }
        else {
            stack[stackTop++] = value;
        }
    }

    private Value pop() {
        if (stackTop <= 0) {
            LOGGER.debug(String.format("Stack underflow at instruction address %04d", ip));
            return null;
        }
        else {
            return stack[--stackTop];
        }
    }

    private Value peek(int offset) {
        if (stackTop - offset - 1 < 0) {
            return null;
        }
        else {
            return stack[stackTop - offset - 1];
        }
    }

    private static Optional<Double> asDouble(Value value) {
        if (validate(value, DoubleValue.class) != null) {
            return Optional.of(validate(value, DoubleValue.class).value);
        }
        else if (validate(value, LongValue.class, String.format("Expected value to be of type DoubleValue or LongValue, but got type '%s'", value != null ? value.getClass().getSimpleName() : null)) != null) {
            return Optional.of((double)validate(value, LongValue.class).value);
        }
        else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Value> T validate(Value value, Class<T> clazz) {
        if (value != null && clazz.isInstance(value)) {
            return (T)value;
        }
        else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Value> T validate(Value value, Class<T> clazz, String debugMessage) {
        if (value != null && clazz.isInstance(value)) {
            return (T)value;
        }
        else {
            LOGGER.debug(debugMessage);
            return null;
        }
    }
}