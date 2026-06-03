package net.andrespr.casinorocket.block.custom;

import com.mojang.serialization.MapCodec;
import net.andrespr.casinorocket.block.entity.custom.ChipTableEntity;
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

public class ChipTableBlock extends BaseEntityBlock implements EntityBlock {

    public static final MapCodec<ChipTableBlock> CODEC = simpleCodec(ChipTableBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape LEG1 = Block.box(1,0,1,3,15,3);
    private static final VoxelShape LEG2 = Block.box(1,0,13,3,15,15);
    private static final VoxelShape LEG3 = Block.box(13,0,13,15,15,15);
    private static final VoxelShape LEG4 = Block.box(13,0,1,15,15,3);
    private static final VoxelShape LEGS = Shapes.or(LEG1, LEG2, LEG3, LEG4);

    private static final VoxelShape STAND1 = Block.box(3,4,1,13,6,3);
    private static final VoxelShape STAND2 = Block.box(1,4,3,3,6,13);
    private static final VoxelShape STAND3 = Block.box(3,4,13,13,6,15);
    private static final VoxelShape STAND4 = Block.box(13,4,3,15,6,13);
    private static final VoxelShape STAND = Shapes.or(STAND1, STAND2, STAND3, STAND4);

    private static final VoxelShape TABLE1 = Block.box(3,11,1,13,13,3);
    private static final VoxelShape TABLE2 = Block.box(3,11,13,13,13,15);
    private static final VoxelShape TABLE3 = Block.box(1,11,3,3,13,13);
    private static final VoxelShape TABLE4 = Block.box(13,11,3,15,13,13);
    private static final VoxelShape TABLES = Shapes.or(TABLE1, TABLE2, TABLE3, TABLE4);

    private static final VoxelShape CHIPS = Block.box(3,9,3,13,15,13);
    private static final VoxelShape BLOCK_SHAPE = Shapes.or(LEGS, STAND, TABLES, CHIPS);

    public ChipTableBlock(Properties settings) {
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
        if(!world.isClientSide && world.getBlockEntity(pos) instanceof ChipTableEntity chipTableEntity) {
            player.openMenu(chipTableEntity);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    // === SHAPE ===
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return BLOCK_SHAPE;
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
        return new ChipTableEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

}

