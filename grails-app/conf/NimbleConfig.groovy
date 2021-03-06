/*
 *  Nimble, an extensive application base for Grails
 *  Copyright (C) 2009 Intient Pty Ltd
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

nimble {
    organization {
        name = ""
        displayname = ""
        description = ""
        logo = ""
        logosmall = ""
        url = ""
    }

    localusers {
        authentication {
            enabled = true
        }
        registration {
            enabled = true
        }
    }

    facebook {
        federationprovider {
            enabled = true
            autoprovision = true
        }

        apikey = ""
        secretkey = ""
        //glenn.saqui@bskyb.com/testingapi9
    }

    openid {
        federationprovider {
            enabled = true
            autoprovision = true
        }
    }

    messaging {
        registration {
            subject = "Your new account is ready!"
        }
        passwordreset {
            subject = "Your password has been reset"
            external.subject = "Your password reset request"
        }

        mail {
            from = "App <app@company.com>"
            host = ""
            port = 25
            username = ""
            password = ""
            props = ["mail.smtp.auth": "false",
              "mail.smtp.socketFactory.port": "25",
              "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
              "mail.smtp.socketFactory.fallback": "false"]
        }
    }

    implementation {
    	user = "backchat.User"
    	profile = "backchat.Profile"
    }
}

environments {
    development {
        nimble {
            recaptcha {
                enabled = false
                secureapi = false

                // These keys are generated by the ReCaptcha service
                publickey = ""
                privatekey = ""

                // Include the noscript tags in the generated captcha
                noscript = true
            }
        }
    }
    production {
        nimble {
            recaptcha {
                enabled = false
                secureapi = false

                // These keys are generated by the ReCaptcha service
                publickey = ""
                privatekey = ""

                // Include the noscript tags in the generated captcha
                noscript = true
            }
        }
    }
}
