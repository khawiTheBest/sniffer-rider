package khawi.snifferrider.mixin;

import khawi.snifferrider.SnifferRiderEntityAccess;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerMixin {
    @Inject(method = "handlePlayerInput", at = @At("HEAD"))
    private void snifferRider$input(ServerboundPlayerInputPacket packet, CallbackInfo ci) {
        ServerGamePacketListenerImpl self = (ServerGamePacketListenerImpl)(Object)this;
        Entity vehicle = self.player.getVehicle();
        if (vehicle instanceof SnifferRiderEntityAccess access) {
            access.snifferRider$setInput(packet.getXxa(), packet.getZza(), packet.isJumping());
        }
    }
}
