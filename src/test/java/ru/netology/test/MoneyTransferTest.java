package ru.netology.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.netology.data.DataHelper.*;


public class MoneyTransferTest {
    DashboardPage dashboard;
    CardInfo firstCardInfo;
    CardInfo secondCardInfo;
    int balanceFirstCard;
    int balanceSecondCard;

    @BeforeEach
    void setupForTransferMoneyBetweenOwnCards() {
        var info = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(info);
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(info);
        dashboard = verificationPage.validVerify(verificationCode);
        firstCardInfo = DataHelper.getFirstCardInfo();
        secondCardInfo = DataHelper.getSecondCardInfo();
        balanceFirstCard = dashboard.getCardBalance(firstCardInfo);
        balanceSecondCard = dashboard.getCardBalance(secondCardInfo);
    }

    @Test
    void transferFirstToSecondTest() {
        var amount = generateValidAmount(balanceFirstCard);
        var expectedBalanceFirstCard = balanceFirstCard - amount;
        var expectedBalanceSecondCard = balanceSecondCard + amount;
        var transferPage = dashboard.selectedCardForTransfer(secondCardInfo);
        dashboard = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        dashboard.reloadDashboardPage();
        dashboard.checkBalance(firstCardInfo, expectedBalanceFirstCard);
        dashboard.checkBalance(secondCardInfo, expectedBalanceSecondCard);
        assertAll(() -> dashboard.checkBalance(firstCardInfo, expectedBalanceFirstCard),
                () -> dashboard.checkBalance(secondCardInfo, expectedBalanceSecondCard));
    }

    @Test
    void transferSecondToFirstTest() {
        var amount = generateValidAmount(balanceSecondCard);
        var expectedBalanceFirstCard = balanceFirstCard + amount;
        var expectedBalanceSecondCard = balanceSecondCard - amount;
        var transferPage = dashboard.selectedCardForTransfer(firstCardInfo);
        dashboard = transferPage.makeValidTransfer(String.valueOf(amount), secondCardInfo);
        dashboard.reloadDashboardPage();
        dashboard.checkBalance(firstCardInfo, expectedBalanceFirstCard);
        dashboard.checkBalance(secondCardInfo, expectedBalanceSecondCard);
        assertAll(() -> dashboard.checkBalance(firstCardInfo, expectedBalanceFirstCard),
                () -> dashboard.checkBalance(secondCardInfo, expectedBalanceSecondCard));
    }

    @Test
    void errorMessageIfAmountMoreBalanceTest() {
        var amount = generateInvalidAmount(balanceSecondCard);
        var transferPage = dashboard.selectedCardForTransfer(firstCardInfo);
        transferPage.transfer(String.valueOf(amount), secondCardInfo);
        assertAll(() -> transferPage.errorMessage("Выполнена попытка перевода суммы, превышающей остаток на карте списания"),
                () -> dashboard.reloadDashboardPage(),
                () -> dashboard.checkBalance(firstCardInfo, balanceFirstCard),
                () -> dashboard.checkBalance(secondCardInfo, balanceSecondCard));
    }
}