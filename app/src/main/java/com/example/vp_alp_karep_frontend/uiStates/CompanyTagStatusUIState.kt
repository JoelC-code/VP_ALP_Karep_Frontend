package com.example.vp_alp_karep_frontend.uiStates

import com.example.vp_alp_karep_frontend.models.CompanyTagsModel

sealed interface CompanyTagStatusUIState {
    data class Success(
        val tags: List<CompanyTagsModel>
    ): CompanyTagStatusUIState
    object Start: CompanyTagStatusUIState
    object Loading: CompanyTagStatusUIState
    data class Failed(
        val errorMessage: String
    ): CompanyTagStatusUIState
}