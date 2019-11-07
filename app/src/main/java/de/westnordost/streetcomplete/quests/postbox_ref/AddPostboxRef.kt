package de.westnordost.streetcomplete.quests.postbox_ref

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.Countries
import de.westnordost.streetcomplete.data.osm.SimpleOverpassQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.download.OverpassMapDataDao
import de.westnordost.streetcomplete.ktx.containsAny

class AddPostboxRef(o: OverpassMapDataDao) : SimpleOverpassQuestType<PostboxRefAnswer>(o) {

    override val tagFilters = "nodes with amenity = post_box and !ref and !ref:signed"

    override val icon = R.drawable.ic_quest_mail_ref
    override val commitMessage = "Add postbox refs"

    // source: https://commons.wikimedia.org/wiki/Category:Post_boxes_by_country
    override val enabledForCountries: Countries = Countries.noneExcept(
        "FR","GB","GG","IM","JE","MT","IE","SG","CZ","SK","CH","US"
    )

    override fun getTitleArgs(tags: Map<String, String>, featureName: Lazy<String?>): Array<String?> {
        val name = tags["name"] ?: tags["brand"] ?: tags["operator"]
        return if (name != null) arrayOf(name) else arrayOf()
    }

    override fun getTitle(tags: Map<String, String>): Int {
        val hasName = tags.keys.containsAny(listOf("name","brand","operator"))
        return if (hasName) R.string.quest_postboxRef_name_title
               else         R.string.quest_postboxRef_title
    }

    override fun createForm() = AddPostboxRefForm()

    override fun applyAnswerTo(answer: PostboxRefAnswer, changes: StringMapChangesBuilder) {
        when(answer) {
            is NoRefVisible -> changes.add("ref:signed", "no")
            is Ref ->          changes.add("ref", answer.ref)
        }
    }
}
