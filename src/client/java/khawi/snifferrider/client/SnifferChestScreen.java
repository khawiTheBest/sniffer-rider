package khawi.snifferrider.client;

import khawi.snifferrider.SnifferChestMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SnifferChestScreen extends AbstractContainerScreen<SnifferChestMenu> {
    public SnifferChestScreen(SnifferChestMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 190;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        int left = (width - imageWidth) / 2;
        int top = (height - imageHeight) / 2;

        graphics.fill(left, top, left + imageWidth, top + imageHeight, 0xFF202020);

        for (int i = 0; i < 17; i++) {
            int row = i / 9;
            int col = i % 9;
            int x = left + 8 + col * 18;
            int y = top + 18 + row * 18;
            graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF8A8A8A);
        }
    }
}
