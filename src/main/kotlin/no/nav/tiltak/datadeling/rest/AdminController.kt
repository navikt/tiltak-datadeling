package no.nav.tiltak.datadeling.rest

import jakarta.servlet.http.HttpServletRequest
import no.nav.tiltak.datadeling.FeiledeMeldingerRepository
import no.nav.tiltak.datadeling.db.tables.records.FeiledeMeldingerRecord
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val feiledeMeldingerRepository: FeiledeMeldingerRepository
) {
    @GetMapping("/feilede-meldinger")
    fun feiledeMeldinger(request: HttpServletRequest): ResponseEntity<List<FeiledeMeldingerRecord>> {
//        if (!hasGroupClaim(request, "fb516b74-0f2e-4b62-bad8-d70b82c3ae0b")) {
//            return ResponseEntity(HttpStatus.FORBIDDEN)
//        }
        return ResponseEntity.ok(feiledeMeldingerRepository.hentFeiledeMeldinger())
    }
}
