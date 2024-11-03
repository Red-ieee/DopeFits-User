package com.example.dopefits.com.example.dopefits.FCM

import com.google.auth.oauth2.GoogleCredentials
import java.io.IOException
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets


class AccessToken {
    private val firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging"

    fun getAccessToken(): String? {
        try {
            val jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"dopefits99\",\n" +
                    "  \"private_key_id\": \"1cced9668eb4642a2de62800915ded451ec5d5bf\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDgR3yPmsAIvT25\\n4YlJnWy+OWCFeQ3RX5uHze1Y4W7eHC13MG0Yq1FEmDAOlDcKsZOkCvhGKc9RfhgT\\nO+ZmBicbSmyt10AWX2N5p0/7O0vVnegtY7ub1jJIFJmSWqttupWHVFitJu9eBFpL\\nm8yJVhFODzYOrrN0JrpXqGxcC/1pYIUxZyK/exLCp2XKh8ggLwDJXr6Fl4aJrSKL\\nH0ozp3dH7a7a2D1LNDSMm+YWflVIt3kL06D+7q6mFNt//FsnCs6RKn8ubBzZ0Ps1\\ncFzQvRoKcEFDbuUYPHTh4AJ91H2S5MZ1QZaEpzfNtX2yWO8vLqXy9wKpikHpYuHa\\noWuss6rvAgMBAAECggEADz7sybpESZPLqWIaegWYicnOtOQIu23bXbzy4HLanYCe\\n2pZRJDcpHBeYVLVglHqDxcf1Hpfi/vAqZMxSmiba67EAeETEl4C+e1uTRUm/mvLU\\nAJnh+/LwkhPMaLV/J8NiPMPQD10xMlvrY25g1gkivgKsswzPKBsl2aWaUBuEi3Ai\\np2mT7Mmsv7PcTCqgmOj6NARcNkOk50D7cCBwfXa1prGW09pYXo9QZSX13PWGKZ1r\\n+fUdtTM2jHhrtxuF5qIOZc4uXRuIDqqRKy4fQjAwvHKWqaeNqAUS81DZGZisTQmh\\nOMslKxB9EKt5GkpKWRaz/m7Fk9vnen/n7UbEhENUiQKBgQD1qIx4kT/tpWBkGe0f\\ncHLgQVbqJvgT3yLXxH9NWorxFqEN2W8GgojGPvYDNX7oMBlIIRe8Q18r+tB7V3pk\\nE8ectrBY9ZAYhpo8CNa3XdRXY0Bvj2OPOnr3lpZYEYi4IUCdWUzE2ywmqW+JIdUB\\nMHVAFUaL5OHr0LpwDEWPxQddywKBgQDpuIkaycAVsUS1QwaIEuJGvrQXkOlk0sVw\\nlKOD1eHKNMpxsAhgDFKJkmlRrFbDg2i3TWS9lPMb8UJpQ7tQMTQUf108RYHnlPlF\\nMZUyRi1C3//jY1pjKjKjBnVBsu6DjMSc0QC3VOoFhmPZdJDDy4VyNnviXHLRgTgx\\nm92uVarC7QKBgBTxOIByyYNl7NC70RY0DOcHSvSNO+rdw+enDb83b87bwgEfPzLL\\n4AMejPR7FAWRTCZ1A8P97lgeerV5IKr1cLfwbxo2XQFqQC3MBxn8usR0ZqLcE327\\nW45N3SVG4WPpGcwQN9Y0ZRAqs6J6KuF+ExDSf4AflA75bVtuAGQgRN9/AoGAXbP5\\na/8E/u1yUYXMwZNomtpsU6JqoFyMggVlxT5j5vDhVVExGmj2Umebx/3jT0GJIyxp\\nPkTQbxx79r4MBCul6K3jkH2BhnpIsgAeA+j0zoIGEoFLxMQvRwMY0b1OOPmf/gsh\\nUV8+7/YgZBeCzqzzFIZZhrhv5CT9hY4rA3EfJEUCgYEAmX54ZzVk0GeJo6/LdU5l\\nQfcpRdY8bxkKq9gjimb3+RZ5OgL01YnXFy7t2GaPPSal/aPtboCzwFjht3LOdUPl\\nnwArKqf+MhhownchKHVoLoB8rH0nK5sLYhZ0TPyEq30ol6gXinpFINxpyH16tVVT\\nfj/r7R/q8Zq6OQM1d9HpZZM=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-645x3@dopefits99.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"114263719176209376062\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-645x3%40dopefits99.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n"

            val stream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
            val googleCredential = GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(firebaseMessagingScope))
            googleCredential.refresh();
            return googleCredential.accessToken.tokenValue
        } catch (e: IOException){
            return null
        }
    }
}