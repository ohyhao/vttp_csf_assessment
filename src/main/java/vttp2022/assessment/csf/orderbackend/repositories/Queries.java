package vttp2022.assessment.csf.orderbackend.repositories;

public interface Queries {
    
    public static final String SQL_INSERT_ORDER =
        "insert into orders (name, email, pizza_size, thick_crust, sauce, toppings, comments) values (?, ?, ?, ?, ?, ?, ?)";

    public static final String SQL_GET_ORDERS_BY_EMAIL =
        "select * from orders where email = ?";

}
