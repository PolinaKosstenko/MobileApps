package com.example.harrypotterapi.model

data class Question(
    val text: String,
    val answers: List<Answer>
)

data class Answer(
    val text: String,
    val points: Map<String, Int> // "gryffindor" -> 3 и т.д.
)

val sortingQuestions = listOf(
    Question(
        "Какое качество ты больше всего ценишь в друге?",
        listOf(
            Answer(
                "Честность и смелость", mapOf(
                    "gryffindor" to 3, "slytherin" to 0, "ravenclaw" to 0, "hufflepuff" to 0,
                )
            ),
            Answer(
                "Хитрость и умение добиваться целей", mapOf(
                    "gryffindor" to 0, "slytherin" to 3, "ravenclaw" to 0, "hufflepuff" to 0,
                )
            ),
            Answer(
                "Ум и эрудицию, чтобы обсудить любую тему", mapOf(
                    "gryffindor" to 3, "slytherin" to 0, "ravenclaw" to 3, "hufflepuff" to 0,
                )
            ),
            Answer(
                "Верность и готовность прийти на помощь в любое время", mapOf(
                    "gryffindor" to 0, "slytherin" to 0, "ravenclaw" to 0, "hufflepuff" to 3,
                )
            ),
        )
    ),
    Question(
        "Если бы тебе досталась бузинная палочка, ты бы использовал её для",
        listOf(
            Answer(
                "Укрепления своей власти и влияния", mapOf(
                    "gryffindor" to 0, "slytherin" to 4, "ravenclaw" to 0, "hufflepuff" to 0,
                )
            ),
            Answer(
                "Защиты слабого даже ценой опасности для себя", mapOf(
                    "gryffindor" to 4, "slytherin" to 0, "ravenclaw" to 0, "hufflepuff" to 0,
                )
            ),
            Answer(
                "Передачи в музей Хогвартса, чтобы меч никому не навредил", mapOf(
                    "gryffindor" to 2, "slytherin" to 0, "ravenclaw" to 0, "hufflepuff" to 2,
                )
            ),
            Answer(
                "Изучения древней магии, заложенной в палочке", mapOf(
                    "gryffindor" to 0, "slytherin" to 0, "ravenclaw" to 4, "hufflepuff" to 0,
                )
            ),
        )
    ),
    Question(
        "Выбери занятие в свободный вечер",
        listOf(
            Answer(
                "Помощь Хагриду в выхаживании раненой фестралы", mapOf(
                    "gryffindor" to 0, "slytherin" to 0, "ravenclaw" to 0, "hufflepuff" to 5,
                )
            ),
            Answer(
                "Полёт на метле над Запретным лесом с риском встретить дракона", mapOf(
                    "gryffindor" to 4, "slytherin" to 0, "ravenclaw" to 0, "hufflepuff" to 0,
                )
            ),
            Answer(
                "Шахматы на выбывание с тайной ставкой на редкий ингредиент", mapOf(
                    "gryffindor" to 0, "slytherin" to 4, "ravenclaw" to 0, "hufflepuff" to 0,
                )
            ),
            Answer(
                "Чтение древнего гримуара о превращениях", mapOf(
                    "gryffindor" to 0, "slytherin" to 0, "ravenclaw" to 5, "hufflepuff" to 0,
                )
            )
        )
    ),
    Question(
        "Какую фразу ты скорее скажешь перед трудным испытанием?",
        listOf(
            Answer(
                "Важно просчитать все варианты, тогда риск снизится", mapOf(
                    "gryffindor" to 0, "slytherin" to 0, "ravenclaw" to 4, "hufflepuff" to 0,
                )
            ),
            Answer(
                "Вперёд! Страх — это не повод отступать", mapOf(
                    "gryffindor" to 5, "slytherin" to 0, "ravenclaw" to 0, "hufflepuff" to 0,
                )
            ),
            Answer(
                "Лишь бы никто не пострадал… Я справлюсь", mapOf(
                    "gryffindor" to 0, "slytherin" to 0, "ravenclaw" to 0, "hufflepuff" to 4,
                )
            ),
            Answer(
                "Я найду способ использовать это в своих интересах", mapOf(
                    "gryffindor" to 0, "slytherin" to 5, "ravenclaw" to 0, "hufflepuff" to 0,
                )
            ),
        )
    ),
    Question(
        "Ты нашёл в Выручай-комнате древний артефакт. Что ты сделаешь в первую очередь?",
        listOf(
            Answer(
                "Осмотрю его на предмет защиты — вдруг он опасен, но применять первым не буду",
                mapOf(
                    "gryffindor" to 2, "slytherin" to 0, "ravenclaw" to 4, "hufflepuff" to 2,
                )
            ),
            Answer(
                "Попробую активировать тайную силу артефакта, чтобы проверить," +
                        " даст ли он мне преимущество.",
                mapOf(
                    "gryffindor" to 1, "slytherin" to 4, "ravenclaw" to 0, "hufflepuff" to 0,
                )
            ),
            Answer(
                "Отнесу профессору, чтобы артефакт изучили и не использовали во вред", mapOf(
                    "gryffindor" to 0, "slytherin" to 0, "ravenclaw" to 2, "hufflepuff" to 4,
                )
            ),
            Answer(
                "Немедленно испытаю в деле — рискну, если это поможет решить большую проблему",
                mapOf(
                    "gryffindor" to 4, "slytherin" to 1, "ravenclaw" to 0, "hufflepuff" to 0,
                )
            ),
        )
    ),
)
