package ch.immi.pension;

import ch.immi.pension.data.PotsRebalancing;
import ch.immi.pension.data.Threshold;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PotsRebalancer Tests")
class PotsRebalancerTest {
    static private final int MAX_AMOUNT = 500000;
    static private final int KONTO1_YEARLY_BEZUG = 24000;

    static private final int KONTO1 = KONTO1_YEARLY_BEZUG * 3;
    static private final int KONTO2 = KONTO1_YEARLY_BEZUG * 5;
    static private final int KONTO3 = MAX_AMOUNT - KONTO1 - KONTO2;


    static private final int KONTO3_PLUS10 = 5;
    static private final int KONTO3_PLUS5 = 0;
    static private final int KONTO3_MIN20 = 5;
    static private final int KONTO3_MIN30 = 10;

    static private final Threshold threshold = Threshold.of(KONTO1_YEARLY_BEZUG);

    private static Stream<Arguments> constellations() {
        final int KONTO1_MIN_VALUE = threshold.getKonto1Min();
        final int KONTO1_OPT_VALUE = threshold.getKonto1Opt();
        final int KONTO2_MIN_VALUE = threshold.getKonto2Min();
        final int KONTO2_OPT_VALUE = threshold.getKonto2Opt();
        
        final int KONTO1_MIN_TEST = KONTO1_MIN_VALUE - 1000;
        final int KONTO1_OPT_TEST = KONTO1_OPT_VALUE - 1000;
        final int KONTO2_MIN_TEST = KONTO2_MIN_VALUE - 1000;
        final int KONTO2_OPT_TEST = KONTO2_OPT_VALUE - 1000;
        return Stream.of(
                // KONTO 2 minium
                Arguments.of("Test 1", KONTO1_MIN_TEST, KONTO2_MIN_TEST, KONTO3 - 50000, KONTO1_MIN_VALUE, KONTO2_MIN_VALUE),
                Arguments.of("Test 2", KONTO1_MIN_TEST, KONTO2_MIN_TEST, KONTO3, KONTO1_MIN_VALUE, KONTO2_MIN_VALUE),
                Arguments.of("Test 3", KONTO1_MIN_TEST, KONTO2_MIN_TEST, KONTO3 + 20000, KONTO1_MIN_VALUE, KONTO2_MIN_VALUE),
                Arguments.of("Test 4", KONTO1_OPT_TEST, KONTO2_MIN_TEST, KONTO3 - 50000, KONTO1_OPT_TEST, KONTO2_MIN_VALUE),
                Arguments.of("Test 5", KONTO1_OPT_TEST, KONTO2_MIN_TEST, KONTO3, KONTO1_OPT_TEST, KONTO2_MIN_VALUE),
                Arguments.of("Test 6", KONTO1_OPT_TEST, KONTO2_MIN_TEST, KONTO3 + 20000, KONTO1_OPT_TEST, KONTO2_MIN_VALUE),
                Arguments.of("Test 7", KONTO1, KONTO2_MIN_TEST, KONTO3 - 50000, KONTO1, KONTO2_MIN_VALUE),
                Arguments.of("Test 8", KONTO1, KONTO2_MIN_TEST, KONTO3, KONTO1, KONTO2_MIN_VALUE),
                Arguments.of("Test 9", KONTO1, KONTO2_MIN_TEST, KONTO3 + 20000, KONTO1, KONTO2_MIN_VALUE),

                // KONTO 2 optimal
                Arguments.of("Test 10", KONTO1_MIN_TEST, KONTO2_OPT_TEST, KONTO3 - 50000, KONTO1_OPT_VALUE, KONTO2_OPT_TEST - (KONTO1_YEARLY_BEZUG + 1000)),
                Arguments.of("Test 11", KONTO1_MIN_TEST, KONTO2_OPT_TEST, KONTO3, KONTO1_OPT_VALUE, KONTO2_OPT_TEST - (KONTO1_YEARLY_BEZUG + 1000)),
                Arguments.of("Test 12", KONTO1_MIN_TEST, KONTO2_OPT_TEST, KONTO3 + 20000, KONTO1_OPT_VALUE, KONTO2_OPT_TEST - (KONTO1_YEARLY_BEZUG + 1000)),
                Arguments.of("Test 13", KONTO1_OPT_TEST, KONTO2_OPT_TEST, KONTO3 - 50000, KONTO1_OPT_VALUE, KONTO2_OPT_TEST - 1000),
                Arguments.of("Test 14", KONTO1_OPT_TEST, KONTO2_OPT_TEST, KONTO3, KONTO1_OPT_VALUE, KONTO2_OPT_TEST - 1000),
                Arguments.of("Test 15", KONTO1_OPT_TEST, KONTO2_OPT_TEST, KONTO3 + 20000, KONTO1_OPT_VALUE, KONTO2_OPT_TEST + 1000),
                Arguments.of("Test 16", KONTO1, KONTO2_OPT_TEST, KONTO3 - 50000, KONTO1, KONTO2_OPT_TEST),
                Arguments.of("Test 17", KONTO1, KONTO2_OPT_TEST, KONTO3, KONTO1, KONTO2_OPT_TEST),
                Arguments.of("Test 18", KONTO1, KONTO2_OPT_TEST, KONTO3 + 20000, KONTO1_OPT_VALUE, KONTO2_OPT_TEST + 1000),

                // KONTO 2 unchanged
                Arguments.of("Test 19", KONTO1_MIN_TEST, KONTO2, KONTO3 - 50000, KONTO1_MIN_VALUE, KONTO2_MIN_VALUE),
                Arguments.of("Test 20", KONTO1_MIN_TEST, KONTO2, KONTO3, KONTO1_MIN_VALUE, KONTO2_MIN_VALUE),
                Arguments.of("Test 21", KONTO1_MIN_TEST, KONTO2, KONTO3 + 20000, KONTO1_MIN_VALUE, KONTO2_MIN_VALUE),
                Arguments.of("Test 22", KONTO1_OPT_TEST, KONTO2, KONTO3 - 50000, KONTO1_OPT_VALUE, KONTO2_MIN_VALUE),
                Arguments.of("Test 23", KONTO1_OPT_TEST, KONTO2, KONTO3, KONTO1_OPT_VALUE, KONTO2_MIN_VALUE),
                Arguments.of("Test 24", KONTO1_OPT_TEST, KONTO2, KONTO3 + 20000, KONTO1_OPT_VALUE, KONTO2_OPT_VALUE),
                Arguments.of("Test 25", KONTO1, KONTO2, KONTO3 - 50000, KONTO1, KONTO2),
                Arguments.of("Test 26", KONTO1, KONTO2, KONTO3, KONTO1, KONTO2),
                Arguments.of("Test 27", KONTO1, KONTO2, KONTO3 + 20000, KONTO1, KONTO2),

                // KONTO 2 plus 5000
                Arguments.of("Test 28", KONTO1_MIN_TEST, KONTO2 + 5000, KONTO3 - 50000, KONTO1_OPT_VALUE, KONTO2 - 35000),
                Arguments.of("Test 29", KONTO1_MIN_TEST, KONTO2 + 5000, KONTO3, KONTO1_OPT_VALUE, KONTO2 - 35000),
                Arguments.of("Test 30", KONTO1_MIN_TEST, KONTO2 + 5000, KONTO3 + 20000, KONTO1_OPT_VALUE, KONTO2),
                Arguments.of("Test 31", KONTO1_OPT_TEST, KONTO2 + 5000, KONTO3 - 50000, KONTO1 - 20000, KONTO2),
                Arguments.of("Test 32", KONTO1_OPT_TEST, KONTO2 + 5000, KONTO3, KONTO1 - 20000, KONTO2),
                Arguments.of("Test 33", KONTO1_OPT_TEST, KONTO2 + 5000, KONTO3 + 20000, KONTO1_OPT_VALUE, KONTO2),
                Arguments.of("Test 34", KONTO1, KONTO2 + 5000, KONTO3 - 50000, KONTO1, KONTO2 + 5000),
                Arguments.of("Test 35", KONTO1, KONTO2 + 5000, KONTO3, KONTO1, KONTO2 + 5000),
                Arguments.of("Test 36", KONTO1, KONTO2 + 5000, KONTO3 + 20000, KONTO1, KONTO2 + 5000),

                // KONTO 2/3 minus 20%
                Arguments.of("Test 37", KONTO1_MIN_TEST, newKonto2(-21), newKonto3(-21), KONTO1_OPT_VALUE - 10000, newKonto2(-21) - 30000),
                Arguments.of("Test 38", KONTO1_MIN_TEST, newKonto2(-21), newKonto3(-21), KONTO1_OPT_VALUE - 10000, newKonto2(-21) - 30000),
                Arguments.of("Test 39", KONTO1_MIN_TEST, newKonto2(-21), newKonto3(-21), KONTO1_OPT_VALUE - 10000, newKonto2(-21) - 30000),
                Arguments.of("Test 40", KONTO1_OPT_TEST, newKonto2(-21), newKonto3(-21), KONTO1 - 27250, newKonto2(-21) - 5000),
                Arguments.of("Test 41", KONTO1_OPT_TEST, newKonto2(-21), newKonto3(-21), KONTO1 - 27250, newKonto2(-21) - 5000),
                Arguments.of("Test 42", KONTO1_OPT_TEST, newKonto2(-21), newKonto3(-21), KONTO1 - 27250, newKonto2(-21) - 5000),
                Arguments.of("Test 43", KONTO1, newKonto2(-21), newKonto3(-21), KONTO1 - 4000, newKonto2(-21)),
                Arguments.of("Test 44", KONTO1, newKonto2(-21), newKonto3(-21), KONTO1 - 4000, newKonto2(-21)),
                Arguments.of("Test 45", KONTO1, newKonto2(-21), newKonto3(-21), KONTO1 - 4000, newKonto2(-21)),
                Arguments.of("Test 46", KONTO1 + 20000, newKonto2(-21), newKonto3(-21), KONTO1 + 15000, newKonto2(-21)),
                Arguments.of("Test 47", KONTO1 + 20000, newKonto2(-21), newKonto3(-21), KONTO1 + 15000, newKonto2(-21)),
                Arguments.of("Test 48", KONTO1 + 20000, newKonto2(-21), newKonto3(-21), KONTO1 + 15000, newKonto2(-21)),

                // KONTO 2/3 minus30%
                Arguments.of("Test 49", KONTO1_MIN_TEST, newKonto2(-31), newKonto3(-31), KONTO1_MIN_VALUE, newKonto2(-31) - 10000),
                Arguments.of("Test 50", KONTO1_MIN_TEST, newKonto2(-31), newKonto3(-31), KONTO1_MIN_VALUE, newKonto2(-31) - 10000),
                Arguments.of("Test 51", KONTO1_MIN_TEST, newKonto2(-31), newKonto3(-31), KONTO1_MIN_VALUE, newKonto2(-31) - 10000),
                Arguments.of("Test 52", KONTO1_OPT_TEST, newKonto2(-31), newKonto3(-31), KONTO1 - 25500, newKonto2(-31) - 5000),
                Arguments.of("Test 53", KONTO1_OPT_TEST, newKonto2(-31), newKonto3(-31), KONTO1 - 25500, newKonto2(-31) - 5000),
                Arguments.of("Test 54", KONTO1_OPT_TEST, newKonto2(-31), newKonto3(-31), KONTO1 - 25500, newKonto2(-31) - 5000),
                Arguments.of("Test 55", KONTO1, newKonto2(-31), newKonto3(-31), KONTO1 - 8000, newKonto2(-31)),
                Arguments.of("Test 56", KONTO1, newKonto2(-31), newKonto3(-31), KONTO1 - 8000, newKonto2(-31)),
                Arguments.of("Test 57", KONTO1, newKonto2(-31), newKonto3(-31), KONTO1 - 8000, newKonto2(-31)),
                Arguments.of("Test 58", KONTO1 + 20000, newKonto2(-31), newKonto3(-31), KONTO1 + 10000, newKonto2(-31)),
                Arguments.of("Test 59", KONTO1 + 20000, newKonto2(-31), newKonto3(-31), KONTO1 + 10000, newKonto2(-31)),
                Arguments.of("Test 60", KONTO1 + 20000, newKonto2(-31), newKonto3(-31), KONTO1 + 10000, newKonto2(-31))
        );
    }

    private static int newKonto2(double profit) {
        return (int)(KONTO2 + (KONTO2 * profit/100));
    }

    private static int newKonto3(double profit) {
        return (int)(KONTO3 + (KONTO3 * profit/100));
    }

    @ParameterizedTest
    @MethodSource("constellations")
    public void testConstellations(String title, int konto1, int konto2, int konto3, int minNewKonto1, int minNewKonto2) {
        PotsRebalancing result = rebalance(title, konto1, KONTO1, konto2, KONTO3, konto3, false);
        int newKonto1 = konto1 + result.getFrom2To1() + result.getFrom3To1() - result.getFrom1To3();
        int newKonto2 = konto2 - result.getFrom2To1() + result.getFrom3To2();
        int newKonto3 = konto3 - result.getFrom3To1() - result.getFrom3To2() + result.getFrom1To3();

        System.out.printf("Check Konto 1: %d >= %d\n", newKonto1, minNewKonto1);
        assertTrue(newKonto1 >= minNewKonto1, String.format("Konto 1 falsch: %d >= expected %d", newKonto1, minNewKonto1));
        System.out.printf("Check Konto 2: %d >= %d\n", newKonto2, minNewKonto2);
        assertTrue(newKonto2 >= minNewKonto2, String.format("Konto 2 falsch: %d >= expected %d", newKonto2, minNewKonto2));
        System.out.printf("Kontostand: Alt: %d (%d, %d, %d) = Neu: %d (%d, %d, %d)\n",
                konto1 + konto2 + konto3, konto1, konto2, konto3, newKonto1 + newKonto2 + newKonto3, newKonto1, newKonto2, newKonto3);
        assertEquals(konto1 + konto2 + konto3, newKonto1 + newKonto2 + newKonto3, "Kontostand falsch");
    }

    @Test
    public void testCrash() {
        int YEARLY_AMOUNT = 20000;
        int konto1 = KONTO1;
        int konto2 = KONTO2;
        int konto3 = KONTO3;
        double profit2 = -0.15;
        double profit3 = -0.3;
        int crashyears = 10;
        int aktien = KONTO3;

        System.out.printf("================= INITIAL =================%n");
        System.out.printf("Konto 1: %d\n", konto1);
        System.out.printf("Konto 2: %d\n", konto2);
        System.out.printf("Konto 3: %d\n", konto3);

        int lastKonto2 = konto2;
        int lastKonto3 = konto3;

        konto2 = (int)(konto2 + konto2 * profit2);
        konto3 = (int)(konto3 + konto3 * profit3);

        for (int i=1; i<=crashyears; i++) {
            konto1 = konto1 - YEARLY_AMOUNT;
            PotsRebalancing result = rebalance(String.format("Crash %d Jahr", i), konto1, lastKonto2, konto2, lastKonto3, konto3, true);
            lastKonto2 = konto2;
            lastKonto3 = konto3;
            konto1 = konto1 + result.getFrom2To1() + result.getFrom3To1() - result.getFrom1To3();
            konto2 = konto2 + result.getFrom3To2() - result.getFrom2To1();
            konto3 = konto3 + result.getFrom1To3() - result.getFrom3To1() - result.getFrom3To2();
            aktien = aktien - result.getFrom1To3() - result.getFrom3To1() - result.getFrom3To2();
            System.out.println("Aktien: " + aktien);
        }

        System.out.printf("\n================= AFTER CRASH =================%n");
        System.out.printf("Konto 1: %d\n", konto1);
        System.out.printf("Konto 2: %d\n", konto2);
        System.out.printf("Konto 3: %d\n", konto3);
    }

    private PotsRebalancing rebalance(String title, int konto1, int lastKonto2, int konto2, int lastKonto3, int konto3, boolean crash) {
        PotsRebalancer balancer = new PotsRebalancer(konto1, konto2, konto3,
                getProfit(lastKonto2, konto2), getProfit(lastKonto3, konto3), KONTO1_YEARLY_BEZUG);
        PotsRebalancing result = balancer.rebalancing();
        int newKonto1 = konto1 + result.getFrom2To1() + result.getFrom3To1() - result.getFrom1To3();
        int newKonto2 = konto2 - result.getFrom2To1() + result.getFrom3To2();
        int newKonto3 = konto3 - result.getFrom3To1() - result.getFrom3To2() + result.getFrom1To3();

        System.out.println();
        System.out.printf("================= %s =================%n", title);
        System.out.printf("Konto 1: %d (%s) (min: %d, opt: %d, max: %s)\n", newKonto1, getZuwachs(newKonto1, konto1),
                threshold.getKonto1Min(), threshold.getKonto1Opt(), threshold.getKonto1Max());
        System.out.printf("Konto 2: %d (%s) (min: %d, opt: %d, max: %d)\n", newKonto2, getZuwachs(newKonto2, konto2),
                threshold.getKonto2Min(), threshold.getKonto2Opt(), threshold.getKonto2Max());
        System.out.printf("Konto 3: %d (%s) (10: %d, 5: %d, -20: %d, -30: %d)\n", newKonto3, getZuwachs(newKonto3, konto3),
                KONTO3_PLUS10, KONTO3_PLUS5, KONTO3_MIN20, KONTO3_MIN30);

        return result;
    }

    private double getProfit(double oldBalance, double newBalance) {
        double profit = 0d;
        if (oldBalance > 0) {
            profit = (newBalance - oldBalance) / oldBalance * 100;
        }
        return profit;
    }

    private String getZuwachs(int newKonto, int konto) {
        int zuwachs = newKonto - konto;
        return zuwachs > 0? String.format("+%d", zuwachs): Integer.toString(zuwachs);
    }
}


