package xyz.mrmelon54.BetterEventChests.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.mixin.object.builder.client.ModelPredicateProviderRegistrySpecificAccessor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import xyz.mrmelon54.BetterEventChests.config.ConfigStructure;
import xyz.mrmelon54.BetterEventChests.enums.ChristmasChestsEnabled;
import xyz.mrmelon54.BetterEventChests.models.ChristmasChestModelProvider;
import xyz.mrmelon54.BetterEventChests.utils.ChestBoatArray;

import java.util.Calendar;

public class BetterChristmasChestsClient implements ClientModInitializer {
    private static BetterChristmasChestsClient instance;
    private ConfigStructure config;

    @Override
    public void onInitializeClient() {
        instance = this;

        AutoConfig.register(ConfigStructure.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ConfigStructure.class).getConfig();

        ChristmasChestModelProvider christmasChestModelProvider = new ChristmasChestModelProvider();
        ModelPredicateProviderRegistrySpecificAccessor.callRegister(Items.CHEST_MINECART, new Identifier("christmas_chest"), christmasChestModelProvider);
        for (Item chestBoat : ChestBoatArray.ChestBoats)
            ModelPredicateProviderRegistrySpecificAccessor.callRegister(chestBoat, new Identifier("christmas_chest"), christmasChestModelProvider);
    }

    public boolean isChristmasDates() {
        Calendar calendar = Calendar.getInstance();
        return (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DATE) >= 24 && calendar.get(Calendar.DATE) <= 26);
    }

    public boolean isChristmas() {
        ConfigStructure config = getConfig();
        return config.christmasModeEnabled == ChristmasChestsEnabled.ALWAYS || (config.christmasModeEnabled == ChristmasChestsEnabled.AT_CHRISTMAS && isChristmasDates());
    }

    public boolean enableCharmPresents() {
        return getConfig().charmPresentsEnabled;
    }

    public boolean enableChristmasChest() {
        return getConfig().christmasChestEnabled;
    }

    public boolean enableChristmasTrappedChest() {
        return getConfig().christmasTrappedChestEnabled;
    }

    public boolean enableChristmasEnderChest() {
        return getConfig().christmasEnderChestEnabled;
    }

    public boolean enableChristmasMinecartWithChest() {
        return getConfig().christmasMinecartWithChestEnabled;
    }

    public boolean enableChristmasDonkey() {
        return getConfig().christmasDonkeyEnabled;
    }

    public boolean enableChristmasHorse() {
        return getConfig().christmasHorseEnabled;
    }

    public boolean enableChristmasZombieHorse() {
        return getConfig().christmasZombieHorseEnabled;
    }

    public boolean enableChristmasChestBoat() {
        return getConfig().christmasChestBoatEnabled;
    }

    public static BetterChristmasChestsClient getInstance() {
        return instance;
    }

    public ConfigStructure getConfig() {
        return config;
    }
}
