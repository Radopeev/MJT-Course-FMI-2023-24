package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.KeyGenerator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MJTSpaceScannerTest {

    private MJTSpaceScanner spaceScannerTest;
    private Reader missionsReader;
    private Reader rocketsReader;
    private Rijndael rijndaelTest;

    @BeforeEach
    void setup() throws NoSuchAlgorithmException {
        missionsReader = new BufferedReader(new StringReader("""
            Unnamed: 0,Company Name,Location,Datum,Detail,Status Rocket," Rocket",Status Mission
            0,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri Aug 07, 2020",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,"50.0 ",Success
            1,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Aug 06, 2020",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,"29.75 ",Success
            2,SpaceX,"Pad A, Boca Chica, Texas, USA","Tue Aug 04, 2020",Starship Prototype | 150 Meter Hop,StatusActive,,Success
            3,Roscosmos,"Site 200/39, Baikonur Cosmodrome, Kazakhstan","Thu Jul 30, 2020",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,"65.0 ",Success
            4,ULA,"SLC-41, Cape Canaveral AFS, Florida, USA","Thu Jul 30, 2020",Atlas V 541 | Perseverance,StatusActive,"145.0 ",Success
            5,CASC,"LC-9, Taiyuan Satellite Launch Center, China","Sat Jul 25, 2020","Long March 4B | Ziyuan-3 03, Apocalypse-10 & NJU-HKU 1",StatusActive,"64.68 ",Success
            6,Roscosmos,"Site 31/6, Baikonur Cosmodrome, Kazakhstan","Thu Jul 23, 2020",Soyuz 2.1a | Progress MS-15,StatusActive,"48.5 ",Success
            7,CASC,"LC-101, Wenchang Satellite Launch Center, China","Thu Jul 23, 2020",Long March 5 | Tianwen-1,StatusActive,,Success
            8,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Mon Jul 20, 2020",Falcon 9 Block 5 | ANASIS-II,StatusActive,"50.0 ",Success
            9,JAXA,"LA-Y1, Tanegashima Space Center, Japan","Sun Jul 19, 2020",H-IIA 202 | Hope Mars Mission,StatusActive,"90.0 ",Success
            10,Northrop,"LP-0B, Wallops Flight Facility, Virginia, USA","Wed Jul 15, 2020",Minotaur IV | NROL-129,StatusActive,"46.0 ",Success
            11,ExPace,"Site 95, Jiuquan Satellite Launch Center, China","Fri Jul 10, 2020","Kuaizhou 11 | Jilin-1 02E, CentiSpace-1 S2",StatusActive,"28.3 ",Failure
            12,CASC,"LC-3, Xichang Satellite Launch Center, China","Thu Jul 09, 2020",Long March 3B/E | Apstar-6D,StatusActive,"29.15 ",Success
            13,IAI,"Pad 1, Palmachim Airbase, Israel","Mon Jul 06, 2020",Shavit-2 | Ofek-16,StatusActive,,Success
            14,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Sat Jul 04, 2020",Long March 2D | Shiyan-6 02,StatusActive,"29.75 ",Success
            15,Rocket Lab,"Rocket Lab LC-1A, M?Âhia Peninsula, New Zealand","Sat Jul 04, 2020",Electron/Curie | Pics Or It Didn??¦t Happen,StatusActive,"7.5 ",Failure
            16,CASC,"LC-9, Taiyuan Satellite Launch Center, China","Fri Jul 03, 2020",Long March 4B | Gaofen Duomo & BY-02,StatusActive,"64.68 ",Success
            17,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Tue Jun 30, 2020",Falcon 9 Block 5 | GPS III SV03,StatusActive,"50.0 ",Success
            18,CASC,"LC-2, Xichang Satellite Launch Center, China","Tue Jun 23, 2020",Long March 3B/E | Beidou-3 G3,StatusActive,"29.15 ",Success
            19,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Wed Jun 17, 2020","Long March 2D | Gaofen-9 03, Pixing III A & HEAD-5",StatusActive,"29.75 ",Success
            20,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Sat Jun 13, 2020",Falcon 9 Block 5 | Starlink V1 L8 & SkySat 16 to 18,StatusActive,"50.0 ",Success
            21,Rocket Lab,"Rocket Lab LC-1A, M?Âhia Peninsula, New Zealand","Sat Jun 13, 2020",Electron/Curie | Don't stop me now!,StatusActive,"7.5 ",Success
            22,CASC,"LC-9, Taiyuan Satellite Launch Center, China","Wed Jun 10, 2020",Long March 2C | Haiyang-1D,StatusActive,"30.8 ",Success
            23,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Thu Jun 04, 2020",Falcon 9 Block 5 | Starlink V1 L7,StatusActive,"50.0 ",Success
            24,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Sun May 31, 2020",Long March 2D | Gaofen-9-02 & HEAD-4,StatusActive,"29.75 ",Success
            25,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Sat May 30, 2020",Falcon 9 Block 5 | SpaceX Demo-2,StatusActive,"50.0 ",Success
            26,CASC,"Xichang Satellite Launch Center, China","Fri May 29, 2020",Long March 11 | XJS-G and XJS-H,StatusActive,"5.3 ",Success
            27,Virgin Orbit,"Cosmic Girl, Mojave Air and Space Port, California, USA","Mon May 25, 2020",LauncherOne | Demo Flight,StatusActive,"12.0 ",Failure
            28,VKS RF,"Site 43/4, Plesetsk Cosmodrome, Russia","Fri May 22, 2020",Soyuz 2.1b/Fregat-M | Cosmos 2546,StatusActive,,Success
            29,MHI,"LA-Y2, Tanegashima Space Center, Japan","Wed May 20, 2020",H-IIB | HTV-9,StatusRetired,"112.5 ",Success
            30,ULA,"SLC-41, Cape Canaveral AFS, Florida, USA","Sun May 17, 2020",Atlas V 501 | OTV-6 (USSF-7),StatusActive,"120.0 ",Success
            """));

        rocketsReader = new BufferedReader(new StringReader("""
            	Name	Wiki	Rocket Height
            0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m
            1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m
            2,Unha-2,https://en.wikipedia.org/wiki/Unha,28.0 m
            3,Unha-3,https://en.wikipedia.org/wiki/Unha,32.0 m
            4,Vanguard,https://en.wikipedia.org/wiki/Vanguard_(rocket),23.0 m
            5,Vector-H,https://en.wikipedia.org/wiki/Vector-H,18.3 m
            6,Vector-R,https://en.wikipedia.org/wiki/Vector-R,13.0 m
            7,Vega,https://en.wikipedia.org/wiki/Vega_(rocket),29.9 m
            8,Vega C,https://en.wikipedia.org/wiki/Vega_(rocket),35.0 m
            9,Vega E,https://en.wikipedia.org/wiki/Vega_(rocket),35.0 m
            10,VLS-1,https://en.wikipedia.org/wiki/VLS-1,19.0 m
            11,Volna,https://en.wikipedia.org/wiki/Volna,15.0 m
            12,Voskhod,https://en.wikipedia.org/wiki/Voskhod_(rocket),31.0 m
            13,Vostok,https://en.wikipedia.org/wiki/Vostok-K,31.0 m
            14,Vostok-2,https://en.wikipedia.org/wiki/Vostok-2_(rocket)	
            15,Vostok-2A,https://en.wikipedia.org/wiki/Vostok_(rocket_family)	
            16,Vostok-2M,https://en.wikipedia.org/wiki/Vostok-2M	
            17,Vulcan Centaur,https://en.wikipedia.org/wiki/Vulcan_%28rocket%29,58.3 m
            18,Zenit-2,https://en.wikipedia.org/wiki/Zenit-2,57.0 m
            19,Zenit-2 FG,https://en.wikipedia.org/wiki/Zenit_%28rocket_family%29,57.0 m
            20,Zenit-3 SL,https://en.wikipedia.org/wiki/Zenit_%28rocket_family%29,59.6 m
            21,Zenit-3 SLB,https://en.wikipedia.org/wiki/Zenit_%28rocket_family%29,57.0 m
            22,Zenit-3 SLBF,https://en.wikipedia.org/wiki/Zenit-3F,57.0 m
            23,ZÃ©phyr,https://fr.wikipedia.org/wiki/Z%C3%A9phyr_(fus%C3%A9e),12.3 m
            24,ZhuQue-1,https://en.wikipedia.org/wiki/LandSpace,19.0 m
            25,ZhuQue-2,https://en.wikipedia.org/wiki/LandSpace#Zhuque-2	
            26,Angara 1.1,https://en.wikipedia.org/wiki/Angara_(rocket_family),35.0 m
            27,Angara 1.2,https://en.wikipedia.org/wiki/Angara_(rocket_family),41.5 m
            28,Angara A5/Briz-M,https://en.wikipedia.org/wiki/Angara_(rocket_family)#Angara_A5	
            29,Angara A5/DM-03,https://en.wikipedia.org/wiki/Angara_(rocket_family)#Angara_A5	
            30,Angara A5M,https://en.wikipedia.org/wiki/Angara_(rocket_family)#Angara_A5	
            99,Atlas V 501,https://en.wikipedia.org/wiki/Atlas_V,62.2 m
            103,Atlas V 541,https://en.wikipedia.org/wiki/Atlas_V,62.2 m
            148,Delta IV Medium+ (4,2),https://en.wikipedia.org/wiki/Delta_IV,62.5 m
            194,Jielong-1
            """));
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecretKey secretKey = keyGenerator.generateKey();
        rijndaelTest = new Rijndael(secretKey);

        spaceScannerTest = new MJTSpaceScanner(missionsReader, rocketsReader, secretKey);
    }

    @Test
    void testGetAllMissions() throws NoSuchAlgorithmException {
        var missionsReaderInTest = new BufferedReader(new StringReader("""
            Unnamed: 0,Company Name,Location,Datum,Detail,Status Rocket," Rocket",Status Mission
            0,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri Aug 07, 2020",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,"50.0 ",Success
            1,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Aug 06, 2020",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,"29.75 ",Success
            2,SpaceX,"Pad A, Boca Chica, Texas, USA","Tue Aug 04, 2020",Starship Prototype | 150 Meter Hop,StatusActive,,Success
            3,Roscosmos,"Site 200/39, Baikonur Cosmodrome, Kazakhstan","Thu Jul 30, 2020",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,"65.0 ",Success
            4,ULA,"SLC-41, Cape Canaveral AFS, Florida, USA","Thu Jul 30, 2020",Atlas V 541 | Perseverance,StatusActive,"145.0 ",Success
            5,CASC,"LC-9, Taiyuan Satellite Launch Center, China","Sat Jul 25, 2020","Long March 4B | Ziyuan-3 03, Apocalypse-10 & NJU-HKU 1",StatusActive,"64.68 ",Success
            6,Roscosmos,"Site 31/6, Baikonur Cosmodrome, Kazakhstan","Thu Jul 23, 2020",Soyuz 2.1a | Progress MS-15,StatusActive,"48.5 ",Success
            7,CASC,"LC-101, Wenchang Satellite Launch Center, China","Thu Jul 23, 2020",Long March 5 | Tianwen-1,StatusActive,,Success
            8,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Mon Jul 20, 2020",Falcon 9 Block 5 | ANASIS-II,StatusActive,"50.0 ",Success
            9,JAXA,"LA-Y1, Tanegashima Space Center, Japan","Sun Jul 19, 2020",H-IIA 202 | Hope Mars Mission,StatusActive,"90.0 ",Success
            10,Northrop,"LP-0B, Wallops Flight Facility, Virginia, USA","Wed Jul 15, 2020",Minotaur IV | NROL-129,StatusActive,"46.0 ",Success
            11,ExPace,"Site 95, Jiuquan Satellite Launch Center, China","Fri Jul 10, 2020","Kuaizhou 11 | Jilin-1 02E, CentiSpace-1 S2",StatusActive,"28.3 ",Failure
            12,CASC,"LC-3, Xichang Satellite Launch Center, China","Thu Jul 09, 2020",Long March 3B/E | Apstar-6D,StatusActive,"29.15 ",Success
            13,IAI,"Pad 1, Palmachim Airbase, Israel","Mon Jul 06, 2020",Shavit-2 | Ofek-16,StatusActive,,Success
            14,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Sat Jul 04, 2020",Long March 2D | Shiyan-6 02,StatusActive,"29.75 ",Success
            15,Rocket Lab,"Rocket Lab LC-1A, M?Âhia Peninsula, New Zealand","Sat Jul 04, 2020",Electron/Curie | Pics Or It Didn??¦t Happen,StatusActive,"7.5 ",Failure
            16,CASC,"LC-9, Taiyuan Satellite Launch Center, China","Fri Jul 03, 2020",Long March 4B | Gaofen Duomo & BY-02,StatusActive,"64.68 ",Success
            17,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Tue Jun 30, 2020",Falcon 9 Block 5 | GPS III SV03,StatusActive,"50.0 ",Success
            18,CASC,"LC-2, Xichang Satellite Launch Center, China","Tue Jun 23, 2020",Long March 3B/E | Beidou-3 G3,StatusActive,"29.15 ",Success
            19,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Wed Jun 17, 2020","Long March 2D | Gaofen-9 03, Pixing III A & HEAD-5",StatusActive,"29.75 ",Success
            20,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Sat Jun 13, 2020",Falcon 9 Block 5 | Starlink V1 L8 & SkySat 16 to 18,StatusActive,"50.0 ",Success
            21,Rocket Lab,"Rocket Lab LC-1A, M?Âhia Peninsula, New Zealand","Sat Jun 13, 2020",Electron/Curie | Don't stop me now!,StatusActive,"7.5 ",Success
            22,CASC,"LC-9, Taiyuan Satellite Launch Center, China","Wed Jun 10, 2020",Long March 2C | Haiyang-1D,StatusActive,"30.8 ",Success
            23,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Thu Jun 04, 2020",Falcon 9 Block 5 | Starlink V1 L7,StatusActive,"50.0 ",Success
            24,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Sun May 31, 2020",Long March 2D | Gaofen-9-02 & HEAD-4,StatusActive,"29.75 ",Success
            25,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Sat May 30, 2020",Falcon 9 Block 5 | SpaceX Demo-2,StatusActive,"50.0 ",Success
            26,CASC,"Xichang Satellite Launch Center, China","Fri May 29, 2020",Long March 11 | XJS-G and XJS-H,StatusActive,"5.3 ",Success
            27,Virgin Orbit,"Cosmic Girl, Mojave Air and Space Port, California, USA","Mon May 25, 2020",LauncherOne | Demo Flight,StatusActive,"12.0 ",Failure
            28,VKS RF,"Site 43/4, Plesetsk Cosmodrome, Russia","Fri May 22, 2020",Soyuz 2.1b/Fregat-M | Cosmos 2546,StatusActive,,Success
            29,MHI,"LA-Y2, Tanegashima Space Center, Japan","Wed May 20, 2020",H-IIB | HTV-9,StatusRetired,"112.5 ",Success
            30,ULA,"SLC-41, Cape Canaveral AFS, Florida, USA","Sun May 17, 2020",Atlas V 501 | OTV-6 (USSF-7),StatusActive,"120.0 ",Success
            """));
        List<Mission> expected;
        try (var bf = new BufferedReader(missionsReaderInTest)) {
            String firstLine = bf.readLine();
            expected = bf.lines().map(Mission::of).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Collection<Mission> actual = spaceScannerTest.getAllMissions();
        assertIterableEquals(expected, actual,
            "Result is different than expected");
    }

    @Test
    void testGetAllRockets() {
        var rocketsReaderInTest = new BufferedReader(new StringReader("""
            	Name	Wiki	Rocket Height
            0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m
            1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m
            2,Unha-2,https://en.wikipedia.org/wiki/Unha,28.0 m
            3,Unha-3,https://en.wikipedia.org/wiki/Unha,32.0 m
            4,Vanguard,https://en.wikipedia.org/wiki/Vanguard_(rocket),23.0 m
            5,Vector-H,https://en.wikipedia.org/wiki/Vector-H,18.3 m
            6,Vector-R,https://en.wikipedia.org/wiki/Vector-R,13.0 m
            7,Vega,https://en.wikipedia.org/wiki/Vega_(rocket),29.9 m
            8,Vega C,https://en.wikipedia.org/wiki/Vega_(rocket),35.0 m
            9,Vega E,https://en.wikipedia.org/wiki/Vega_(rocket),35.0 m
            10,VLS-1,https://en.wikipedia.org/wiki/VLS-1,19.0 m
            11,Volna,https://en.wikipedia.org/wiki/Volna,15.0 m
            12,Voskhod,https://en.wikipedia.org/wiki/Voskhod_(rocket),31.0 m
            13,Vostok,https://en.wikipedia.org/wiki/Vostok-K,31.0 m
            14,Vostok-2,https://en.wikipedia.org/wiki/Vostok-2_(rocket)	
            15,Vostok-2A,https://en.wikipedia.org/wiki/Vostok_(rocket_family)	
            16,Vostok-2M,https://en.wikipedia.org/wiki/Vostok-2M	
            17,Vulcan Centaur,https://en.wikipedia.org/wiki/Vulcan_%28rocket%29,58.3 m
            18,Zenit-2,https://en.wikipedia.org/wiki/Zenit-2,57.0 m
            19,Zenit-2 FG,https://en.wikipedia.org/wiki/Zenit_%28rocket_family%29,57.0 m
            20,Zenit-3 SL,https://en.wikipedia.org/wiki/Zenit_%28rocket_family%29,59.6 m
            21,Zenit-3 SLB,https://en.wikipedia.org/wiki/Zenit_%28rocket_family%29,57.0 m
            22,Zenit-3 SLBF,https://en.wikipedia.org/wiki/Zenit-3F,57.0 m
            23,ZÃ©phyr,https://fr.wikipedia.org/wiki/Z%C3%A9phyr_(fus%C3%A9e),12.3 m
            24,ZhuQue-1,https://en.wikipedia.org/wiki/LandSpace,19.0 m
            25,ZhuQue-2,https://en.wikipedia.org/wiki/LandSpace#Zhuque-2,	
            26,Angara 1.1,https://en.wikipedia.org/wiki/Angara_(rocket_family),35.0 m
            27,Angara 1.2,https://en.wikipedia.org/wiki/Angara_(rocket_family),41.5 m
            28,Angara A5/Briz-M,https://en.wikipedia.org/wiki/Angara_(rocket_family)#Angara_A5	
            29,Angara A5/DM-03,https://en.wikipedia.org/wiki/Angara_(rocket_family)#Angara_A5	
            30,Angara A5M,https://en.wikipedia.org/wiki/Angara_(rocket_family)#Angara_A5	
            99,Atlas V 501,https://en.wikipedia.org/wiki/Atlas_V,62.2 m
            103,Atlas V 541,https://en.wikipedia.org/wiki/Atlas_V,62.2 m
            148,Delta IV Medium+ (4,2),https://en.wikipedia.org/wiki/Delta_IV,62.5 m
            194,Jielong-1
            """));
        List<Rocket> expected = new ArrayList<>();
        try (var bf = new BufferedReader(rocketsReaderInTest)) {
            String firstLine = bf.readLine();
            expected = bf.lines().map(Rocket::of).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Collection<Rocket> actual = spaceScannerTest.getAllRockets();
        assertIterableEquals(expected, actual,
            "Result is different than expected");
    }

    @Test
    void testGetAllMissionsWithSpecificMissionStatusThrowsWhenMissionStatusIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getAllMissions(null),
            "Should throw an exception when mission status is null");
    }

    @Test
    void testGetAllMissionsWithSpecificMissionStatus() {
        Collection<Mission> expected = List.of(
            Mission.of(
                "11,ExPace,\"Site 95, Jiuquan Satellite Launch Center, China\",\"Fri Jul 10, 2020\",\"Kuaizhou 11 | Jilin-1 02E, CentiSpace-1 S2\",StatusActive,\"28.3 \",Failure"),
            Mission.of(
                "15,Rocket Lab,\"Rocket Lab LC-1A, M?Â\u0081hia Peninsula, New Zealand\",\"Sat Jul 04, 2020\",Electron/Curie | Pics Or It Didn??¦t Happen,StatusActive,\"7.5 \",Failure"),
            Mission.of(
                "27,Virgin Orbit,\"Cosmic Girl, Mojave Air and Space Port, California, USA\",\"Mon May 25, 2020\",LauncherOne | Demo Flight,StatusActive,\"12.0 \",Failure")
        );
        Collection<Mission> actual = spaceScannerTest.getAllMissions(MissionStatus.FAILURE);
        assertIterableEquals(expected, actual,
            "Result is different than expected");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsThrowsWhenFromIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getCompanyWithMostSuccessfulMissions(null, LocalDate.now()),
            "Should throw an exception when from is null");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsThrowsWhenToIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getCompanyWithMostSuccessfulMissions(LocalDate.now(), null),
            "Should throw an exception when to is null");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsThrowsWhenTimeFramesMisMatch() {
        LocalDate tempDate = LocalDate.MAX;
        assertThrows(TimeFrameMismatchException.class,
            () -> spaceScannerTest.getCompanyWithMostSuccessfulMissions(tempDate, LocalDate.now()),
            "Should throw an exception when time frames mismatch");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissions() {
        LocalDate from = LocalDate.of(2020, 1, 1);
        LocalDate to = LocalDate.of(2020, 12, 31);
        String expected = "CASC";
        String actual = spaceScannerTest.getCompanyWithMostSuccessfulMissions(from, to);
        assertEquals(expected, actual,
            "Result is different than expected");
    }

    @Test
    void testGetMissionsPerCountry() {
        Collection<Mission> usa = List.of(
            Mission.of(
                "0,SpaceX,\"LC-39A, Kennedy Space Center, Florida, USA\",\"Fri Aug 07, 2020\",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,\"50.0 \",Success"),
            Mission.of(
                "2,SpaceX,\"Pad A, Boca Chica, Texas, USA\",\"Tue Aug 04, 2020\",Starship Prototype | 150 Meter Hop,StatusActive,,Success"),
            Mission.of(
                "4,ULA,\"SLC-41, Cape Canaveral AFS, Florida, USA\",\"Thu Jul 30, 2020\",Atlas V 541 | Perseverance,StatusActive,\"145.0 \",Success"),
            Mission.of(
                "8,SpaceX,\"SLC-40, Cape Canaveral AFS, Florida, USA\",\"Mon Jul 20, 2020\",Falcon 9 Block 5 | ANASIS-II,StatusActive,\"50.0 \",Success"),
            Mission.of(
                "10,Northrop,\"LP-0B, Wallops Flight Facility, Virginia, USA\",\"Wed Jul 15, 2020\",Minotaur IV | NROL-129,StatusActive,\"46.0 \",Success"),
            Mission.of(
                "17,SpaceX,\"SLC-40, Cape Canaveral AFS, Florida, USA\",\"Tue Jun 30, 2020\",Falcon 9 Block 5 | GPS III SV03,StatusActive,\"50.0 \",Success"),
            Mission.of(
                "20,SpaceX,\"SLC-40, Cape Canaveral AFS, Florida, USA\",\"Sat Jun 13, 2020\",Falcon 9 Block 5 | Starlink V1 L8 & SkySat 16 to 18,StatusActive,\"50.0 \",Success"),
            Mission.of(
                "23,SpaceX,\"SLC-40, Cape Canaveral AFS, Florida, USA\",\"Thu Jun 04, 2020\",Falcon 9 Block 5 | Starlink V1 L7,StatusActive,\"50.0 \",Success"),
            Mission.of(
                "25,SpaceX,\"LC-39A, Kennedy Space Center, Florida, USA\",\"Sat May 30, 2020\",Falcon 9 Block 5 | SpaceX Demo-2,StatusActive,\"50.0 \",Success"),
            Mission.of(
                "27,Virgin Orbit,\"Cosmic Girl, Mojave Air and Space Port, California, USA\",\"Mon May 25, 2020\",LauncherOne | Demo Flight,StatusActive,\"12.0 \",Failure"),
            Mission.of(
                "30,ULA,\"SLC-41, Cape Canaveral AFS, Florida, USA\",\"Sun May 17, 2020\",Atlas V 501 | OTV-6 (USSF-7),StatusActive,\"120.0 \",Success")
        );
        Collection<Mission> china = List.of(
            Mission.of(
                "1,CASC,\"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China\",\"Thu Aug 06, 2020\",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,\"29.75 \",Success"),
            Mission.of(
                "5,CASC,\"LC-9, Taiyuan Satellite Launch Center, China\",\"Sat Jul 25, 2020\",\"Long March 4B | Ziyuan-3 03, Apocalypse-10 & NJU-HKU 1\",StatusActive,\"64.68 \",Success"),
            Mission.of(
                "7,CASC,\"LC-101, Wenchang Satellite Launch Center, China\",\"Thu Jul 23, 2020\",Long March 5 | Tianwen-1,StatusActive,,Success"),
            Mission.of(
                "11,ExPace,\"Site 95, Jiuquan Satellite Launch Center, China\",\"Fri Jul 10, 2020\",\"Kuaizhou 11 | Jilin-1 02E, CentiSpace-1 S2\",StatusActive,\"28.3 \",Failure"),
            Mission.of(
                "12,CASC,\"LC-3, Xichang Satellite Launch Center, China\",\"Thu Jul 09, 2020\",Long March 3B/E | Apstar-6D,StatusActive,\"29.15 \",Success"),
            Mission.of(
                "14,CASC,\"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China\",\"Sat Jul 04, 2020\",Long March 2D | Shiyan-6 02,StatusActive,\"29.75 \",Success"),
            Mission.of(
                "16,CASC,\"LC-9, Taiyuan Satellite Launch Center, China\",\"Fri Jul 03, 2020\",Long March 4B | Gaofen Duomo & BY-02,StatusActive,\"64.68 \",Success"),
            Mission.of(
                "18,CASC,\"LC-2, Xichang Satellite Launch Center, China\",\"Tue Jun 23, 2020\",Long March 3B/E | Beidou-3 G3,StatusActive,\"29.15 \",Success"),
            Mission.of(
                "19,CASC,\"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China\",\"Wed Jun 17, 2020\",\"Long March 2D | Gaofen-9 03, Pixing III A & HEAD-5\",StatusActive,\"29.75 \",Success"),
            Mission.of(
                "22,CASC,\"LC-9, Taiyuan Satellite Launch Center, China\",\"Wed Jun 10, 2020\",Long March 2C | Haiyang-1D,StatusActive,\"30.8 \",Success"),
            Mission.of(
                "24,CASC,\"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China\",\"Sun May 31, 2020\",Long March 2D | Gaofen-9-02 & HEAD-4,StatusActive,\"29.75 \",Success"),
            Mission.of(
                "26,CASC,\"Xichang Satellite Launch Center, China\",\"Fri May 29, 2020\",Long March 11 | XJS-G and XJS-H,StatusActive,\"5.3 \",Success")
        );
        Collection<Mission> kazakhstan = List.of(
            Mission.of(
                "3,Roscosmos,\"Site 200/39, Baikonur Cosmodrome, Kazakhstan\",\"Thu Jul 30, 2020\",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,\"65.0 \",Success"),
            Mission.of(
                "6,Roscosmos,\"Site 31/6, Baikonur Cosmodrome, Kazakhstan\",\"Thu Jul 23, 2020\",Soyuz 2.1a | Progress MS-15,StatusActive,\"48.5 \",Success")
        );
        Collection<Mission> japan = List.of(
            Mission.of(
                "9,JAXA,\"LA-Y1, Tanegashima Space Center, Japan\",\"Sun Jul 19, 2020\",H-IIA 202 | Hope Mars Mission,StatusActive,\"90.0 \",Success"),
            Mission.of(
                "29,MHI,\"LA-Y2, Tanegashima Space Center, Japan\",\"Wed May 20, 2020\",H-IIB | HTV-9,StatusRetired,\"112.5 \",Success")
        );
        Collection<Mission> israel = List.of(
            Mission.of(
                "13,IAI,\"Pad 1, Palmachim Airbase, Israel\",\"Mon Jul 06, 2020\",Shavit-2 | Ofek-16,StatusActive,,Success")
        );
        Collection<Mission> newZealand = List.of(
            Mission.of(
                "15,Rocket Lab,\"Rocket Lab LC-1A, M?Â\u0081hia Peninsula, New Zealand\",\"Sat Jul 04, 2020\",Electron/Curie | Pics Or It Didn??¦t Happen,StatusActive,\"7.5 \",Failure"),
            Mission.of(
                "21,Rocket Lab,\"Rocket Lab LC-1A, M?Â\u0081hia Peninsula, New Zealand\",\"Sat Jun 13, 2020\",Electron/Curie | Don't stop me now!,StatusActive,\"7.5 \",Success")
        );
        Collection<Mission> russia = List.of(
            Mission.of(
                "28,VKS RF,\"Site 43/4, Plesetsk Cosmodrome, Russia\",\"Fri May 22, 2020\",Soyuz 2.1b/Fregat-M | Cosmos 2546,StatusActive,,Success")
        );
        Map<String, Collection<Mission>> expected = Map.of("USA", usa,
            "China", china,
            "Kazakhstan", kazakhstan,
            "Japan", japan,
            "Israel", israel,
            "NewZealand", newZealand,
            "Russia", russia);

        Map<String, Collection<Mission>> actual = spaceScannerTest.getMissionsPerCountry();
        assertEquals(expected, actual,
            "Result is different than expected");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWhenNIsLessOrEqualThanZero() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getTopNLeastExpensiveMissions(-1, MissionStatus.FAILURE, RocketStatus.STATUS_ACTIVE),
            "Should throw an exception when n is less or equal to zero");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWhenMissionStatusIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getTopNLeastExpensiveMissions(1, null, RocketStatus.STATUS_ACTIVE),
            "Should throw an exception when mission status is null");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWhenRocketStatusIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getTopNLeastExpensiveMissions(1, MissionStatus.FAILURE, null),
            "Should throw an exception when mission status is null");
    }

    @Test
    void testGetTopNLeastExpensiveMissions() {
        List<Mission> expected = List.of(
            Mission.of(
                "26,CASC,\"Xichang Satellite Launch Center, China\",\"Fri May 29, 2020\",Long March 11 | XJS-G and XJS-H,StatusActive,\"5.3 \",Success"),
            Mission.of(
                "21,Rocket Lab,\"Rocket Lab LC-1A, M?Â\u0081hia Peninsula, New Zealand\",\"Sat Jun 13, 2020\",Electron/Curie | Don't stop me now!,StatusActive,\"7.5 \",Success"),
            Mission.of(
                "12,CASC,\"LC-3, Xichang Satellite Launch Center, China\",\"Thu Jul 09, 2020\",Long March 3B/E | Apstar-6D,StatusActive,\"29.15 \",Success")
        );
        List<Mission> actual =
            spaceScannerTest.getTopNLeastExpensiveMissions(3, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE);

        assertIterableEquals(expected, actual,
            "Result is different than expected");
    }

    @Test
    void testGetMostDesiredLocationForMissionsPerCompany() {
        Map<String, String> expected = new HashMap<>();
        expected.put("SpaceX", "SLC-40, Cape Canaveral AFS, Florida, USA");
        expected.put("CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China");
        expected.put("Roscosmos", "Site 31/6, Baikonur Cosmodrome, Kazakhstan");
        expected.put("ULA", "SLC-41, Cape Canaveral AFS, Florida, USA");
        expected.put("JAXA", "LA-Y1, Tanegashima Space Center, Japan");
        expected.put("Northrop", "LP-0B, Wallops Flight Facility, Virginia, USA");
        expected.put("ExPace", "Site 95, Jiuquan Satellite Launch Center, China");
        expected.put("IAI", "Pad 1, Palmachim Airbase, Israel");
        expected.put("Rocket Lab", "Rocket Lab LC-1A, M?Â\u0081hia Peninsula, New Zealand");
        expected.put("Virgin Orbit", "Cosmic Girl, Mojave Air and Space Port, California, USA");
        expected.put("VKS RF", "Site 43/4, Plesetsk Cosmodrome, Russia");
        expected.put("MHI", "LA-Y2, Tanegashima Space Center, Japan");

        Map<String, String> actual = spaceScannerTest.getMostDesiredLocationForMissionsPerCompany();

        assertEquals(expected, actual,
            "Result is different than expected");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyThrowsWhenFromIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getLocationWithMostSuccessfulMissionsPerCompany(null, LocalDate.now()),
            "Should throw an exception when from is null");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyThrowsWhenToIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.now(), null),
            "Should throw an exception when from is null");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyThrowsWhenTimeFramesMismatch() {
        LocalDate tempDate = LocalDate.MAX;
        assertThrows(TimeFrameMismatchException.class,
            () -> spaceScannerTest.getLocationWithMostSuccessfulMissionsPerCompany(tempDate, LocalDate.now()),
            "Should throw an exception when time frames mismatch");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompany() {
        Map<String, String> expected = new HashMap<>();
        expected.put("SpaceX", "SLC-40, Cape Canaveral AFS, Florida, USA");
        expected.put("CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China");
        expected.put("Roscosmos", "Site 31/6, Baikonur Cosmodrome, Kazakhstan");
        expected.put("ULA", "SLC-41, Cape Canaveral AFS, Florida, USA");
        expected.put("JAXA", "LA-Y1, Tanegashima Space Center, Japan");
        expected.put("Northrop", "LP-0B, Wallops Flight Facility, Virginia, USA");
        expected.put("IAI", "Pad 1, Palmachim Airbase, Israel");
        expected.put("Rocket Lab", "Rocket Lab LC-1A, M?Â\u0081hia Peninsula, New Zealand");
        expected.put("VKS RF", "Site 43/4, Plesetsk Cosmodrome, Russia");
        expected.put("MHI", "LA-Y2, Tanegashima Space Center, Japan");
        LocalDate from = LocalDate.of(2020, 1, 1);
        LocalDate to = LocalDate.of(2020, 12, 31);

        Map<String, String> actual = spaceScannerTest.getLocationWithMostSuccessfulMissionsPerCompany(from, to);

        assertEquals(expected, actual,
            "Result is different than expected");
    }

    @Test
    void testGetTopNTallestRocketsWhenNIsLessOrEqualToZero() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getTopNTallestRockets(-1),
            "Should throw an exception when n is less or equal to zero");
    }

    @Test
    void testGetTopNTallestRockets() {
        List<Rocket> expected = List.of(
            Rocket.of("148,Delta IV Medium+ (4,2),https://en.wikipedia.org/wiki/Delta_IV,62.5 m"),
            Rocket.of("99,Atlas V 501,https://en.wikipedia.org/wiki/Atlas_V,62.2 m")
        );

        List<Rocket> actual = spaceScannerTest.getTopNTallestRockets(2);

        assertIterableEquals(expected, actual,
            "Result is different than expected");
    }

    @Test
    void testGetWikiPageForRocket() {
        Map<String, Optional<String>> expected = new HashMap<>();
        expected.put("Tsyklon-3", Optional.of("https://en.wikipedia.org/wiki/Tsyklon-3"));
        expected.put("Tsyklon-4M", Optional.of("https://en.wikipedia.org/wiki/Cyclone-4M"));
        expected.put("Unha-2", Optional.of("https://en.wikipedia.org/wiki/Unha"));
        expected.put("Unha-3", Optional.of("https://en.wikipedia.org/wiki/Unha"));
        expected.put("Vanguard", Optional.of("https://en.wikipedia.org/wiki/Vanguard_(rocket)"));
        expected.put("Vector-H", Optional.of("https://en.wikipedia.org/wiki/Vector-H"));
        expected.put("Vector-R", Optional.of("https://en.wikipedia.org/wiki/Vector-R"));
        expected.put("Vega", Optional.of("https://en.wikipedia.org/wiki/Vega_(rocket)"));
        expected.put("Vega C", Optional.of("https://en.wikipedia.org/wiki/Vega_(rocket)"));
        expected.put("Vega E", Optional.of("https://en.wikipedia.org/wiki/Vega_(rocket)"));
        expected.put("VLS-1", Optional.of("https://en.wikipedia.org/wiki/VLS-1"));
        expected.put("Volna", Optional.of("https://en.wikipedia.org/wiki/Volna"));
        expected.put("Voskhod", Optional.of("https://en.wikipedia.org/wiki/Voskhod_(rocket)"));
        expected.put("Vostok", Optional.of("https://en.wikipedia.org/wiki/Vostok-K"));
        expected.put("Vostok-2", Optional.of("https://en.wikipedia.org/wiki/Vostok-2_(rocket)"));
        expected.put("Vostok-2A", Optional.of("https://en.wikipedia.org/wiki/Vostok_(rocket_family)"));
        expected.put("Vostok-2M", Optional.of("https://en.wikipedia.org/wiki/Vostok-2M"));
        expected.put("Vulcan Centaur", Optional.of("https://en.wikipedia.org/wiki/Vulcan_%28rocket%29"));
        expected.put("Zenit-2", Optional.of("https://en.wikipedia.org/wiki/Zenit-2"));
        expected.put("Zenit-2 FG", Optional.of("https://en.wikipedia.org/wiki/Zenit_%28rocket_family%29"));
        expected.put("Zenit-3 SL", Optional.of("https://en.wikipedia.org/wiki/Zenit_%28rocket_family%29"));
        expected.put("Zenit-3 SLB", Optional.of("https://en.wikipedia.org/wiki/Zenit_%28rocket_family%29"));
        expected.put("Zenit-3 SLBF", Optional.of("https://en.wikipedia.org/wiki/Zenit-3F"));
        expected.put("ZÃ©phyr", Optional.of("https://fr.wikipedia.org/wiki/Z%C3%A9phyr_(fus%C3%A9e)"));
        expected.put("ZhuQue-1", Optional.of("https://en.wikipedia.org/wiki/LandSpace"));
        expected.put("ZhuQue-2", Optional.of("https://en.wikipedia.org/wiki/LandSpace#Zhuque-2"));
        expected.put("Angara 1.1", Optional.of("https://en.wikipedia.org/wiki/Angara_(rocket_family)"));
        expected.put("Angara 1.2", Optional.of("https://en.wikipedia.org/wiki/Angara_(rocket_family)"));
        expected.put("Angara A5/Briz-M", Optional.of("https://en.wikipedia.org/wiki/Angara_(rocket_family)#Angara_A5"));
        expected.put("Angara A5/DM-03", Optional.of("https://en.wikipedia.org/wiki/Angara_(rocket_family)#Angara_A5"));
        expected.put("Angara A5M", Optional.of("https://en.wikipedia.org/wiki/Angara_(rocket_family)#Angara_A5"));
        expected.put("Atlas V 501", Optional.of("https://en.wikipedia.org/wiki/Atlas_V"));
        expected.put("Atlas V 541", Optional.of("https://en.wikipedia.org/wiki/Atlas_V"));
        expected.put("Delta IV Medium+ (4,2)", Optional.of("https://en.wikipedia.org/wiki/Delta_IV"));
        expected.put("Jielong-1", Optional.empty());
        Map<String, Optional<String>> actual = spaceScannerTest.getWikiPageForRocket();

        assertEquals(expected, actual,
            "Result if different than expected");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWhenIsEqualOrLessThanZero() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getWikiPagesForRocketsUsedInMostExpensiveMissions(-1, MissionStatus.FAILURE,
                RocketStatus.STATUS_ACTIVE),
            "Should throw an exception when n is less or equal than zero");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWhenMissionStatusIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getWikiPagesForRocketsUsedInMostExpensiveMissions(1, null,
                RocketStatus.STATUS_ACTIVE),
            "Should throw an exception when mission status is null");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWhenRocketStatusIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.getWikiPagesForRocketsUsedInMostExpensiveMissions(1, MissionStatus.SUCCESS, null),
            "Should throw an exception when rocket status is null");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissions() {
        List<String> expected = List.of(
            "https://en.wikipedia.org/wiki/Atlas_V",
            "https://en.wikipedia.org/wiki/Atlas_V");

        List<String> actual =
            spaceScannerTest.getWikiPagesForRocketsUsedInMostExpensiveMissions(2, MissionStatus.SUCCESS,
                RocketStatus.STATUS_ACTIVE);

        assertIterableEquals(expected, actual,
            "Result is different than expected");
    }

    @Test
    void testSaveMostReliableRocketWhenFromIsNull() {
        OutputStream temp = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.saveMostReliableRocket(temp, null, LocalDate.now()),
            "Should throw an exception when from is null");
    }

    @Test
    void testSaveMostReliableRocketWhenToIsNull() {
        OutputStream temp = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> spaceScannerTest.saveMostReliableRocket(temp, LocalDate.now(), null),
            "Should throw an exception when from is null");
    }

    @Test
    void testSaveMostReliableRocketWhenTimeFramesMismatch() {
        OutputStream temp = new ByteArrayOutputStream();
        assertThrows(TimeFrameMismatchException.class,
            () -> spaceScannerTest.saveMostReliableRocket(temp, LocalDate.MAX, LocalDate.now()),
            "Should thrown an exception when time frames mismatch");
    }

    @Test
    void testSaveMostReliableRocket() throws CipherException, IOException {
        String expected = "H-IIA 202";
        LocalDate from = LocalDate.of(2020, 1, 1);
        LocalDate to = LocalDate.of(2020, 12, 31);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        spaceScannerTest.saveMostReliableRocket(outputStream, from, to);
        ByteArrayInputStream encryptedData = new ByteArrayInputStream(outputStream.toByteArray());
        ByteArrayOutputStream decryptedData = new ByteArrayOutputStream();

        rijndaelTest.decrypt(encryptedData, decryptedData);

        byte[] decrypted = decryptedData.toByteArray();
        encryptedData.close();
        decryptedData.close();
        assertArrayEquals(expected.getBytes(), decrypted,
            "Result is different than expected");
    }
}
