package khawi.snifferrider.mixin;

import khawi.snifferrider.SnifferChestMenu;
import khawi.snifferrider.SnifferRider;
import khawi.snifferrider.SnifferRiderEntityAccess;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.passive.Sniffer;
import net.minecraft.world.entity.vehicle.Vehicle;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MoverType;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sniffer.class)
public abstract class SnifferEntityMixin implements SnifferRiderEntityAccess {
    @Unique private boolean snifferRider$saddled;
    @Unique private boolean snifferRider$hasChest;
    @Unique private SimpleContainer snifferRider$chest = new SimpleContainer(17);
    @Unique private float snifferRider$sideways;
    @Unique private float snifferRider$forward;
    @Unique private boolean snifferRider$jump;

    @Override public boolean snifferRider$isSaddled() { return snifferRider$saddled; }
    @Override public void snifferRider$setSaddled(boolean value) { snifferRider$saddled = value; }
    @Override public boolean snifferRider$hasChest() { return snifferRider$hasChest; }
    @Override public void snifferRider$setHasChest(boolean value) { snifferRider$hasChest = value; }
    @Override public SimpleContainer snifferRider$getChest() { return snifferRider$chest; }

    @Override
    public void snifferRider$setInput(float sideways, float forward, boolean jump) {
        snifferRider$sideways = sideways;
        snifferRider$forward = forward;
        snifferRider$jump = jump;
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void snifferRider$interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        Sniffer self = (Sniffer)(Object)this;

        if (stack.is(Items.SADDLE) && !snifferRider$saddled) {
            snifferRider$saddled = true;
            if (!player.getAbilities().instabuild) stack.shrink(1);
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (stack.is(Items.CHEST) && snifferRider$saddled && !snifferRider$hasChest) {
            snifferRider$hasChest = true;
            if (!player.getAbilities().instabuild) stack.shrink(1);
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (stack.isEmpty() && snifferRider$saddled && snifferRider$hasChest && !player.isShiftKeyDown() && player.level() instanceof ServerLevel serverLevel) {
            player.openMenu(new ExtendedScreenHandlerFactory<Integer>() {
                @Override public Component getDisplayName() {
                    return Component.translatable("container.sniffer_rider.chest");
                }
                @Override public Integer getScreenOpeningData(ServerPlayer serverPlayer) {
                    return self.getId();
                }
                @Override public AbstractContainerMenu createMenu(int syncId, net.minecraft.world.entity.player.Inventory inventory, Player p) {
                    return new SnifferChestMenu(syncId, inventory, self.getId(), snifferRider$chest);
                }
            });
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (stack.isEmpty() && snifferRider$saddled && !player.isShiftKeyDown()) {
            if (!self.isVehicle()) {
                player.startRiding(self);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Inject(method = "getControllingPassenger", at = @At("HEAD"), cancellable = true)
    private void snifferRider$controller(CallbackInfoReturnable<LivingEntity> cir) {
        Sniffer self = (Sniffer)(Object)this;
        if (snifferRider$saddled && self.getFirstPassenger() instanceof LivingEntity living) {
            cir.setReturnValue(living);
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void snifferRider$move(CallbackInfo ci) {
        Sniffer self = (Sniffer)(Object)this;
        if (!(self.getFirstPassenger() instanceof Player player) || !snifferRider$saddled) return;

        self.setYRot(player.getYRot());
        self.setYHeadRot(player.getYRot());

        float forward = snifferRider$forward;
        float sideways = snifferRider$sideways;
        if (Math.abs(forward) < 0.01f && Math.abs(sideways) < 0.01f) {
            self.setDeltaMovement(0, self.getDeltaMovement().y, 0);
            return;
        }

        float speed = 0.22f;
        if (forward < 0) speed *= 0.5f;

        double rad = Math.toRadians(self.getYRot());
        double sin = -Math.sin(rad);
        double cos = Math.cos(rad);

        double dx = (forward * sin + sideways * cos) * speed;
        double dz = (forward * cos - sideways * sin) * speed;

        self.getNavigation().stop();
        self.setDeltaMovement(dx, self.getDeltaMovement().y, dz);
        self.move(MoverType.SELF, self.getDeltaMovement());
    }
}
