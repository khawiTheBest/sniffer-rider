package khawi.snifferrider;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SnifferChestMenu extends AbstractContainerMenu {
    private final SimpleContainer chest;
    private final int entityId;

    public SnifferChestMenu(int syncId, Inventory playerInventory, int entityId, SimpleContainer serverChest) {
        super(SnifferRider.SNIFFER_CHEST_MENU, syncId);
        this.entityId = entityId;
        this.chest = serverChest != null ? serverChest : new SimpleContainer(17);

        for (int i = 0; i < 17; i++) {
            int row = i / 9;
            int col = i % 9;
            int x = 8 + col * 18;
            int y = 18 + row * 18;
            addSlot(new Slot(chest, i, x, y));
        }

        addStandardInventorySlots(playerInventory, 8, 72);
    }

    public int getEntityId() {
        return entityId;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot == null || !slot.hasItem()) return empty;

        ItemStack original = slot.getItem();
        ItemStack copy = original.copy();

        if (slotIndex < 17) {
            if (!moveItemStackTo(original, 17, slots.size(), true)) return empty;
        } else if (!moveItemStackTo(original, 0, 17, false)) {
            return empty;
        }

        if (original.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();

        if (original.getCount() == copy.getCount()) return empty;
        slot.onTake(player, original);
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }
}
