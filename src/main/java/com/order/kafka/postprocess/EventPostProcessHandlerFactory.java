package com.order.kafka.postprocess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EventPostProcessHandlerFactory {
    private final Map<String, BasePostProcess> handlers;

    @Autowired
    public EventPostProcessHandlerFactory(Map<String, BasePostProcess> handlers) {
        this.handlers = handlers;
    }

    public BasePostProcess getHandler(String handlerName) {
        return handlers.get(handlerName);
    }

    public Map<String, BasePostProcess> getAllHandlers() {
        return handlers;
    }
}
