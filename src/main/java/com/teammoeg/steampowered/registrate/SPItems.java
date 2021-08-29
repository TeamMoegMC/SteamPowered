package com.teammoeg.steampowered.registrate;

import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import com.teammoeg.steampowered.SteamPowered;
import com.teammoeg.steampowered.item.Multimeter;

public class SPItems {

    private static final CreateRegistrate REGISTRATE = SteamPowered.registrate.get()
            .itemGroup(() -> SteamPowered.itemGroup);

    public static final ItemEntry<Multimeter> MULTIMETER =
            REGISTRATE.item("multimeter", Multimeter::new)
                    .properties((p) -> p.stacksTo(1))
                    .register();

    public static void register() {
        Create.registrate().addToSection(MULTIMETER, AllSections.KINETICS);
    }
}
