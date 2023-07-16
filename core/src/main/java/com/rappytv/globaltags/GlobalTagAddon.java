package com.rappytv.globaltags;

import com.rappytv.globaltags.config.GlobalTagConfig;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class GlobalTagAddon extends LabyAddon<GlobalTagConfig> {

    @Override
    protected void enable() {
        registerSettingCategory();
    }

    @Override
    protected Class<? extends GlobalTagConfig> configurationClass() {
        return GlobalTagConfig.class;
    }
}
