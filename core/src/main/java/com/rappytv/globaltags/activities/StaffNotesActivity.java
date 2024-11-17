package com.rappytv.globaltags.activities;

import com.rappytv.globaltags.api.GlobalTagAPI;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import java.util.UUID;

@AutoActivity
public class StaffNotesActivity extends SimpleActivity {

    private final GlobalTagAPI api;
    private final UUID uuid;
    private final String username;

    public StaffNotesActivity(GlobalTagAPI api, UUID uuid, String username) {
        this.api = api;
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        api.getApiHandler().getNotes(uuid, (response) -> {
            // TODO: Implement activity
        });
    }
}
