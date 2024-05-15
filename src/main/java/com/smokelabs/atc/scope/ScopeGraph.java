package com.smokelabs.atc.scope;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScopeGraph {
    private static ScopeGraph instance;

    private List<ScopeGraphRelationship> relationships;

    /**
     * Constructor. Set up our scope graph.
     */
    private ScopeGraph() {
        // log.info("initializing the scope graph");
        log.info("scope graph has not been implemented yet, ignoring");
    }

    /**
     * Get the global singleton. If one is not initialize, begin doing so and set
     * it.
     * 
     * @return
     */
    public static ScopeGraph getInstance() {
        if (instance == null) {
            instance = new ScopeGraph();
        }
        return instance;
    }
}
