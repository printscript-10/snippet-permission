package org.prinstcript10.snippetpermission.permission.service

import jakarta.transaction.Transactional
import org.prinstcript10.snippetpermission.permission.model.dto.ShareSnippetDTO
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
        snippetId: String,
        userId: String,
    ) {
        val permissions = permissionRepository.findAllBySnippetId(snippetId)
        if (permissions.isNotEmpty()) {
            throw ConflictException("Permission already exists for this snippet")
        }

        permissionRepository.save(
            SnippetPermission(
                snippetId = snippetId,
                userId = userId,
                ownership = SnippetOwnership.OWNER,
            ),
        )
    }

    fun shareSnippet(shareSnippetDTO: ShareSnippetDTO, userId: String) {
        val permission = permissionRepository.findByUserIdAndSnippetId(
            userId = userId,
            snippetId = shareSnippetDTO.snippetId,
        )

        if (permission == null) {
            throw NotFoundException("No permission found matching that userId and snippetId")
        }

        if (permission.ownership != SnippetOwnership.OWNER) {
            throw ConflictException("You do not have permission to delete this snippet")
        }

        try {
            permissionRepository.save(
                SnippetPermission(
                    snippetId = shareSnippetDTO.snippetId,
                    userId = shareSnippetDTO.userId,
                    ownership = SnippetOwnership.SHARED,
                ),
            )
        } catch (e: DataAccessException) {
            throw ConflictException("Failed to save permission due to a conflict: " + e.message)
        }
    }

    fun getSnippetPermission(snippetId: String, userId: String): SnippetPermission {
        val permission = permissionRepository.findByUserIdAndSnippetId(
            userId = userId,
            snippetId = snippetId,
        )
        if (permission == null) {
            throw NotFoundException("No permission found matching that userId and snippetId")
        }
        return permission
    }

    fun getAllSnippetPermissions(userId: String): List<SnippetPermission> {
        return permissionRepository.findByUserId(userId)
    }

    @Transactional
    fun deleteSnippetPermissions(snippetId: String, userId: String) {
        val permission = permissionRepository.findByUserIdAndSnippetId(
            userId = userId,
            snippetId = snippetId,
        )

        if (permission == null) {
            throw NotFoundException("No permission found matching that userId and snippetId")
        }

        if (permission.ownership != SnippetOwnership.OWNER) {
            throw ConflictException("You do not have permission to delete this snippet")
        }

        permissionRepository.deleteAllBySnippetId(snippetId)
    }
}
