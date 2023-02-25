package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Repository
public class OrderRepository {
    private HashMap<String,Order> orders;
    private HashMap<String,DeliveryPartner> partnerMap;
    private HashMap<String,String> orderPartnerMap;
    private HashMap<String, DeliveryPartner> deliveryPartners ;


    public OrderRepository() {
        this.orders = new HashMap<String,Order>();
        this.partnerMap = new HashMap<>();
        this.orderPartnerMap = new HashMap<>();
        this.deliveryPartners = new HashMap<>();
    }
    public void addOrder(Order order) {
        orders.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        partnerMap.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId,String partnerId){


    }
    public Order getOrderById(String orderId) {
        return orders.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        DeliveryPartner deliveryPartner = deliveryPartner.get(partnerId);
        return (deliveryPartner != null) ? deliveryPartner.getNumberOfOrders() : 0;
    }

    public List<Order> getOrdersByPartnerId(String partnerId) {
        return orders.values().stream()
                .filter(order -> order.getDeliveryPartner() != null && order.getDeliveryPartner().getPartnerId().equals(partnerId))
                .collect(Collectors.toList());
    }

    public List<Order> getAllOrders() {
        return orders.values().stream().collect(Collectors.toList());
    }

    public int getCountOfUnassignedOrders() {
        return (int) orders.values().stream().filter(order -> order.getDeliveryTime() == null).count();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        return (int) orders.values().stream()
                .filter(order -> order.getDeliveryPartner() != null && order.getDeliveryPartner().getPartnerId().equals(partnerId)
                        && order.getDeliveryTime().compareTo(time) < 0)
                .count();
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        return orders.values().stream()
                .filter(order -> order.getDeliveryPartner() != null && order.getDeliveryPartner().getPartnerId().equals(partnerId))
                .map(Order::getDeliveryTime)
                .max(String::compareTo)
                .orElse(null);
    }

    public void deleteOrderById(String orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            DeliveryPartner deliveryPartner = order.getDeliveryPartner();
            if (deliveryPartner != null) {
                deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders() - 1);
            }
            orders.remove(orderId);
        }
    }

    public void deletePartnerById(String partnerId) {
        deliveryPartners.remove(partnerId);
        orders.values().stream()
                .filter(order -> order.getDeliveryPartner() != null && order.getDeliveryPartner().getPartnerId().equals(partnerId))
                .forEach(order -> order.setDeliveryPartner(null));
    }
}


