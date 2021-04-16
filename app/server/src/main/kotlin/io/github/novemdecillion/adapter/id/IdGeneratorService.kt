package io.github.novemdecillion.adapter.id

import com.fasterxml.uuid.Generators
import org.springframework.stereotype.Component
import java.util.*

@Component
class IdGeneratorService {
  fun generate(): UUID = Generators.timeBasedGenerator().generate()
}