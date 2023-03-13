package bagus2x.tl.presentation.common

object Utils {

    fun profile(name: String): String {
        return "https://api.dicebear.com/5.x/pixel-art-neutral/png?seed=$name"
    }

    fun Map<String, Map<String, Boolean>>.asParams(): Map<String, List<String>> {
        return mapValues {
            it.value
                .filter { options -> options.value }
                .map { option -> option.key }
        }
    }
}
