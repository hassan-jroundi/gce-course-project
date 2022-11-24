package ma.sorec.gcecourse;

import ma.sorec.gcecourse.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    InitService initService;

    @GetMapping(value = "/test")
    public void test() {

//        initService.ajouterEcuriesAvecBoxs();
//        initService.ajouterBatimentsAvecChambresEtLits();
    }
}
