package ch.immi.pension;

import ch.immi.pension.data.PotsRebalancing;
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
    static private int KONTO1 = 80000;
    static private int KONTO2 = 120000;
    static private int KONTO3 = 300000;

    static private int KONTO1_CRI = 30000;
    static private int KONTO1_MIN = 60000;

    static private int KONTO2_CRI = 60000;
    static private int KONTO2_MIN = 90000;
    static private int KONTO2_MAX = 160000;

    static private int KONTO3_PLUS10 = 5;
    static private int KONTO3_PLUS5 = 0;
    static private int KONTO3_MIN20 = 5;
    static private int KONTO3_MIN30 = 10;

    private static Stream<Arguments> constellations() {
        final int KONTO1_CRI_TEST = KONTO1_CRI - 1000;
        final int KONTO1_MIN_TEST = KONTO1_MIN - 1000;
        final int KONTO2_CRI_TEST = KONTO2_CRI - 1000;
        final int KONTO2_MIN_TEST = KONTO2_MIN - 1000;
        return Stream.of(
                // KONTO 2 critical
                Arguments.of("Test 1", KONTO1_CRI_TEST, KONTO2_CRI_TEST, KONTO3 - 50000, KONTO1_CRI, KONTO2_CRI),
                Arguments.of("Test 2", KONTO1_CRI_TEST, KONTO2_CRI_TEST, KONTO3, KONTO1_CRI, KONTO2_CRI),
                Arguments.of("Test 3", KONTO1_CRI_TEST, KONTO2_CRI_TEST, KONTO3 + 20000, KONTO1_CRI, KONTO2_CRI),
                Arguments.of("Test 4", KONTO1_MIN_TEST, KONTO2_CRI_TEST, KONTO3 - 50000, KONTO1_MIN_TEST, KONTO2_CRI),
                Arguments.of("Test 5", KONTO1_MIN_TEST, KONTO2_CRI_TEST, KONTO3, KONTO1_MIN_TEST, KONTO2_CRI),
                Arguments.of("Test 6", KONTO1_MIN_TEST, KONTO2_CRI_TEST, KONTO3 + 20000, KONTO1_MIN_TEST, KONTO2_CRI),
                Arguments.of("Test 7", KONTO1, KONTO2_CRI_TEST, KONTO3 - 50000, KONTO1, KONTO2_CRI),
                Arguments.of("Test 8", KONTO1, KONTO2_CRI_TEST, KONTO3, KONTO1, KONTO2_CRI),
                Arguments.of("Test 9", KONTO1, KONTO2_CRI_TEST, KONTO3 + 20000, KONTO1, KONTO2_CRI),

                // KONTO 2 minimum
                Arguments.of("Test 10", KONTO1_CRI_TEST, KONTO2_MIN_TEST, KONTO3 - 50000, KONTO1_MIN - 10000, KONTO2_MIN_TEST - 21000),
                Arguments.of("Test 11", KONTO1_CRI_TEST, KONTO2_MIN_TEST, KONTO3, KONTO1_MIN - 10000, KONTO2_MIN_TEST - 21000),
                Arguments.of("Test 12", KONTO1_CRI_TEST, KONTO2_MIN_TEST, KONTO3 + 20000, KONTO1_MIN - 10000, KONTO2_MIN_TEST - 21000),
                Arguments.of("Test 13", KONTO1_MIN_TEST, KONTO2_MIN_TEST, KONTO3 - 50000, KONTO1_MIN, KONTO2_MIN_TEST - 1000),
                Arguments.of("Test 14", KONTO1_MIN_TEST, KONTO2_MIN_TEST, KONTO3, KONTO1_MIN, KONTO2_MIN_TEST - 1000),
                Arguments.of("Test 15", KONTO1_MIN_TEST, KONTO2_MIN_TEST, KONTO3 + 20000, KONTO1_MIN, KONTO2_MIN_TEST + 1000),
                Arguments.of("Test 16", KONTO1, KONTO2_MIN_TEST, KONTO3 - 50000, KONTO1, KONTO2_MIN_TEST),
                Arguments.of("Test 17", KONTO1, KONTO2_MIN_TEST, KONTO3, KONTO1, KONTO2_MIN_TEST),
                Arguments.of("Test 18", KONTO1, KONTO2_MIN_TEST, KONTO3 + 20000, KONTO1, KONTO2_MIN_TEST + 1000),

                // KONTO 2 unchanged
                Arguments.of("Test 19", KONTO1 - 60000, KONTO2, KONTO3 - 50000, KONTO1_MIN, KONTO2 - 40000),
                Arguments.of("Test 20", KONTO1 - 60000, KONTO2, KONTO3, KONTO1_MIN, KONTO2 - 40000),
                Arguments.of("Test 21", KONTO1 - 60000, KONTO2, KONTO3 + 20000, KONTO1_MIN, KONTO2 - 10000),
                Arguments.of("Test 22", KONTO1 - 25000, KONTO2, KONTO3 - 50000, KONTO1 - 20000, KONTO2 - 5000),
                Arguments.of("Test 23", KONTO1 - 25000, KONTO2, KONTO3, KONTO1 - 25000, KONTO2 - 5000),
                Arguments.of("Test 24", KONTO1 - 25000, KONTO2, KONTO3 + 20000, KONTO1_MIN, KONTO2 - 5000),
                Arguments.of("Test 25", KONTO1, KONTO2, KONTO3 - 50000, KONTO1, KONTO2),
                Arguments.of("Test 26", KONTO1, KONTO2, KONTO3, KONTO1, KONTO2),
                Arguments.of("Test 27", KONTO1, KONTO2, KONTO3 + 20000, KONTO1, KONTO2),

                // KONTO 2 plus 5000
                Arguments.of("Test 28", KONTO1 - 60000, KONTO2 + 5000, KONTO3 - 50000, KONTO1_MIN, KONTO2 - 35000),
                Arguments.of("Test 29", KONTO1 - 60000, KONTO2 + 5000, KONTO3, KONTO1_MIN, KONTO2 - 35000),
                Arguments.of("Test 30", KONTO1 - 60000, KONTO2 + 5000, KONTO3 + 20000, KONTO1_MIN, KONTO2),
                Arguments.of("Test 31", KONTO1 - 25000, KONTO2 + 5000, KONTO3 - 50000, KONTO1 - 20000, KONTO2),
                Arguments.of("Test 32", KONTO1 - 25000, KONTO2 + 5000, KONTO3, KONTO1 - 20000, KONTO2),
                Arguments.of("Test 33", KONTO1 - 25000, KONTO2 + 5000, KONTO3 + 20000, KONTO1_MIN, KONTO2),
                Arguments.of("Test 34", KONTO1, KONTO2 + 5000, KONTO3 - 50000, KONTO1, KONTO2 + 5000),
                Arguments.of("Test 35", KONTO1, KONTO2 + 5000, KONTO3, KONTO1, KONTO2 + 5000),
                Arguments.of("Test 36", KONTO1, KONTO2 + 5000, KONTO3 + 20000, KONTO1, KONTO2 + 5000),

                // KONTO 2/3 minus 20%
                Arguments.of("Test 37", KONTO1 - 60000, newKonto2(-21), newKonto3(-21), KONTO1_MIN - 10000, newKonto2(-21) - 30000),
                Arguments.of("Test 38", KONTO1 - 60000, newKonto2(-21), newKonto3(-21), KONTO1_MIN - 10000, newKonto2(-21) - 30000),
                Arguments.of("Test 39", KONTO1 - 60000, newKonto2(-21), newKonto3(-21), KONTO1_MIN - 10000, newKonto2(-21) - 30000),
                Arguments.of("Test 40", KONTO1 - 25000, newKonto2(-21), newKonto3(-21), KONTO1 - 27250, newKonto2(-21) - 5000),
                Arguments.of("Test 41", KONTO1 - 25000, newKonto2(-21), newKonto3(-21), KONTO1 - 27250, newKonto2(-21) - 5000),
                Arguments.of("Test 42", KONTO1 - 25000, newKonto2(-21), newKonto3(-21), KONTO1 - 27250, newKonto2(-21) - 5000),
                Arguments.of("Test 43", KONTO1, newKonto2(-21), newKonto3(-21), KONTO1 - 4000, newKonto2(-21)),
                Arguments.of("Test 44", KONTO1, newKonto2(-21), newKonto3(-21), KONTO1 - 4000, newKonto2(-21)),
                Arguments.of("Test 45", KONTO1, newKonto2(-21), newKonto3(-21), KONTO1 - 4000, newKonto2(-21)),
                Arguments.of("Test 46", KONTO1 + 20000, newKonto2(-21), newKonto3(-21), KONTO1 + 15000, newKonto2(-21)),
                Arguments.of("Test 47", KONTO1 + 20000, newKonto2(-21), newKonto3(-21), KONTO1 + 15000, newKonto2(-21)),
                Arguments.of("Test 48", KONTO1 + 20000, newKonto2(-21), newKonto3(-21), KONTO1 + 15000, newKonto2(-21)),

                // KONTO 2/3 minus30%
                Arguments.of("Test 49", KONTO1 - 60000, newKonto2(-31), newKonto3(-31), KONTO1_CRI, newKonto2(-31) - 10000),
                Arguments.of("Test 50", KONTO1 - 60000, newKonto2(-31), newKonto3(-31), KONTO1_CRI, newKonto2(-31) - 10000),
                Arguments.of("Test 51", KONTO1 - 60000, newKonto2(-31), newKonto3(-31), KONTO1_CRI, newKonto2(-31) - 10000),
                Arguments.of("Test 52", KONTO1 - 25000, newKonto2(-31), newKonto3(-31), KONTO1 - 25500, newKonto2(-31) - 5000),
                Arguments.of("Test 53", KONTO1 - 25000, newKonto2(-31), newKonto3(-31), KONTO1 - 25500, newKonto2(-31) - 5000),
                Arguments.of("Test 54", KONTO1 - 25000, newKonto2(-31), newKonto3(-31), KONTO1 - 25500, newKonto2(-31) - 5000),
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
        PotsRebalancer balancer = new PotsRebalancer(konto1, konto2, konto3, getProfit(KONTO2, konto2), getProfit(KONTO3, konto3));
        balancer.setKonto1Params(KONTO1_CRI, KONTO1_MIN);
        balancer.setKonto2Params(KONTO2_CRI, KONTO2_MIN, KONTO2_MAX);
        balancer.setKonto3Params(KONTO3_PLUS10, KONTO3_PLUS5, KONTO3_MIN20, KONTO3_MIN30);

        PotsRebalancing result = rebalance(title, konto1, konto2, konto3);
        int newKonto1 = konto1 + result.getFrom2To1() + result.getFrom3To1() - result.getFrom1To3();
        int newKonto2 = konto2 - result.getFrom2To1() + result.getFrom3To2();
        int newKonto3 = konto3 - result.getFrom3To1() - result.getFrom3To2() + result.getFrom1To3();

        System.out.println(String.format("Check Konto 1: %d >= %d", newKonto1, minNewKonto1));
        assertTrue(newKonto1 >= minNewKonto1, String.format("Konto 1 falsch: %d >= expected %d", newKonto1, minNewKonto1));
        System.out.println(String.format("Check Konto 2: %d >= %d", newKonto2, minNewKonto2));
        assertTrue(newKonto2 >= minNewKonto2, String.format("Konto 2 falsch: %d >= expected %d", newKonto2, minNewKonto2));
        System.out.println(String.format("Kontostand: Alt: %d (%d, %d, %d) = Neu: %d (%d, %d, %d)",
                konto1 + konto2 + konto3, konto1, konto2, konto3, newKonto1 + newKonto2 + newKonto3, newKonto1, newKonto2, newKonto3));
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
        int crashyears = 8;
        int aktien = KONTO3;

        for (int i=1; i<=crashyears; i++) {
            konto1 = konto1 - YEARLY_AMOUNT;
            konto2 = (int)(konto2 + konto2 * profit2);
            konto3 = (int)(konto3 + konto3 * profit3);
            PotsRebalancing result = rebalance(String.format("Crash %d Jahr", i), konto1, konto2, konto3);
            konto1 = konto1 + result.getFrom2To1() + result.getFrom3To1() - result.getFrom1To3();
            konto2 = konto2 + result.getFrom3To2() - result.getFrom2To1();
            konto3 = konto3 + result.getFrom1To3() - result.getFrom3To1() - result.getFrom3To2();
            aktien = aktien - result.getFrom1To3() - result.getFrom3To1() - result.getFrom3To2();
            System.out.println("Aktien: " + aktien);
        }
    }

    private PotsRebalancing rebalance(String title, int konto1, int konto2, int konto3) {
        PotsRebalancer balancer = new PotsRebalancer(konto1, konto2, konto3, getProfit(KONTO2, konto2), getProfit(KONTO3, konto3));
        balancer.setKonto1Params(KONTO1_CRI, KONTO1_MIN);
        balancer.setKonto2Params(KONTO2_CRI, KONTO2_MIN, KONTO2_MAX);
        balancer.setKonto3Params(KONTO3_PLUS10, KONTO3_PLUS5, KONTO3_MIN20, KONTO3_MIN30);

        PotsRebalancing result = balancer.rebalancing();
        int newKonto1 = konto1 + result.getFrom2To1() + result.getFrom3To1() - result.getFrom1To3();
        int newKonto2 = konto2 - result.getFrom2To1() + result.getFrom3To2();
        int newKonto3 = konto3 - result.getFrom3To1() - result.getFrom3To2() + result.getFrom1To3();

        System.out.println();
        System.out.println(String.format("================= %s =================", title));
        System.out.println(String.format("Konto 1: %d (%s) (cri: %d, min: %d)", newKonto1, getZuwachs(newKonto1, konto1),
                KONTO1_CRI, KONTO1_MIN));
        System.out.println(String.format("Konto 2: %d (%s) (cri: %d, min: %d, max: %d)", newKonto2, getZuwachs(newKonto2, konto2),
                KONTO2_CRI, KONTO2_MIN, KONTO2_MAX));
        System.out.println(String.format("Konto 3: %d (%s) (10: %d, 5: %d, -20: %d, -30: %d)", newKonto3, getZuwachs(newKonto3, konto3),
                KONTO3_PLUS10, KONTO3_PLUS5, KONTO3_MIN20, KONTO3_MIN30));

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


