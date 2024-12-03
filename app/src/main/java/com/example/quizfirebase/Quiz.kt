package com.example.quizfirebase

class Quiz {
    lateinit var question: String
    lateinit var option1: String
    lateinit var option2: String
    lateinit var option3: String
    lateinit var option4: String
    lateinit var answer: String

    constructor(
        question: String,
        option1: String,
        option2: String,
        option3: String,
        option4: String,
        answer: String
    ) {
        this.question = question
        this.option1 = option1
        this.option2 = option2
        this.option3 = option3
        this.option4 = option4
        this.answer = answer
    }

    constructor() {}
}