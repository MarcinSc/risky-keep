package com.gempukku.riskykeep.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gempukku.riskykeep.RiskyKeepGameTrackerRenderer;
import com.gempukku.riskykeep.RiskyKeepIntroRenderer;
import com.gempukku.riskykeep.SizedApplicationAdapter;

public class DesktopLauncher {
    public static void main(String[] arg) {
        SizedApplicationAdapter renderer = new RiskyKeepGameTrackerRenderer();
//        SizedApplicationAdapter renderer = new RiskyKeepIntroRenderer();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = renderer.getDesiredWidth();
        config.height = renderer.getDesiredHeight();
        config.resizable = false;
        new LwjglApplication(renderer, config);
    }
}
