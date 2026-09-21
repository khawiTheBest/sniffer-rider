package khawi.snifferrider;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;

public class SnifferRider implements ModInitializer {
    public static final String MOD_ID = "sniffer_rider";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static final MenuType<SnifferChestMenu> SNIFFER_CHEST_MENU =
            Registry.register(
                    Registries.MENU,
                    id("sniffer_chest"),
                    new ExtendedScreenHandlerType<>(
                            (syncId, inventory, entityId) ->
                                    new SnifferChestMenu(syncId, inventory, entityId, null),
                            ByteBufCodecs.VAR_INT
                    )
            );

    @Override
    public void onInitialize() {
    }
}
