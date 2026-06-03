package net.andrespr.casinorocket.block.custom;

import com.mojang.serialization.MapCodec;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.entity.custom.SlotMachineEntity;
import net.andrespr.casinorocket.network.s2c.SlotConfigSyncS2CPayload;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class SlotMachineBlock extends BaseEntityBlock implements EntityBlock {

    public static final MapCodec<SlotMachineBlock> CODEC = simpleCodec(SlotMachineBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape BOTTOM_PART = Block.box(1,0,2,15,14,15);
    private static final VoxelShape MIDDLE_PART = Block.box(1,14,1,15,16,15);
    private static final VoxelShape UPPER_PART = Block.box(1,0,5,15,16,15);
    private static final VoxelShape LEFT_PART = Block.box(14,0,1,15,11,5);
    private static final VoxelShape RIGHT_PART = Block.box(1,0,1,2,11,5);

    private static final VoxelShape LOWER_SHAPE = Shapes.or(BOTTOM_PART, MIDDLE_PART);
    private static final VoxelShape UPPER_SHAPE = Shapes.or(UPPER_PART, LEFT_PART, RIGHT_PART);

    private static final Map<Direction, VoxelShape> LOWER_SHAPES_BY_DIRECTION = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> UPPER_SHAPES_BY_DIRECTION = new EnumMap<>(Direction.class);

    static {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            LOWER_SHAPES_BY_DIRECTION.put(dir, rotateShape(dir, LOWER_SHAPE));
            UPPER_SHAPES_BY_DIRECTION.put(dir, rotateShape(dir, UPPER_SHAPE));
        }
    }

    public SlotMachineBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    // === CODEC ===
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    // === INTERACTION ===
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos,
                                 Player player, BlockHitResult hit) {

        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockPos lowerPos = pos.below();
            BlockState lowerState = world.getBlockState(lowerPos);
            if (lowerState.is(this)) {
                return this.useWithoutItem(lowerState, world, lowerPos, player, hit);
            }
            return InteractionResult.PASS;
        }

        if (!world.isClientSide) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SlotMachineEntity slotMachineEntity) {

                if (slotMachineEntity.isInUse() && !slotMachineEntity.isUsedBy(player)) {
                    CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.slot_machine_occupied", true);
                    return InteractionResult.CONSUME;
                }

                if (!slotMachineEntity.tryLock(player)) {
                    return InteractionResult.CONSUME;
                }

                player.openMenu(slotMachineEntity);
                return InteractionResult.CONSUME;

            }
        }

        return InteractionResult.SUCCESS;
    }

    // === SHAPE ===
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        DoubleBlockHalf half = state.getValue(HALF);
        return half == DoubleBlockHalf.LOWER
                ? LOWER_SHAPES_BY_DIRECTION.get(facing)
                : UPPER_SHAPES_BY_DIRECTION.get(facing);
    }

    // === PLACEMENT ===
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Level world = ctx.getLevel();
        if (pos.getY() < world.getMaxBuildHeight() - 1 && world.getBlockState(pos.above()).canBeReplaced(ctx)) {
            return this.defaultBlockState()
                    .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                    .setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
        if (!world.isClientSide) {
            CasinoRocket.LOGGER.info("[DEBUG] SlotMachineBlock placed at {}", pos);
            BlockEntity be = world.getBlockEntity(pos);
            CasinoRocket.LOGGER.info("[DEBUG] BlockEntity = {}", be);
        }
    }

    // === BREAK ===
    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        DoubleBlockHalf half = state.getValue(HALF);
        BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
        BlockState otherState = world.getBlockState(otherPos);

        if (otherState.is(this) && otherState.getValue(HALF) != half) {
            world.destroyBlock(otherPos, !player.isCreative());
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                                   LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (direction.getAxis() == net.minecraft.core.Direction.Axis.Y) {
            if (half == DoubleBlockHalf.LOWER && direction == net.minecraft.core.Direction.UP && (!neighborState.is(this) || neighborState.getValue(HALF) != DoubleBlockHalf.UPPER)) {
                return Blocks.AIR.defaultBlockState();
            }
            if (half == DoubleBlockHalf.UPPER && direction == net.minecraft.core.Direction.DOWN && (!neighborState.is(this) || neighborState.getValue(HALF) != DoubleBlockHalf.LOWER)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    // === FACING ===
    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    // === PROPERTIES ===
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    // === BLOCK ENTITY ===
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SlotMachineEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // === HELPERS ===
    private static VoxelShape rotateShape(Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1],
                    Shapes.box(
                            1 - maxZ, minY, minX,
                            1 - minZ, maxY, maxX
                    )));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        return buffer[0];
    }

}

