package com.cpssurplus.controllers


import com.cpssurplus.domains.forms.OrderForm
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController {

    @GetMapping("/")
    String index(Model model) {
        model.addAttribute('orderForm', new OrderForm())
        model.addAttribute('masks', ["1.jpg", "2.jpg", "3.jpg",
        "3 (1).jpg", "4.jpg", "5.jpg", "6.jpg", "7.jpg", "8.jpg", "9.jpg"])
        model.addAttribute('certificates', ["1.jpg", "2.jpg"])
        return "index"
    }
}
