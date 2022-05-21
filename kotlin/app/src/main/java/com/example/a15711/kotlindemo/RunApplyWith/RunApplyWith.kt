package com.example.a15711.kotlindemo.RunApplyWith

/**
 * object相当于单例类
 */
object RunApplyWith {
    /**
     * run：lambda表达式中含有上下文，最后一个为返回值
     */
    val resultRun=StringBuilder().run {
        append("I")
        append(" am")
        append(" run")
        toString()
    }
    /**
     * with：lambda表达式中含有上下文，最后一个为返回值
     */
    val resultWith=with(StringBuilder()){
        append("I")
        append(" am")
        append(" with")
        toString()
    }
    /**
     * apply：lambda表达式中含有上下文，返回自身
     */
    val resultApply=StringBuilder().apply {
        append("I")
        append(" am")
        append(" apply")
    }
}