package com.kapatsyn.coursework.dto;

import com.kapatsyn.coursework.entity.USer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserDTO {

    @Autowired
    private SessionFactory sessionFactory;

    public USer findUser(String userName) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.find(USer.class, userName);
    }

}
