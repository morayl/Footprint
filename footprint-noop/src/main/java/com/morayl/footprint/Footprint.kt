package com.morayl.footprint

/**
 * NoOp
 * footprint-ktx has been deprecated.
 * Please see below for the migration.
 * https://github.com/morayl/Footprint/blob/master/CHANGELOG.md
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
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
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
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun footprint(priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun footprint(vararg messages: Any?, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun accentFootprint(vararg messages: Any? = emptyArray(), logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun simpleFootprint(message: Any?, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun simpleFootprint(vararg messages: Any?, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun jsonFootprint(target: Any?, indent: Int = defaultJsonIndentCount, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun <T> T.withJsonFootprint(indent: Int = defaultJsonIndentCount, priority: LogPriority = defaultLogPriority,
                            logTag: String = defaultLogTag, block: (T) -> Any? = { this }): T {
    return this
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun <T> T.withFootprint(priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag, block: (T) -> Any? = { this }): T {
    return this
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun <T> T.withSimpleFootprint(priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag, block: (T) -> Any? = { this }): T {
    return this
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun pairFootprint(vararg pairs: Pair<String, Any?>, priority: LogPriority = defaultLogPriority, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun Throwable.stacktraceFootprint(priority: LogPriority = defaultStackTraceLogLevel, logTag: String = defaultLogTag) {
}

/**
 * NoOp
 */
@Deprecated(message = "footprint-ktx has been deprecated. Please see github for the migration. https://github.com/morayl/Footprint/blob/master/CHANGELOG.md ")
fun stacktraceFootprint(priority: LogPriority = defaultStackTraceLogLevel, logTag: String = defaultLogTag) {
}
