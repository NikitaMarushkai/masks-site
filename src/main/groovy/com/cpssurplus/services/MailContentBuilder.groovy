package com.cpssurplus.services

import com.cpssurplus.domains.enums.Countries
import com.cpssurplus.domains.forms.OrderForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

/**
 * Created by unlim_000 on 24.02.2017.
 */

@Service
class MailContentBuilder {

    private TemplateEngine templateEngine

    @Autowired
    MailContentBuilder(TemplateEngine templateEngine){
        this.templateEngine = templateEngine
    }

    String buildNewOrderRequest(OrderForm orderForm) {
        Context context = new Context()
        context.setVariable("orderId", orderForm.orderId)
        context.setVariable("email", orderForm.email)
        context.setVariable("phone", orderForm.phone)
        context.setVariable("name", orderForm.name)
        context.setVariable("qty", orderForm.qty)
        context.setVariable("comment", orderForm.comment)
        context.setVariable("shippingAddress", orderForm.shippingAddress)
        context.setVariable("price", orderForm.country == Countries.BELGIUM ? getTotalPrice(orderForm.qty).first : "Needs manual calculation")
        return templateEngine.process("mail/newOrderMailTemplate", context)
    }

    String buildNewCustomerOrderConfirmation(OrderForm orderForm) throws HttpClientErrorException {
        Context context = new Context()
        context.setVariable("name", orderForm.name)
        context.setVariable("order", orderForm.orderId)
        context.setVariable("qty", orderForm.qty)
        context.setVariable("amount", orderForm.country == Countries.BELGIUM ? '€' + getTotalPrice(orderForm.qty).first +
                "(VAT ${getTotalPrice(orderForm.qty).second})" : "Price will be sent to you after calculation")
        context.setVariable("shippingAddress", orderForm.shippingAddress)
        return templateEngine.process("mail/customerOrderConfirmationTemplate", context)
    }

    private static Tuple2<Double, Double> getTotalPrice(Integer qty) {
        double priceShippingSmallest = 2.39;
        double priceShippingLess15 = 3.39;
        double priceShippingEach15 = 3.10
        double maskPriceLess15 = 3.87
        double maskPriceLess30 = 3.67
        double maskPriceLowest = 3.57
        switch (qty) {
            case 3:
                double amount = qty * maskPriceLess15 + priceShippingSmallest
                return new Tuple2<>((amount + (amount * 0.06)).round(2), (amount * 0.06).round(2))
                break
            case { it > 3 && it < 15 }:
                double amount = qty * maskPriceLess15 + priceShippingLess15
                return new Tuple2<>((amount + (amount * 0.06)).round(2), (amount * 0.06).round(2))
                break
            case { it >=15 && it < 30 }:
                double amount = qty * maskPriceLess30 + priceShippingLess15 + (priceShippingEach15 / 15 * qty)
                return new Tuple2<>((amount + (amount * 0.06)).round(2), (amount * 0.06).round(2))
                break
            case { it >= 30  }:
                double amount = qty * maskPriceLowest + priceShippingLess15 + priceShippingEach15 + (priceShippingEach15 / 15 * qty)
                return new Tuple2<>((amount + (amount * 0.06)).round(2), (amount * 0.06).round(2))
                break
            default:
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "QTY too small")
        }
    }
}
