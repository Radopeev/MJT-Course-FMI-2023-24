package bg.sofia.uni.fmi.mjt.football;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FootballPlayerAnalyzer {
    private static final int MINIMUM_OVERALL_DIFF = 3;
    private List<Player> players;
    public FootballPlayerAnalyzer(Reader reader) {
        try (var buffRead = new BufferedReader(reader)) {
            String first = buffRead.readLine();
            players = buffRead.lines().map(Player::of).toList();
        } catch (IOException e) {
            throw new UncheckedIOException("A problem occurred while reading from the file", e);
        }
    }

    public List<Player> getAllPlayers() {
        return List.copyOf(players);
    }

    public Set<String> getAllNationalities() {
        return players.stream().map(Player::nationality).collect(Collectors.toSet());
    }

    public Player getHighestPaidPlayerByNationality(String nationality) {
        if (nationality == null) {
            throw new IllegalArgumentException("Nationality cannot be null");
        }
        return players.stream()
            .filter(p -> p.nationality().equals(nationality))
            .max(Comparator.comparing(Player::wageEuro))
            .orElseThrow(() -> new NoSuchElementException("There is no player with the provided nationality"));
    }

    public Map<Position, Set<Player>> groupByPosition() {
        return players.stream()
            .flatMap(player -> player.positions().stream().map(position -> Map.entry(position, player)))
            .collect(Collectors.groupingBy(Map.Entry::getKey,
                Collectors.mapping(Map.Entry::getValue, Collectors.toSet())));
    }

    public Optional<Player> getTopProspectPlayerForPositionInBudget(Position position, long budget) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }

        return players.stream()
            .filter(player -> player.positions().contains(position))
            .filter(player -> player.valueEuro() <= budget)
            .max(Comparator.comparing(player -> (double)(player.overallRating() + player.potential()) / player.age()));
    }

    public Set<Player> getSimilarPlayers(Player player) {
        if ( player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        return players.stream()
            .filter(p -> p != player)
            .filter(p -> p.positions().stream().anyMatch(player.positions()::contains))
            .filter(p -> p.preferredFoot() == player.preferredFoot())
            .filter(p -> Math.abs(p.overallRating() - player.overallRating()) <= MINIMUM_OVERALL_DIFF)
            .collect(Collectors.toUnmodifiableSet());

    }

    public Set<Player> getPlayersByFullNameKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty() || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword cannot be null, blank or empty");
        }
        return players.stream()
            .filter(player -> player.fullName().contains(keyword))
            .collect(Collectors.toUnmodifiableSet());
    }
}