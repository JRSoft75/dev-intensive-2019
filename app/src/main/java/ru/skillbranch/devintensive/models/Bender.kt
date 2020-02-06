package ru.skillbranch.devintensive.models


class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val matchResult = Regex(question.condition).find(answer)
        var check = ""
//        if (matchResult == null) {
//            check = question.condition_text + "\n"
//        }
        return if(matchResult == null){
            question.condition_text + "\n" +
                    question.question to status.color
        } else if (question.answers.contains(answer.toLowerCase())) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
        } else {
            if(status==Status.CRITICAL){
                status = Status.NORMAL
                question = Question.NAME
                "Это неправильный ответ. Давай все по новой\n" +
                        question.question to status.color

            }else {
                status = status.nextStatus()
                "Это неправильный ответ!\n" +
                        question.question to status.color
            }
        }
    }


    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }


    enum class Question(
        val question: String,
        val answers: List<String>,
        val condition: String,
        val condition_text: String
    ) {
        NAME(
            "Как меня зовут?",
            listOf("бендер", "bender"),
            "^[A-ZА-Я].+",
            "Имя должно начинаться с заглавной буквы"
        ) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION(
            "Назови мою профессию?",
            listOf("сгибальщик", "bender"),
            "^[a-zа-я].+",
            "Профессия должна начинаться со строчной буквы"
        ) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL(
            "Из чего я сделан?",
            listOf("металл", "дерево", "metal", "iron", "wood"),
            "^[^\\d]+\$",
            "Материал не должен содержать цифр"
        ) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY(
            "Когда меня создали?",
            listOf("2993"),
            "^\\d+\$",
            "Год моего рождения должен содержать только цифры"
        ) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL(
            "Мой серийный номер?",
            listOf("2716057"),
            "^\\d{7}\$",
            "Серийный номер содержит только цифры, и их 7"
        ) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf(), ".+", "") {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
    }



}