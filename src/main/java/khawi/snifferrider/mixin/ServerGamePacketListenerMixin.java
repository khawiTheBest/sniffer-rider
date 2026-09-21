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
            var input = packet.input();
            float forward = (input.forward() ? 1.0f : 0.0f) - (input.backward() ? 1.0f : 0.0f);
            float sideways = (input.right() ? 1.0f : 0.0f) - (input.left() ? 1.0f : 0.0f);
            access.snifferRider$setInput(sideways, forward, input.jump());
        }
    }
}
