package Pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;
import Exception.CouponSystemException;
import Exception.ExceptionMessage;

public class ConnectionPool {
	public static final int NUMBER_OF_CONNECTIONS = 10;
	private static ConnectionPool instance = null;
	private final Stack<Connection> connections = new Stack<>();
	private String dbUrl = "jdbc:mysql://localhost:3306/CouponSystem?createDatabaseIfNotExist=true";
	private String dbUser = "root";
	private String dbPassword = "1234";

	private ConnectionPool() throws SQLException {
		openAllConnections();
	}

	private void openAllConnections() throws SQLException {
		for (int counter = 0; counter < NUMBER_OF_CONNECTIONS; counter++) {
			Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			connections.push(connection);
		}
	}

	public void closeAllConnections() throws CouponSystemException {
		synchronized (connections) {
			while (connections.size() < NUMBER_OF_CONNECTIONS) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				connections.removeAllElements();
			} catch (Exception e) {
				System.out.println(ExceptionMessage.GENERAL_ERROR.getMessage());
				;
			}
		}
		System.out.println("All connections closed.");
	}

	public static ConnectionPool getInstance() {
		if (instance == null) {
			synchronized (ConnectionPool.class) {
				if (instance == null) {
					try {
						instance = new ConnectionPool();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		return instance;
	}

	public Connection getConnection() {
		synchronized (connections) {
			if (connections.isEmpty()) {
				try {
					System.out.println("=====> " + Thread.currentThread().getName() + " is waiting");
					System.out.println(connections.size());
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return connections.pop();

		}
	}

	public void returnConnection(Connection connection) {
		synchronized (connections) {
			connections.push(connection);
			connections.notify();
		}
	}

}