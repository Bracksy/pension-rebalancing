package ch.immi.pension;

import ch.immi.pension.util.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Pot3SystemTest {
    static private final double PERCENTAGE_3A = 0.3;
    static private final double THRESHOLD = 0.1;

    final double PRICE_3A = 97.7d;
    final double PRICE_3B = 42.02d;

    private static Stream<Arguments> constellations() {
        return Stream.of(
                // KONTO 2 minium
                Arguments.of("Test 1", 4000, 6000, 3000, 7000)
        );
    }

    @ParameterizedTest
    @MethodSource("constellations")
    public void testConstellations(String title, int pot3a, int pot3b,
                                   int expectedPot3a, int expectedPot3b) {
        Pot3System system = new Pot3System((int)(pot3a / PRICE_3A), PRICE_3A, (int)(pot3b / PRICE_3B), PRICE_3B);
        system.setParams(PERCENTAGE_3A, 1-PERCENTAGE_3A, THRESHOLD);
        Pots3State state = system.rebalance();

        System.out.println();
        System.out.printf("================= %s =================%n", title);
    }
}
