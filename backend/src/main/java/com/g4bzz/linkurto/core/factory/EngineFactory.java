package com.g4bzz.linkurto.core.factory;

import com.g4bzz.linkurto.core.engines.Engine;
import com.g4bzz.linkurto.core.engines.EngineType;
import com.g4bzz.linkurto.core.engines.HashingEngine;
import org.springframework.stereotype.Component;

@Component
public class EngineFactory {
    public Engine getEngine(EngineType type){
        return switch (type) {
            case HASHING -> new HashingEngine();
            default -> throw new IllegalArgumentException("Invalid engine type");
        };
    }
}
