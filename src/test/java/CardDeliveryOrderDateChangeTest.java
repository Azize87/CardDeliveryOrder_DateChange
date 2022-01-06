import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;
import ru.netology.data.RegistrationByCardInfo;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.generateDate;

public class CardDeliveryOrderDateChangeTest {


    @Test
    void shouldChangeOfDateForCardDelivery() {
        RegistrationByCardInfo info = DataGenerator.Registration.generateByCard("ru");
        //отправка формы на доставку карты

        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue(info.getCity());
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), generateDate(3));
        $("[data-test-id='name'] input").setValue(info.getName());
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[data-test-id='agreement'] .checkbox__text").click();
        $(".button__text").shouldHave(Condition.text("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__content").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на  " + generateDate(3)), Duration.ofSeconds(15));

        //изменение даты

        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), generateDate(8));
        $(".button__text").shouldHave(Condition.text("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(Condition.visible)
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(15));
        $$("button .button__text").find(Condition.text("Перепланировать")).click();
        $("[data-test-id='success-notification'] .notification__content").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на  " + generateDate(8)), Duration.ofSeconds(15));


    }
}

