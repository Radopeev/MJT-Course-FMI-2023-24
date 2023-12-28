package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FootballPlayerAnalyzerTest {

    private FootballPlayerAnalyzer analyzerForTesting;

    @BeforeEach
    void setUp(){
        BufferedReader bufferedReader = new BufferedReader( new StringReader("""
            name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot
            L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left
            C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right
            P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right
            L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right
            K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right
            V. van Dijk;Virgil van Dijk;7/8/1991;27;193.04;92.1;CB;Netherlands;88;90;59500000;215000;Right
            K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right
            S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right
            M. Neuer;Manuel Neuer;3/27/1986;32;193.04;92.1;GK;Germany;89;89;38000000;130000;Right
            E. Cavani;Edinson Roberto Cavani Gómez;2/14/1987;32;185.42;77.1;ST;Uruguay;89;89;60000000;200000;Right
            Sergio Busquets;Sergio Busquets i Burgos;7/16/1988;30;187.96;76.2;CDM,CM;Spain;89;89;51500000;315000;Right
            T. Courtois;Thibaut Courtois;5/11/1992;26;198.12;96.2;GK;Belgium;89;90;53500000;240000;Left
            M. ter Stegen;Marc-André ter Stegen;4/30/1992;26;187.96;84.8;GK;Germany;89;92;58000000;240000;Right
            Kepa;Kepa Arrizabalaga;10/3/1994;24;185.42;84.8;GK;Spain;84;90;31000000;92000;Right
            Pau López;Pau López Sabata;12/13/1994;24;187.96;77.1;GK;Spain;82;87;21500000;21000;Left"""));

        analyzerForTesting = new FootballPlayerAnalyzer(bufferedReader);
    }

    @Test
    void testGetAllPlayer() throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new StringReader("""
            name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot
            L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left
            C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right
            P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right
            L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right
            K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right
            V. van Dijk;Virgil van Dijk;7/8/1991;27;193.04;92.1;CB;Netherlands;88;90;59500000;215000;Right
            K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right
            S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right
            M. Neuer;Manuel Neuer;3/27/1986;32;193.04;92.1;GK;Germany;89;89;38000000;130000;Right
            E. Cavani;Edinson Roberto Cavani Gómez;2/14/1987;32;185.42;77.1;ST;Uruguay;89;89;60000000;200000;Right
            Sergio Busquets;Sergio Busquets i Burgos;7/16/1988;30;187.96;76.2;CDM,CM;Spain;89;89;51500000;315000;Right
            T. Courtois;Thibaut Courtois;5/11/1992;26;198.12;96.2;GK;Belgium;89;90;53500000;240000;Left
            M. ter Stegen;Marc-André ter Stegen;4/30/1992;26;187.96;84.8;GK;Germany;89;92;58000000;240000;Right
            Kepa;Kepa Arrizabalaga;10/3/1994;24;185.42;84.8;GK;Spain;84;90;31000000;92000;Right
            Pau López;Pau López Sabata;12/13/1994;24;187.96;77.1;GK;Spain;82;87;21500000;21000;Left"""));
        List<Player> listForTesting = new ArrayList<>();

        String first = String.valueOf(bufferedReader.readLine());
        listForTesting = bufferedReader.lines().map(Player::of).toList();

        assertIterableEquals(listForTesting,analyzerForTesting.getAllPlayers(),
            "Expected list and actual list do not match");
    }

    @Test
    void testGroupByPosition(){
        Map<Position, Set<Player>> mapForTest = new HashMap<>();
        Set<Player> expectedGKs = new HashSet<>(
            List.of(Player.of("M. Neuer;Manuel Neuer;3/27/1986;32;193.04;92.1;GK;Germany;89;89;38000000;130000;Right"),
                Player.of(
                    "T. Courtois;Thibaut Courtois;5/11/1992;26;198.12;96.2;GK;Belgium;89;90;53500000;240000;Left"),
                Player.of(
                    "M. ter Stegen;Marc-André ter Stegen;4/30/1992;26;187.96;84.8;GK;Germany;89;92;58000000;240000;Right"),
                Player.of("Kepa;Kepa Arrizabalaga;10/3/1994;24;185.42;84.8;GK;Spain;84;90;31000000;92000;Right"),
                Player.of("Pau López;Pau López Sabata;12/13/1994;24;187.96;77.1;GK;Spain;82;87;21500000;21000;Left")));
        Set<Player> expectedCBs = new HashSet<>(List.of(
            Player.of("K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right"),
            Player.of("V. van Dijk;Virgil van Dijk;7/8/1991;27;193.04;92.1;CB;Netherlands;88;90;59500000;215000;Right")
        ));
        Set<Player> expectedCAMs= new HashSet<>(List.of(
            Player.of("C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right"),
            Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right")
        ));
        Set<Player> expectedCDMs= new HashSet<>(List.of(
            Player.of("Sergio Busquets;Sergio Busquets i Burgos;7/16/1988;30;187.96;76.2;CDM,CM;Spain;89;89;51500000;315000;Right")
        ));
        Set<Player> expectedLWs = new HashSet<>(List.of(
            Player.of("L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right")
        ));
        Set<Player> expectedCMs = new HashSet<>(List.of(
            Player.of("C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right"),
            Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right"),
            Player.of("Sergio Busquets;Sergio Busquets i Burgos;7/16/1988;30;187.96;76.2;CDM,CM;Spain;89;89;51500000;315000;Right")
        ));
        Set<Player> expectedRWs = new HashSet<>(List.of(
            Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left"),
            Player.of("K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right")
        ));
        Set<Player> expectedRMs = new HashSet<>(List.of(
            Player.of("C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right"),
            Player.of("K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right")
        ));
        Set<Player> expectedCFs = new HashSet<>(List.of(
            Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left")
        ));
        Set<Player> expectedSTs = new HashSet<>(List.of(
            Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left"),
            Player.of("L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right"),
            Player.of("K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right"),
            Player.of("S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right"),
            Player.of("E. Cavani;Edinson Roberto Cavani Gómez;2/14/1987;32;185.42;77.1;ST;Uruguay;89;89;60000000;200000;Right")
        ));
        mapForTest.put(Position.GK,expectedGKs);
        mapForTest.put(Position.CB,expectedCBs);
        mapForTest.put(Position.CAM,expectedCAMs);
        mapForTest.put(Position.CDM,expectedCDMs);
        mapForTest.put(Position.LW,expectedLWs);
        mapForTest.put(Position.CM,expectedCMs);
        mapForTest.put(Position.RW,expectedRWs);
        mapForTest.put(Position.RM,expectedRMs);
        mapForTest.put(Position.CF,expectedCFs);
        mapForTest.put(Position.ST,expectedSTs);

        assertEquals(mapForTest,analyzerForTesting.groupByPosition(),
            "Expected map and actual map do not match");
    }

    @Test
    void testGetAllNationalities() throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new StringReader("""
            name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot
            L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left
            C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right
            P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right
            L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right
            K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right
            V. van Dijk;Virgil van Dijk;7/8/1991;27;193.04;92.1;CB;Netherlands;88;90;59500000;215000;Right
            K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right
            S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right
            M. Neuer;Manuel Neuer;3/27/1986;32;193.04;92.1;GK;Germany;89;89;38000000;130000;Right
            E. Cavani;Edinson Roberto Cavani Gómez;2/14/1987;32;185.42;77.1;ST;Uruguay;89;89;60000000;200000;Right
            Sergio Busquets;Sergio Busquets i Burgos;7/16/1988;30;187.96;76.2;CDM,CM;Spain;89;89;51500000;315000;Right
            T. Courtois;Thibaut Courtois;5/11/1992;26;198.12;96.2;GK;Belgium;89;90;53500000;240000;Left
            M. ter Stegen;Marc-André ter Stegen;4/30/1992;26;187.96;84.8;GK;Germany;89;92;58000000;240000;Right
            Kepa;Kepa Arrizabalaga;10/3/1994;24;185.42;84.8;GK;Spain;84;90;31000000;92000;Right
            Pau López;Pau López Sabata;12/13/1994;24;187.96;77.1;GK;Spain;82;87;21500000;21000;Left"""));
        List<String> listForTesting = new ArrayList<>();

        String first = String.valueOf(bufferedReader.readLine());
        listForTesting = bufferedReader.lines().map(Player::of).toList().stream().map(Player::nationality).toList();

        assertTrue(listForTesting.containsAll(analyzerForTesting.getAllNationalities())
            && analyzerForTesting.getAllNationalities().containsAll(listForTesting),
            "Expected list and actual list do not have the same elements");
    }

    @Test
    void testGetHighestPaidPlayerByNationality(){
        Player playerForTest = Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left");

        assertEquals(playerForTest, analyzerForTesting.getHighestPaidPlayerByNationality("Argentina"),
            "Expected player and actual do not match");
    }

    @Test
    void testGetProspectForPositionInBudgetWhenPositionIsNull(){
        assertThrows(IllegalArgumentException.class, () -> analyzerForTesting.getTopProspectPlayerForPositionInBudget(null,1),
            "Expected exception when position is null");
    }

    @Test
    void testGetProspectForPositionInBudgetWhenBudgetIsNegative(){
        assertThrows(IllegalArgumentException.class, () -> analyzerForTesting.getTopProspectPlayerForPositionInBudget(Position.CB,-1),
            "Expected exception when budget is negative");
    }

    @Test
    void testGetProspectForPositionInBudget(){
        Optional<Player> playerForTesting = Optional.of(
            Player.of("K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right"));

        assertEquals(playerForTesting, analyzerForTesting.getTopProspectPlayerForPositionInBudget(Position.RW,100000000),
            "Expected player does not match actual player");
    }

    @Test
    void testGetHighestPaidPlayerByNationalityWhenNationalityIsNull(){
        assertThrows(IllegalArgumentException.class, () -> analyzerForTesting.getHighestPaidPlayerByNationality(null),
            "Expected exception when nationality is null");
    }

    @Test
    void testGetSimilarPlayersWhenPlayerIsNull(){
        assertThrows(IllegalArgumentException.class, () -> analyzerForTesting.getSimilarPlayers(null),
            "Expected exception when player is null");
    }

    @Test
    void testGetSimilarPlayers(){
        Player playerTest = Player.of("K. Navas;Keylor Navas;12/15/1986;32;185.42;79.8;GK;Costa Rica;87;87;30500000;195000;Right");

        Set<Player> expectedResult = Set.of(
            Player.of("M. Neuer;Manuel Neuer;3/27/1986;32;193.04;92.1;GK;Germany;89;89;38000000;130000;Right"),
            Player.of("M. ter Stegen;Marc-André ter Stegen;4/30/1992;26;187.96;84.8;GK;Germany;89;92;58000000;240000;Right"),
            Player.of("Kepa;Kepa Arrizabalaga;10/3/1994;24;185.42;84.8;GK;Spain;84;90;31000000;92000;Right"));

        assertTrue(expectedResult.containsAll(analyzerForTesting.getSimilarPlayers(playerTest))
        && analyzerForTesting.getSimilarPlayers(playerTest).containsAll(expectedResult),
            "Expected set and actual set do not match");
    }
    @Test
    void testGetPlayerByFullNameKeywordWhenKeyWordIsNull(){
        assertThrows(IllegalArgumentException.class, () -> analyzerForTesting.getPlayersByFullNameKeyword(null),
            "Expected exception when keyword is null");
    }

    @Test
    void testGetPlayerByFullNameKeywordWhenKeyWordIsEmpty(){
        assertThrows(IllegalArgumentException.class, () -> analyzerForTesting.getPlayersByFullNameKeyword(""),
            "Expected exception when keyword is empty");
    }

    @Test
    void testGetPlayerByFullNameKeywordWhenKeyWord(){
        Set<Player> setForTest = Set.of(
            Player.of("S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right"),
            Player.of("Sergio Busquets;Sergio Busquets i Burgos;7/16/1988;30;187.96;76.2;CDM,CM;Spain;89;89;51500000;315000;Right"));

        assertTrue(setForTest.containsAll(analyzerForTesting.getPlayersByFullNameKeyword("gio"))
        && analyzerForTesting.getPlayersByFullNameKeyword("gio").containsAll(setForTest),
            "Expected set do not match actual set");
    }


}
