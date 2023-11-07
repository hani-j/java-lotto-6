package lotto.domain.lotto;

import static lotto.domain.lotto.LottoCriteria.getAllValues;
import static lotto.domain.lotto.LottoRule.PERCENT;
import static lotto.domain.lotto.LottoRule.PRICE;
import static lotto.util.Utils.getRoundUpTwoDecimalPlace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LottoResult {

    private final WinningLotto winningLotto;
    private final List<Lotto> issuedLotto;
    private Map<LottoCriteria, Long> rankingResult;

    public LottoResult(final WinningLotto winningLotto, final List<Lotto> issuedLotto) {
        this.winningLotto = winningLotto;
        this.issuedLotto = issuedLotto;
        initRankingResult();
        saveResult();
    }

    private void initRankingResult() {
        rankingResult = new HashMap<>();

        getAllValues().stream()
                .forEach(rank -> rankingResult.put(rank, 0L));
    }

    private void saveResult() {
        issuedLotto.stream().forEach(lotto -> matchingNumber(lotto));
    }

    public void matchingNumber(final Lotto lotto) {
        Long count = getMatchingCount(lotto);

        getAllValues().stream()
                .forEach(rank -> incrementResult(count, rank, lotto));
    }

    private Long getMatchingCount(final Lotto lotto) {
        return lotto.getNumbers().stream()
                .filter(winningLotto.lotto().getNumbers()::contains)
                .count();
    }

    private void incrementResult(final Long count, final LottoCriteria rank, final Lotto lotto) {
        if (matchRank(count, rank, lotto)) {
            rankingResult.merge(rank, 1L, Long::sum);
        }
    }

    private boolean matchRank(final Long count, final LottoCriteria rank, final Lotto lotto) {
        if ((count == rank.getMatchNumber() && !rank.hasBonus()) ||
                isSecondPlace(count, rank, lotto)) {
            return true;
        }
        return false;
    }

    private boolean isSecondPlace(final Long count, final LottoCriteria rank, final Lotto lotto) {
        return count == rank.getMatchNumber() && rank.hasBonus() && isBonusContain(lotto);
    }

    public final boolean isBonusContain(final Lotto lotto) {
        return lotto.getNumbers().contains(winningLotto.bonus());
    }

    public final float getReturnRate() {
        Long totalAmount = getTotalAmount();
        float returnRate = (float) totalAmount / getInvestMoney() * PERCENT.getValue();

        return getRoundUpTwoDecimalPlace(returnRate);
    }

    private Long getTotalAmount() {
        return getAllValues().stream()
                .map(rank -> rankingResult.get(rank) * rank.getAmount())
                .reduce(0L, Long::sum);
    }

    private int getInvestMoney() {
        return issuedLotto.size() * PRICE.getValue();
    }

    public final Map<LottoCriteria, Long> getRankingResult() {
        return rankingResult;
    }
}