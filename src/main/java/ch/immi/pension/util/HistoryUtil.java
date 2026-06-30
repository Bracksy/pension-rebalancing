package ch.immi.pension.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HistoryUtil {
    private final static String HISTORY_TEMPLATE_PATH = "templates/history_template.html";

    private final static String CURRENT_BALANCE = "CURRENT_BALANCE";
    private final static String CURRENT_ACCOUNT_1 = "CURRENT_ACCOUNT_1";
    private final static String CURRENT_ACCOUNT_2 = "CURRENT_ACCOUNT_2";
    private final static String CURRENT_AMOUNT_3A = "CURRENT_AMOUNT_3A";
    private final static String CURRENT_PRICE_3A = "CURRENT_PRICE_3A";
    private final static String CURRENT_ACCOUNT_3A = "CURRENT_ACCOUNT_3A";
    private final static String CURRENT_AMOUNT_3B = "CURRENT_AMOUNT_3B";
    private final static String CURRENT_PRICE_3B = "CURRENT_PRICE_3B";
    private final static String CURRENT_ACCOUNT_3B = "CURRENT_ACCOUNT_3B";

    private final static String NEW_BALANCE = "NEW_BALANCE";
    private final static String NEW_ACCOUNT_1 = "NEW_ACCOUNT_1";
    private final static String NEW_ACCOUNT_2 = "NEW_ACCOUNT_2";
    private final static String NEW_AMOUNT_3A = "NEW_AMOUNT_3A";
    private final static String NEW_PRICE_3A = "NEW_PRICE_3A";
    private final static String NEW_ACCOUNT_3A = "NEW_ACCOUNT_3A";
    private final static String NEW_AMOUNT_3B = "NEW_AMOUNT_3B";
    private final static String NEW_PRICE_3B = "NEW_PRICE_3B";
    private final static String NEW_ACCOUNT_3B = "NEW_ACCOUNT_3B";

    private final static String REBALANCING_OUTPUT = "REBALANCING_OUTPUT";

    public static @NotNull String fill(@NotNull ThreePotState state, @NotNull ThreePotState newState, String output)
            throws IOException, URISyntaxException, IllegalArgumentException {
        String template = Files.readString(Paths.get(HISTORY_TEMPLATE_PATH));
        int current_balance = state.getPot1() + state.getPot2() + (int)(state.getNumOfShares3a() * state.getPrice3a())
                + (int)(state.getNumOfShares3b() * state.getPrice3b());
        int account3a = (int)(state.getNumOfShares3a() * state.getPrice3a());
        int account3b = (int)(state.getNumOfShares3b() * state.getPrice3b());

        int new_balance = newState.getPot1() + newState.getPot2() + (int)(newState.getNumOfShares3a() * newState.getPrice3a())
                + (int)(newState.getNumOfShares3b() * newState.getPrice3b());
        int newAccount3a = (int)(newState.getNumOfShares3a() * newState.getPrice3a());
        int newAccount3b = (int)(newState.getNumOfShares3b() * newState.getPrice3b());

        return template
                .replace(CURRENT_BALANCE, FormatUtil.getDecimalFormatter().format(current_balance))
                .replace(CURRENT_ACCOUNT_1, FormatUtil.getDecimalFormatter().format(state.getPot1()))
                .replace(CURRENT_ACCOUNT_2, FormatUtil.getDecimalFormatter().format(state.getPot2()))
                .replace(CURRENT_AMOUNT_3A, FormatUtil.getDecimalFormatter().format(state.getNumOfShares3a()))
                .replace(CURRENT_PRICE_3A, FormatUtil.getDoubleFormatter().format(state.getPrice3a()))
                .replace(CURRENT_ACCOUNT_3A, FormatUtil.getDecimalFormatter().format(account3a))
                .replace(CURRENT_AMOUNT_3B, FormatUtil.getDecimalFormatter().format(state.getNumOfShares3b()))
                .replace(CURRENT_PRICE_3B, FormatUtil.getDoubleFormatter().format(state.getPrice3b()))
                .replace(CURRENT_ACCOUNT_3B, FormatUtil.getDecimalFormatter().format(account3b))
                .replace(NEW_BALANCE, FormatUtil.getDecimalFormatter().format(new_balance))
                .replace(NEW_ACCOUNT_1, FormatUtil.getDecimalFormatter().format(newState.getPot1()))
                .replace(NEW_ACCOUNT_2, FormatUtil.getDecimalFormatter().format(newState.getPot2()))
                .replace(NEW_AMOUNT_3A, FormatUtil.getDecimalFormatter().format(newState.getNumOfShares3a()))
                .replace(NEW_PRICE_3A, FormatUtil.getDoubleFormatter().format(newState.getPrice3a()))
                .replace(NEW_ACCOUNT_3A, FormatUtil.getDecimalFormatter().format(newAccount3a))
                .replace(NEW_AMOUNT_3B, FormatUtil.getDecimalFormatter().format(newState.getNumOfShares3b()))
                .replace(NEW_PRICE_3B, FormatUtil.getDoubleFormatter().format(newState.getPrice3b()))
                .replace(NEW_ACCOUNT_3B, FormatUtil.getDecimalFormatter().format(newAccount3b))
                .replace(REBALANCING_OUTPUT, output);
    }

    public static void write(String configuration, @NotNull String text) throws IOException, URISyntaxException, IllegalArgumentException {
        Files.write(Paths.get(configuration + ".html"), text.getBytes());
    }
}
