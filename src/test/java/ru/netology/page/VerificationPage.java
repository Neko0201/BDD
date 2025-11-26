package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;
import com.codeborne.selenide.Condition;
import static com.codeborne.selenide.Selenide.$;


public class VerificationPage {

    private SelenideElement codeInput = $("[data-test-id=code] input");

    public VerificationPage() {
        codeInput.shouldBe(Condition.visible);
    }

    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        $("[data-test-id=code] input").setValue(verificationCode.getCode());
        $("[data-test-id=action-verify]").click();
        return new DashboardPage();
    }

}