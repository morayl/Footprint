package com.morayl.footprint

/**
 * NoOp
 */

private var enableInternal = true
private var defaultLogTag = "Footprint"
private var defaultLogPriority = LogPriority.DEBUG
private var showJsonExceptionInternal = false
private var forceSimpleInternal = false
private var defaultStackTraceLogLevel = LogPriority.ERROR
private var defaultJsonIndentCount = 4

/**
 * NoOp
 */
fun configFootprint(
        enable: Boolean = enableInternal,
        logTag: String = defaultLogTag,
        logPriority: LogPriority = defaultLogPriority,
        showInternalJsonException: Boolean = showJsonExceptionInternal,
        forceSimple: Boolean = forceSimpleInternal,
        stacktraceLogLogPriority: LogPriority = defaultStackTraceLogLevel,
        jsonIndentCount: Int = defaultJsonIndentCount
) {
}

/**
 * NoOp
 */
fun footprint(priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
fun footprint(vararg messages: Any?, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
fun accentFootprint(vararg messages: Any? = emptyArray(), logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
fun simpleFootprint(message: Any?, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
fun simpleFootprint(vararg messages: Any?, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
fun jsonFootprint(target: Any?, indent: Int = defaultJsonIndentCount, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
fun <T> T.withJsonFootprint(indent: Int = defaultJsonIndentCount, priority: LogPriority = defaultLogPriority,
                            logTag: String = defaultLogTag, block: (T) -> Any? = { this }): T {
    return this
}

/**
 * NoOp
 */
fun <T> T.withFootprint(priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag, block: (T) -> Any? = { this }): T {
    return this
}

/**
 * NoOp
 */
fun <T> T.withSimpleFootprint(priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag, block: (T) -> Any? = { this }): T {
    return this
}

/**
 * NoOp
 */
fun pairFootprint(vararg pairs: Pair<String, Any?>, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
fun Throwable.stacktraceFootprint(priority: LogPriority = defaultStackTraceLogLevel, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
fun stacktraceFootprint(priority: LogPriority = defaultStackTraceLogLevel, logTag: String = defaultLogTag) {
}
