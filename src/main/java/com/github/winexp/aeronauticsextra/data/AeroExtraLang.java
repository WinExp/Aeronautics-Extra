package com.github.winexp.aeronauticsextra.data;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.lang.LangNumberFormat;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class AeroExtraLang {
    public static LangBuilder builder() {
        return new LangBuilder(AeronauticsExtra.MOD_ID);
    }

    public static LangBuilder text(String text) {
        return builder().text(text);
    }

    public static LangBuilder number(double number) {
        return text(LangNumberFormat.format(number));
    }

    public static LangBuilder translate(String key, Object... args) {
        return builder().translate(key, args);
    }

    public static List<Component> translatedOptions(String prefix, String... keys) {
        List<Component> result = new ArrayList<>(keys.length);
        for (String key : keys)
            result.add(translate((prefix != null ? prefix + "." : "") + key).component());
        return result;
    }
}
