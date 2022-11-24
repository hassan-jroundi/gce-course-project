package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.dto.UtilisateurDTO;
import ma.sorec.gcecourse.mapper.UtilisateurMapper;
import ma.sorec.gcecourse.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Api("Login Controller")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Se connecter")
    public UtilisateurDTO login(@RequestBody UtilisateurDTO utilisateur) throws Exception {
        return UtilisateurMapper.INSTANCE.entityToDto(loginService.login(utilisateur.getLogin(), utilisateur.getMotDePasse()));
    }

    @PostMapping(value = "/logout", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Se déconnecter")
    public Boolean logout(@RequestBody String idUtilisateur) {
        return loginService.seDeconnecter(idUtilisateur);
    }

}
