package Pool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import Exception.CouponSystemException;

public class ConnectionPool {

    private Set<Connection> connections = new HashSet<Connection>(); // Set for connection managment
    public static final int MAX_Connections = 150; // max connection allows
    private String dbUrl = "jdbc:mysql://localhost:3306/CouponSystem?createDatabaseIfNotExist=true";
    private String dbUser = "root";
    private String dbPassword = "1234";
    private boolean Active;
    private static ConnectionPool instance = null;

    static {
		try {
			instance = new ConnectionPool();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 
    // for loop for max connections allowed
    ConnectionPool() throws SQLException{
        for (int i = 0; i < MAX_Connections; i++) {
            connections.add(DriverManager.getConnection(dbUrl, dbUser, dbPassword));
        }
        Active = true;
        System.out.println("ConnectionPool up");
    } 

    
    // if connection pull is null create new CP
	public static ConnectionPool getInstance() throws CouponSystemException {
		return instance;
	}


    // SYNC conntction, wait if there is no place for new connection
    // using iteraton to remove connection and return it
	public synchronized Connection getConnection() throws CouponSystemException {
		if (!Active) {
			throw new CouponSystemException("getConnection failed - connection pool not active");
		}
		while (this.connections.isEmpty()) {
			try {
				System.out.println("=====> " + Thread.currentThread().getName() + " is witing");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Iterator<Connection> it = this.connections.iterator();
		Connection con = it.next();
		it.remove();
		return con;
	}

    public synchronized void returnCOnnection(Connection con) {
		this.connections.add(con);
		notify();
	}
    
	public synchronized void closeAllConnections() throws CouponSystemException {
		this.Active = false;
		while (this.connections.size() < MAX_Connections) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Connection connection : connections) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponSystemException("closeAllConnections failure", e);
			}
		}
		System.out.println("connection pool down");
	}
	
    




        public static void main(String[] args) throws SQLException, CouponSystemException {
ConnectionPool cp = new ConnectionPool();
cp.getConnection();
ConnectionPool.getInstance();
System.out.println(ConnectionPool.getInstance());
System.out.println(cp);

ConnectionPool cp1 = new ConnectionPool();
ConnectionPool cp2 = new ConnectionPool();
ConnectionPool cp3 = new ConnectionPool();
ConnectionPool cp4 = new ConnectionPool();
ConnectionPool cp5 = new ConnectionPool();
cp1.getConnection();
cp2.getConnection();
cp3.getConnection();
cp4.getConnection();
cp5.getConnection();
cp.closeAllConnections();
}



}
