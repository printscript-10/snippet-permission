package org.prinstcript10.snippetpermission.permission.repository

import org.prinstcript10.snippetpermission.permission.model.entity.SnippetPermission
import org.prinstcript10.snippetpermission.permission.model.enum.SnippetOwnership
import org.springframework.data.jpa.repository.JpaRepository

interface SnippetPermissionRepository : JpaRepository<SnippetPermission, Long> {
    fun findByUserIdAndSnippetId(userId: String, snippetId: String): SnippetPermission?
    fun deleteAllBySnippetId(snippetId: String)
    fun findAllBySnippetId(snippetId: String): List<SnippetPermission>
    fun findByUserId(userId: String): List<SnippetPermission>
    fun findFirstBySnippetIdAndOwnership(snippetId: String, ownership: SnippetOwnership): SnippetPermission?
}
