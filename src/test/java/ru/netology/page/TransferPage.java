package ru.netology.page;

import com.codeborne.selenide.*;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement amountInput = $("[data-test-id='amount'] input");
    private SelenideElement cardNumberFrom = $("[data-test-id='from'] input");
    private SelenideElement headTransfer = $(Selectors.byText("Пополнение карты"));
    private SelenideElement errorMessage = $("[data-test-id = 'error-notification'] .notification__content");

    public TransferPage() {
        headTransfer.shouldBe(Condition.visible);
    }

    public void transfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(amountToTransfer);
        cardNumberFrom.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public DashboardPage makeValidTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        transfer(amountToTransfer, cardInfo);
        return new DashboardPage();
    }

    public void errorMessage(String expectedText) {
        errorMessage.shouldBe(Condition.and("Произошла ошибка", Condition.text(expectedText), Condition.visible));
    }

}