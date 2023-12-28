package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;
import bg.sofia.uni.fmi.mjt.itinerary.Location;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Node implements Comparable<Node> {
    private Node parent = null;
    private List<Edge> neighbours;
    private BigDecimal f = new BigDecimal(Double.MAX_VALUE);

    private BigDecimal g = new BigDecimal(Double.MAX_VALUE);
    private City city;

    Node(City city) {
        if (city == null) {
            throw new IllegalArgumentException("City cannot be null");
        }

        this.city = city;
        this.neighbours = new ArrayList<>();
    }

    public void addBranch(BigDecimal exactPrice, Node node, Journey journey) {
        if (exactPrice == null) {
            throw new IllegalArgumentException("Exact price was not null");
        }

        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        if (journey == null) {
            throw new IllegalArgumentException("Journey cannot be null");
        }

        Edge newEdge = new Edge(exactPrice, node, journey);
        neighbours.add(newEdge);
    }

    public BigDecimal calculateHeuristic(Node target) {
        return Location.getManhattanDistanceBetweenTwoCities(city.location(), target.city.location())
            .multiply(Journey.TAX_PER_KILOMETER);
    }

    public BigDecimal getG() {
        return g;
    }

    public City getCity() {
        return city;
    }

    public List<Edge> getNeighbors() {
        return neighbours;
    }

    public void setParent(Node parent) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent cannot be null");
        }
        this.parent = parent;
    }

    public void setParentToNull() {
        parent = null;
    }

    public void setF(BigDecimal f) {
        if (f == null) {
            throw new IllegalArgumentException("F cannot be null");
        }

        this.f = f;
    }

    public void setG(BigDecimal g) {
        if (g == null) {
            throw new IllegalArgumentException("G cannot be null");
        }
        this.g = g;
    }

    public Node getParent() {
        return parent;
    }

    public static class Edge implements Comparable<Edge> {
        Edge(BigDecimal weight, Node node, Journey journey) {
            if (weight == null) {
                throw new IllegalArgumentException("Weight cannot be null");
            }

            if (node == null) {
                throw new IllegalArgumentException("Node cannot be null");
            }
            if (journey == null) {
                throw new IllegalArgumentException("Journey cannot be null");
            }
            this.weight = weight;
            this.node = node;
            this.journey = journey;
        }

        private Journey journey;
        private BigDecimal weight;
        private Node node;

        public Journey getJourney() {
            return journey;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public Node getNode() {
            return node;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Edge otherEdge)) {
                return false;
            }
            return Objects.equals(weight, otherEdge.weight) &&
                Objects.equals(node, otherEdge.node) &&
                Objects.equals(journey, otherEdge.journey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(weight, node, journey);
        }

        @Override
        public int compareTo(Edge o) {
            return weight.compareTo(o.weight);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Node node)) {
            return false;
        }

        return city.equals(node.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city);
    }

    @Override
    public int compareTo(Node n) {
        if (f.compareTo(n.f) == 0) {
            return city.name().compareTo(n.city.name());
        }
        return f.compareTo(n.f);
    }
}