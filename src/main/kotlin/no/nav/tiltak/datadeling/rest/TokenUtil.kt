package no.nav.tiltak.datadeling.rest

import com.nimbusds.jwt.JWTClaimsSet
import jakarta.servlet.http.HttpServletRequest

fun getJwtToken(request: HttpServletRequest): String? =
     request.getHeader("Authorization")

fun getClaimsFromToken(jwtToken: String): JWTClaimsSet =
    JWTClaimsSet.parse(jwtToken)

fun hasGroupClaim(request: HttpServletRequest, group: String): Boolean {
    val groupClaim = getJwtToken(request)?.let{
        getClaimsFromToken(it).getClaim("groups")
    }
    if (groupClaim is List<*>) {
        return groupClaim.contains(group)
    }
    return false
}
