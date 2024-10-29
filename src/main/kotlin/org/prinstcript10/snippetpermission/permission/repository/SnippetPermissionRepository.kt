package org.prinstcript10.snippetpermission.permission.repository

import org.prinstcript10.snippetpermission.permission.model.entity.SnippetPermission
import org.springframework.data.jpa.repository.JpaRepository

interface SnippetPermissionRepository : JpaRepository<SnippetPermission, Long> {
    fun findByUserIdAndSnippetId(userId: String, snippetId: String): SnippetPermission?
}
