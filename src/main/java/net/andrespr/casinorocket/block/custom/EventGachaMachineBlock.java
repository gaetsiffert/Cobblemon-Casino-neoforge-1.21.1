package net.andrespr.casinorocket.block.custom;

import com.mojang.serialization.MapCodec;
import net.andrespr.casinorocket.games.gachapon.GachaMachinesUtils;
import net.andrespr.casinorocket.games.gachapon.GachaponUtils;
import net.andrespr.casinorocket.games.gachapon.PokemonGachaponUtils;
import net.andrespr.casinorocket.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
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

public class EventGachaMachineBlock extends Block {

    public static final MapCodec<EventGachaMachineBlock> CODEC = simpleCodec(EventGachaMachineBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape MIDDLE_PART = Block.box(4,0,1,12,16,15);
    private static final VoxelShape LEFT_MIDDLE_PART = Block.box(12,0,2,14,16,14);
    private static final VoxelShape LEFT_PART = Block.box(14,0,4,15,16,12);
    private static final VoxelShape RIGHT_MIDDLE_PART = Block.box(2,0,2,4,16,14);
    private static final VoxelShape RIGHT_PART = Block.box(1,0,4,2,16,12);

    private static final VoxelShape UP_MIDDLE_PART = Block.box(0.5,0,0.5,15.5,1,15.5);
    private static final VoxelShape UP_LOWER_PART = Block.box(0,1,0,16,15,16);
    private static final VoxelShape UP_UPPER_PART = Block.box(0.5,15,0.5,15.5,16,15.5);

    private static final VoxelShape LOWER_SHAPE = Shapes.or(MIDDLE_PART, LEFT_MIDDLE_PART, LEFT_PART, RIGHT_MIDDLE_PART, RIGHT_PART);
    private static final VoxelShape UPPER_SHAPE = Shapes.or(UP_UPPER_PART, UP_MIDDLE_PART, UP_LOWER_PART);

    public EventGachaMachineBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    // === INTERACTION ===
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos,
                              Player player, BlockHitResult hit) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockPos lowerPos = pos.below();
            BlockState lowerState = world.getBlockState(lowerPos);

            if (lowerState.is(this)) {
                return this.useWithoutItem(lowerState, world, lowerPos, player, hit);
            }

            return InteractionResult.PASS;
        }

        if (world.isClientSide) return InteractionResult.SUCCESS;

        ItemStack stack = player.getMainHandItem();

        if (!stack.is(ModItems.EVENT_COIN)) {
            player.displayClientMessage(Component.literal("This machine only accepts Event Coins!").withStyle(ChatFormatting.RED), true);
            return InteractionResult.FAIL;
        }

        boolean hasItems = GachaponUtils.hasValidPool("event");
        boolean hasPokemon = PokemonGachaponUtils.hasValidPool("event");
        if (!hasItems && !hasPokemon) {
            player.displayClientMessage(Component.literal("There's no event at the moment.").withStyle(ChatFormatting.RED), true);
            return InteractionResult.FAIL;
        }

        InteractionResult result = GachaMachinesUtils.handleEventUse(world, pos, player);
        if (result == InteractionResult.SUCCESS) {
            stack.shrink(1);
        }
        return result;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        Direction facing = state.getValue(FACING);
        GachaMachinesUtils.finishEventDispense(world, pos, facing);
    }

    // === SHAPE ===
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? LOWER_SHAPE : UPPER_SHAPE;
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
    }

    // === BREAK ===
    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockPos lowerPos = pos.below();
            BlockState lowerState = world.getBlockState(lowerPos);

            if (lowerState.is(this) && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                if (!player.isCreative()) {
                    popResource(world, lowerPos, new ItemStack(this));
                }
                world.levelEvent(2001, lowerPos, Block.getId(lowerState));
                world.setBlock(lowerPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
            }
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                                   LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y) {
            if (half == DoubleBlockHalf.LOWER && direction == Direction.UP && (!neighborState.is(this)
                    || neighborState.getValue(HALF) != DoubleBlockHalf.UPPER)) {
                return Blocks.AIR.defaultBlockState();
            }
            if (half == DoubleBlockHalf.UPPER && direction == Direction.DOWN && (!neighborState.is(this)
                    || neighborState.getValue(HALF) != DoubleBlockHalf.LOWER)) {
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

}

