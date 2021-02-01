package io.github.novemdecillion.slide

import org.asciidoctor.Asciidoctor
import org.asciidoctor.OptionsBuilder
import org.junit.jupiter.api.Test

class AsciiDocTest {
  @Test
  fun test() {
    val asciidoctor = Asciidoctor.Factory.create()
    val options = OptionsBuilder.options().inPlace(true)
    val result = asciidoctor.convert("""
    段落その１
    
     １つ以上の空白で始まる連続した行はリテラル段落とみなされます。
     リレラル段落はあらかじめフォーマットされたテキストとして扱われます。
     テキストは固定幅のフォントで表示され、
     改行はそのまま改行として表示されます。
    
    段落その２

    """.trimIndent(), options)
    println(result)
  }
}