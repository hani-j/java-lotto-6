package lotto.util;

import static lotto.domain.LottoRule.MAXIMUM;
import static lotto.domain.LottoRule.MINIMUM;
import static lotto.domain.LottoRule.PRICE;
import static lotto.domain.LottoRule.SIZE;
import static lotto.util.ErrorMessage.INPUT_DUPLICATE_NUMBER;
import static lotto.util.ErrorMessage.INPUT_NOT_IN_RANGE;
import static lotto.util.ErrorMessage.INPUT_NOT_NUMBER;
import static lotto.util.ErrorMessage.INPUT_NOT_THOUSAND_UNIT;
import static lotto.util.ErrorMessage.INPUT_OUT_OF_SIZE;
import static lotto.util.ErrorMessage.INPUT_TOO_MUCH_MONEY;

import java.util.Arrays;

public class Validator {

    public void validateMoney(final String money) {
        validateNumber(money);
        validateThousandUnit(Integer.parseInt(money));
        validateAmount(Long.parseLong(money));
    }

    private void validateNumber(final String number) {
        final String REGEX = "[0-9]+";
        if (!number.matches(REGEX)) {
            throw new IllegalArgumentException(INPUT_NOT_NUMBER.getMessage());
        }
    }

    private void validateThousandUnit(final int money) {
        if (money <= 0 || money % PRICE.getValue() != 0) {
            throw new IllegalArgumentException(INPUT_NOT_THOUSAND_UNIT.getMessage());
        }
    }

    private void validateAmount(final Long money) {
        if (money > 2000000000) {
            throw new IllegalArgumentException(INPUT_TOO_MUCH_MONEY.getMessage());
        }
    }

    public void validateWinningNumber(final String lotto) {
        String[] lottoNumbers = lotto.split(",", -1);
        validateSize(lottoNumbers);
        validateDuplicate(lottoNumbers);
        Arrays.stream(lottoNumbers).forEach(number -> validateOneNumber(number));
    }

    private void validateOneNumber(final String number) {
//        validateNull(number);
        validateNumber(number);
        validateNumberInRange(Integer.parseInt(number));
    }

//    private void validateNull(final String number) {
//        if (number == null) {
//            throw new IllegalArgumentException(INPUT_NULL.getMessage());
//        }
//    }

    private void validateNumberInRange(final int number) {
        if (number < MINIMUM.getValue() || number > MAXIMUM.getValue()) {
            throw new IllegalArgumentException(INPUT_NOT_IN_RANGE.getMessage());
        }
    }

    private void validateSize(String[] lotto) {
        if (lotto.length != SIZE.getValue()) {
            throw new IllegalArgumentException(INPUT_OUT_OF_SIZE.getMessage());
        }
    }

    private void validateDuplicate(String[] lotto) {
        if (Arrays.stream(lotto).distinct().count() != lotto.length) {
            throw new IllegalArgumentException(INPUT_DUPLICATE_NUMBER.getMessage());
        }
    }
}
