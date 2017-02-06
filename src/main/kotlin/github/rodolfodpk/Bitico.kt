package github.rodolfodpk

import org.eclipse.collections.api.multimap.MutableMultimap
import org.eclipse.collections.impl.factory.Multimaps

/**
 * A main function for execution
 */
fun main(args: Array<String>) {

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

}

/**
 * Added these type alias to improve readability
 */

typealias EntityDatabase = MutableMap<String, EntityInstance> // name as a key

typealias EntityInstance = MutableMultimap<String, String> // fieldId as key and fieldValue as values

/**
 * Function to build the present records (by applying the facts to it's respective records)
 * return an EntityDatabase
 */
fun entityDatabase(facts: List<List<Any>>, schema: List<List<String>>) : EntityDatabase {

    val db: EntityDatabase = mutableMapOf()
    val schemaAsMap = schema.associateBy({ it[0] } , { it[2] })

    println("\n*** given these facts ")

    for ((recordId, fieldId, fieldValue, flag) in facts) {

        println ("$recordId, $fieldId, $fieldValue, $flag")

        // if adding this entity for first time
        val entityInstance = db.get(recordId)
        if (entityInstance == null) {
            if (!(flag as Boolean)) continue
            val newInstance = Multimaps.mutable.list.with(fieldId as String, fieldValue as String)
            db.put(recordId as String, newInstance)
            continue // just add it then continue
        }

        // if adding this field for the first time (for the current record instance)
        val fieldValues = entityInstance.get(fieldId as String)
        if (fieldValues.size==0) {
            if (!(flag as Boolean)) continue
            entityInstance.put(fieldId, fieldValue as String)
            db.put(recordId as String, entityInstance)
            continue // just add it then continue
        }

        // if the fieldValue must be deleted
        if (!(flag as Boolean)) {
            entityInstance.remove(fieldId, fieldValue)
            db.put(recordId as String, entityInstance)
            continue // just remove it then continue
        }

        // if field id already exists for the current record instance and it must be added with new state
        val schemaOfField = schemaAsMap[fieldId]
        if (schemaOfField.equals("one")) {
            entityInstance.removeAll(fieldId)
        }
        entityInstance.put(fieldId, fieldValue as String)
        db.put(recordId as String, entityInstance)

    }

    println("\n--- then these are the resulting states after all facts")

    db.forEach { println(it) }

    return db
}


/**
 * Function to return the list of present facts
 */
fun presentFactsList(db : EntityDatabase): List<List<Any>> {

    val result: MutableList<List<Any>> = mutableListOf()
    for ((entityId, fieldsValues) in db) {
         fieldsValues.forEachKeyValue {
             _fieldId, _fieldValue -> result.add(listOf(entityId, _fieldId, _fieldValue,true))
         }
    }
    return result
}
