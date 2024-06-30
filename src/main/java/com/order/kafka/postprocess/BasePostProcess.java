package com.order.kafka.postprocess;

public interface BasePostProcess<T>  {
    void postEventProcess(T event);

}
