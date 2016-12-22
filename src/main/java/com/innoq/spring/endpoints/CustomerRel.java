package com.innoq.spring.endpoints;


import com.mercateo.common.rest.schemagen.link.relation.Relation;
import com.mercateo.common.rest.schemagen.link.relation.RelationContainer;

public enum CustomerRel implements RelationContainer {

    CUSTOMERS;

    @Override
    public Relation getRelation() {
        return Relation.of(name().toLowerCase());
    }
}
