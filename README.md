GMail Service
=============

### Why a GMail service?

ManyWho already have an official [Email Service](https://github.com/manywho/service-email) , then what is the motivation to build a GMail Service.

These are the most important points:

- **The Target** for this service is people who is a beginner in the ManyWho platform, and who is interested in "play" with the platform but also learn something that can be used in production.
- **The Goal** is to be able to create a simple flow without need of external configuration (the current Email Service require some configuration in your email account). At the same time this is a basic example of GMail service that some day can be extended with more functionality.
- **Oauth2 support** this service also take advantage of the oauth support offered by GMail (the email service is a more generic solution).

Of course it will allow you to integrate your Flows with [GMail](https://www.gmail.com) but with a very limited functionality.

## Running the service
(recommended for beginners)

The service is currently running in Heroku, you can install it in your ManyWho account using this url "https://blooming-shelf-71026.herokuapp.com/api/gmail/1"


## Running the service by yourself
(advanced setup)

If you want to run the service by yourself, you will need to create an app in GMail and download credentials using the [Google Console] (https://console.developers.google.com)

Then  you have two choices of how to pass this credentials to the app:
- Copy the credentials and paste it in a file called **credentials.properties** in the folder **resources** (the file should look like the example credentials.properties.dist).
- using **environment variables** (in this case you don't need to create the file):

Example of environment variable

```
GMAIL_APP_CREDENTIALS = {"web":{"client_id":"xxx-xxx.apps.googleusercontent.com","project_id":"xxx","auth_uri":"https://accounts.google.com/o/oauth2/auth","token_uri":"https://accounts.google.com/o/oauth2/token","auth_provider_x509_cert_url":"https://www.googleapis.com/oauth2/v1/certs","client_secret":"xxx","redirect_uris":["https://flow.manywho.com/api/run/1/oauth2"],"javascript_origins":["https://flow.manywho.com"]}}
```

### Running the service using Heroku

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

You will be also asked for GMAIL_APP_CREDENTIALS and you should paste in the input field.

## Contributing

Contribution are welcome to the project - whether they are feature requests, improvements or bug fixes!


## License

This service is released under the [MIT License](http://opensource.org/licenses/mit-license.php).