mapOf(
    "mapstructVersion" to "1.5.3.Final"
).forEach { (name, version) ->
    project.extra.set(name, version)
}
