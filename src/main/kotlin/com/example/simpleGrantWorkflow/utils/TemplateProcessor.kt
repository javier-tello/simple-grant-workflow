package com.example.simpleGrantWorkflow.utils

class TemplateProcessor {
    fun processTemplate(template: String, placeholders: Map<String, String>): String {
        var processedTemplate = template
        placeholders.forEach { (key, value) ->
            processedTemplate = processedTemplate.replace("{{${key}}}", value)
        }
        return processedTemplate
    }
}