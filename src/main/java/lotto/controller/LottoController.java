package lotto.controller;

import static lotto.util.Utils.stringToInt;
import static lotto.util.Utils.stringToIntegerSortedList;
import static lotto.util.Validator.validateBonusNumber;
import static lotto.util.Validator.validateMoney;
import static lotto.util.Validator.validateWinningNumber;
import static lotto.view.OutputMessage.REQUEST_BONUS_NUMBER;
import static lotto.view.OutputMessage.REQUEST_PURCHASE_AMOUNT;
import static lotto.view.OutputMessage.REQUEST_WINNING_NUMBER;

import java.util.List;
import lotto.service.LottoDto;
import lotto.service.LottoService;
import lotto.service.ResultDto;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoController {
    private final InputView inputView;
    private final OutputView outputView;
    private final LottoService lottoService;

    public LottoController(InputView inputView, OutputView outputView, LottoService lottoService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.lottoService = lottoService;
    }

    public void run() {
        buyLotto();
        setWinningNumbers();
        showResults();
    }

    public void buyLotto() {
        int lottoQuantity = lottoService.calculateLotto(getPurchaseAmount());
        outputView.printPurchaseResult(lottoQuantity);

        LottoDto lottoDto = lottoService.issueLotto(lottoQuantity);
        outputView.printIssuedLottos(lottoDto.getLottos());
    }

    public void setWinningNumbers() {
        List<Integer> winningNumber = getWinningNumber();
        int bonusNumber = getBonusNumber(winningNumber);
        lottoService.saveWinningLotto(winningNumber, bonusNumber);
    }

    public void showResults() {
        outputView.printWinningStatistics();
        ResultDto resultDto = lottoService.getResult();
        outputView.printWinningDetails(resultDto.getResult());
        outputView.printTotalReturn(resultDto.getReturnRate());
    }

    public final int getPurchaseAmount() {
        try {
            outputView.printMessage(REQUEST_PURCHASE_AMOUNT.getMessage());
            String money = inputView.inputMessage();
            validateMoney(money);
            return stringToInt(money);
        } catch (IllegalArgumentException exception) {
            outputView.printMessage(exception.getMessage());
            return getPurchaseAmount();
        }
    }

    public final List<Integer> getWinningNumber() {
        try {
            outputView.printMessage(REQUEST_WINNING_NUMBER.getMessage());
            String winningNumber = inputView.inputMessage();
            validateWinningNumber(winningNumber);
            return stringToIntegerSortedList(winningNumber);
        } catch (IllegalArgumentException exception) {
            outputView.printMessage(exception.getMessage());
            return getWinningNumber();
        }
    }

    public final int getBonusNumber(List<Integer> winningNumber) {
        try {
            outputView.printMessage(REQUEST_BONUS_NUMBER.getMessage());
            String bonusNumber = inputView.inputMessage();
            validateBonusNumber(winningNumber, bonusNumber);
            return stringToInt(bonusNumber);
        } catch (IllegalArgumentException exception) {
            outputView.printMessage(exception.getMessage());
            return getBonusNumber(winningNumber);
        }
    }
}
