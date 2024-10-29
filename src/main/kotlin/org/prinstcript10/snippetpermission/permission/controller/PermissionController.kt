package org.prinstcript10.snippetpermission.permission.controller

import jakarta.validation.Valid
import org.prinstcript10.snippetpermission.permission.model.dto.CreateSnippetPermissionDTO
import org.prinstcript10.snippetpermission.permission.model.enum.SnippetOwnership
import org.prinstcript10.snippetpermission.permission.service.PermissionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.validation.annotation.Validated
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

    @PostMapping
    fun createSnippetPermission(
        @Valid @RequestBody createSnippetPermissionDTO: CreateSnippetPermissionDTO,
        @AuthenticationPrincipal jwt: Jwt,
    ) {
        return permissionService.createSnippetPermission(createSnippetPermissionDTO, jwt.subject)
    }

    @GetMapping("/{id}")
    fun getSnippetPermission(
        @PathVariable("id") snippetId: String,
        @AuthenticationPrincipal jwt: Jwt,
    ): SnippetOwnership {
        return permissionService.getSnippetPermission(snippetId, jwt.subject)
    }
}
