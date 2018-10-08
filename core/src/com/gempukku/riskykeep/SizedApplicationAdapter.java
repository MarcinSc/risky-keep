package com.gempukku.riskykeep;

import com.badlogic.gdx.ApplicationAdapter;

public abstract class SizedApplicationAdapter extends ApplicationAdapter {
    public abstract int getDesiredWidth();

    public abstract int getDesiredHeight();
}
