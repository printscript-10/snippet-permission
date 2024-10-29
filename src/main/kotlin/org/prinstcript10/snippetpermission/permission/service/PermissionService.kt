package org.prinstcript10.snippetpermission.permission.service

import org.prinstcript10.snippetpermission.permission.model.dto.CreateSnippetPermissionDTO
import org.prinstcript10.snippetpermission.permission.model.entity.SnippetPermission
import org.prinstcript10.snippetpermission.permission.model.enum.SnippetOwnership
import org.prinstcript10.snippetpermission.permission.repository.SnippetPermissionRepository
import org.prinstcript10.snippetpermission.shared.exception.ConflictException
import org.prinstcript10.snippetpermission.shared.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
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
        try {
            permissionRepository.save(snippetPermission)
        } catch (e: DataAccessException) {
            // ? cambiar por: failed to save permission due to a conflict. Please ensure the permission does not already exist.
            throw ConflictException("Failed to save permission due to a conflict: " + e.message)
        }
    }
    fun getSnippetPermission(snippetId: String, userId: String): SnippetOwnership {
        val permission = permissionRepository.findByUserIdAndSnippetId(
            userId = userId,
            snippetId = snippetId
        )
        if(permission == null){
            throw NotFoundException("No permission found matching that userId and snippetId")
        }
        return permission.ownership
    }
}
