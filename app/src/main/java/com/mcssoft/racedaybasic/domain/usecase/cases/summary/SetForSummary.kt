package com.mcssoft.racedaybasic.domain.usecase.cases.summary

import com.mcssoft.racedaybasic.data.repository.database.IDbRepo
import javax.inject.Inject

class SetForSummary  @Inject constructor(
    private val iDbRepo: IDbRepo
) {

}