package org.prinstcript10.snippetpermission.permission.model.dto

import jakarta.validation.constraints.NotBlank

data class ShareSnippetDTO(
    @NotBlank(message = "snippetId is required")
    val snippetId: String,

    @NotBlank(message = "userId is required")
    val userId: String,
)
