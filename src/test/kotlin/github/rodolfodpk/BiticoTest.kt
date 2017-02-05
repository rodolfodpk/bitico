package github.rodolfodpk

import org.junit.Test

class BiticoTest {

    @Test
    fun testing_with_original_dateset() {

        val facts = listOf(listOf("gabriel", "endereço", "av rio branco, 109", true),
                listOf("joão", "endereço", "rua alice, 10", true),
                listOf("joão", "endereço", "rua bob, 88", true),
                listOf("joão", "telefone", "234-5678", true),
                listOf("joão", "telefone", "91234-5555", true),
                listOf("joão", "telefone", "234-5678", false),
                listOf("gabriel", "telefone", "98888-1111", true),
                listOf("gabriel", "telefone", "56789-1010", true)
        )

        val schema = listOf(listOf("endereço", "cardinality", "one"),
                listOf("telefone", "cardinality", "many"))

        val presentFactsList = presentFactsList(entityDatabase(facts, schema))

        println("\n--- present facts ")
        presentFactsList.forEach({println(it)})

        assert(presentFactsList.size==5)
        assert(presentFactsList.contains(facts[0]))
        assert(presentFactsList.contains(facts[6]))
        assert(presentFactsList.contains(facts[7]))
        assert(presentFactsList.contains(facts[2]))
        assert(presentFactsList.contains(facts[4]))
    }

    @Test
    fun testing_with_dateset2() {

        val facts = listOf(listOf("gabriel", "endereço", "av rio branco, 109", true),
                listOf("joão", "telefone", "234-5678", false),
                listOf("gabriel", "telefone", "56789-1010", true)
        )

        val schema = listOf(listOf("endereço", "cardinality", "one"),
                     listOf("telefone", "cardinality", "many"))

        val presentFactsList = presentFactsList(entityDatabase(facts, schema))

        println("\n--- present facts ")
        presentFactsList.forEach({println(it)})

        assert(presentFactsList.size==2)
        assert(presentFactsList.contains(facts[0]))
        assert(presentFactsList.contains(facts[2]))
    }
}
