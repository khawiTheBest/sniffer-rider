package khawi.snifferrider;

import net.minecraft.world.SimpleContainer;

public interface SnifferRiderEntityAccess {
    boolean snifferRider$isSaddled();
    void snifferRider$setSaddled(boolean value);
    boolean snifferRider$hasChest();
    void snifferRider$setHasChest(boolean value);
    SimpleContainer snifferRider$getChest();
    void snifferRider$setInput(float sideways, float forward, boolean jump);
}
