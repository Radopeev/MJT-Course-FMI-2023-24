package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;
import bg.sofia.uni.fmi.mjt.itinerary.comparator.JourneyComparator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private final Map<City, Node> nodes;

    private void assertEdges(List<Journey> edges) {
        if (edges == null) {
            throw new IllegalArgumentException("List of edges cannot be null");
        }
    }

    public Graph(List<Journey> edges) {
        assertEdges(edges);

        List<Journey> copyOfEdges = new ArrayList<>(edges);
        copyOfEdges.sort(new JourneyComparator());
        nodes = new LinkedHashMap<>();

        initializeAllCitiesInMap(copyOfEdges);
        constructGraph(copyOfEdges);
    }

    private void initializeAllCitiesInMap(List<Journey> edges) {
        for (Journey curr : edges) {
            nodes.put(curr.from(), new Node(curr.from()));
            nodes.put(curr.to(), new Node(curr.to()));
        }
    }

    private void constructGraph(List<Journey> edges) {
        for (int i = 0; i < edges.size(); i++) {
            City startCity = edges.get(i).from();
            Node startNode = new Node(startCity);
            Journey currEdge = null;
            if (i != edges.size() - 1) {
                while (startCity.equals(edges.get(i + 1).from())) {
                    currEdge = edges.get(i);
                    Node child = new Node(currEdge.to());
                    startNode.addBranch(currEdge.getPriceWithTaxes(), child, currEdge);
                    i++;
                    if (i == edges.size() - 1) {
                        break;
                    }
                }
            }
            currEdge = edges.get(i);
            startNode.addBranch(currEdge.getPriceWithTaxes(), new Node(currEdge.to()), currEdge);
            nodes.put(startCity, startNode);
        }
    }

    public Node getNodeInGraph(City city) {
        if (city == null) {
            throw new IllegalArgumentException("City cannot be null");
        }
        return nodes.get(city);
    }

    public Map<City, Node> getNodes() {
        return nodes;
    }
}
