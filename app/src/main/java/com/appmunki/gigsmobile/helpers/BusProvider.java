package com.appmunki.gigsmobile.helpers;

import com.squareup.otto.Bus;

/**
 * Created by radzell on 5/13/14.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
