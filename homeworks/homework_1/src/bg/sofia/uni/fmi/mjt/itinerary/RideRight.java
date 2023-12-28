package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.graph.Graph;
import bg.sofia.uni.fmi.mjt.itinerary.graph.Node;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.SequencedCollection;
import java.util.Set;

public class RideRight implements ItineraryPlanner {
    private final List<Journey> schedule;
    private final Graph graph;
    public RideRight(List<Journey> schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null");
        }
        graph = new Graph(schedule);
        this.schedule = schedule;
    }

    private void restartNodes() {
        for (Node node : graph.getNodes().values()) {
            node.setParentToNull();
        }
    }

    private void assertStartAndDestination(City start, City destination) throws CityNotKnownException {
        boolean isStartPresent = false;
        boolean isDestPresent = false;
        for (Journey journey : schedule) {
            if (journey.from().equals(start)) {
                isStartPresent = true;
            }
            if (journey.to().equals(destination)) {
                isDestPresent = true;
            }
            if (isStartPresent && isDestPresent) {
                return;
            }
        }
        throw new CityNotKnownException("Start and destination have to exist in the schedule");
    }

    private void backtracking(List<Journey> path, Node node, Set<Node> visitedNodes) {
        while (node.getParent() != null && !visitedNodes.contains(node)) {
            visitedNodes.add(node);
            for (Node.Edge edge : node.getParent().getNeighbors()) {
                if (edge.getNode().equals(node)) {
                    path.add(edge.getJourney());
                    break;
                }
            }
            node = node.getParent();
        }
    }

    private void updateNode(Node neighbour, Node currentNode, BigDecimal totalWeight, Node endNode) {
        neighbour.setParent(currentNode);
        neighbour.setG(totalWeight);
        BigDecimal newFValue = neighbour.getG().add(neighbour.calculateHeuristic(endNode));
        neighbour.setF(newFValue);
    }

    private void searchNeighbours(boolean allowTransfer, Node currentNode, City start,
                                  PriorityQueue<Node> openList, PriorityQueue<Node> closedList, Node endNode) {
        for (Node.Edge edge : currentNode.getNeighbors()) {
            Node neighbour = graph.getNodeInGraph(edge.getNode().getCity());
            BigDecimal totalWeight = currentNode.getG().add(edge.getWeight());

            if (allowTransfer || edge.getJourney().from().equals(start)) {
                if (!openList.contains(neighbour) && !closedList.contains(neighbour)) {
                    updateNode(neighbour, currentNode, totalWeight, endNode);
                    openList.add(graph.getNodeInGraph(neighbour.getCity()));
                } else if (totalWeight.compareTo(neighbour.getG()) < 0) {
                    updateNode(neighbour, currentNode, totalWeight, endNode);

                    Node neighborNode = graph.getNodeInGraph(neighbour.getCity());
                    if (closedList.contains(neighborNode)) {
                        closedList.remove(neighborNode);
                        openList.add(neighborNode);
                    }
                }
            }
        }
    }

    private void assertFoundPath(boolean isFound) throws NoPathToDestinationException {
        if (!isFound) {
            throw  new NoPathToDestinationException("There is no path to that destination");
        }
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
        throws CityNotKnownException, NoPathToDestinationException {
        assertStartAndDestination(start, destination);

        List<Journey> path = new ArrayList<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();
        PriorityQueue<Node> closedList = new PriorityQueue<>();
        Set<Node> visitedNodes = new HashSet<>();

        Node startNode = graph.getNodeInGraph(start);
        Node endNode = graph.getNodeInGraph(destination);
        boolean isFound = false;

        openList.add(startNode);
        while (!openList.isEmpty()) {
            Node currentNode = openList.peek();
            if (currentNode.getCity().equals(destination)) {
                isFound = true;
                backtracking(path, graph.getNodeInGraph(currentNode.getCity()), visitedNodes);
                break;
            }

            searchNeighbours(allowTransfer, currentNode, start, openList, closedList, endNode);

            openList.remove(currentNode);
            closedList.add(currentNode);
        }
        assertFoundPath(isFound);
        restartNodes();
        return path.reversed();
    }
}
