package contracts

import org.springframework.cloud.contract.spec.Contract


/**
 * Contract definition, written in a Groovy DSL
 */

Contract.make {

    /**
     * Request which completes with HTTP statusCode 200
     */
    request {
        method 'GET'
        url('/demo/getUser')
    }
    response {
        status 200
        headers {
            header('Content-Type',  "text/plain;charset=ISO-8859-1")
        }
        body('test')
    }
}