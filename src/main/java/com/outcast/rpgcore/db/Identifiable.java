package com.outcast.rpgcore.db;

import javax.annotation.Nonnull;
import java.io.Serializable;

public interface Identifiable<ID extends Serializable> {
    @Nonnull
    ID getId();
}
