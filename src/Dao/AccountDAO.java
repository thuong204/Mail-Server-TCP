
package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import Helpers.connect;
import Models.Account;

public class AccountDAO {

    public AccountDAO() {
    }

    // Kiểm tra xem tài khoản đã tồn tại hay chưa
    public boolean checkExist(String username) {
        String sql = "SELECT Id, Username FROM Account WHERE Username = ?";
        try (
            Connection conn = connect.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra đăng nhập
    public Account checkLogin(String username, String password) {
        String sql = "SELECT Id, Username, Password FROM Account WHERE Username = ? AND Password = ?";
        try (
            Connection conn = connect.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    Account acc = new Account();
                    acc.setId(resultSet.getInt("Id"));
                    acc.setUsername(resultSet.getString("Username"));
                    acc.setPassword(resultSet.getString("Password"));
                    return acc;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Đăng ký tài khoản mới
    public void registerAccount(String username, String password) {
        String sql = "INSERT INTO Account (Username, Password) VALUES (?, ?)";
        try (
            Connection conn = connect.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách tất cả tài khoản
    public ArrayList<Account> getAll() {
        String sql = "SELECT * FROM Account";
        ArrayList<Account> accounts = new ArrayList<>();
        try (
            Connection conn = connect.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery()
        ) {
            while (resultSet.next()) {
                Account acc = new Account();
                acc.setId(resultSet.getInt("Id"));
                acc.setUsername(resultSet.getString("Username"));
                acc.setPassword(resultSet.getString("Password"));
                accounts.add(acc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    // Lấy tài khoản theo tên đăng nhập
    public Account getByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE Username = ?";
        try (
            Connection conn = connect.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    Account acc = new Account();
                    acc.setId(resultSet.getInt("Id"));
                    acc.setUsername(resultSet.getString("Username"));
                    acc.setPassword(resultSet.getString("Password"));
                    return acc;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tài khoản theo ID
    public Account getById(int id) {
        String sql = "SELECT * FROM Account WHERE Id = ?";
        try (
            Connection conn = connect.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    Account acc = new Account();
                    acc.setId(resultSet.getInt("Id"));
                    acc.setUsername(resultSet.getString("Username"));
                    acc.setPassword(resultSet.getString("Password"));
                    return acc;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
