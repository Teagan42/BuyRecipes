package rocks.teagantotally.buy_recipes.data.entities

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.util.ArrayList

/**
 * Represents a recipe entity in the database.
 *
 * @property id The unique identifier of the recipe. It is auto-generated.
 * @property name The name of the recipe.
 * @property items The list of products associated with the recipe. It is a many-to-many
 * relationship.
 */
@Entity
@Table(name = "recipes")
class Recipe(
        var name: String,
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "recipe_items",
                joinColumns = [JoinColumn(name = "recipe_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "product_id", referencedColumnName = "id")],
        )
        var items: MutableList<Product> = ArrayList(),
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false

    other as Recipe

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id ?: 0
  }

  override fun toString(): String {
    return "Recipe(id=$id, name='$name', items=$items)"
  }
}
