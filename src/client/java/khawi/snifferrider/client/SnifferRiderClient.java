package khawi.snifferrider.client;

import khawi.snifferrider.SnifferRider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class SnifferRiderClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(SnifferRider.SNIFFER_CHEST_MENU, SnifferChestScreen::new);
    }
}
