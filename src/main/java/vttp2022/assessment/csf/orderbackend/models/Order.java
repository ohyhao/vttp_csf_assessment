package vttp2022.assessment.csf.orderbackend.models;

import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

// IMPORTANT: You can add to this class, but you cannot delete its original content

public class Order {

	private Integer orderId;
	private String name;
	private String email;
	private Integer size;
	private String sauce;
	private Boolean thickCrust;
	private List<String> toppings = new LinkedList<>();
	private String comments;

	public void setOrderId(Integer orderId) { this.orderId = orderId; }
	public Integer getOrderId() { return this.orderId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setEmail(String email) { this.email = email; }
	public String getEmail() { return this.email; }

	public void setSize(Integer size) { this.size = size; }
	public Integer getSize() { return this.size; }

	public void setSauce(String sauce) { this.sauce = sauce; }
	public String getSauce() { return this.sauce; }

	public void setThickCrust(Boolean thickCrust) { this.thickCrust = thickCrust; }
	public Boolean isThickCrust() { return this.thickCrust; }

	public void setToppings(List<String> toppings) { this.toppings = toppings; }
	public List<String> getToppings() { return this.toppings; }
	public void addTopping(String topping) { this.toppings.add(topping); }

	public void setComments(String comments) { this.comments = comments; }
	public String getComments() { return this.comments; }

	public static Order create(String json) {

		Order order = new Order();
		Boolean crust = false;
		JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();
		
		order.setName(data.getString("name"));
		order.setEmail(data.getString("email"));
		order.setSize(data.getInt("size"));
		order.setSauce(data.getString("sauce"));
		
		if (data.getString("base") == "thick") {
			crust = true;
		} else {
			crust = false;
		}
		order.setThickCrust(crust);
		JsonArray toppingsArr = data.getJsonArray("toppings");
		List<String> list = new LinkedList<>();
		for (int i = 0; i < toppingsArr.size(); i++ ) {
			list.add(toppingsArr.getString(i));
		}
		order.setToppings(list);
		order.setComments(data.getString("comments"));

		return order;
	}

	public static Order populate(SqlRowSet rs) {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
		o.setName(rs.getString("name"));
		o.setEmail(rs.getString("email"));
		o.setSize(rs.getInt("size"));
		o.setThickCrust(rs.getBoolean("thick_crust"));
		o.setSauce(rs.getString("sauce"));
		List<String> toppingsList = Arrays.asList(rs.getString("toppings"));
		o.setToppings(toppingsList);
		o.setComments(rs.getString("comments"));
        return o;
    }
}
