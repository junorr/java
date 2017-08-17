package com.hascode.tutorial.boundary;

import java.util.Collections;

import org.neo4j.graphdb.Path;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;

import com.hascode.tutorial.entity.TrainStation;

public class TrainTimetableExample {
    public static void main(String[] args) {

        SessionFactory sessionFactory = new SessionFactory("com.hascode");
        final Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        TrainStation london = new TrainStation("London");
        TrainStation brighton = new TrainStation("Brighton");
        TrainStation portsmouth = new TrainStation("Portsmouth");
        TrainStation bristol = new TrainStation("Bristol");
        TrainStation oxford = new TrainStation("Oxford");
        TrainStation gloucester = new TrainStation("Gloucester");
        TrainStation northampton = new TrainStation("Northampton");
        TrainStation southampton = new TrainStation("Southampton");

        london.addConnection(brighton, 52);
        brighton.addConnection(portsmouth, 49);
        portsmouth.addConnection(southampton, 20);
        london.addConnection(oxford, 95);
        oxford.addConnection(southampton, 66);
        oxford.addConnection(northampton, 45);
        northampton.addConnection(bristol, 114);
        southampton.addConnection(bristol, 77);
        northampton.addConnection(gloucester, 106);
        gloucester.addConnection(bristol, 35);

        session.save(london);
        session.save(brighton);
        session.save(portsmouth);
        session.save(bristol);
        session.save(oxford);
        session.save(gloucester);
        session.save(northampton);
        session.save(southampton);

        System.out.println(session.countEntitiesOfType(TrainStation.class) + " stations saved");
        getRoute("London", "Bristol", session);
        getRoute("London", "Southampton", session);

        tx.close();
    }

    private static void getRoute(final String from, final String destination, final Session session) {
        System.out.printf("searching for the shortest route from %s to %s..\n", from, destination);

        String cypherQuery = String.format(
                "MATCH (from:TrainStation {name:'%s'}), (to:TrainStation {name:'%s'}), paths=allShortestPaths((from)-[:LEADS_TO*]->(to)) WITH REDUCE(dist = 0, rel in rels(paths) | dist + rel.distance) AS distance, paths RETURN paths, distance ORDER BY distance LIMIT 1",
                from, destination);
        final Result result = session.query(cypherQuery, Collections.emptyMap());
        System.out.printf("shortest way from %s to %s via\n", from, destination);
        result.queryResults().forEach(entry -> {
            Long distance = (Long) entry.get("distance");
            Path path = (Path) entry.get("paths");
            System.out.printf("distance: %s\n", distance);
            path.nodes().forEach(node -> {
                System.out.printf("- %s\n", node.getProperty("name"));
            });
        });
    }
}
