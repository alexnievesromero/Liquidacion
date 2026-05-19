package Liquidacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/descargue")
public class DescargueController {

 

//        @Autowired
//        private PrecioService precioService;

        @GetMapping
        public String mostrarprecios(Model model) {
//            List<PrecioEntity> precios = precioService.listarTodos();
//            System.out.println("PRECIOS DESDE DB: " + precios);
//            model.addAttribute("listaPrecios", precios);
            return "descargueCombustible"; // Este es el archivo HTML en templates/
        }
    }
