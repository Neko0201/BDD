package ru.netology.page;

import lombok.Value;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private SelenideElement header = $("[data-test-id=dashboard]");
    private final SelenideElement reloadButton = $("[data-test-id = 'action-reload']");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";


    public DashboardPage() {
        header.shouldBe(visible);
    }

    private SelenideElement getCard(DataHelper.CardInfo cardInfo) {
        return cards.findBy(Condition.attribute("data-test-id", cardInfo.getTestId()));
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        val text = getCard(cardInfo).getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage selectedCardForTransfer(DataHelper.CardInfo cardInfo) {
        getCard(cardInfo).$("button").click();
        return new TransferPage();
    }

    public void reloadDashboardPage() {
        reloadButton.click();
        header.shouldBe(visible);

    }

    public void checkBalance(DataHelper.CardInfo cardInfo, int balanceExpected) {
        cards.findBy(Condition.attribute("data-test-id", cardInfo.getTestId())).shouldBe(visible)
                .shouldBe(text(balanceStart + balanceExpected + balanceFinish));
    }
}