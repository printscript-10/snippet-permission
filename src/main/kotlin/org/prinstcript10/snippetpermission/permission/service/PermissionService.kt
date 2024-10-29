package org.prinstcript10.snippetpermission.permission.service

import org.prinstcript10.snippetpermission.permission.model.dto.CreateSnippetPermissionDTO
import org.prinstcript10.snippetpermission.permission.model.entity.SnippetPermission
import org.prinstcript10.snippetpermission.permission.repository.SnippetPermissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService(
    @Autowired
    private val permissionRepository: SnippetPermissionRepository,
) {

    fun createSnippetPermission(
        createSnippetPermissionDTO: CreateSnippetPermissionDTO,
        userId: String,
    ) {
        val snippetPermission = SnippetPermission(
            snippetId = createSnippetPermissionDTO.snippetId,
            userId = userId,
            ownership = createSnippetPermissionDTO.ownership
        )
        permissionRepository.save(snippetPermission)
    }
}
