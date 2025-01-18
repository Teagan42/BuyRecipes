package rocks.teagantotally.buy_recipes.data.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

/**
 * Represents a 1 entity in the "products" table.
 *
 * @property id The unique identifier for the 1. It is auto-generated.
 * @property name The name of the 1.
 * @property priceInCents The price of the 1 in cents.
 * @Property items The list of recipes that contain the 1. It is a many-to-many relationship.
 */
@Entity
@Table(name = "products")
class Product(
        val name: String,
        val priceInCents: Int,
        @JsonIgnore @ManyToMany(mappedBy = "items") var recipes: MutableList<Recipe> = ArrayList(),
        @JsonIgnore @ManyToMany(mappedBy = "items") var carts: MutableList<Cart> = ArrayList(),
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false

    other as Product

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id ?: 0
  }

  // override fun toString(): String {
  //   return "Product(id=$id, name='$name', priceInCents=$priceInCents, recipes=$recipes,
  // carts=$carts)"
  // }
}
