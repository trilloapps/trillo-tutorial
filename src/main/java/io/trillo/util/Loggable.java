package io.trillo.util;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Loggable {
    default Logger log(){
        return LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    }
}