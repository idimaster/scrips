package org.scripts.drools

data class EvaluationContext(var domain: String, var policy: String, val items: List<EvaluationItem>)