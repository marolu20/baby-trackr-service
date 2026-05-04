package com.babytrackr.service.application.util

/*
Capitalizes the first character of a string
*/
fun toTitleCase(name: String) = name.replaceFirstChar { it.titlecase() }
