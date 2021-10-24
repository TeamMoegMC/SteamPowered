package com.teammoeg.steampowered.registrate;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import com.teammoeg.steampowered.SteamPowered;
import net.minecraft.item.Item;

public class SPItems {

    private static final CreateRegistrate REGISTRATE = SteamPowered.registrate.get()
            .itemGroup(() -> SteamPowered.itemGroup);

    public static final ItemEntry<Item> BRONZE_SHEET =
            REGISTRATE.item("bronze_sheet", Item::new)
                    .register();

    public static void register() {
    }
}
