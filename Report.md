# Отчет о проведенной работе по автоматизации тестирования и прогоне автотестов

Всего написано/запущено 24 теста
из них 17 успешных и 7 неуспешных (70,83% успешности)

Отчет о прогоне тестов можно посмотреть в Allure (скриншоты отчета ниже)
Для запуска Allure необходимо запустить в терминале: ./gradlew allureServe

## Скриншоты отчета Allure
![скриншот](https://github.com/ElenaMughi/CourseProject/blob/master/pick1.PNG)
![скриншот](https://github.com/ElenaMughi/CourseProject/blob/master/pick2.PNG)


Все найденные баги зарегистрированы в Issues к данному проекту (список багов ниже)
Из них - 7 на основании автотестов (из них 3 бага критичных (API, неверная обработка ответа сервера)),
3 бага - ручное тестирование (Орфографические ошибки заголовка и т.п.)

## Список багов
* [Сообщение об успешности заявки для отклоненной карты #1](<https://github.com/ElenaMughi/CourseProject/issues/1#issue-1304657564>)
* [Для случайной карты выдается два сообщения и лог с ошибкой на сервере #2](<https://github.com/ElenaMughi/CourseProject/issues/2#issue-1304669938>)
* [Доступен ввод некорректного CVC/CVV кода "000" #3](<https://github.com/ElenaMughi/CourseProject/issues/3#issue-1307124860>)
* [Доступен ввод ФИО владельца из одного символа #4](<https://github.com/ElenaMughi/CourseProject/issues/4#issue-1307126497>)
* [Доступен ввод русских букв в поле Владелец #5](<https://github.com/ElenaMughi/CourseProject/issues/5#issue-1307128142>)
* [Доступен ввод нулевого месяца #6](<https://github.com/ElenaMughi/CourseProject/issues/6#issue-1307179547>)
* [Ошибки в информации о туре. #7](<https://github.com/ElenaMughi/CourseProject/issues/7#issue-1307191501>)
* [Допустим ввод цифр и символов в поле Владелец #8](<https://github.com/ElenaMughi/CourseProject/issues/8#issue-1307192720>)
* [Остаются подсказки о неверных данных после ввода корректных #9](<https://github.com/ElenaMughi/CourseProject/issues/9#issue-1307199315>)
* [После отправки запроса с рандомной картой возникает ошибка на сервере, а в базе платеж APPROVED #10](<https://github.com/ElenaMughi/CourseProject/issues/10#issue-1307202523>)
