package org.prinstcript10.snippetpermission.permission.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.prinstcript10.snippetpermission.permission.model.enum.SnippetOwnership
import org.prinstcript10.snippetpermission.shared.baseModel.BaseModel

@Entity
data class SnippetPermission(
    val snippetId: String,

    val userId: String,

    @Enumerated(EnumType.STRING)
    val ownership: SnippetOwnership,
) : BaseModel() {
    constructor() : this("", "", SnippetOwnership.OWNER)
}
