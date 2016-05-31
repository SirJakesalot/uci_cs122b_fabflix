package moviedb_model;

public class Sale {
    // May need to may the id an int
    private String id;
    private String customer_id;
    private String movie_id;
    private String sale_date;

    public Sale(String id, String customer_id, String movie_id, String sale_date) {
        this.id = id;
        this.customer_id = customer_id;
        this.movie_id = movie_id;
        this.sale_date = sale_date;
    }

    // Sale get functions
    public String id() { return id; }
    public String customer_id() { return customer_id; }
    public String movie_id() { return movie_id; }
    public String sale_date() { return sale_date; }

    // Sale set functions
    public String id(String new_id) { id = new_id; return id(); }
    public String customer_id(String new_customer_id) { customer_id = new_customer_id; return customer_id(); }
    public String movie_id(String new_movie_id) { movie_id = new_movie_id; return movie_id(); }
    public String sale_date(String new_sale_date) { sale_date = new_sale_date; return sale_date(); }

/*    
    public static void main(String[] args) {
        Sale s = new Sale("1", "5", "37", "12/24/08");
        System.out.println(s.customer_id());
        s.customer_id("99");
        System.out.println(s.customer_id());
        System.out.println(s.getTableEntry());
    }
*/
}
