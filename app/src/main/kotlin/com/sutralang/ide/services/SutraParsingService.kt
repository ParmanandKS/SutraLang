package com.sutralang.ide.services

import com.sutralang.ide.engine.SutraEngine

class SutraParsingService {
    private val engine = SutraEngine()

    fun executeCode(code: String): String {
        return try {
            engine.run(code)
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
    
    fun getVariableValue(name: String): Any? {
        return engine.variables[name]
    }
}
