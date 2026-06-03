package net.andrespr.casinorocket.block.custom;

import com.mojang.serialization.MapCodec;
import net.andrespr.casinorocket.block.entity.custom.BlackjackTableEntity;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class BlackjackTableBlock extends BaseEntityBlock implements EntityBlock {

    public static final MapCodec<BlackjackTableBlock> CODEC = simpleCodec(BlackjackTableBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape RIGHT_LEG = Block.box(2,0,4,6,13,14);
    private static final VoxelShape LEFT_LEG = Block.box(10,0,4,14,13,14);

    private static final VoxelShape TABLE1 = Block.box(4,13,1,12,15,2);
    private static final VoxelShape TABLE2 = Block.box(2,13,2,14,15,3);
    private static final VoxelShape TABLE3 = Block.box(1,13,3,15,15,4);
    private static final VoxelShape TABLE4 = Block.box(0,13,4,16,15,16);

    private static final VoxelShape BLOCK_SHAPE = Shapes.or(RIGHT_LEG, LEFT_LEG, TABLE1, TABLE2, TABLE3, TABLE4);
    private static final Map<Direction, VoxelShape> BLOCK_SHAPE_BY_DIRECTION = new EnumMap<>(Direction.class);

    static {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BLOCK_SHAPE_BY_DIRECTION.put(dir, rotateShape(dir, BLOCK_SHAPE));
        }
    }

    public BlackjackTableBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    // === CODEC ===
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    // === INTERACTION ===
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide) {

            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof BlackjackTableEntity table) {

                if (table.isInUse() && !table.isUsedBy(player)) {
                    CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.blackjack_table_occupied", true);
                    return InteractionResult.CONSUME;
                }

                if (!table.tryLock(player)) {
                    return InteractionResult.CONSUME;
                }

                player.openMenu(table);
                return InteractionResult.CONSUME;

            }
        }

        return InteractionResult.SUCCESS;
    }

    // === SHAPE ===
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return BLOCK_SHAPE_BY_DIRECTION.get(facing);
    }

    // === PLACEMENT ===
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
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
        builder.add(FACING);
    }

    // === BLOCK ENTITY ===
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlackjackTableEntity(pos, state);
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

