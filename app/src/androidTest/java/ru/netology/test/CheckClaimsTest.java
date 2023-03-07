package ru.netology.test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static ru.netology.resourses.WaitId.waitId;

import androidx.test.espresso.PerformException;
import androidx.test.rule.ActivityTestRule;

import net.datafaker.Faker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.netology.activity.ClaimFragment;
import ru.netology.activity.ClaimsPageFragment;
import ru.netology.activity.LoginPageFragment;
import ru.netology.activity.MainPageFragment;
import ru.netology.data.ClaimsInfo;
import ru.netology.data.HospiceData;
import ru.netology.data.LoginInfo;

@RunWith(AllureAndroidJUnit4.class)
public class CheckClaimsTest {

    Faker faker = new Faker();

    @Rule
    public ActivityTestRule<AppActivity> activityTestRule =
            new ActivityTestRule<>(AppActivity.class);

    @Before
    public void enterToApp() {
        LoginPageFragment loginPage = new LoginPageFragment();
        LoginInfo.LogInfo loginInfo = LoginInfo.getLogInfo();
        try {
            onView(isRoot()).perform(waitId(R.id.enter_button, 10000));
            loginPage.toComeIn(loginInfo);
        } catch (PerformException e) {
            System.out.println("не найдено" + R.id.enter_button);
        }
    }

    @Test
    @DisplayName("2. Создание заявки и проверка заполнения полей(без исполнителя)")
    public void createClaimWithCheckEmptyFieldsTest() {

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPage();
        ClaimFragment claim = claimsPage.callCreateNewClaim(); //вызов создания заявки
        claimInfo = claim.checkingEmptyFieldsWhenCreatingClaim(claimInfo); // проверка пустых полей

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);
        claimsPage.goToMainPage();
    }

    @Test
    @DisplayName("3. Проверка отмены создания заявки")
    public void createClaimWithCheckCancelTest() {

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPage();
        ClaimFragment claim = claimsPage.callCreateNewClaim();
        claim.cancellationCreateClaim(claimInfo, false); //не сохранять при подтверждении

        claim = claimsPage.callCreateNewClaim();
        claimInfo = claim.cancellationCreateClaim(claimInfo, true); //сохранить при подтверждении

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);
        claimsPage.goToMainPage();
    }

    @Test
    @DisplayName("4. Создание заявки с проверкой исполнителя и статуса.")
    public void createClaimWithCheckExecutorTest() {

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPage();

        ClaimFragment claim = claimsPage.callCreateNewClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

// TODO: на случай возможности вставки другого исполнителя - в текущей версии не используется

//        ClaimsInfo.ClaimInfo claimInfo2 =
//                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.PETROV.getTitle()); //заявка
//        claim = claimsPage.callCreateClaim();
//        claimInfo2 = claim.createClaim(claimInfo2);
//        claim = claimsPage.toFoundClaimWithFilter(claimInfo2);
//        claim.checkClaimFields(claimInfo2);

        ClaimsInfo.ClaimInfo claimInfo3 =
                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.IVANOV.getTitle()); //заявка
        claim = claimsPage.callCreateNewClaim();
        claimInfo3 = claim.createClaim(claimInfo3);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo3);
        claim.checkClaimFields(claimInfo3);
    }

    @Test
    @DisplayName("5.1 Добавление/редактирование (с отменой) комментариев к заявке в статусе Открыто.")
    public void createAndEditCommentsOnOpenClaimTest() {

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPage();

        ClaimFragment claim = claimsPage.callCreateNewClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        String comment = faker.bothify("Comment #??#??#??#??#???");
        claimInfo = claim.writeComment(claimInfo, comment, false); // не сохранять коммент
        claimInfo = claim.writeComment(claimInfo, comment, true); // сохранять коммент
        claim.checkComment(claimInfo, comment);

        String comment2 = faker.bothify("Comment #??#??#??#??#???");
        claim.editComment(comment, comment2, false); // не сохранять исправления
        claim.checkComment(claimInfo, comment);
        claim.editComment(comment, comment2, true); // сохранять исправления
        claim.checkComment(claimInfo, comment2);

        claimInfo = claim.writeComment(claimInfo, comment, true); // сохранять коммент
        claim.checkComment(claimInfo, comment);
    }

    @Test
    @DisplayName("5.2 Добавление/редактирование нескольких комментариев к заявке в статусе в Работе.")
    public void createAndEditCommentsOnWorkClaimTest() {

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.IVANOV.getTitle()); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        ClaimFragment claim = claimsPage.callCreateNewClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        String comment = faker.bothify("Comment #??#??#??#??#???");
        String comment2 = faker.bothify("Comment2 #??#??#??#??#???");

        claimInfo = claim.writeComment(claimInfo, comment, true);
        claim.editComment(comment, comment2, true); // не сохранять исправления
        claim.checkComment(claimInfo, comment2);
        claimInfo = claim.writeComment(claimInfo, comment, true);
        claim.checkComment(claimInfo, comment);

        String comment3 = faker.bothify("Comment2 #??#??#??#??#???");
        claimInfo = claim.writeComment(claimInfo, comment3, true);
        claim.checkComment(claimInfo, comment3);
    }

    @Test
    @DisplayName("6.1. Редактирование названия и описания заявки в статусе Открыто.")
    public void editOpenStatusClaimTest() {

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        ClaimFragment claim = claimsPage.callCreateNewClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.editTitleAndDescriptionInClaim(claimInfo, false); //не сохраняем
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.editTitleAndDescriptionInClaim(claimInfo, true);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);
    }

    @Test
    @DisplayName("6.2. Изменение Исполнителя в заявке в статусе Открыто.")
    public void editExecutorInOpenStatusClaimTest() {

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        ClaimFragment claim = claimsPage.callCreateNewClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.changeExecutor(claimInfo, claimInfo.getAuthor()); //сохраняем новый статус

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo); // проверка в том числе статуса
    }

    @Test
    @DisplayName("6.3. Прохождение по статусам заявки начиная со статуса Открыто.")
    public void changeClaimStatusFromOpenTest() {

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithOutFIO(); //заявка без исполнителя

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        ClaimFragment claim = claimsPage.callCreateNewClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.toChangeStatusClaim
                (claimInfo, HospiceData.claimsStatus.WORK.getTitle(), true);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.toChangeStatusClaim // статус без изменений
                (claimInfo, HospiceData.claimsStatus.EXEC.getTitle(), false);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.toChangeStatusClaim
                (claimInfo, HospiceData.claimsStatus.EXEC.getTitle(), true);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);
    }

    @Test
    @DisplayName("6.4. Прохождение по статусам отмены заявки начиная со статуса В работе.")
    public void changeClaimStatusFromWorkTest() {

        ClaimsInfo.ClaimInfo claimInfo =
                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.IVANOV.getTitle());

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimsPage = mainPage.goToClaimsPageFromClaimBox();
        ClaimFragment claim = claimsPage.callCreateNewClaim();
        claimInfo = claim.createClaim(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.toChangeStatusClaim
                (claimInfo, HospiceData.claimsStatus.OPEN.getTitle(), false);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.toChangeStatusClaim
                (claimInfo, HospiceData.claimsStatus.OPEN.getTitle(), true);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);

        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claimInfo = claim.toChangeStatusClaim
                (claimInfo, HospiceData.claimsStatus.CANCEL.getTitle(), true);
        claim = claimsPage.toFoundClaimWithFilter(claimInfo);
        claim.checkClaimFields(claimInfo);
    }

    @Test
    @DisplayName("11. Проверка доступности символов при создании заявки.")
    public void checkPrintAllSymbolsAndLettersInClaimsTest() {

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPageFromClaimBox();

        String[] text = {
                HospiceData.SymbolsAndLetters.EnglishAndSymbol40.getTitle() + faker.bothify("??#??"),
                HospiceData.SymbolsAndLetters.EnglishUpAndSymbol40.getTitle() + faker.bothify("??#??"),
                HospiceData.SymbolsAndLetters.Letters40.getTitle() + faker.bothify("??#????#??"),
                faker.bothify("?")
        };

        ClaimsInfo.ClaimInfo claim;
        for (int i = 0; i < text.length; i = i + 1) {         //русские символы пропущены
            claim = ClaimsInfo.getClaimInfoWithChoiceTitleAndDiscr(text[i], text[i]); //заявка
            ClaimFragment claimFragment = claimPage.callCreateNewClaim();
            claim = claimFragment.createClaim(claim); // Exec
            claimFragment = claimPage.toFoundClaimWithFilter(claim);
            claimFragment.writeComment(claim, text[i], true);
            claimFragment.closeClaim();
        }
    }

    @Test
    @DisplayName("10. Проверка фильтров заявок. Статусы")
    public void checkClaimsFilterTest() {

        ClaimsInfo.ClaimInfo claims[] = {
                ClaimsInfo.getClaimInfoWithOutFIO(), //заявка1 Open
                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.IVANOV.getTitle()), //заявка2 Work
                ClaimsInfo.getClaimInfoWithChoiceFIO(HospiceData.fio.IVANOV.getTitle()), //заявка3 Exec
                ClaimsInfo.getClaimInfoWithOutFIO() //заявка4 Cancel
        };

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPage();

        ClaimFragment claimFragment = claimPage.callCreateNewClaim();
        claims[0] = claimFragment.createClaim(claims[0]); // Open

        claimFragment = claimPage.callCreateNewClaim();
        claims[1] = claimFragment.createClaim(claims[1]); // Work

        claimFragment = claimPage.callCreateNewClaim();
        claims[2] = claimFragment.createClaim(claims[2]); // Exec
        claimFragment = claimPage.toFoundClaimWithFilter(claims[2]); //проверка по фильтру в работе
        claims[2] = claimFragment.toChangeStatusClaim(claims[2], HospiceData.claimsStatus.EXEC.getTitle(), true);
        claimFragment = claimPage.toFoundClaimWithFilter(claims[2]); //проверка по фильтру в выполнено
        claimFragment.closeClaim();

        claimFragment = claimPage.callCreateNewClaim();
        claims[3] = claimFragment.createClaim(claims[3]); // Cancel
        claimFragment = claimPage.toFoundClaimWithFilter(claims[3]); //проверка по фильтру в открыто
        claims[3] = claimFragment.toChangeStatusClaim(claims[3], HospiceData.claimsStatus.CANCEL.getTitle(), true);
        claimPage.toFoundClaimWithFilter(claims[3]); //проверка по фильтру в отмена
        claimFragment.closeClaim();

        boolean filter[] = {true, true, true, true};
        for (ClaimsInfo.ClaimInfo cl : claims) { //фильтр со всеми галочками
            claimPage.toFoundClaimWithRandomFilter(cl, filter);
            claimFragment.closeClaim();
        }

        boolean filter1[] = {false, true, true, false};
        claimPage.toFoundClaimWithRandomFilter(claims[1], filter1);
        claimFragment.closeClaim();
        claimPage.toFoundClaimWithRandomFilter(claims[2], filter1);
        claimFragment.closeClaim();

        boolean filter2[] = {false, false, true, true};
        claimPage.toFoundClaimWithRandomFilter(claims[2], filter2);
        claimFragment.closeClaim();
        claimPage.toFoundClaimWithRandomFilter(claims[3], filter2);
        claimFragment.closeClaim();

        boolean filter3[] = {false, true, true, true};
        for (int i = 1; i < 4; i++) {
            claimPage.toFoundClaimWithRandomFilter(claims[i], filter3);
            claimFragment.closeClaim();
        }
    }

    @Test
    @DisplayName("15. Проверка даты и времени в заявке.")
    public void createClaimTestWithDateAndTime() {

        MainPageFragment mainPage = new MainPageFragment();
        ClaimsPageFragment claimPage = mainPage.goToClaimsPage();

        Arrays.asList(HospiceData.fio.values()).forEach(fio -> {
                    ClaimsInfo.ClaimInfo claim =
                            ClaimsInfo.getClaimInfoWithChoiceDateTime(fio.getTitle());

                    ClaimFragment claimFragment = claimPage.callCreateNewClaim();
                    claim = claimFragment.createClaim(claim);
                    claimFragment = claimPage.toFoundClaimWithFilter(claim);
                    claimFragment.checkClaimFields(claim);
                    if (claim.getExecutor() != HospiceData.fio.EMPTY.getTitle()) {
                        // Для заявки с исполнителем переводим заявку в статус Open для изменения времени
                        claimFragment = claimPage.toFoundClaimWithFilter(claim);
                        claim = claimFragment.
                                toChangeStatusClaim(claim, HospiceData.claimsStatus.OPEN.getTitle(), true);
                    }
                    claimFragment = claimPage.toFoundClaimWithFilter(claim);
                    claim = claimFragment.editDataTime(claim, // ставим более раннее время
                            LocalDate.now().plusDays(10).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            LocalTime.now().minusHours(3).format(DateTimeFormatter.ofPattern("kk:mm")));
                    claimFragment = claimPage.toFoundClaimWithFilter(claim);
                    claimFragment.checkClaimFields(claim);
                }
        );
    }
}
