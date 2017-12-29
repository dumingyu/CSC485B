import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.print.DocFlavor.STRING;
 

public class chicagor {
 
    /**
     * Connect to the test.db database
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:/Users/MingyuD/crime.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
 
    double length,width,bucketlen,bucketwid,xdiff,ydiff,xboun,ybound;
    String sqlminX,sqlmaxX,sqlminY,sqlmaxY,sql,where;
    double largest,x,y;

    public void mapRange(){
        sqlminX = "select min(minX) from chicagor;";
        sqlmaxX = "select max(maxX) from chicagor;";
        sqlminY = "select min(minY) from chicagor;";
        sqlmaxY = "select max(maxY) from chicagor;";
        
       // String query = "select " + sql + " from chicagor";
        try (Connection conn = this.connect();
             //Statement stat  = conn.createStatement();
             //ResultSet rs  = stat.executeQuery(query);
             Statement min1  = conn.createStatement();
             ResultSet minX  = min1.executeQuery(sqlminX);
             Statement max1  = conn.createStatement();
             ResultSet maxX  = max1.executeQuery(sqlmaxX);
             Statement min2  = conn.createStatement();
             ResultSet minY  = min2.executeQuery(sqlminY);
             Statement max2  = conn.createStatement();
             ResultSet maxY  = max2.executeQuery(sqlmaxY)){
           
            xbound = minX.getDouble("min(minX)");
            ybound = minY.getDouble("min(minY)");
            length = maxX.getDouble("max(maxX)") - minX.getDouble("min(minX)");
            width = maxY.getDouble("max(maxY)") - minY.getDouble("min(minY)");
            bucketlen = length/100;
            bucketwid = width/100;
            xdiff = bucketlen/10;
            ydiff = bucketwid/10;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    for (int j = 0; j < 1000; j++){
    	for (int i = 0; i < 1000; i++){
            sql = "select count(*) from chicagor ";
            where = "where maxX <= " + (xbound + bucketlen + (i * xdiff)) + " and maxY <= " + (ybound + bucketwid + (j * ydiff))
                    + " and minX >= " + (xbound + (i * xdiff)) + " and minY >= " + (ybound + (j * ydiff));
        
           
            
            
            try (Connection conn2 = this.connect();
                 Statement bucket = conn2.createStatement();
                 ResultSet num = bucket.executeQuery(sql+where)){
         
                    if(num.getDouble("count(*)") > largest){
                        largest = num.getDouble("count(*)");
                        x = i;
                        y = j;
                    }
                    
            } catch (SQLException e2){
                    System.out.println(e2.getMessage());
            }

        }
    }
    System.out.println("bucket" + x + " " + y + "  has largest crime that is " + largest);
    }

    public static void main(String[] args) {
       chicagor map = new chicagor();
       map.mapRange();
       
    }
 
}