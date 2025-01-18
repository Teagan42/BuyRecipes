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
 * Represents a shopping cart containing multiple products.
 *
 * @property id The unique identifier of the cart.
 * @property totalInCents The total cost of all items in the cart, in cents.
 * @property items The list of products in the cart.
 *
 * @constructor Creates a new Cart instance.
 *
 * @param id The unique identifier of the cart. Defaults to null.
 * @param totalInCents The total cost of all items in the cart, in cents.
 * @param items The list of products in the cart. Defaults to an empty list.
 *
 * @see rocks.teagantotally.buy_recipes.data.entities.Product
 */
@Entity
@Table(name = "carts")
class Cart(
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "cart_items",
                joinColumns = [JoinColumn(name = "cart_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "product_id", referencedColumnName = "id")],
        )
        var items: MutableList<Product> = ArrayList(),
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
) {
  val totalInCents: Int
    get() = items.sumOf { it.priceInCents }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false

    other as Cart

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id ?: 0
  }

  override fun toString(): String {
    return "Cart(id=$id, totalInCents=$totalInCents, items=$items)"
  }
}
