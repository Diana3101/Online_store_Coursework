package com.kapatsyn.coursework.dto;

import com.kapatsyn.coursework.entity.Thing;
import com.kapatsyn.coursework.form.ThingForm;
import com.kapatsyn.coursework.model.ThingInfo;
import com.kapatsyn.coursework.pagination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Date;

@Transactional
@Repository
public class ThingDTO {
    @Autowired
    private SessionFactory sessionFactory;

    public Thing findThing(String code) {
        try {
            String sql = "Select e from " + Thing.class.getName() + " e Where e.code =:code ";

            Session session = this.sessionFactory.getCurrentSession();
            Query<Thing> query = session.createQuery(sql, Thing.class);
            query.setParameter("code", code);
            return (Thing) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ThingInfo findThingInfo(String code) {
        Thing thing = this.findThing(code);
        if (thing == null) {
            return null;
        }
        return new ThingInfo(thing.getCode(), thing.getName(), thing.getPrice());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void save(ThingForm thingForm) {

        Session session = this.sessionFactory.getCurrentSession();
        String code = thingForm.getCode();

        Thing thing = null;

        boolean isNew = false;
        if (code != null) {
            thing = this.findThing(code);
        }
        if (thing == null) {
            isNew = true;
            thing = new Thing();
            thing.setCreateDate(new Date());
        }
        thing.setCode(code);
        thing.setName(thingForm.getName());
        thing.setPrice(thingForm.getPrice());

        if (thingForm.getFileData() != null) {
            byte[] image = null;
            try {
                image = thingForm.getFileData().getBytes();
            } catch (IOException e) {
            }
            if (image != null && image.length > 0) {
                thing.setImage(image);
            }
        }
        if (isNew) {
            session.persist(thing);
        }
        // If error in DB, Exceptions will be thrown out immediately
        session.flush();
    }

    public PaginationResult<ThingInfo> queryThings(int page, int maxResult, int maxNavigationPage,
                                                   String likeName) {
        String sql = "Select new " + ThingInfo.class.getName() //
                + "(p.code, p.name, p.price) " + " from "//
                + Thing.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        sql += " order by p.createDate desc ";
        //
        Session session = this.sessionFactory.getCurrentSession();
        Query<ThingInfo> query = session.createQuery(sql, ThingInfo.class);

        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<ThingInfo>(query, page, maxResult, maxNavigationPage);
    }

    public PaginationResult<ThingInfo> queryThings(int page, int maxResult, int maxNavigationPage) {
        return queryThings(page, maxResult, maxNavigationPage, null);
    }
}
