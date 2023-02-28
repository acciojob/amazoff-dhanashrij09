package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Repository
public class OrderRepository {
    private HashMap<String, Order> orders;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, String> orderPartnerMap;
    private HashMap<String, List<String>> deliveryMap;


    public OrderRepository() {
        this.orders = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.orderPartnerMap = new HashMap<String, String>();
        this.deliveryMap = new HashMap<String, List<String>>();
    }

    public void addOrder(Order order) {
        orders.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        partnerMap.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if (orders.containsKey(orderId) && partnerMap.containsKey(partnerId))
            orderPartnerMap.put(orderId, partnerId);

        List<String> currentOrders = new ArrayList<>();
        if (deliveryMap.containsKey(partnerId)) {
            currentOrders = deliveryMap.get(partnerId);
        }
        currentOrders.add(orderId);
        deliveryMap.put(partnerId, currentOrders);

        //update the numbersOfOrders
        DeliveryPartner deliveryPartner = partnerMap.get(partnerId);
        deliveryPartner.setNumberOfOrders(currentOrders.size());
    }

    public Order getOrderById(String orderId) {
        return orders.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        return deliveryMap.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return deliveryMap.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> allOrder = new ArrayList<>();
        for (String order : orders.keySet()) {
            allOrder.add(order);
        }
        return allOrder;
    }

    public int getCountOfUnassignedOrders() {
        return orders.size() - partnerMap.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId) {
        int count = 0;
        List<String> totalOrders = deliveryMap.get(partnerId);

        for (String orderId : totalOrders) {
            int deliveryTime = orders.get(orderId).getDeliveryTime();
            if (deliveryTime > time) {
                count++;
            }
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        int maxTime = 0;
        List<String> totalOrders = deliveryMap.get(partnerId);
        for (String orderId : totalOrders) {
            int currentTime = orders.get(orderId).getDeliveryTime();
            maxTime = Math.max(maxTime, currentTime);
        }
        return maxTime;
    }

    public void deleteOrderById(String orderId) {
      orders.remove(orderId);
      String delId = orderPartnerMap.get(orderId);
      orderPartnerMap.remove(orderId);
      List<String> order = deliveryMap.get(delId);
      order.remove(orderId);

      partnerMap.get(delId).setNumberOfOrders(deliveryMap.get(delId).size());
    }

    public void deletePartnerById(String partnerId) {
        partnerMap.remove(partnerId);
        List<String> listOfOrders = deliveryMap.get(partnerId);
        deliveryMap.remove(partnerId);
        for (String orderId : listOfOrders) {
            orderPartnerMap.remove(orderId);
        }
    }
}


