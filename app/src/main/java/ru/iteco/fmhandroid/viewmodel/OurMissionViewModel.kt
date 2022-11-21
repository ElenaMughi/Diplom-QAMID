package ru.iteco.fmhandroid.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.ui.viewdata.OurMissionItemViewData

class OurMissionViewModel : ViewModel() {
    private val ourMissionItemList = flowOf(
        OurMissionItemViewData(
            id = 1,
            title = "«Хоспис для меня - это то, каким должен быть мир.\"",
            titleBackgroundColor = R.color.splash_screen_title_1,
            description = """"Ну, идеальное устройство мира в моих глазах. Где никто не оценивает, никто не осудит, где говоришь, и тебя слышат, где, если страшно, тебя обнимут и возьмут за руку, а если холодно тебя согреют.” Юля Капис, волонтер""",
            isOpen = false
        ),
        OurMissionItemViewData(
            id = 2,
            title = "Хоспис в своем истинном понимании - это творчество",
            titleBackgroundColor = R.color.splash_screen_title_2,
            description = "Нет шаблона и стандарта, есть только дух, который живет в разных домах по-разному. Но всегда он добрый, любящий и помогающий.",
            isOpen = false
        ),
        OurMissionItemViewData(
            id = 3,
            title = "“В хосписе не работают плохие люди” В.В. Миллионщикова\"",
            titleBackgroundColor = R.color.splash_screen_title_3,
            description = "Все сотрудники хосписа - это адвокаты пациента, его прав и потребностей. Поиск путей решения различных задач - это и есть хосписный индивидуальный подход к паллиативной помощи.",
            isOpen = false
        ),
        OurMissionItemViewData(
            id = 4,
            title = "«Хоспис – это философия, из которой следует сложнейшая наука медицинской помощи умирающим и искусство ухода, в котором сочетается компетентность и любовь» С. Сандерс",
            titleBackgroundColor = R.color.splash_screen_title_4,
            description = "“Творчески и осознанно подойти к проектированию опыта умирания. Создать пространство физическое и психологическое, чтобы позволить жизни отыграть себя до конца. И тогда человек не просто уходит с дороги. Тогда старение и умирание могут стать процессом восхождения до самого конца” \n" +
                    "Би Джей Миллер, врач, руководитель проекта \"Дзен-хоспис\"",
            isOpen = false
        ),
        OurMissionItemViewData(
            id = 5,
            title = "Служение человеку с теплом, любовью и заботой",
            titleBackgroundColor = R.color.splash_screen_title_5,
            description = "\"Если пациента нельзя вылечить, это не значит, что для него ничего нельзя сделать. То, что кажется мелочью, пустяком в жизни здорового человека - для пациента имеет огромный смысл.\"",
            isOpen = false
        ),
        OurMissionItemViewData(
            id = 6,
            title = "\"Хоспис продлевает жизнь, дает надежду, утешение и поддержку.\"",
            titleBackgroundColor = R.color.splash_screen_title_6,
            description = "\" Хоспис - это мои новые друзья. Полная перезагрузка жизненных ценностей. В хосписе нет страха и одиночества.\"\n" +
                    "Евгения Белоусова, дочь пациентки Ольги Васильевны",
            isOpen = false
        ),
        OurMissionItemViewData(
            id = 7,
            title = "\"Двигатель хосписа - милосердие плюс профессионализм\"\n" +
                    "А.В. Гнездилов, д.м.н., один из пионеров хосписного движения.",
            titleBackgroundColor = R.color.splash_screen_title_1,
            description = "\"Делай добро... А добро заразительно. По-моему, все люди милосердны. Нужно просто говорить с ними об этом, суметь разбудить в них чувство сострадания, заложенное от рождения\" - В.В. Миллионщикова",
            isOpen = false
        ),
        OurMissionItemViewData(
            id = 8,
            title = "Важен каждый!",
            titleBackgroundColor = R.color.splash_screen_title_2,
            description = "\"Каждый, кто оказывается в стенах хосписа, имеет огромное значение в жизни хосписа и его подопечных\"",
            isOpen = false
        )
    )

    private val isOpenItemIds = MutableStateFlow<Set<Int>>(emptySet())

    var data: Flow<List<OurMissionItemViewData>> =
        ourMissionItemList.toSingleListItem().combine(isOpenItemIds) { itemList, isOpenIds ->
            itemList.map {
                val id = requireNotNull(it.id) { "ourMissionItem id must not be null" }
                OurMissionItemViewData(
                    id = it.id,
                    title = it.title,
                    titleBackgroundColor = it.titleBackgroundColor,
                    description = it.description,
                    isOpen = isOpenIds.contains(id)
                )
            }
        }

    fun onCard(ourMissionItem: OurMissionItemViewData) {
        if (isOpenItemIds.value.contains(ourMissionItem.id)) isOpenItemIds.value -= ourMissionItem.id
        else isOpenItemIds.value += ourMissionItem.id
    }

    private fun <T> Flow<T>.toSingleListItem(): Flow<List<T>> = flow {
        val list = toList(mutableListOf())
        emit(list)
    }
}