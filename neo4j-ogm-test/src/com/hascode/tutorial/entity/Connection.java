package com.hascode.tutorial.entity;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = Connection.TYPE)
public class Connection {
    public static final String TYPE = "LEADS_TO";

    private Long id;

    @StartNode
    private TrainStation start;

    @EndNode
    private TrainStation end;

    private int distance;

    public Connection() {
    }

    public Connection(TrainStation start, TrainStation end, int distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainStation getStart() {
        return start;
    }

    public void setStart(TrainStation start) {
        this.start = start;
    }

    public TrainStation getEnd() {
        return end;
    }

    public void setEnd(TrainStation end) {
        this.end = end;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Connection other = (Connection) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
