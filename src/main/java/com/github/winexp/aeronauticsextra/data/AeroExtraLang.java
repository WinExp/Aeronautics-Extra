package com.github.winexp.aeronauticsextra.data;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import net.createmod.catnip.lang.LangBuilder;

public class AeroExtraLang {
    public static LangBuilder builder() {
        return new LangBuilder(AeronauticsExtra.MOD_ID);
    }

    public static LangBuilder text(String text) {
        return builder().text(text);
    }

    public static LangBuilder translate(String key, Object... args) {
        return builder().translate(key, args);
    }
}
