package ch.immi.pension.util;

public class ThreePotSystem {
    final PotThreshold potThreshold;
    final double pot3aPercentage;
    final double pot3bPercentage;

    public ThreePotSystem(PotThreshold potThreshold, double pot3aPercentage, double pot3bPercentage) {
        this.potThreshold = potThreshold;
        this.pot3aPercentage = pot3aPercentage;
        this.pot3bPercentage = pot3bPercentage;
    }

    public void updateState(ThreePotState state) {
        if (state.getPot1() < potThreshold.getPot1Min()) {
            if (state.getPot2() < potThreshold.getPot2Min()) {
                if (canSellShares(state)) {
                    sellSharesToOpt(state, potThreshold);
                } else {
                    int pot1ToMinAmount = potThreshold.getPot1Min() - state.getPot1();
                    sellSharesForPots(state, pot1ToMinAmount, 0);
                }
            } else if (state.getPot2() < potThreshold.getPot2Opt()) {
                if (canSellShares(state)) {
                    sellSharesToOpt(state, potThreshold);
                } else {
                    int pot1ToMinAmount = potThreshold.getPot1Min() - state.getPot1();
                    state.moveFrom2To1(pot1ToMinAmount);
                }
            } else if (state.getPot2() < potThreshold.getPot2Max()) {
                if (canSellShares(state)) {
                    pot1OptWithPot2AndShares(state, potThreshold);
                } else {
                    int pot1ToOptAmount = potThreshold.getPot1Opt() - state.getPot1();
                    state.moveFrom2To1(pot1ToOptAmount);
                }
            } else {
                int pot2DownToOpt = state.getPot2() -  potThreshold.getPot2Opt();
                buySharesFromPot2(state, pot2DownToOpt);
                if (state.getPot2() > potThreshold.getPot2Max()) {
                    int pot1ToOptAmount = potThreshold.getPot1Opt() - state.getPot1();
                    if (canSellShares(state)) {
                        sellSharesForPots(state, pot1ToOptAmount, 0);
                    } else {
                        state.moveFrom2To1(pot1ToOptAmount);
                    }
                }
            }
        } else if (state.getPot1() < potThreshold.getPot1Opt()) {
            if (state.getPot2() < potThreshold.getPot2Min()) {
                if (canSellShares(state)) {
                    sellSharesToOpt(state, potThreshold);
                } else {
                    // Do nothing
                }
            } else if (state.getPot2() < potThreshold.getPot2Opt()) {
                if (canSellShares(state)) {
                    sellSharesToOpt(state, potThreshold);
                } else {
                    // Do nothing
                }
            } else if (state.getPot2() < potThreshold.getPot2Max()) {
                int pot1ToOptAmount = potThreshold.getPot1Opt() - state.getPot1();
                state.moveFrom2To1(pot1ToOptAmount);
                if (canSellShares(state) && state.getPot2() < potThreshold.getPot2Opt()) {
                    int pot2ToOptAmount = potThreshold.getPot2Opt() - state.getPot2();
                    sellSharesForPots(state, 0, pot2ToOptAmount);
                }
            } else {
                int pot2DownToOpt = state.getPot2() -  potThreshold.getPot2Opt();
                buySharesFromPot2(state,  pot2DownToOpt);
                if (state.getPot2() > potThreshold.getPot2Max()) {
                    int pot1ToOptAmount = potThreshold.getPot1Opt() - state.getPot1();
                    if (canSellShares(state)) {
                        sellSharesForPots(state, pot1ToOptAmount, 0);
                    } else {
                        state.moveFrom2To1(pot1ToOptAmount);
                    }
                }
            }
        } else {
            if (state.getPot2() < potThreshold.getPot2Min()) {
                if (canSellShares(state)) {
                    pot2OptWithPot1AndShares(state, potThreshold);
                } else {
                    // Nothing
                }
            } else if (state.getPot2() < potThreshold.getPot2Opt()) {
                if (canSellShares(state)) {
                    pot2OptWithPot1AndShares(state, potThreshold);
                } else {
                    // Nothing
                }
            } else if (state.getPot2() < potThreshold.getPot2Max()) {
                // Nothing
            } else {
                int pot2DownToOptAmount = state.getPot2() - potThreshold.getPot2Opt();
                buySharesFromPot2(state, pot2DownToOptAmount);
            }
        }
        updateHighestPrice(state);
    }

    private void updateHighestPrice(ThreePotState state) {
        double newHighestPrice3a = Math.max(state.getPrice3a(), state.getHighestPrice3a());
        if (state.getHighestPrice3a() < newHighestPrice3a) {
            state.setHighestPrice3a(newHighestPrice3a);
        }
        double newHighestPrice3b = Math.max(state.getPrice3b(), state.getHighestPrice3b());
        if (state.getHighestPrice3b() < newHighestPrice3b) {
            state.setHighestPrice3b(newHighestPrice3b);
        }
    }

    private boolean canSellShares(ThreePotState state) {
        boolean canSell3a = state.getPrice3a() >= state.getHighestPrice3a();
        boolean canSell3b = state.getPrice3b() >= state.getHighestPrice3b();
        return canSell3a && canSell3b;
    }

    private void sellSharesToOpt(ThreePotState state, PotThreshold potThreshold) {
        int pot1ToOptAmount = potThreshold.getPot1Opt() - state.getPot1();
        int pot2ToOptAmount = potThreshold.getPot2Opt() - state.getPot2();
        sellSharesForPots(state, pot1ToOptAmount, pot2ToOptAmount);
    }

    private void pot1OptWithPot2AndShares(ThreePotState state, PotThreshold potThreshold) {
        int pot2DownToOptAmount = state.getPot2() - potThreshold.getPot2Opt();
        state.moveFrom2To1(pot2DownToOptAmount);
        if (state.getPot1() < potThreshold.getPot1Opt()) {
            int pot1ToOptAmount = potThreshold.getPot1Opt() - state.getPot1();
            sellSharesForPots(state, pot1ToOptAmount, 0);
        }
    }

    private void pot2OptWithPot1AndShares(ThreePotState state, PotThreshold potThreshold) {
        int pot1DownToOptAmount = state.getPot1() - potThreshold.getPot1Opt();
        state.moveFrom2To1(pot1DownToOptAmount);
        if (state.getPot2() < potThreshold.getPot2Opt()) {
            int pot2ToOptAmount = potThreshold.getPot2Opt() - state.getPot2();
            sellSharesForPots(state, 0, pot2ToOptAmount);
        }
    }

    private void sellSharesForPots(ThreePotState state, int pot1Amount, int pot2Amount) {
        if (pot1Amount > 0) {
            int sharesToSell3a = (int)((pot1Amount * pot3aPercentage) / state.getPrice3a()) + 1;
            int sharesToSell3b = (int)((pot1Amount * pot3bPercentage) / state.getPrice3b());
            if (sharesToSell3b > 0) {
                sharesToSell3b += 1;
            }
            state.moveFrom3To1(sharesToSell3a, sharesToSell3b);
        }
        if (pot2Amount > 0) {
            int sharesToSell3a = (int)((pot2Amount * pot3aPercentage) / state.getPrice3a()) + 1;
            int sharesToSell3b = (int)((pot2Amount * pot3bPercentage) / state.getPrice3b());
            if (sharesToSell3b > 0) {
                sharesToSell3b += 1;
            }
            state.moveFrom3To2(sharesToSell3a, sharesToSell3b);
        }
    }

    private void buySharesFromPot2(ThreePotState state, int pot2Amount) {
        if (pot2Amount > 0) {
            int sharesToBuy3a = (int)(pot2Amount * pot3aPercentage / state.getPrice3a());
            int sharesToBuy3b = (int)(pot2Amount * pot3bPercentage / state.getPrice3b());
            state.moveFrom2To3(sharesToBuy3a, sharesToBuy3b);
        }
    }
}
