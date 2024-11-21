package org.prinstcript10.snippetpermission.permission.service

import jakarta.transaction.Transactional
import org.prinstcript10.snippetpermission.permission.model.dto.ShareSnippetDTO
import org.prinstcript10.snippetpermission.permission.model.entity.SnippetPermission
import org.prinstcript10.snippetpermission.permission.model.enum.SnippetOwnership
import org.prinstcript10.snippetpermission.permission.repository.SnippetPermissionRepository
import org.prinstcript10.snippetpermission.shared.exception.ConflictException
import org.prinstcript10.snippetpermission.shared.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service

@Service
class PermissionService(
    @Autowired
    private val permissionRepository: SnippetPermissionRepository,
) {

    private val logger = LoggerFactory.getLogger(PermissionService::class.java)

    fun createSnippetPermission(
        snippetId: String,
        userId: String,
    ) {
        val permissions = permissionRepository.findAllBySnippetId(snippetId)
        logger.info("Creating snippet permission for snippet: $snippetId")
        if (permissions.isNotEmpty()) {
            logger.error("Permission for: $snippetId, already exists for user: $userId")
            throw ConflictException("Permission already exists for this snippet")
        }

        permissionRepository.save(
            SnippetPermission(
                snippetId = snippetId,
                userId = userId,
                ownership = SnippetOwnership.OWNER,
            ),
        )
        logger.info("Snippet permission for snippet: $snippetId created successfully")
    }

    fun shareSnippet(shareSnippetDTO: ShareSnippetDTO, userId: String) {
        logger.info("Sharing snippet: ${shareSnippetDTO.snippetId} to user: ${shareSnippetDTO.userId}")
        val permission = permissionRepository.findByUserIdAndSnippetId(
            userId = userId,
            snippetId = shareSnippetDTO.snippetId,
        )

        if (permission == null) {
            logger.error("Missing permission for user: $userId")
            throw NotFoundException("No permission found matching that userId and snippetId")
        }

        if (permission.ownership != SnippetOwnership.OWNER) {
            logger.error("User: $userId is not owner and cannot share snippet: ${shareSnippetDTO.snippetId}")
            throw ConflictException("You do not have permission to share this snippet")
        }

        try {
            permissionRepository.save(
                SnippetPermission(
                    snippetId = shareSnippetDTO.snippetId,
                    userId = shareSnippetDTO.userId,
                    ownership = SnippetOwnership.SHARED,
                ),
            )
            logger.info("Snippet: ${shareSnippetDTO.snippetId} shared successfully to user: ${shareSnippetDTO.userId}")
        } catch (e: DataAccessException) {
            logger.error("Failed to save permission due to a conflict: " + e.message)
            throw ConflictException("Failed to save permission due to a conflict: " + e.message)
        }
    }

    fun getSnippetPermission(snippetId: String, userId: String): SnippetPermission {
        logger.info("Getting snippet permission for snippet: $snippetId and user: $userId")
        val permission = permissionRepository.findByUserIdAndSnippetId(
            userId = userId,
            snippetId = snippetId,
        )
        if (permission == null) {
            logger.error("Missing permission for user: $userId")
            throw NotFoundException("No permission found matching that userId and snippetId")
        }
        return permission
    }

    fun getAllSnippetPermissions(userId: String): List<SnippetPermission> {
        logger.info("Getting all snippet permissions for user: $userId")
        return permissionRepository.findByUserId(userId)
    }

    @Transactional
    fun deleteSnippetPermissions(snippetId: String, userId: String) {
        logger.info("Deleting snippet permissions: $snippetId")
        val permission = permissionRepository.findByUserIdAndSnippetId(
            userId = userId,
            snippetId = snippetId,
        )

        if (permission == null) {
            logger.error("Missing permission for user: $userId")
            throw NotFoundException("No permission found matching that userId and snippetId")
        }

        if (permission.ownership != SnippetOwnership.OWNER) {
            logger.error("User: $userId is not owner and cannot delete this snippet")
            throw ConflictException("You do not have permission to delete this snippet")
        }

        permissionRepository.deleteAllBySnippetId(snippetId)
        logger.info("Deleted snippet permission successfully for snippet: $snippetId user: $userId")
    }

    fun getSnippetOwner(snippetId: String, userId: String): SnippetPermission {
        logger.info("Getting ownership for snippet: $snippetId")
        val permission = permissionRepository.findByUserIdAndSnippetId(
            userId = userId,
            snippetId = snippetId,
        )

        if (permission == null) {
            logger.error("Missing permission for user: $userId")
            throw NotFoundException("No permission found matching that userId and snippetId")
        }

        return permissionRepository.findFirstBySnippetIdAndOwnership(snippetId, SnippetOwnership.OWNER)!!
    }
}
