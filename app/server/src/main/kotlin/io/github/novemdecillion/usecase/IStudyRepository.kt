package io.github.novemdecillion.usecase

import io.github.novemdecillion.domain.Study

interface IStudyRepository {
  fun update(study: Study)
}