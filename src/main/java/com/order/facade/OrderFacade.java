package com.order.facade;

import com.order.request.OrderContext;

public interface OrderFacade {
    default void process(OrderContext context){
        validateOrder(context);
        makeDesicion(context);
        populateSecurityKeys(context);
        processOrder(context);
        updateOrderStatus(context);
        publishNextOrder(context);
    }

    void populateSecurityKeys(OrderContext context);


    void publishNextOrder(OrderContext context);

    void updateOrderStatus(OrderContext context);

    void processOrder(OrderContext context);

    void makeDesicion(OrderContext context);

    void validateOrder(OrderContext context);
}
