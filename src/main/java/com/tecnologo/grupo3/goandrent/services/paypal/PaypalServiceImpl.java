package com.tecnologo.grupo3.goandrent.services.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalServiceImpl {

    private static final String usernameGoAndRent = "AYnQJNi-O__v7GYcLWR4W4FVkaMY_JqKRt7ujIgs43Csz_peoJkbVOW90OJFDkla3hkAGU8woj5Zne-_";
    private static final String passwordGoAndRent = "ED_3SsNKOSl_v9ZLTHf7yh8eg6D3eCv9Kr0Ql3ugUGnzEvsv58yIU43GSCiHjZor1Eue0wrhX_6GGfRX";

    private APIContext apiContext = new APIContext(usernameGoAndRent, passwordGoAndRent, "sandbox");


    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        //total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        //amount.setTotal(String.format("%.3f", total));
        amount.setTotal(total.toString());

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
}
