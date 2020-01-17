package sample.extended;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import jdk.jshell.PersistentSnippet;
import jdk.jshell.spi.SPIResolutionException;
import sample.model.*;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class DB_Manager {
    private static DB_Manager instance;

    public static DB_Manager getInstance() {
        if (instance == null) {
            instance = new DB_Manager();
        }
        return instance;
    }

    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/ims_database?currentSchema=ims_schema";
    private static final String USER = "postgres";
    private static final String PASS = "19990920";
    private Connection connection;
    private Statement statement;

    public DB_Manager() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            statement = connection.createStatement();



        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }
    }

    public int getLastProductID() {
        try {
            statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM product");
            ResultSet res = ps.executeQuery();
            int id = 0;
            while (res.next()) {
                id = res.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return 0;
        }
    }

    public int getLastPartID() {
        try {
            statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM part");
            ResultSet res = ps.executeQuery();
            int id = 0;
            while (res.next()) {
                id = res.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return 0;
        }
    }

    public ObservableList<Product> getProducts() {
        ObservableList<Product> p = FXCollections.observableArrayList();
        try {
            statement = connection.createStatement();
            statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM product");
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                Product prod;
                int id = res.getInt(1);
                String name = res.getString(2);
                int inv = res.getInt(3);
                double price = res.getDouble(4);
                int max = res.getInt(5);
                int min = res.getInt(6);
                prod = new Product(id, name, price, inv, max, min);
                if (getAssociatedParts(id).size() != 0 && getAssociatedParts(id) != null) {
                    prod.setAssociatedParts(getAssociatedParts(id));
                }
                p.add(prod);
            }
            return p;
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Part> getParts() {
        ObservableList<Part> p = FXCollections.observableArrayList();
        try {
            statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM part");
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                Part part;
                int id = res.getInt(1);
                String name = res.getString(2);
                int inv = res.getInt(3);
                double price = res.getDouble(4);
                int max = res.getInt(5);
                int min = res.getInt(6);
                if (res.getString(8) == null) {
                    int machineId = res.getInt(7);
                    part = new InHouse(id, name, price, inv, max, min, machineId);

                } else {
                    String comp_name = res.getString(8);
                    part = new Outsourced(id, name, price, inv, max, min, comp_name);
                }


                p.add(part);
            }
            return p;

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Part> getAssociatedParts(int id) {
        ObservableList<Part> asParts = FXCollections.observableArrayList();
        try {
            statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM product_parts WHERE prod_id = ?");
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                int id_p = res.getInt(1);
                PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM part WHERE id =?");
                ps2.setInt(1, id_p);
                ResultSet resForParts = ps2.executeQuery();
                while (resForParts.next()) {
                    Part p;
                    int id_part = resForParts.getInt(1);
                    String name = resForParts.getString(2);
                    int inv = resForParts.getInt(3);
                    double price = resForParts.getDouble(4);
                    int max = resForParts.getInt(5);
                    int min = resForParts.getInt(6);
                    if (resForParts.getString(8) == null) {
                        int machineId = resForParts.getInt(7);

                        p = new InHouse(id_part, name, price, inv, max, min, machineId);

                    } else {
                        String comp_name = resForParts.getString(8);
                        p = new Outsourced(id_part, name, price, inv, max, min, comp_name);
                    }
                    asParts.add(p);
                }
            }
            return asParts;
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return null;
        }
    }

    public void addPart(Part p) {
        try {
            statement = connection.createStatement();

            String sql_str = "INSERT INTO part VALUES(?,?,?,?,?,?,?,?)";
            //  String sql_val = p.getName() + "," + p.getStock() + "," + p.getPrice() + "," + p.getMax() + "," + p.getMin() + ",";
            PreparedStatement ps = connection.prepareStatement(sql_str);
            ps.setInt(1, p.getId());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getStock());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getMax());
            ps.setInt(6, p.getMin());

            if (p.getClass().equals(InHouse.class)) {
                InHouse i = (InHouse) p;
                //    sql_val += i.getMachineId();
                ps.setInt(7, i.getMachineId());
                ps.setString(8, null);
            } else {
                Outsourced o = (Outsourced) p;
                ps.setInt(7, 0);
                ps.setString(8, o.getCompanyName());
                //    sql_val += o.getCompanyName();

            }
            ps.execute();

            //   statement.execute(sql_str);
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    public void addProduct(Product p) {
        try {
            statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO product VALUES(?,?,?,?,?,?)");

            ps.setInt(1, p.getId());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getStock());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getMax());
            ps.setInt(6, p.getMin());
            ps.execute();

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

    }

    public void updatePart(Part p, int id_part) {
        try {

            PreparedStatement ps = connection.prepareStatement("UPDATE part SET name = ? , inv = ?, price = ?, max = ?, min = ?, machine_id= ?, comp_name=? WHERE id= ?");
            ps.setInt(8, p.getId());
            ps.setString(1, p.getName());
            ps.setInt(2, p.getStock());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getMax());
            ps.setInt(5, p.getMin());

            if (p.getClass().equals(InHouse.class)) {
                InHouse i = (InHouse) p;
                ps.setInt(6, i.getMachineId());
            } else {
                Outsourced o = (Outsourced) p;

                ps.setString(7, o.getCompanyName());
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void updateProduct(Product p, int id_prod) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE product SET name = ? , inv = ?, price = ?, max = ?, min = ?  WHERE id= ?");


            ps.setString(1, p.getName());
            ps.setInt(2, p.getStock());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getMax());
            ps.setInt(5, p.getMin());

            ps.setInt(6, id_prod);
            ps.execute();
            PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM product_parts");

            ResultSet res = ps2.executeQuery();
            ArrayList<Integer> idArr = new ArrayList<Integer>();
            while (res.next()) {
                if (res.getInt(2) == p.getId()) {//prod_id
                    idArr.add(res.getInt(3));//part_id
                }
            }

            for (int i = 0; i < p.getAllAssociatedParts().size(); i++) {
                boolean fl = false;
                for (int j = 0; j < p.getAllAssociatedParts().size(); j++) {
                    if (p.getAllAssociatedParts().get(i).getId() == idArr.get(j)) {
                        fl = true;
                    }
                }
                if (!fl) {
                    addAssociatedPart(p.getAllAssociatedParts().get(i), p.getId());
                }
            }

            for (int i = 0; i < idArr.size(); i++) {
                boolean fl = false;
                for (int j = 0; j < idArr.size(); j++) {
                    if (p.getAllAssociatedParts().get(j).getId() == idArr.get(i)) {
                        fl = true;
                    }
                }
                if (!fl) {
                    deleteAssociatedPart(p.getAllAssociatedParts().get(i).getId(), p.getId());
                }
            }

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    public void addAssociatedPart(Part p, int id) {
        try {

            PreparedStatement ps = connection.prepareStatement("INSERT INTO product_parts(prod_id,part_id) VALUES (?,?)");
            ps.setInt(1, id);
            ps.setInt(2, p.getId());
            ps.execute();
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    public void addAssociatedParts(ObservableList<Part> p, int id) {
        try {
            for (int i = 0; i < p.size(); i++) {

                PreparedStatement ps = connection.prepareStatement("INSERT INTO product_parts(prod_id,part_id) VALUES (?,?)");
                ps.setInt(1, id);
                ps.setInt(2, p.get(i).getId());
                ps.execute();
            }

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    public void deleteAssociatedPart(int id_part, int id_prod) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE  FROM product_parts WHERE prod_id = ? AND part_id = ?");
            ps.setInt(1, id_prod);
            ps.setInt(2, id_part);
            ps.execute();

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    public void deletePart(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE  FROM part WHERE id = ?");
            ps.setInt(1, id);
            ps.execute();

            PreparedStatement ps2 = connection.prepareStatement("DELETE  FROM product_parts WHERE part_id = ?");
            ps2.setInt(1, id);
            ps2.execute();

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    public void deleteProduct(int id) {
        try {

            PreparedStatement ps = connection.prepareStatement("DELETE  FROM product WHERE id = ?");
            ps.setInt(1, id);
            ps.execute();

            PreparedStatement ps2 = connection.prepareStatement("DELETE  FROM product_parts WHERE prod_id = ?");
            ps2.setInt(1, id);
            ps2.execute();


        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

}
