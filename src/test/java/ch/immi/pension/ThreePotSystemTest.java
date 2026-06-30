package ch.immi.pension;

import ch.immi.pension.util.PotThreshold;
import ch.immi.pension.util.ThreePotState;
import ch.immi.pension.util.ThreePotSystem;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreePotSystemTest {
    static private final int KONTO1_YEARLY_BEZUG = 24000;

    static private final double PERCENTAGE_3A = 0.4;

    final int NUMBER_OF_SHARES_3A = 1500;
    final int NUMBER_OF_SHARES_3B = 4000;
    static final double PRICE_3A = 100d;
    static final double PRICE_3B = 40d;

    static private final int TOLERANCE = (int)Math.round(Math.max(PRICE_3A, PRICE_3B));

    static private final PotThreshold threshold = PotThreshold.of(KONTO1_YEARLY_BEZUG);

    private static Stream<Arguments> constellations() {
        final int KONTO1_MIN_VALUE = threshold.getPot1Min();
        final int KONTO1_OPT_VALUE = threshold.getPot1Opt();
        final int KONTO2_MIN_VALUE = threshold.getPot2Min();
        final int KONTO2_OPT_VALUE = threshold.getPot2Opt();
        final int KONTO2_MAX_VALUE = threshold.getPot2Opt();

        final int LOWER_THAN_KONTO1_MIN = KONTO1_MIN_VALUE - 10;
        final int LOWER_THAN_KONTO1_OPT = KONTO1_OPT_VALUE - 10;
        final int BIGGER_THAN_KONTO1_OPT = KONTO1_OPT_VALUE + 10;

        final int LOWER_THAN_KONTO2_MIN = KONTO2_MIN_VALUE - 10;
        final int LOWER_THAN_KONTO2_OPT = KONTO2_OPT_VALUE - 10;
        final int LOWER_THAN_KONTO2_MAX = KONTO2_MAX_VALUE - 10;
        final int BIGGER_THAN_KONTO2_MAX = KONTO2_MAX_VALUE + 10;

        final double PRICE_3A = ThreePotSystemTest.PRICE_3A;
        final double PRICE_3B = ThreePotSystemTest.PRICE_3B;

        return Stream.of(
                // KONTO 2 minium
                Arguments.of("Test 1", LOWER_THAN_KONTO1_MIN, LOWER_THAN_KONTO2_MIN, PRICE_3A - 1, PRICE_3B - 1, KONTO1_OPT_VALUE, KONTO2_OPT_VALUE),
                Arguments.of("Test 2", LOWER_THAN_KONTO1_MIN, LOWER_THAN_KONTO2_MIN, PRICE_3A + 1, PRICE_3B + 1, KONTO1_MIN_VALUE, LOWER_THAN_KONTO2_MIN),
                Arguments.of("Test 3", LOWER_THAN_KONTO1_MIN, LOWER_THAN_KONTO2_OPT, PRICE_3A - 1, PRICE_3B - 1, KONTO1_OPT_VALUE, KONTO2_OPT_VALUE),
                Arguments.of("Test 4", LOWER_THAN_KONTO1_MIN, LOWER_THAN_KONTO2_OPT, PRICE_3A + 1, PRICE_3B + 1, KONTO1_MIN_VALUE, LOWER_THAN_KONTO2_OPT),
                Arguments.of("Test 5", LOWER_THAN_KONTO1_MIN, LOWER_THAN_KONTO2_MAX, PRICE_3A - 1, PRICE_3B - 1, KONTO1_OPT_VALUE, KONTO2_OPT_VALUE),
                Arguments.of("Test 6", LOWER_THAN_KONTO1_MIN, LOWER_THAN_KONTO2_MAX, PRICE_3A + 1, PRICE_3B + 1, KONTO1_OPT_VALUE, KONTO2_OPT_VALUE),
                Arguments.of("Test 7", LOWER_THAN_KONTO1_MIN, BIGGER_THAN_KONTO2_MAX, PRICE_3A - 1, PRICE_3B - 1, KONTO1_OPT_VALUE, KONTO2_OPT_VALUE),
                Arguments.of("Test 8", LOWER_THAN_KONTO1_MIN, BIGGER_THAN_KONTO2_MAX, PRICE_3A + 1, PRICE_3B + 1, KONTO1_OPT_VALUE, KONTO2_OPT_VALUE)
        );
    }

    @ParameterizedTest
    @MethodSource("constellations")
    public void testConstellations(String title, int pot1, int pot2,
                                   double highest3a,
                                   double highest3b,
                                   int expectedMinPot1, int expectedMinPot2) {
        ThreePotSystem system = new ThreePotSystem(threshold, PERCENTAGE_3A, 1-PERCENTAGE_3A);
        ThreePotState state = new ThreePotState(pot1, pot2, NUMBER_OF_SHARES_3A, PRICE_3A, NUMBER_OF_SHARES_3B, PRICE_3B,
                highest3a, highest3b);
        system.updateState(state);

        System.out.println();
        System.out.printf("================= %s =================%n", title);
        System.out.printf("Check Konto 1: %d >= %d\n", state.getPot1(), expectedMinPot1);

        System.out.printf("Check Konto 2: %d >= %d\n", state.getPot2(),expectedMinPot2);
        int pot3 = (int)(NUMBER_OF_SHARES_3A * PRICE_3A + NUMBER_OF_SHARES_3B * PRICE_3B);
        int newPot3 = (int)(state.getNumOfShares3a() * state.getPrice3a() + state.getNumOfShares3b() * state.getPrice3b());
        System.out.printf("Kontostand: Alt: %d (%d, %d, %d) = Neu: %d (%d, %d, %d)\n",
                pot1 + pot2 + pot3, pot1, pot2, pot3, state.getPot1() + state.getPot2() + newPot3, state.getPot1(), state.getPot2(), newPot3);

        assertTrue(state.getPot1() >= expectedMinPot1, String.format("Konto 1 falsch: %d >= expected %d", state.getPot1(), expectedMinPot1));
        assertTrue(state.getPot2() >= expectedMinPot2, String.format("Konto 2 falsch: %d >= expected %d", state.getPot2(), expectedMinPot2));
        assertTrue(pot1 + pot2 + pot3 >= state.getPot1() + state.getPot2() + newPot3, "Kontostand falsch");
    }

    private void assertAlmostEquals(int value, int expected, String message) {
        boolean isAlmost = value - TOLERANCE < expected || value + TOLERANCE > expected;
        assertTrue(isAlmost, "Kontostand falsch");
    }
}