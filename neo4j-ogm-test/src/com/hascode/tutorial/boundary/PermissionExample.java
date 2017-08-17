package com.hascode.tutorial.boundary;

import java.util.Collections;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import com.hascode.tutorial.entity.Group;
import com.hascode.tutorial.entity.Permission;
import com.hascode.tutorial.entity.Role;
import com.hascode.tutorial.entity.User;
import java.io.IOException;
import java.util.Properties;
import org.neo4j.ogm.config.Configuration;
import us.pserver.dyna.ResourceLoader;

public class PermissionExample {
    public static void main(String[] args) throws IOException {

        //SessionFactory sessionFactory = new SessionFactory("com.hascode");
        Configuration conf = new Configuration();
        Properties props = new Properties();
        props.load(ResourceLoader.caller().loadStream("/resources/ogm.properties"));
        conf.driverConfiguration()
            .setDriverClassName(props.getProperty("driver"))
            .setURI(props.getProperty("uri"))
            .setCredentials(
                props.getProperty("username"), 
                props.getProperty("password")
            );
        SessionFactory sessionFactory = new SessionFactory(conf, "com.hascode");
        Session session = sessionFactory.openSession();

        Permission canRead = new Permission("permission-read");
        Permission canWrite = new Permission("permission-write");
        Permission canDelete = new Permission("permission-delete");

        Role adminRole = new Role("administrator");
        Role editorRole = new Role("editor");
        Role guestRole = new Role("guest");

        Group group1 = new Group("group1");
        Group group2 = new Group("group2");
        Group group3 = new Group("group3");

        User fred = new User("Fred");
        User sally = new User("Sally");
        User lisa = new User("Lisa");

        adminRole.addPermission(canRead);
        adminRole.addPermission(canWrite);
        adminRole.addPermission(canDelete);

        editorRole.addPermission(canRead);
        editorRole.addPermission(canWrite);

        guestRole.addPermission(canRead);

        group1.addPermission(canRead);
        group1.addPermission(canWrite);
        group2.addPermission(canRead);
        group3.addPermission(canRead);

        group1.addRole(editorRole);
        group2.addRole(adminRole);

        fred.addToGroup(group1);
        sally.addToGroup(group2);
        lisa.addToGroup(group3);

        session.save(fred);
        session.save(sally);
        session.save(lisa);

        fetchPermissionFor(session, "Fred");
        fetchPermissionFor(session, "Sally");
        fetchPermissionFor(session, "Lisa");

    }

    private static void fetchPermissionFor(final Session session, String name) {
        System.out.printf("fetching permissions for %s: \n", name);
        String cypherQuery = String.format("MATCH (u:User{name:'%s'})-[*]->(p:Permission) RETURN p", name);
        session.query(Permission.class, cypherQuery, Collections.emptyMap()).forEach(System.out::println);
    }

}
