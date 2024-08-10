package me.diamondy.velasus.utils;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class SerializerUtil {
    public final static LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .hexCharacter('#')
            .character('&')
            .hexColors()
            .build();
}


