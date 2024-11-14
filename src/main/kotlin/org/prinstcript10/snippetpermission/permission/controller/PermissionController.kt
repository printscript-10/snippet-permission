package org.prinstcript10.snippetpermission.permission.controller

import jakarta.validation.Valid
import org.prinstcript10.snippetpermission.permission.model.dto.ShareSnippetDTO
import org.prinstcript10.snippetpermission.permission.model.entity.SnippetPermission
import org.prinstcript10.snippetpermission.permission.service.PermissionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("permissions")
@Validated
class PermissionController(
    @Autowired
    private val permissionService: PermissionService,
) {

    @PostMapping("share")
    fun shareSnippet(
        @Valid @RequestBody shareSnippetDTO: ShareSnippetDTO,
        @AuthenticationPrincipal jwt: Jwt,
    ) {
        return permissionService.shareSnippet(shareSnippetDTO, jwt.subject)
    }

    @PostMapping("{id}")
    fun createSnippetPermission(
        @PathVariable("id") snippetId: String,
        @AuthenticationPrincipal jwt: Jwt,
    ) {
        return permissionService.createSnippetPermission(snippetId, jwt.subject)
    }

    @GetMapping("owner/{id}")
    fun getSnippetOwner(
        @PathVariable("id") snippetId: String,
        @AuthenticationPrincipal jwt: Jwt,
    ): SnippetPermission {
        return permissionService.getSnippetOwner(snippetId, jwt.subject)
    }

    @GetMapping("/{id}")
    fun getSnippetPermission(
        @PathVariable("id") snippetId: String,
        @AuthenticationPrincipal jwt: Jwt,
    ): SnippetPermission {
        return permissionService.getSnippetPermission(snippetId, jwt.subject)
    }

    @GetMapping()
    fun getAllSnippetPermissions(
        @AuthenticationPrincipal jwt: Jwt,
    ): List<SnippetPermission> {
        return permissionService.getAllSnippetPermissions(jwt.subject)
    }

    @DeleteMapping("/{id}")
    fun deleteSnippetPermissions(
        @PathVariable("id") snippetId: String,
        @AuthenticationPrincipal jwt: Jwt,
    ) {
        return permissionService.deleteSnippetPermissions(snippetId, jwt.subject)
    }
}
