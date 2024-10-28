package org.prinstcript10.snippetpermission.permission.model.dto

import jakarta.validation.constraints.NotBlank
import org.prinstcript10.snippetpermission.permission.model.enum.SnippetOwnership
import org.prinstcript10.snippetpermission.shared.enumValidator.EnumFormat

data class CreateSnippetPermissionDTO(
    @NotBlank(message = "snippetId is required")
    val snippetId: String,

    @EnumFormat(enumClass = SnippetOwnership::class, message = "Invalid ownership")
    val ownership: SnippetOwnership,
)
