package vttp2022.assessment.csf.orderbackend.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonObject;
import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.models.Response;
import vttp2022.assessment.csf.orderbackend.services.OrderService;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderRestController {

    private Logger logger = Logger.getLogger(OrderRestController.class.getName());

    @Autowired
    private OrderService orderSvc;

    @PostMapping(path="/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addOrder(@RequestBody String payload) {

        logger.info("payload: %s".formatted(payload));

        Order order;
        Response resp;

        try {
            order = Order.create(payload);
            System.out.printf("order: %s".formatted(order));
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage(ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp.toJson().toString());
        }
        
        try {
            orderSvc.createOrder(order);
            resp = new Response();
            resp.setCode(201);
            resp.setMessage(order.getName());
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(resp.toJson().toString());
        } catch (Exception ex) {
            resp = new Response();
            resp.setCode(400);
            resp.setMessage("Request to add order has failed");
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(resp.toJson().toString());
        }   
    }
        
    @GetMapping(path="order/{email}/all")
    public ResponseEntity<String> getOrders(@PathVariable String email) {

        Response resp;

        List<OrderSummary> summaries = orderSvc.getOrdersByEmail(email);
        List<JsonObject> jsonObjList = new LinkedList<>();
        for (OrderSummary summary: summaries) {
            jsonObjList.add(OrderSummary.toJson(summary));
        }

        logger.info("List: %s".formatted(summaries.toString()));

        if (summaries.isEmpty()) {
            resp = new Response();
                resp.setCode(400);
                resp.setMessage("List is empty!");
                resp.setData(jsonObjList.toString());
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(jsonObjList.toString());
        } else {
            resp = new Response();
                resp.setCode(200);
                resp.setMessage("List successfully retrieved");
                resp.setData(summaries.toString());
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(jsonObjList.toString());
            }
    }
}
