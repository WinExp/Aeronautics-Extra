package com.github.winexp.aeronauticsextra.data;

import com.github.winexp.aeronauticsextra.AeroExtraBlockTags;
import com.github.winexp.aeronauticsextra.AeroExtraItemTags;
import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.function.BiConsumer;

@EventBusSubscriber(modid = AeronauticsExtra.MOD_ID)
public class AeroExtraDataGen {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gatherDataHighPriority(GatherDataEvent event) {
        if (event.getMods().contains(AeronauticsExtra.MOD_ID)) {
            addExtraRegistrateData();
        }
    }

    private static void addExtraRegistrateData() {
        AeroExtraBlockTags.addGenerator();
        AeroExtraItemTags.addGenerator();

        AeronauticsExtra.getRegistrate().addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("interface", langConsumer);
        });
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/%s/lang/default/%s.json".formatted(AeronauticsExtra.MOD_ID, fileName);
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }
}
