package vttp2022.assessment.csf.orderbackend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;

import static vttp2022.assessment.csf.orderbackend.repositories.Queries.*;

import java.util.LinkedList;
import java.util.List;

@Repository
public class OrderRepository {
    
    @Autowired
    private JdbcTemplate template;

    public int addOrder(Order order) {
        List<String> list = order.getToppings();
       int count = template.update(SQL_INSERT_ORDER, order.getName(), order.getEmail(), order.getSize(),
        order.isThickCrust(), order.getSauce(), String.join(", ", list), order.getComments());
       return count;
    }

    public List<OrderSummary> getOrders(String email) {

        List<OrderSummary> summaries = new LinkedList<>();
        
        SqlRowSet rs = template.queryForRowSet(SQL_GET_ORDERS_BY_EMAIL, email);
        while (rs.next()) {
            OrderSummary summary = OrderSummary.populate(rs);
            summaries.add(summary);
        }
        return summaries;
    }

}
