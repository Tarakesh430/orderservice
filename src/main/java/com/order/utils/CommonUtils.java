package com.order.utils;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

@UtilityClass
public class CommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
    public<T> Optional<T> getOptional(Supplier<T> resolver){
        try{
            return Optional.ofNullable(resolver.get());
        }catch(Exception ex){
            logger.info("Exception Occurred in Resolver {}",ex.getMessage());
        }
        return Optional.empty();
    }

    public long getEpochTimeStamp(){
        return Instant.now().toEpochMilli();
    }
}

