package com.teammoeg.steampowered.oldcreatestuff;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.network.chat.Component;

public interface IGoggleInformation extends IHaveGoggleInformation {

    String spacing = "    ";


    Component componentSpacing = Component.literal(spacing);

}
