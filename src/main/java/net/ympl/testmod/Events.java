package net.ympl.testmod;

import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=Testmod.MODID)
public class Events {
    @SubscribeEvent
    public static void onNoteblockChange(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        ItemStack heldItem = player.getMainHandItem();
        Level level = event.getLevel();

        if (state.getBlock() == Blocks.NOTE_BLOCK && heldItem.getItem() == Items.STICK) {
            event.setCanceled(true);
            int newNote = (heldItem.getCount()) % 25;
            BlockState newState = state.setValue(NoteBlock.NOTE, newNote);
            level.setBlock(pos, newState, 3);

            float pitch = (float) Math.pow(2.0D, (newNote - 12) / 12.0D);
            level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.get(), SoundSource.BLOCKS, 3.0F, pitch);

            double noteParticleValue = newNote / 24.0D;
            level.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, noteParticleValue, 0.0D, 0.0D);
        }
    }
}
