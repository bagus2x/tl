package bagus2x.tl

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

object TestUtils {
    private val words = LoremIpsum(500)
        .values
        .joinToString(" ")
        .split(" ")
        .map { word ->
            word
                .replace(".", "")
                .replace(",", "")
                .replace(" ", "")
                .replace("\n", "")
        }

    fun getRandomWords(words: Int): String {
        return buildString {
            repeat(words) {
                append(this@TestUtils.words.random())
                if (it != words - 1) {
                    append(" ")
                }
            }
        }
    }
}

fun main() {
    println(TestUtils.getRandomWords(5))
}
