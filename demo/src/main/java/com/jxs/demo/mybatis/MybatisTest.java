package com.jxs.demo.mybatis;


import com.jxs.demo.mybatis.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

public class MybatisTest {
    public static <InputStream> void main(String[] args) {

        UserMapper userMapper = (UserMapper) Proxy.newProxyInstance(MybatisTest.class.getClassLoader(), new Class<?>[]{UserMapper.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                System.out.println(Arrays.asList(args));
                //获取注解
                Select annotation = method.getAnnotation(Select.class);
                if (annotation != null) {
                    String[] value = annotation.value();
                    System.out.println(Arrays.asList(value));
                }
                return null;
            }

            ;
        });
        userMapper.selectUserList(1,"bb");


    }



    interface UserMapper{
        @Select("select * from user where id = #{id} and user_name = #{userName}")
        List<User> selectUserList(Integer id,String userName);
    }
}
