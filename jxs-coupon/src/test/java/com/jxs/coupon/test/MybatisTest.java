package com.jxs.coupon.test;

import java.sql.*;

public class MybatisTest {

    public static void main(String[] args) {
        ResultSet resultSet = null;
        try {
            //加载mysql 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //创建一个连接
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gulimall_sms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai","root","root");

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from sms_coupon");

            System.out.println(resultSet);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

    }

}
