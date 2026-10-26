package server.web.recolte

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.JsonNode
import tools.jackson.databind.json.JsonMapper

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecolteApiTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val json: JsonMapper,
) {
    private val clientFields = listOf(
        "id", "code", "nom", "prenom", "telephone",
        "montant_souscription", "nombre_tranches", "tranche", "tranches",
        "created_at", "updated_at", "address_client",
    )
    @Test
    fun legacyAndVersionedApiCoverTheCollectionFlow() {
        get("/api/v1/public/communes")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.communes[3].nom").value("Gombe"))

        post("/api/client", """{"nom":"Kabila"}""")
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message.prenom[0]").value("The prenom field is required."))

        val created = post(
            "/api/client",
            """
            {
              "nom": "Kabila",
              "prenom": "Elie",
              "telephone": "0999000111",
              "commune_id": 4,
              "avenue": "Limete",
              "quartier": "Residentiel",
              "numero_parcelle": "12",
              "montant_souscription": 24000,
              "nombre_tranches": 4
            }
            """.trimIndent(),
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.message").value("Compte client créer avec succès"))
            .andExpect(jsonPath("$.client.avenue").value("limete"))
            .andExpect(jsonPath("$.client.commune_id").value(4))
            .andExpect(jsonPath("$.souscription.montant_souscription").value(24000))
            .andExpect(jsonPath("$.souscription.nombre_tranches").value(4))
            .andExpect(jsonPath("$.souscription.tranche").value("quatre tranche"))
            .andExpect(jsonPath("$.souscription.tranches[0].montant").value(6000))
            .andExpect(jsonPath("$.souscription.tranches[3].numero").value(4))
            .andExpect(jsonPath("$.souscription.tranches[3].montant").value(6000))
            .andReturn()

        val clientId = json.readTree(created.response.contentAsString).path("client").path("client_id").asLong()

        get("/api/client")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.clients[0].code").value("HDC-1"))
            .andExpect(jsonPath("$.clients[0].montant_souscription").value(24000))
            .andExpect(jsonPath("$.clients[0].tranche").value("quatre tranche"))
            .andExpect(jsonPath("$.clients[0].tranches[3].montant").value(6000))
            .andExpect(jsonPath("$.clients[0].address_client[0].avenue").value("limete"))

        post("/api/client/auth", """{"telephone":"0999000111","avenue":"Inconnue"}""")
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.message").value("L'avenue inserez n'est pas associé à votre numéro"))

        val clientLogin = post("/api/v1/public/clients/auth", """{"telephone":"0999000111","avenue":"LIMETE"}""")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("login success"))
            .andExpect(jsonPath("$.client.code").value("HDC-1"))
            .andExpect(jsonPath("$.client.montant_souscription").value(24000))
            .andExpect(jsonPath("$.client.nombre_tranches").value(4))
            .andExpect(jsonPath("$.client.tranche").value("quatre tranche"))
            .andExpect(jsonPath("$.client.address_client[0].quartier").value("Residentiel"))
            .andReturn()
        check(fieldNames(clientLogin, "client") == clientFields)

        val agent = post(
            "/api/agent",
            """
            {
              "nom": "Mukendi",
              "postnom": "Jean",
              "prenom": "Paul",
              "genre": "M",
              "telephone": "081000",
              "address": "Gombe",
              "date_naissance": "1990-01-01"
            }
            """.trimIndent(),
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.message").value("Compte Agent créer avec succès"))
            .andExpect(jsonPath("$.agent.code").value("hda-1"))
            .andExpect(jsonPath("$.agent.city_id").value(1))
            .andReturn()

        get("/api/agent")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.agents[0].code").value("hda-1"))
            .andExpect(jsonPath("$.agents[0].city_id").value(1))

        post(
            "/api/agent",
            """{"nom":"Mukendi","postnom":"Jean","prenom":"Paul","telephone":"081111"}""",
        ).andExpect(status().isForbidden)
            .andExpect(jsonPath("$.message").value("Ce compte est déjà enregistrer"))

        post("/api/agent/auth", """{"code":"HDA-1"}""")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("login success"))
            .andExpect(jsonPath("$.agent.code").value("hda-1"))
            .andExpect(jsonPath("$.agent.city_id").value(1))
            .andExpect(jsonPath("$.client[0].code").value("HDC-1"))
            .andExpect(jsonPath("$.client[0].address_client[0].avenue").value("limete"))
            .andExpect(jsonPath("$.trash_client").isEmpty)

        val trash = post(
            "/api/trash",
            """{"client_id":$clientId,"state_trash_id":3}""",
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.message").value("On arrive pour nettoyer votre poubelle !!"))
            .andExpect(jsonPath("$.data.is_active").value(1))
            .andExpect(jsonPath("$.data.client_id").value(clientId))
            .andExpect(jsonPath("$.data.state_trash_id").value(3))
            .andExpect(jsonPath("$.data.client").doesNotExist())
            .andReturn()

        val trashId = json.readTree(trash.response.contentAsString).path("data").path("id").asLong()

        get("/api/trash/$clientId")
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.message").value(""))
            .andExpect(jsonPath("$.data.state_trash_id").value(3))
            .andExpect(jsonPath("$.data.client.telephone").value("0999000111"))
            .andExpect(jsonPath("$.data.client.address_client[0].avenue").value("limete"))

        val agentLogin = post("/api/agent/auth", """{"code":"hda-1"}""")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("login success"))
            .andExpect(jsonPath("$.agent.code").value("hda-1"))
            .andExpect(jsonPath("$.agent.nom").value("Mukendi"))
            .andExpect(jsonPath("$.agent.postnom").value("Jean"))
            .andExpect(jsonPath("$.agent.prenom").value("Paul"))
            .andExpect(jsonPath("$.agent.telephone").value("081000"))
            .andExpect(jsonPath("$.agent.genre").value("M"))
            .andExpect(jsonPath("$.agent.address").value("Gombe"))
            .andExpect(jsonPath("$.agent.date_naissance").value("1990-01-01"))
            .andExpect(jsonPath("$.agent.city_id").value(1))
            .andExpect(jsonPath("$.client[0].code").value("HDC-1"))
            .andExpect(jsonPath("$.client[0].montant_souscription").value(24000))
            .andExpect(jsonPath("$.client[0].tranches.length()").value(4))
            .andExpect(jsonPath("$.client[0].address_client[0].commune_id").value(4))
            .andExpect(jsonPath("$.client[0].address_client[0].numero_parcelle").value("12"))
            .andExpect(jsonPath("$.client[0].address_client[0].quartier").value("Residentiel"))
            .andExpect(jsonPath("$.trash_client[0].client_id").value(clientId))
            .andExpect(jsonPath("$.trash_client[0].state_trash_id").value(3))
            .andExpect(jsonPath("$.trash_client[0].is_active").value(1))
            .andExpect(jsonPath("$.trash_client[0].client.code").value("HDC-1"))
            .andExpect(jsonPath("$.trash_client[0].client.address_client[0].avenue").value("limete"))
            .andReturn()
        check(fieldNames(agentLogin) == listOf("message", "agent", "client", "trash_client"))
        check(fieldNames(agentLogin, "agent") == listOf(
            "id", "code", "city_id", "nom", "postnom", "prenom", "telephone", "genre", "address",
            "date_naissance", "created_at", "updated_at",
        ))
        check(fieldNames(agentLogin, "client", 0) == clientFields)
        check(fieldNames(agentLogin, "client", 0, "address_client", 0) == listOf(
            "id", "client_id", "commune_id", "avenue", "numero_parcelle", "quartier", "created_at", "updated_at",
        ))
        check(
            json.readTree(agentLogin.response.contentAsString).path("agent").path("created_at").asString()
                .matches(Regex("""\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{6}Z""")),
        )

        post("/api/trash/clean/$trashId", """{"client_id":$clientId,"state_trash_id":3}""")
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.message").value("Nettoyage de la poubelle encours"))
            .andExpect(jsonPath("$.data.state_trash_id").value(2))
            .andExpect(jsonPath("$.data.client").doesNotExist())

        post("/api/v1/public/trashes/clean/$trashId", """{"client_id":$clientId,"state_trash_id":2}""")
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.message").value("Votre poubelle est actuellement propre"))
            .andExpect(jsonPath("$.data.state_trash_id").value(1))

        get("/api/trash")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.trash_clients[0].client.address_client[0].avenue").value("limete"))

        get("/api/v1/protected/users")
            .andExpect(status().isUnauthorized)

        val registered = post(
            "/api/v1/public/auth/register",
            """{"name":"Admin","email":"admin@recolte.test","password":"secret-pass"}""",
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.access_token", containsString(".")))
            .andReturn()

        val access = json.readTree(registered.response.contentAsString).path("access_token").asString()
        val refresh = json.readTree(registered.response.contentAsString).path("refresh_token").asString()

        get("/api/v1/protected/users") { header("Authorization", "Bearer $access") }
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.user.email").value("admin@recolte.test"))
            .andExpect(jsonPath("$.users[0].name").value("Admin"))

        post("/api/v1/public/auth/refresh", """{"refresh_token":"$refresh"}""")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.access_token").exists())

        get("/")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("hd-recolte"))

        json.readTree(agent.response.contentAsString).path("agent").path("date_naissance").let { node: JsonNode ->
            check(node.asString() == "1990-01-01")
        }
    }

    @Test
    fun subscriptionAmountSplitsIntoOneToFourMonthlyInstallments() {
        val body = { tranche: String, montant: Long ->
            """
            {
              "nom": "Ilunga",
              "prenom": "Grace",
              "telephone": "081$montant",
              "avenue": "Kasai",
              "quartier": "Centre",
              "montant_souscription": $montant,
              "tranche": "$tranche"
            }
            """.trimIndent()
        }

        post(
            "/api/client",
            """
            {
              "nom": "Ilunga",
              "prenom": "Grace",
              "telephone": "081100001",
              "avenue": "Kasai",
              "quartier": "Centre"
            }
            """.trimIndent(),
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message.montant_souscription[0]").value("The montant_souscription field is required."))

        post(
            "/api/client",
            """
            {
              "nom": "Ilunga",
              "prenom": "Grace",
              "telephone": "081100002",
              "avenue": "Kasai",
              "quartier": "Centre",
              "montant_souscription": 0,
              "nombre_tranches": 1
            }
            """.trimIndent(),
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Le montant de souscription doit être supérieur à 0"))

        post(
            "/api/client",
            """
            {
              "nom": "Ilunga",
              "prenom": "Grace",
              "telephone": "081100003",
              "avenue": "Kasai",
              "quartier": "Centre",
              "montant_souscription": 9000,
              "nombre_tranches": 5
            }
            """.trimIndent(),
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message.nombre_tranches[0]").value("Choisissez une tranche, deux tranche, trois tranche ou quatre tranche."))

        post("/api/client", body("une tranche", 8000))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.souscription.tranche").value("une tranche"))
            .andExpect(jsonPath("$.souscription.tranches.length()").value(1))
            .andExpect(jsonPath("$.souscription.tranches[0].montant").value(8000))

        post("/api/client", body("deux tranches", 10000))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.souscription.nombre_tranches").value(2))
            .andExpect(jsonPath("$.souscription.tranche").value("deux tranche"))
            .andExpect(jsonPath("$.souscription.tranches[0].montant").value(5000))
            .andExpect(jsonPath("$.souscription.tranches[1].montant").value(5000))

        post("/api/client", body("trois tranche", 10000))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.souscription.tranche").value("trois tranche"))
            .andExpect(jsonPath("$.souscription.tranches[0].montant").value(3334))
            .andExpect(jsonPath("$.souscription.tranches[1].montant").value(3333))
            .andExpect(jsonPath("$.souscription.tranches[2].montant").value(3333))

        post("/api/client", body("quatre tranche", 10001))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.souscription.tranche").value("quatre tranche"))
            .andExpect(jsonPath("$.souscription.tranches[0].montant").value(2501))
            .andExpect(jsonPath("$.souscription.tranches[3].montant").value(2500))
    }

    private fun fieldNames(result: MvcResult, vararg path: Any): List<String> {
        var node = json.readTree(result.response.contentAsString)
        path.forEach { step ->
            node = when (step) {
                is Int -> node.path(step)
                else -> node.path(step.toString())
            }
        }
        return node.propertyNames().toList()
    }

    private fun get(path: String, customize: MockHttpServletRequestBuilder.() -> Unit = {}): ResultActions =
        execute(MockMvcRequestBuilders.get(path).apply(customize))

    private fun post(path: String, body: String): ResultActions =
        execute(
            MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body),
        )

    private fun execute(builder: MockHttpServletRequestBuilder): ResultActions {
        val actions = mockMvc.perform(builder)
        val result: MvcResult = actions.andReturn()
        return if (result.request.isAsyncStarted) {
            mockMvc.perform(asyncDispatch(result))
        } else {
            actions
        }
    }
}
