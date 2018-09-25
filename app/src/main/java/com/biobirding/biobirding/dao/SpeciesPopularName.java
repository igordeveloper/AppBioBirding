package com.biobirding.biobirding.dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;

import java.util.List;

public class SpeciesPopularName {
    @Embedded
    public Species species;

    @Relation(parentColumn = "id",
            entityColumn = "id")

    public List<PopularName> popularNames;
}