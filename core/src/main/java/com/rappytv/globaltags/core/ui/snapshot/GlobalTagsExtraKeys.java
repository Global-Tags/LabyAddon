package com.rappytv.globaltags.core.ui.snapshot;

import net.labymod.api.laby3d.renderer.snapshot.ExtraKey;

public class GlobalTagsExtraKeys {

    public static final ExtraKey<GlobalTagsUserSnapshot> GLOBALTAGS_USER = ExtraKey.of(
        "globaltags_user",
        GlobalTagsUserSnapshot.class
    );

}
