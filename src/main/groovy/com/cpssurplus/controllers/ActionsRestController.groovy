package com.cpssurplus.controllers


import com.cpssurplus.domains.forms.OrderForm
import com.cpssurplus.services.MailClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailException
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException

import java.util.concurrent.ThreadLocalRandom

@RestController
@RequestMapping("/async")
class ActionsRestController {


    @Autowired
    private MailClient mailClient

    @PostMapping("/sendMail")
    String processMailForm(@ModelAttribute OrderForm orderForm) {
        try {
            orderForm.orderId = ThreadLocalRandom.current().nextInt(0, 999999)
            mailClient.sendOrderNotification(orderForm)
            mailClient.sendCustomerOrderNotification(orderForm)
        } catch(MailException mailException) {
            mailException.printStackTrace()
            return "There is something wrong with our mail client. Please contact us using contacts section."
        } catch (HttpClientErrorException clientException) {
            clientException.printStackTrace()
            if (clientException.statusText == "QTY too small") {
                return "Ordered amount is less then minimum of 5 items"
            } else {
                return "Something went wrong, please contact us by email directly"
            }
        } catch (Exception e) {
            e.printStackTrace()
            return "Unknown internal error on processing your request. Please contact us using contacts section."
        }
        return "OK";
    }

}
