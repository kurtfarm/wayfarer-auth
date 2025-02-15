package wayfarer_auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@SpringBootApplication
@ConfigurationPropertiesScan
class WayfarerAuthApplication

fun main(args: Array<String>){
    runApplication<WayfarerAuthApplication>(*args)
}
