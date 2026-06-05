package net.andrespr.casinorocket.block.custom;

import com.mojang.serialization.MapCodec;
import net.andrespr.casinorocket.games.records.CasinoLedgerService;
import net.andrespr.casinorocket.screen.custom.ledger.CasinoLedgerScreenHandler;
import net.andrespr.casinorocket.screen.opening.CasinoLedgerOpenData;
import net.andrespr.casinorocket.screen.opening.MenuDataProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CasinoScoreboardBlock extends Block {

    public static final MapCodec<CasinoScoreboardBlock> CODEC = simpleCodec(CasinoScoreboardBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty PART_X = IntegerProperty.create("part_x", 0, 2);
    public static final IntegerProperty PART_Y = IntegerProperty.create("part_y", 0, 1);

    private static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 13, 16, 16, 16);
    private static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 0, 16, 16, 3);
    private static final VoxelShape EAST_SHAPE = Block.box(0, 0, 0, 3, 16, 16);
    private static final VoxelShape WEST_SHAPE = Block.box(13, 0, 0, 16, 16, 16);

    public CasinoScoreboardBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART_X, 1)
                .setValue(PART_Y, 0));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer && serverPlayer.getServer() != null) {
            CasinoLedgerOpenData data = CasinoLedgerService.createSnapshot(serverPlayer.getServer(), serverPlayer);
            serverPlayer.openMenu(new MenuDataProvider<>(
                    Component.translatable("gui.casinorocket.casino_ledger"),
                    data,
                    CasinoLedgerOpenData.CODEC,
                    (syncId, inventory, menuPlayer, openData) -> new CasinoLedgerScreenHandler(syncId, inventory, openData)
            ));
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getPartShape(state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getPartShape(state);
    }

    private static VoxelShape getPartShape(BlockState state) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        if (clickedFace.getAxis().isHorizontal()) {
            BlockState state = this.defaultBlockState()
                    .setValue(FACING, clickedFace)
                    .setValue(PART_X, 1)
                    .setValue(PART_Y, 0);
            return canPlaceAllParts(context, clickedFace) ? state : null;
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable net.minecraft.world.entity.LivingEntity placer,
                            ItemStack stack) {
        Direction facing = state.getValue(FACING);
        for (int partX = 0; partX <= 2; partX++) {
            for (int partY = 0; partY <= 1; partY++) {
                if (partX == 1 && partY == 0) continue;
                BlockPos partPos = getPartPos(pos, facing, partX, partY);
                BlockState partState = state
                        .setValue(PART_X, partX)
                        .setValue(PART_Y, partY);
                level.setBlock(partPos, partState, Block.UPDATE_ALL);
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos anchorPos = getAnchorPos(pos, state);
        BlockPos supportPos = anchorPos.relative(facing.getOpposite());
        if (!level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing)) {
            return false;
        }
        if (state.getValue(PART_X) == 1 && state.getValue(PART_Y) == 0) {
            return true;
        }
        BlockState anchorState = level.getBlockState(anchorPos);
        return anchorState.is(this)
                && anchorState.getValue(FACING) == facing
                && anchorState.getValue(PART_X) == 1
                && anchorState.getValue(PART_Y) == 0;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockPos anchorPos = getAnchorPos(pos, state);
            Direction facing = state.getValue(FACING);
            boolean breakingAnchor = state.getValue(PART_X) == 1 && state.getValue(PART_Y) == 0;

            if (!breakingAnchor && !player.isCreative()) {
                popResource(level, anchorPos, new ItemStack(this));
            }

            List<BlockPos> partsToClear = new ArrayList<>();
            for (int partX = 0; partX <= 2; partX++) {
                for (int partY = 0; partY <= 1; partY++) {
                    BlockPos partPos = getPartPos(anchorPos, facing, partX, partY);
                    if (!partPos.equals(pos) && level.getBlockState(partPos).is(this)) {
                        partsToClear.add(partPos);
                    }
                }
            }

            for (BlockPos partPos : partsToClear) {
                BlockState partState = level.getBlockState(partPos);
                level.levelEvent(2001, partPos, Block.getId(partState));
                level.setBlock(partPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART_X, PART_Y);
    }

    private boolean canPlaceAllParts(BlockPlaceContext context, Direction facing) {
        Level level = context.getLevel();
        BlockPos anchorPos = context.getClickedPos();
        for (int partX = 0; partX <= 2; partX++) {
            for (int partY = 0; partY <= 1; partY++) {
                BlockPos partPos = getPartPos(anchorPos, facing, partX, partY);
                if (!level.getBlockState(partPos).canBeReplaced(context)) {
                    return false;
                }
            }
        }
        BlockPos supportPos = anchorPos.relative(facing.getOpposite());
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
    }

    private static BlockPos getPartPos(BlockPos anchorPos, Direction facing, int partX, int partY) {
        return anchorPos.relative(facing.getClockWise(), partX - 1).above(partY);
    }

    private static BlockPos getAnchorPos(BlockPos partPos, BlockState state) {
        Direction facing = state.getValue(FACING);
        return partPos
                .relative(facing.getClockWise(), 1 - state.getValue(PART_X))
                .below(state.getValue(PART_Y));
    }
}
