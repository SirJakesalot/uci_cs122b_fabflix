package moviedb_model;

import java.sql.*;
import java.util.ArrayList;

public class CartItem {
    private String movie_id;
    private String movie_title;
    private int quantity;

    public CartItem(String movie_id, String movie_title, int quantity) {
        this.movie_id = movie_id;
        this.movie_title = movie_title;
        this.quantity = quantity;
    }

    // CartItem get functions
    public String movie_id() { return movie_id; }
    public String movie_title() { return movie_title; }
    public int quantity() { return quantity; }

    // CartItem set functions
    public String movie_id(String new_movie_id) { movie_id = new_movie_id; return movie_id(); }
    public String movie_title(String new_movie_title) { movie_title = new_movie_title; return movie_title(); }
    public int quantity(int new_quantity) { quantity = new_quantity; return quantity(); }

    public int incrementQuantity() { return ++quantity; }
}
