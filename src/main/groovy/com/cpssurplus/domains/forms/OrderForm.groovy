package com.cpssurplus.domains.forms

class OrderForm {
    Integer orderId
    String title
    String firstName
    String surname
    String email
    String phone
    Integer qty
    String houseNr
    String street
    String town
    String country
    String comment

    String getShippingAddress() {
        String.format("%s, %s, %s, %s", street, houseNr, town, country)
    }

    String getName() {
        String.format("%s %s %s", title, firstName, surname)
    }
}
