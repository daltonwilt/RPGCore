package com.outcast.rpgcore.storage;

import javax.annotation.Nonnull;
import java.io.Serializable;

public interface Identifiable<ID extends Serializable> {
    @Nonnull
    ID getId();
}
