package rocks.teagantotally.buy_recipes.data.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CartTest {
    @Test
    fun `calculateTotal should sum up the prices of all items`() {
        val product1 = Product(id = 1, name = "Product 1", priceInCents = 1000)
        val product2 = Product(id = 2, name = "Product 2", priceInCents = 2000)
        val cart = Cart(items = mutableListOf(product1, product2))

        assertEquals(3000, cart.totalInCents)
    }

    @Test
    fun `calculateTotal should be zero when there are no items`() {
        val cart = Cart(items = mutableListOf())

        assertEquals(0, cart.totalInCents)
    }

    @Test
    fun `calculateTotal should handle large numbers correctly`() {
        val product1 = Product(id = 1, name = "Product 1", priceInCents = 1000000)
        val product2 = Product(id = 2, name = "Product 2", priceInCents = 2000000)
        val cart = Cart(items = mutableListOf(product1, product2))

        assertEquals(3000000, cart.totalInCents)
    }
}
