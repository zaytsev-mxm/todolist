package dev.todolist.utils

fun String.isValidEmail(): Boolean = this.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}\$".toRegex())